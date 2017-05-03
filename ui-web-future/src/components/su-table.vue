<template>
  <div>
    <el-table :data="innerPage.content" :height="innerPageHeight" style="margin-top:5px;" v-loading="innerPageLoading" :element-loading-text="$t('su_table.loading')" @sort-change="sortChange" stripe border>
      <slot></slot>
    </el-table>
    <pageable :page="innerPage" v-on:pageChange="pageChange"></pageable>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      },
      getUrl: {
        required: true
      },

    },
    data() {
      return {
       innerPage : { },
        innerQueryData:{

               page:0,
               size:25,

        },
//        submitQueryData:{},
        innerGetUrl:this.getUrl,
        districts : [],
        innerPageLoading:false
      };
    } ,methods:{
      pageRequest(){
        this.innerPageLoading = true;
//        this.submitQueryData = this.innerQueryData.content;
//        this.submitQueryData.page = this.innerQueryData.page.page;
//        this.submitQueryData.size = this.innerQueryData.page.size;

        axios.get(this.innerGetUrl, {params:this.innerQueryData}).then((response) => {
          this.innerPage = response.data;
          console.log("this.innerPage"+this.innerPage);
          this.innerPageLoading = false;
        })
      }, pageChange(pageNumber, pageSize) {
        this.innerQueryData.page = pageNumber;
        this.innerQueryData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.innerQueryData.order=util.getOrder(column);
        this.innerQueryData.page=0;
        this.pageRequest();
      }
    },watch:{
      value:function(){
          console.log("value");
        this.innerQueryData = Object.assign(this.innerQueryData, this.value);
        this.pageRequest();
      }
    },created () {
      this.innerPageHeight = window.outerHeight -320;
      this.innerQueryData = Object.assign(this.innerQueryData, this.value);
      this.pageRequest();
    }
  };
</script>
