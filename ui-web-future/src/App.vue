<template>
  <section class="db">
    <template v-if="!$route.meta.hidden">
      <!-- header start  -->
      <header class="db-header">
        <div class="backend">
          <div class="backend-item"><router-link  :to="{path: '/'}">{{token.companyName}}</router-link></div>
          <div class="backend-item" v-for="backend in menus.backendList" :key="backend.id"><a  :class="backend.class" href="#" :data-backend-id="backend.id" @click="changeBackend">{{backend.name}}</a></div>
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
              <dl><dd><a v-for="item in backendModule" :key="item.id" :data-choose-category="item.code" @click="changeModule">{{item.name}}</a></dd></dl>
            </div>
          </div>
          <el-menu :default-active="activeMenu" class="db-menu-bar" router unique-opened>
            <template v-for="category in backendModule" ：key="category.id">
              <div  v-show="category.code == activeCategory">
                <template v-for="menuItem in category.menuCategoryList">
                  <el-submenu :index="menuItem.id">
                    <template slot="title">{{$t('app.'+ menuItem.code)}}</template>
                    <el-menu-item :index="cMenu.code" v-for="(cMenu, cIndex) in menuItem.menuList"  :key="cIndex" :route="cMenu">
                      {{$t('app.'+ cMenu.code)}}
                    </el-menu-item>
                  </el-submenu>
                </template>
              </div>
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
        activeMenu: '',
        activeCategory:'',
        backendModule:[],
        id:""
      };
    },computed: mapState({
      account: state => state.global.account,
      menus: state => state.global.menus,
      lang: state => state.global.lang,
      token: state => state.global.token
    }),
    created() {
      var backendModule = this.menus.backendModuleMap;
      for(let item in backendModule){
        let copyItem="";
        if(!copyItem){
          copyItem=item;
          this.backendModule=backendModule[item];
          if( this.backendModule.length>0){
            this.id=this.backendModule[0].id;
            this.activeCategory = this.backendModule[0].code;
            this.getCategory();
          }
        }
        return;
      }

      // set default lang
      Vue.config.lang = this.lang;
    },mounted(){
    },
    watch: {
      '$route'(to, from) {
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
      },changeModule(e) {
        for(var i in this.backendModule){
          if(e.target.dataset.chooseCategory==  this.backendModule[i].code){
            this.activeCategory = this.backendModule[i].code;
          }
        }
      },changeLang(lang) {
        this.$store.dispatch('setLang',lang);
        Vue.config.lang = lang;
        this.$router.push({ name: "home"});
      },
      changeBackend(event){
        this.backendModule = [];
        this.id = event.target.dataset.backendId;
        this.backendModule = this.menus.backendModuleMap[this.id];
        this.getCategory();
      },
      getCategory(){
        var backendList=this.menus.backendList;
        for(let i in backendList){
          if(this.id==backendList[i].id){
            backendList[i].class="backend-item-active";
          }else{
            backendList[i].class=" ";
          }
        }
        for (let i = 0; i < this.backendModule.length; i++) {
          let backendModuleList = this.backendModule[i].menuCategoryList;
          let backendModuleCode = this.backendModule[i].code;
          for (let j = 0; j < backendModuleList.length; j++) {
            for (let k = 0; k < backendModuleList[j].menuList.length; k++) {
              let menu = backendModuleList[j].menuList[k];
              menu.name=menu.code;
              this.activeCategory = this.backendModule[0].code;
              this.menuMap[menu.code] = backendModuleCode;
            }
          }
        }
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
