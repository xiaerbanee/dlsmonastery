<template>
  <section class="db">
    <template v-if="!$route.meta.hidden">
      <!-- header start  -->
      <header class="db-header">
        <router-link class="logo" :to="{path: '/'}">{{account.companyName}}</router-link>
        <div class="user-info" v-if="account.id">
          <span><a href="javscript:void(0);" @click="changeLang('zh-cn')">中文</a> / <a href="javscript:void(0);" @click="changeLang('id')">Indonesia</a></span>
          <span v-text="account.loginName"></span>
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
              <dl><dd><a :class="item.menuCategory.icon" v-for="item in menus" :key="item.id" @click="chooseCategory">{{$t('app.'+ item.menuCategory.code)}}</a></dd></dl>
            </div>
          </div>
          <el-menu :default-active="activeMenu" class="db-menu-bar" router unique-opened>
            <template v-for="category in menus" ：key="category.id">
              <div v-show="category.menuCategory.code == activeCategory">
                <template v-for="menuItem in category.menuItems">
                  <el-submenu :index="menuItem.groupName">
                    <template slot="title">{{$t('app.'+ menuItem.groupName)}}</template>
                    <el-menu-item :index="cMenu.menuCode" v-for="(cMenu, cIndex) in menuItem.menus"  :key="cIndex" :route="cMenu">
                      {{$t('app.'+ cMenu.menuCode)}}
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
      activeCategory:''
    };
  },computed: mapState({
    account: state => state.global.account,
    menus: state => state.global.menus,
    lang: state => state.global.lang
  }),
  created() {
     if(this.menus !=null && this.menus.length>0) {
        for (var i in this.menus) { //一級
            var menuCategoryCode = this.menus[i].menuCategory.code;
            var menuItems = this.menus[i].menuItems;
            for(var j in menuItems) { //二級
              for(var k in menuItems[j].menus) {
                var menu = menuItems[j].menus[k];
                menu.name=menu.menuCode
                this.menuMap[menu.code] = menuCategoryCode;
              }
            }
        }
     }
    // set default lang
    Vue.config.lang = this.lang;
  },mounted(){
    //初始化dictEnum
    axios.get('/api/login/getEnumMap').then((response) => {
      window.enumMap = response.data;
    });
    this.getAuthorityList();
    this.getMenus()
  },
  watch: {
    '$route'(to, from) {
        var activeMenu = this.$route.meta.menu;
        if(activeMenu==null) {
          activeMenu = this.$route.name;
        }
        if(this.menus !=null && this.menus.length>0) {
          if(this.menuMap[activeMenu] == null) {
            this.activeCategory = this.menus[0].menuCategory.code;
          } else {
            this.activeCategory = this.menuMap[activeMenu];
          }
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
        axios.get('/api/logout').then((response)=>{
          this.$store.dispatch('clearGlobal');
          this.$router.push({ path: '/login' });
        });
      }).catch(() => {});
    },chooseCategory(e) {
        for(var i in this.menus ){
            if(e.target.innerText == this.$t('app.'+ this.menus[i].menuCategory.code)){
                this.activeCategory = this.menus[i].menuCategory.code;
          }
        }
    },changeLang(lang) {
      this.$store.dispatch('setLang',lang);
      Vue.config.lang = lang;
      this.$router.push({ name: "home"});
    },getAuthorityList() {
      axios.get('/api/login/getAuthorityList').then((response) => {
        this.$store.dispatch('setAuthorityList',response.data);
      });
    },getMenus() {
      axios.get('/api/sys/menu/getMenus').then((response) => {
       var menus=response.data;
       if(menus !=null && menus.length>0) {
          for (var i in menus) {
              var menuCategoryCode =menus[i].menuCategory.code;
              var menuItems =menus[i].menuItems;
              for(var j in menuItems) {
                for(var k in menuItems[j].menus) {
                  var menu = menuItems[j].menus[k];
                  menu.name=menu.menuCode
                }
              }
          }
       }
        this.$store.dispatch('setMenus',menus);
      });
    }
  }
};

</script>

<style lang="scss">
  @import './styles/_variables.scss';
</style>
