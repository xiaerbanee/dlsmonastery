<template>
  <div>
    <head-tab active="salaryList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:dictEnum:edit'">{{$t('dictEnumList.add')}}</el-button>
        <el-input v-model="formData.password" placeholder="请输入用户密码 " style="width:160px"></el-input>
        <el-button type="primary"@click="pageRequest" icon="search" v-permit="'sys:dictEnum:view'">{{$t('dictEnumList.filter')}}</el-button>
      </el-row>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dictEnumList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="officeName" :label="$t('salaryList.officeName')" sortable width="150"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('salaryList.employeeName')"></el-table-column>
        <el-table-column prop="month" :label="$t('salaryList.month')"></el-table-column>
        <el-table-column prop="productName" :label="$t('salaryList.productName')"></el-table-column>
        <el-table-column prop="productValue" :label="$t('salaryList.productValue')"></el-table-column>
        <el-table-column fixed="right" :label="$t('dictEnumList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:dictEnum:edit'">{{$t('dictEnumList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:dictEnum:delete'">{{$t('dictEnumList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import ElInput from "../../../../node_modules/element-ui/packages/input/src/input";
  export default{
    components: {ElInput},
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
        util.setQuery("salaryList",submitData);
        axios.get('/api/basic/hr/salary?'+qs.stringify(submitData)).then((response) => {
          this.page=response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
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
      that.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/salary/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

