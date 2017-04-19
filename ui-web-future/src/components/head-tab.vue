<template>
  <div class="el-tabs el-tabs--card">
    <div class="el-tabs__header">
      <div class="el-tabs__nav-wrap">
        <div class="el-tabs__nav-scroll">
          <div class="el-tabs__nav">
            <div class="el-tabs__item">{{$t('head_tab.home')}}</div>
            <div  class="el-tabs__item is-closable" v-for="tabName in tabNames"  :key="tabName.key" :data-tab-name="tabName.key" @click.stop="headTabClick" >{{$t('app.' + tabName.key)}}<span class="el-icon-close" :data-tab-name="tabName.key" @click="headTabRemove"></span></div>
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
        currentActive:this.active,
      };
    },methods: {
      headTabClick (event) {
       this.$router.push({ name: event.target.dataset.tabName})
      },
      headTabRemove(event) {
        console.log(this.tabs)
        this.tabs.delete(event.target.dataset.tabName);

        var names = this.getTabNames();
        this.$store.dispatch('setTabs',this.tabs);
        if(names.length==0) {
          this.$router.push({ name: "home"})
        } else {
          var routeName = names[0];
          if(this.currentActive == routeName) {
            this.tabNames = this.getTabNames();
          } else {
              console.log(routeName);
            this.$router.push({ name: routeName.key})
          }
        }
      },
      getTabNames() {
        var tabNames =new Array();
        for(let key of this.tabs.keys()){
          var item = {key:key};
          console.log(key);
          console.log(this.currentActive);
          if(key == this.currentActive) {
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
