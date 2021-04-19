<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
    <p>

      <a-space>
      <a-button size="normal" type="primary" @click="add">
        新增
      </a-button>
      </a-space>
    </p>
      <a-table :columns="columns"
               :data-source="level1"
               :loading="Loading"
               :pagination="false"
               :row-key="record => record.id"
               >
        <template #cover="{ text: cover }">
          <img  v-if="cover" :src="cover" alt="avatar">
        </template>
        <template v-slot:action="{ text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                cancel-text="否"
                ok-text="是"
                title="删除后不可恢复，确认删除?"
                @confirm="handleDelete(record.id)"
            ><a-button type="danager">
              删除
            </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      title="文档表单"
      @ok="handleModalOk"
  >
    <a-form :label-col="{ span: 6}" :model="doc">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>

      <a-form-item label="父文档">
      <a-tree-select
          v-model:value="doc.parent"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :replaceFields="{title: 'name',key: 'id', value: 'id'}"
          :tree-data="treeSelectData"
          placeholder="请选择父文档"
          style="width: 100%"
          tree-default-expand-all
      >

      </a-tree-select>
      </a-form-item>

      <a-form-item label="父文档">
        <a-select v-model:value = "doc.parent" style="width: 175px">
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :disable="doc.id===c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';
import {useRoute} from "vue-router";

export default defineComponent({
  name:'AdminDoc',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '文档',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 一级文档树,children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name:"",
     *    ]}
     *  }]
     *
     * */

    const level1 = ref();

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/doc/all").then((response)=>{
        loading.value = false;
        const data = response.data;

        if(data.success){
          docs.value = data.content;
          console.log("原始数组：",docs.value);
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("树形结构",level1.value);
        }else{
          message.error(data.message);
        }

      });
    };


    onMounted(()=>{
      handleQuery();
    })
    // --------- 表单 ----------
    const treeSelectData = ref();
    treeSelectData.value=[];
    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () =>{
      modalLoading.value = true;
      axios.post("/doc/save",doc.value).then((response)=>{
        const data = response.data; // data = commonResp
        modalLoading.value = false;
        if (data.success){
          modalVisible.value = false;
          // 重新加载列表
          handleQuery();
        }else{
          message.error(data.message);
        }
      });
    }
    /**
     *
     * 将某节点及其子孙节点全部置为disabled
     *
     * */

      const setDisable = (treeSelectData: any,id:any) =>{
        //遍历数组,即遍历某一层节点
      for (let i = 0;i<treeSelectData.length;i++){
        const node = treeSelectData[i];
        if(node.id===id){
          console.log("disabled",node);
          node.disabled = true;
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0;i<treeSelectData.length;j++){
              setDisable(children,children[j].id);
            }
          }else {
            const children = node.children;
            if(Tool.isNotEmpty(children)){
              setDisable(children,id);
            }
          }
        }
      }
    }



    /**
    *  编辑
    * */
    const edit = (record: any) =>{
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      treeSelectData.value.unshift({id:0,name:'无'});
    };
    /**
     *  新增
     * */

    const add = () =>{
      modalVisible.value = true;
      doc.value = {
        ebookId:route.query.ebookId
      };
      treeSelectData.value = Tool.copy(level1.value);
      treeSelectData.value.unshift({id:0,name:'无'});
    };

    /**
     *  删除
     * */

    const handleDelete = (id: number) =>{
      axios.delete("/doc/delete/"+ id).then((response)=>{
        const data = response.data; // data = commonResp

        if (data.success){
          // 重新加载列表
          handleQuery();
        }
      });
    };

    return{
      level1,
      columns,
      loading,
      param,
      edit,
      add,
      handleDelete,
      handleQuery,
      doc,
      modalVisible,
      modalLoading,
      handleModalOk,

      treeSelectData
    }
  },
});


</script>