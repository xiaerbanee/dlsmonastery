<template>
  <div>
    <el-table :data="innerContent"  style="margin-top:5px;" v-loading="innerPageLoading" :element-loading-text="$t('su_process_details.loading')" stripe border>
      <el-table-column prop="processStatus" :label="$t('su_process_details.processStatus')" ></el-table-column>
      <el-table-column prop="auditByName" :label="$t('su_process_details.auditByName')" ></el-table-column>
      <el-table-column prop="auditDate" :label="$t('su_process_details.auditDate')"></el-table-column>
      <el-table-column prop="comment" :label="$t('su_process_details.comment')" ></el-table-column>
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
        innerProcessInstanceId : this.value,
        innerGetUrl:'/api/general/sys/activiti/getActivitiDetail',
        innerPageLoading:false,
      };
    } ,methods:{
      pageRequest(){
        this.innerPageLoading = true;
        axios.get(this.innerGetUrl, {params:{processInstanceId:this.innerProcessInstanceId}}).then((response) => {
          this.innerContent = response.data;
          this.innerPageLoading = false;
        })
      }
    },watch:{
      value:function(){
        this.innerProcessInstanceId = this.value;
        this.pageRequest();
      }
    },created () {
      this.pageRequest();
    }
  };
</script>
