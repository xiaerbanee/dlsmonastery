<template>
  <el-tabs  :value="currentActive" @tab-click="headTabClick" @tab-remove="headTabRemove">
    <el-tab-pane :label="$t('head_tab.home')" name="home"></el-tab-pane>
    <el-tab-pane  closable :label="$t('app.' + tabName)" :name="tabName" v-for="tabName in tabNames" :key="tabName"></el-tab-pane>
  </el-tabs>
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
        tabNames.push(key);
      }
      return tabNames;
    }
 },created () {
    this.tabs= this.$store.state.global.tabs;
    this.tabNames = this.getTabNames();
  }
};
</script>
