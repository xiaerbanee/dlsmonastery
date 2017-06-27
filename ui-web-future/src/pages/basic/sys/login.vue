<template lang="html">
  <div id="login-page" @keyup.enter="login">
    <div class="login-form">
      <div class="input-group">
        <div class="title">OA SYSTEM</div>
      </div>
      <div class="input-group">
        <el-input :autofocus="true" :placeholder="$t('login.inputUserName')" v-model="username"></el-input>
      </div>
      <div class="input-group">
        <el-input :placeholder="$t('login.inputPassword')" type="password" v-model="password"></el-input>
      </div>
      <div class="input-group">
        <el-button @click.native="login" type="primary" :loading="isBtnLoading">{{btnText}}</el-button>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        username: '',
        password: '',
        isBtnLoading: false
      };
    },
    computed: {
      btnText() {
        if (this.isBtnLoading) {
          return this.$t('login.loginFor');
        }
        return this.$t('login.login');
      }
    },
    methods: {
      login() {
        var that = this;
        if (!that.username) {
          that.$message.error(this.$t('login.inputUserName'));
          return;
        }
        if (!that.password) {
          that.$message.error(this.$t('login.inputPassword'));
          return;
        }
        that.isBtnLoading = true;
        let data = {
          grant_type:'password',
          username:that.username,
          password:that.password
        };
        axios.post('/user/login',qs.stringify(data)).then((response)=>{
          that.initLogin();
        }).catch(function (error) {
          that.$store.dispatch('clearGlobal');
          that.isBtnLoading = false;
          that.$message.error("用户名或密码不正确");
        });
      },initLogin() {
        var that = this;
        axios.post('/api/basic/hr/account/getAccountInfo').then((response)=>{
          console.log(response.data.account)
          that.$store.dispatch('setAccount',response.data.account);
          that.$store.dispatch('setMenus',response.data.menus);
          that.$store.dispatch('setAuthorityList',response.data.authorityList);
          that.isBtnLoading = false;
          var redirect = that.$route.query.redirect;
          if (redirect) {
            that.$router.push({path: redirect});
          } else {
            that.$router.push({path: "/"});
          }
        })
      }
    },created () {
      if(checkLogin()) {
        this.initLogin();
      }
    }
  };
</script>
<style lang="scss" scoped>
  #login-page {
    width: 100vw;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #1f2d3d;
  }
  .login-form {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 500px;
    height: 400px;
    border-radius: 5px;
    background: white;
    border: 2px solid #8492a6;
  }
  .title {
    color: #20a0ff;
    font-weight: bold;
    font-size: 40px;
    text-align: center;
    line-height: 2.2;
    font-family: sans-serif;
  }

  .input-group {
    margin-top: 25px;
    width: 80%;
  }

  button {
    width: 100%;
  }


</style>
