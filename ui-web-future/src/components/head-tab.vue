<template>
  <div class="el-tabs el-tabs--card">
    <div class="el-tabs__header">
      <div class="el-tabs__nav-wrap">
        <div class="el-tabs__nav-scroll">
          <div class="el-tabs__nav">
            <div class="el-tabs__item">{{$t('head_tab.home')}}</div>
            <div class="el-tabs__item is-closable is-active">枚举字典<span class="el-icon-close"></span></div>
            <div class="el-tabs__item is-closable">枚举字典编辑<span class="el-icon-close"></span></div>
            <div class="el-tabs__item is-closable">打卡记录<span class="el-icon-close"></span></div>
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
      tabNames:[],
      currentActive:this.active
    };
  },methods: {
    headTabClick (tab) {
      this.$router.push({ name: tab.name, query: this.tabs.get(tab.name)})
    },headTabRemove(tabName) {
        this.tabs.delete(tabName);
        var names = this.getTabNames();
        this.$store.dispatch('setTabs',this.tabs);
        if(names.length==0) {
          this.$router.push({ name: "home"})
        } else {
          var routeName = names[names.length-1];
          if(this.currentActive == routeName) {
             this.tabNames = this.getTabNames();
          } else {
            this.$router.push({ name: routeName,query:this.tabs.get(routeName)})
          }
        }
    },getTabNames() {
      var tabNames =new Array();
      for(let key of this.tabs.keys()){
          var item = {key:key};
          if(key == currentActive) {
              item.isActive = true;
          } else {
              item.isActive = false;
          }
        tabNames.push(item);
      }
      return tabNames;
    }
 },created () {
    this.tabs= this.$store.state.global.tabs;
    this.tabNames = this.getTabNames();
  }
};
</script>
