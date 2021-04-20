<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form :model="param" layout="inline">
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查询
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
                   v-if="level1.length > 0"
                   :columns="columns"
                   :data-source="level1"
                   :loading="Loading"
                   :pagination="false"
                   :row-key="record => record.id"
                   :default-expand-all-rows="true"
          >
            <template #name="{ text,record }">
                {{record.sort}} {{text}}
              </template>
            <template v-slot:action="{ text,record}">
              <a-space size="small">
                <a-button size="small" type="primary" @click="edit(record)">
                  编辑
                </a-button>
                <a-popconfirm
                    cancel-text="否"
                    ok-text="是"
                    title="删除后不可恢复，确认删除?"
                    @confirm="handleDelete(record.id)"
                ><a-button size="small" type="danger">
                  删除
                </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>

        </a-col>
        <a-col :span="16">
          <p>
            <a-form :moel="param" layout="inline">
            <a-form-item>
              <a-button type="primary" @click="handleSave()">
                保存
              </a-button>
            </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical" >
            <a-form-item label="名称:">
              <a-input v-model:value="doc.name" />
            </a-form-item>

            <a-form-item label="父文档:">
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

            <a-form-item label="顺序:" >
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item label="内容:">
              <div id="content" ></div>
            </a-form-item>
          </a-form>

        </a-col>

      </a-row>


    </a-layout-content>
  </a-layout>

</template>


<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';
import {useRoute} from "vue-router";
import { Modal } from 'ant-design-vue';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { createVNode } from 'vue';
import E from 'wangeditor';

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
        dataIndex: 'name',
        slots: { customRender: 'name' }
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
    level1.value = [];

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/doc/find-content/"+ doc.value.id).then((response)=>{
        loading.value = false;
        const data = response.data;

        if(data.success){
          editor.txt.html(data.content);
        }else{
          message.error(data.message);
        }

      });
    };

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
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
        }else{
          message.error(data.message);
        }

      });
    };

    onMounted(()=>{
      handleQuery();
      setTimeout(function (){
        editor.create();
      },100)
    })


    // --------- 表单 ----------
    const treeSelectData = ref();
    treeSelectData.value=[];
    const doc = ref();
    doc.value = {};
    const modalLoading = ref(false);
    const editor = new E('#content')
    editor.config.zIndex = 0;
    const handleSave = () =>{
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save",doc.value).then((response)=>{
        const data = response.data; // data = commonResp
        modalLoading.value = false;
        if (data.success){
          message.success("保存成功！");
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
            for(let j=0;j<children.length;j++){
              setDisable(children,children[j].id);
            }
          }
        }else {
          const children = node.children;
          if(Tool.isNotEmpty(children)){
              setDisable(children,id);
            }
          }
        }
      }


    const deleteIds : Array<string>= [];
    const getDeleteIds = (treeSelectData: any,id:any) =>{
      //遍历数组,即遍历某一层节点
      for (let i = 0;i<treeSelectData.length;i++){
        const node = treeSelectData[i];
        if(node.id===id){
          deleteIds.push(id);
          deleteNames.push(node.name);
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0;j<children.length;j++){
              getDeleteIds(children,children[j].id);
            }
          }
        }else {
            const children = node.children;
            if(Tool.isNotEmpty(children)){
              getDeleteIds(children,id);
            }
          }
        }
      };



    /**
    *  编辑
    * */
    const edit = (record: any) =>{
      editor.txt.clear();
      doc.value = Tool.copy(record);
      handleQueryContent()
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      treeSelectData.value.unshift({id:0,name:'无'});


    };
    /**
     *  新增
     * */

    const add = () =>{
      editor.txt.clear();

      doc.value = {
        ebookId:route.query.ebookId
      };
      treeSelectData.value = Tool.copy(level1.value);
      treeSelectData.value.unshift({id:0,name:'无'});



    };

    /**
     *  删除
     * */

    const deleteNames: Array<string> = [];

    const handleDelete = (id: number) =>{
      deleteIds.length=0;
      deleteNames.length=0;
      getDeleteIds(level1.value,id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
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
      modalLoading,
      handleSave,

      treeSelectData
    }
  },
});


</script>