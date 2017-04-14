<template>
  <div>
    <head-tab :active="$t('accountTaskList.accountTaskList')"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('accountTaskList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('accountTaskList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('accountTaskList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-input v-model="formData.status" auto-complete="off" :placeholder="$t('accountTaskList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountTaskList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountTaskList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountTaskList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('accountTaskList.name')"></el-table-column>
        <el-table-column prop="status" :label="$t('accountTaskList.status')"></el-table-column>
        <el-table-column prop="lastModified.loginName" :label="$t('accountTaskList.lastModified')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('accountTaskList.lastModifiedDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('accountTaskList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>             <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page: {},
        formData: {
          pageNumber: 0,
          pageSize: 25,
          name: '',
          status: ''
        },formLabel: {
          name: {label: this.$t('accountTaskList.name')},
          status: {label: this.$t('accountTaskList.status')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      }
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.getQuery("accountTaskList");
        util.setQuery("accountTaskList",this.formData);
        axios.get('/api/hr/accountTask',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAction:function(id,action){
        if(action=="审核") {
          var name=this.inputForm.name;
          if("仓库调整"==name){
            this.$router.push({ name: 'depotChangeForm', query: { id: id }})
          }else if("员工信息调整"==name){
            this.$router.push({ name: 'employeeForm', query: { id: id }})
          }else if("广告印刷"==name){
            this.$router.push({ name: 'shopPrintForm', query: { id: id }})
          }else if("广告申请"==name){
            this.$router.push({ name: 'shopAdForm', query: { id: id }})
          }else if("文件审批"==name){
            this.$router.push({ name: 'auditFileForm', query: { id: id }})
          }else if("员工工资模版调整"==name){
            this.$router.push({ name: 'depotChangeForm', query: { id: id }})
          }else if("机构调整"==name){
            this.$router.push({ name: '机构调整编辑', query: { id: id }})
          }else if("门店建设申请"==name){
            this.$router.push({ name: 'shopBuildForm', query: { id: id }})
          }
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

