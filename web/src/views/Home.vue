<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined/>{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
    <div v-show="isShowWelcome" class="welcome">
      <the-welcome></the-welcome>
    </div>

      <a-list v-show="!isShowWelcome" :data-source="ebooks" :grid="{ gutter: 20, column: 3 }" :pagination="false" item-layout="vertical" size="large">

        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span >
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span >
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span >
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{item.name}}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>

          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import TheWelcome from "@/components/the-welcome.vue";

export default defineComponent({
  name: 'Home',
  components:{
    TheWelcome,
  },
  setup() {
    const ebooks = ref();  //ref() 函数让其变为一个响应式的数据
    let categoryId2 = 0;

    const handleQueryEbook =()=>{
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 100,
          categoryId2: categoryId2,
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    }

    onMounted(() => {
      handleQueryCategory();

    })
    const isShowWelcome = ref(true);

    const handleClick = (value:any) =>{
      if(value.key==='welcome'){
        isShowWelcome.value=true;
      }else {
        categoryId2 = value.key;
        isShowWelcome.value=false;
        handleQueryEbook();
      }
    }
    const level1 = ref();
    let categorys: any;

    const handleQueryCategory = () => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/category/all").then((response)=>{
        const data = response.data;
        if(data.success){
          categorys = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(categorys,0);
        }else{
          message.error(data.message);
        }
      });
    };



    return {
      ebooks,
      pagination: {
        onChange: (page: any) => {
          console.log(page)
        },
        pageSize: 100,
      },

      level1,
      handleQueryCategory,
      handleClick,
      isShowWelcome
    }
  }
});
</script>

<style scoped>
.ant-avatar{
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>