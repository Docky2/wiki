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
      title="分类表单"
      @ok="handleModalOk"
  >
    <a-form :label-col="{ span: 6}" :model="category">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';

export default defineComponent({
  name:'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const loading = ref(false);
    const columns = [
      {
        title: 'id',
        dataIndex: 'id'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
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
     * 一级分类树,children属性就是二级分类
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
      axios.get("/category/all").then((response)=>{
        loading.value = false;
        const data = response.data;

        if(data.success){
          categorys.value = data.content;
          console.log("原始数组：",categorys.value);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value,0);
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
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () =>{
      modalLoading.value = true;
      axios.post("/category/save",category.value).then((response)=>{
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
    *  编辑
    * */
    const edit = (record: any) =>{
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };
    /**
     *  新增
     * */

    const add = () =>{
      modalVisible.value = true;
      category.value = {};

    };

    /**
     *  删除
     * */

    const handleDelete = (id: number) =>{
      axios.delete("/category/delete/"+ id).then((response)=>{
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
      category,
      modalVisible,
      modalLoading,
      handleModalOk
    }
  },
});


</script>