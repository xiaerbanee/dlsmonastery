<template>
  <div>
    <head-tab active="accountTaskList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('accountTaskList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('accountTaskList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('accountTaskList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('accountTaskList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('accountTaskList.status')">
                <el-input v-model="formData.status" auto-complete="off" :placeholder="$t('accountTaskList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountTaskList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountTaskList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountTaskList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('accountTaskList.name')"></el-table-column>
        <el-table-column prop="status" :label="$t('accountTaskList.status')"></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('accountTaskList.lastModified')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('accountTaskList.lastModifiedDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('accountTaskList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,scope.row.name,'audit')">审核</el-button>
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
          extra:{},
        },
        initPromise:{},
        searchText: '',
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("accountTaskList",submitData);
        axios.get('/api/general/sys/processTask?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
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
      },itemAction:function(id,name,action){
        if(action=="audit") {
          if("仓库调整"==name){
            this.$router.push({ name: 'depotChangeForm', query: { id: id }})
          }else if("员工调整"==name){
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
          }else if("门店建设"==name){
            this.$router.push({ name: 'shopBuildForm', query: { id: id }})
          }else if("销售收款"==name){
            this.$router.push({ name: 'bankInForm', query: { id: id }})
          }
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/general/sys/processTask/getQuery').then((response)=> {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      })
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

