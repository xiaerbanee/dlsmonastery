<template>
  <div class="el-tabs el-tabs--card">
    <div class="el-tabs__header">
      <div class="el-tabs__nav-wrap">
        <div class="el-tabs__nav-scroll">
          <div class="el-tabs__nav">
            <div class="el-tabs__item" :class="homeActive" data-tab-name="home" @click.stop="headTabClick">{{$t('head_tab.home')}}</div>
            <div  class="el-tabs__item is-closable"  :class="tabItem.class" v-for="tabItem in tabList"   :key="tabItem.name" :data-tab-name="tabItem.name" @click.stop="headTabClick" >
              {{$t('menus.menu.' + tabItem.name)}}<span class="el-icon-close" :data-tab-name="tabItem.name" @click.stop="headTabRemove"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    props: ['active'],
    data() {
      return {
        tabs : [],
        tabList:[],
        currentActive:this.active
      }
    },computed: {
        homeActive:function () {
          if(this.active=="home") {
              return "is-active";
          } else {
              return "";
          }
        }
    },methods: {
      headTabClick (event) {
        let name=event.target.dataset.tabName;
        this.$router.push({ name: name, query: this.tabs.get(name)})
      },
      headTabRemove(event) {
        this.tabs.delete(event.target.dataset.tabName);
        var list = this.getTabList();
        this.$store.dispatch('setTabs',this.tabs);
        if(list.length==0) {
          this.$router.push({ name: "home"})
        } else {
          var tabItem = list[list.length-1];
          if(this.currentActive == tabItem.name) {
            this.tabList = this.getTabList();
          } else {
            this.$router.push({ name: tabItem.name})
          }
        }
      },
      getTabList() {
        var tabList =new Array();
        for(let key of this.tabs.keys()){
          var item = {name:key};
          if(key == this.currentActive) {
            item.class = "is-active";
          } else {
            item.class = "";
          }
          tabList.push(item);
        }
        return tabList;
      }
    },created () {
      this.tabs= this.$store.state.global.tabs;
      this.tabList = this.getTabList();
    }
  };
</script>
