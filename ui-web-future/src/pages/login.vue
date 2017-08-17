<template>
  <div id="login-page" @keyup.enter="login">
    <el-form :model="loginForm" :rules="rules" ref="loginForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
      <h3 class="title">BUSINESS SYSTEM</h3>
      <el-form-item prop="company">
        <el-select v-model="loginForm.companyName" clearable style="width:100%">
          <el-option v-for="item in companyNames" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button type="primary" style="width:100%;" @click.native.prevent="login" :loading="logining">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        companyNames:[],
        logining: false,
        loginForm: {
          companyName:'',
          username: '',
          password: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        }
      };
    },
    methods: {
      login() {
        var that = this;
        if(!that.loginForm.companyName){
            that.$message.error("请选择公司");
            return;
        }
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            that.logining = true;
            axios.post('/user/login',qs.stringify(that.loginForm)).then((response)=>{
                that.logining = false;
                if(response.data.success){
                    that.initLogin();
                }else {
                    that.$store.dispatch('clearGlobal');
                    that.$message.error("用户名或密码不正确");
                }
            }).catch(function () {
                that.logining = false;
                that.$store.dispatch('clearGlobal');
                that.$message.error("用户名或密码不正确");
            });
          } else {
            return false;
          }
        });
      },initLogin() {
            var that = this;
            axios.post('/api/basic/hr/account/getAccountInfo').then((response)=>{
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
          var that=this;
          axios.get('/user/isLogin').then((response)=>{
              if(response.data) {
                  that.initLogin();
              } else {
                  axios.get('/user/getCompanyNames').then((response)=>{
                      that.companyNames=response.data;
                  });
              }
          });
      }
  }

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
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>