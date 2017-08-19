<template>
  <div>
    <el-table :data="innerContent"  style="margin-top:5px;" v-loading="innerPageLoading" :element-loading-text="$t('simple_process_details.loading')" stripe border>
      <el-table-column prop="simpleProcessId" :label="$t('simple_process_details.simpleProcessId')" ></el-table-column>
      <el-table-column prop="processStatus" :label="$t('simple_process_details.processStatus')" ></el-table-column>
      <el-table-column prop="opinion" :label="$t('simple_process_details.opinion')" ></el-table-column>
      <el-table-column prop="remarks" :label="$t('simple_process_details.remarks')" ></el-table-column>
      <el-table-column prop="createdByName" :label="$t('simple_process_details.createdByName')" ></el-table-column>
      <el-table-column prop="createdDate" :label="$t('simple_process_details.createdDate')"></el-table-column>
    </el-table>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      },
    },
    data() {
      return {
        innerContent : [],
        innerGetUrl:'/api/ws/future/basic/simpleProcess/findBySimpleProcessId',
        innerPageLoading:false,
      };
    } ,methods:{
      pageRequest(){
        this.innerPageLoading = true;
        axios.get(this.innerGetUrl, {params:{simpleProcessId:this.value}}).then((response) => {
          this.innerContent = response.data;
          this.innerPageLoading = false;
        });
      }
    },watch:{
      value:function(){
        this.pageRequest();
      }
    },created () {
      this.pageRequest();
    }
  };
</script>
