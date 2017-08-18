<template>
  <div>
    <head-tab active="employeeSalaryList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{$t('employeeSalaryList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('employeeSalaryList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('employeeSalaryList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('employeeSalaryList.employeeName')" sortable width="150"></el-table-column>
        <el-table-column prop="category" :label="$t('employeeSalaryList.month')"></el-table-column>
        <el-table-column prop="value" :label="$t('employeeSalaryList.type')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('employeeSalaryList.realGet')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('employeeSalaryList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('employeeSalaryList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('employeeSalaryList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('employeeSalaryList.delete')}}</el-button>
          </template>
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
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("employeeSalaryList",submitData);
        axios.get('/api/basic/sys/employeeSalary?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
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
        this.$router.push({ name: 'employeeSalaryForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'employeeSalaryForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/basic/salary/employeeSalary/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/basic/salary/employeeSalary/getQuery').then((response) =>{
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

