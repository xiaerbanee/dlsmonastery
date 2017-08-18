<template>
  <div>
    <head-tab active="salaryList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:salary:edit'">{{$t('dictEnumList.add')}}</el-button>
        <el-input v-model="formData.password" :placeholder="$t('salaryList.inputPassword')" style="width:160px" type="password"></el-input>
        <el-button type="primary"@click="pageRequest" icon="search">{{$t('dictEnumList.filter')}}</el-button>
      </el-row>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('salaryList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="officeName" :label="$t('salaryList.officeName')" sortable width="150"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('salaryList.employeeName')"></el-table-column>
        <el-table-column prop="month" :label="$t('salaryList.month')"></el-table-column>
        <el-table-column prop="projectName" :label="$t('salaryList.productName')"></el-table-column>
        <el-table-column prop="projectValue" :label="$t('salaryList.productValue')"></el-table-column>
        <el-table-column fixed="right" :label="$t('salaryList.operation')" width="140">
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  export default{

    data() {
      return {
        page:[],
        formData:{},
        pageLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        var submitData=util.deleteExtra(this.formData);
        if(util.isNotBlank(this.formData.password)){
          axios.get('/api/basic/hr/salary?'+qs.stringify(submitData)).then((response) => {
            this.page=response.data
            this.pageLoading = false;
          })
        }else {
          this.pageLoading = false;
        }
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
          this.$router.push("salaryForm")
      }
    },created () {
      var that = this;
      that.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/salary/getQuery').then((response) =>{
        that.formData=response.data;
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

