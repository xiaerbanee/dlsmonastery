<template>
  <div>
    <head-tab active="salaryTemplateList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{$t('salaryTemplateList.add')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('salaryTemplateList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('salaryTemplateList.name')" sortable width="150"></el-table-column>
        <el-table-column prop="salaryTemplateDetails" :label="$t('salaryTemplateList.salaryTemplateDetailNames')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('salaryTemplateList.remarks')" width="140"></el-table-column>
        <el-table-column fixed="right" :label="$t('salaryTemplateList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('salaryTemplateList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('salaryTemplateList.delete')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'export')">{{$t('salaryTemplateList.download')}}</el-button>
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
        axios.get('/api/basic/salary/salaryTemplate?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'salaryTemplateForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'salaryTemplateForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/basic/salary/salaryTemplate/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }else if(action=="export"){
          util.confirmBeforeExportData(this).then(() => {
            window.location.href='/api/basic/salary/salaryTemplate/export?id='+id;
            this.pageRequest();
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/basic/salary/salaryTemplate/getQuery').then((response) =>{
        that.formData=response.data;
      });

    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

