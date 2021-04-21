<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/"><router-link to="/">首页</router-link></a-menu-item>
      <a-menu-item key="/admin/user"><router-link to="/admin/user">用户管理</router-link></a-menu-item>
      <a-menu-item key="/admin/ebook"><router-link to="/admin/ebook">电子书管理</router-link></a-menu-item>
      <a-menu-item key="/admin/category"><router-link to="/admin/category">分类管理</router-link></a-menu-item>
      <a-menu-item key="/about"><router-link to="/about">关于我们</router-link></a-menu-item>
      <a class="login-menu" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal v-model:visible="loginModalVisible"
             :confirm-loading="loginModalLoading"
             title="登录"
             @ok="login"
    >
      <a-form :label-col="{ span: 6 }" :model="loginUser" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.username" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>

</template>

<script lang="ts">
import { defineComponent ,ref,computed } from 'vue';
import store from "@/store";
import axios from 'axios';
import { message } from 'ant-design-vue';


declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup(){

    // 登录后保存
    // const user = computed(() => store.state.user);

    // 用来登录
    const loginUser = ref({
      username: "test",
      password: "test123"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };
    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");

          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };


    // 退出登录
    // const logout = () => {
    //   console.log("退出登录开始");
    //   axios.get('/user/logout/' + user.value.token).then((response) => {
    //     const data = response.data;
    //     if (data.success) {
    //       message.success("退出登录成功！");
    //       store.commit("setUser", {});
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      // user,
      // logout
    }

  }
});
</script>

<style>
  .login-menu{
    float: right;
    color: white;
  }
</style>