<template>
  <section class="db">
    <template v-if="!$route.meta.hidden">
      <!-- header start  -->
      <header class="db-header">
        <div class="backend">
          <div class="backend-item"><router-link  :to="{path: '/'}">{{token.companyName}}</router-link></div>
          <div class="backend-item" v-for="backend in backendList" :key="backend.id"><a  :class="backend.class" href="#" :data-code="backend.code" @click="changeBackend">{{backend.name}}</a></div>
        </div>
        <div class="user-info">
          <span><a href="javscript:void(0);" @click="changeLang('zh-cn')">中文</a> / <a href="javscript:void(0);" @click="changeLang('id')">Indonesia</a></span>
          <span>{{account.loginName}}</span>
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              <img :src="account.avatar">
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>{{$t('app.personMessage')}}</el-dropdown-item>
              <el-dropdown-item>{{$t('app.settings')}}</el-dropdown-item>
              <el-dropdown-item @click.native="logout">{{$t('app.logout')}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </header>
      <!-- header end  -->
      <!-- body start  -->
      <div class="db-body">
        <!-- menu start -->
        <aside class="db-menu-wrapper">
          <div class="db-menu-category">
            <div id="fixed">
              <dl><dd><a v-for="backendModule in backendModuleList" :key="backendModule.id" :data-code="backendModule.code" @click="changeBackendModule">{{backendModule.name}}</a></dd></dl>
            </div>
          </div>
          <el-menu :default-active="activeMenu" class="db-menu-bar" router unique-opened>
            <template v-for="menuCategory in menuCategoryList" ：key="category.id">
              <el-submenu :index="menuCategory.id">
                <template slot="title">{{$t('app.'+ menuCategory.code)}}</template>
                <el-menu-item :index="menu.code" v-for="(menu, index) in menuCategory.menuList"  :key="index" :route="menu">
                  {{$t('app.'+ menu.code)}}
                </el-menu-item>
              </el-submenu>
            </template>
          </el-menu>
        </aside>
        <!-- menu end -->
        <!-- content start -->
        <div class="db-content-wrapper">
          <section class="db-content">
            <router-view></router-view>
          </section>
        </div>
        <!-- content end -->
      </div>
      <!-- body end  -->
    </template>
    <template v-else>
      <router-view></router-view>
    </template>
    <vue-progress-bar></vue-progress-bar>
  </section>
</template>

<script>
  import Vue from 'vue';
  import { mapState } from 'vuex'
  export default {
    data() {
      return {
        menuMap:{},
        backendMap:{},
        backendModuleMap:{},
        backendList:[],
        activeBackend:'',
        backendModuleList:[],
        activeBackendModule:'',
        menuCategoryList:[],
        activeMenu:''
      };
    },computed: mapState({
      account: state => state.global.account,
      menus: state => state.global.menus,
      lang: state => state.global.lang,
      token: state => state.global.token
    }),
    created() {
    },mounted(){
    },
    watch: {
      '$route'(to) {
        //初始化菜单
        if(this.backendList.length==0 && this.menus.length != 0) {
          var menuMap = {};
          var backendMap = {};
          var backendModuleMap = {};
          for (var i in this.menus) {
            var backend= this.menus[i];
            backendMap[backend.code] = backend;
            for(var j in backend.backendModuleList) {
              var backendModule = backend.backendModuleList[j];
              backendModuleMap[backendModule.code] = backendModule;
              for(var k in backendModule.menuCategoryList) {
                var menuCategory = backendModule.menuCategoryList[k];
                for(var l in menuCategory.menuList) {
                  var menu = menuCategory.menuList[l];
                  menu.name = menu.code;
                  menuMap[menu.code]=menu;
                }
              }
            }
          }
          this.backendList = this.menus;
          menuMap["home"] = {
            "backendCode":this.backendList[0].code,
            "backendModuleCode":this.backendList[0].backendModuleList[0].code
          };
          this.menuMap = menuMap;
          this.backendMap = backendMap;
          this.backendModuleMap = backendModuleMap;
        }
        if( to.name!="login"){
          var activeMenu = to.meta.menu;
          if(activeMenu==null) {
            if(this.$route.name){
              activeMenu = this.$route.name;
            }else{
              activeMenu="home"
            }
          }
        }else{
          activeMenu="login";
        }
        if(this.menuMap[activeMenu] != null) {
            var activeBackend = this.menuMap[activeMenu].backendCode;
            var activeBackendModule = this.menuMap[activeMenu].backendModuleCode;
            this.backendModuleList = this.backendMap[activeBackend].backendModuleList;
            this.menuCategoryList = this.backendModuleMap[activeBackendModule].menuCategoryList;
            this.activeBackend=activeBackend;
            this.activeBackendModule=activeBackendModule;
        }
        this.activeMenu = activeMenu;
      }
    },
    methods: {
      logout() {
        this.$confirm(this.$t('app.isLogout'), this.$t('app.tip'), {
          confirmButtonText: this.$t('app.sure'),
          cancelButtonText: this.$t('app.cancel'),
          type: 'info'
        }).then(() => {
          this.$store.dispatch('clearGlobal');
          this.$router.push({ name: 'login' });
        }).catch(() => {});
      },changeLang(lang) {
        this.$store.dispatch('setLang',lang);
        Vue.config.lang = lang;
        this.$router.push({ name: "home"});
      },changeBackendModule(event) {
        var activeBackendModule = event.target.dataset.code;
        this.menuCategoryList = this.backendModuleMap[activeBackendModule].menuCategoryList;
        this.activeBackendModule=activeBackendModule;
      },
      changeBackend(event){
        var activeBackend = event.target.dataset.code;
        var activeBackendModule = this.backendMap[activeBackend].backendModuleList[0].code;
        this.backendModuleList = this.backendMap[activeBackend].backendModuleList;
        this.menuCategoryList = this.backendModuleMap[activeBackendModule].menuCategoryList;
        this.activeBackend=activeBackend;
        this.activeBackendModule=activeBackendModule;

      }
    }
  };

</script>

<style lang="scss">
  @import './styles/_variables.scss';
  .backend{
    position:absolute;
    left:30px;
    width:auto;
    height:auto;
  }
  .backend-item{
    font-size: 2.4rem;
    display: inline-block;
    width:100px;
    height:30px;
    margin-right:20px;
    text-align: center;
    color:#fff;
    line-height: 30px;
    font-size:17px;
    z-index: 9999;
  }
  .backend-item a:hover{
    color:#fff;
    padding-bottom:16px;
    border-bottom:4px solid #20A0FF;
    cursor:pointer
  }
  .backend-item-active{
    color:#20A0FF;
    padding-bottom:16px;
  }
</style>
