<template>
  <div>
    <head-tab active="accountList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:account:edit'">{{$t('accountList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'hr:account:view'">{{$t('accountList.filterOrExport')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('accountList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get" :label-width="formLabelWidth">
          <el-form-item :label="$t('accountList.loginName')">
            <el-input v-model="formData.loginName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('accountList.employeeName')">
            <el-input v-model="formData.employeeName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('accountList.officeName')">
            <office-select v-model="formData.officeId"></office-select>
          </el-form-item>
          <el-form-item :label="$t('accountList.leader')">
            <el-input v-model="formData.leaderName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('accountList.positionName')">
            <el-select v-model="formData.positionId" clearable filterable :placeholder="$t('accountList.selectGroup')">
              <el-option v-for="position in formData.extra.positionList" :key="position.id" :label="position.name" :value="position.id"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData()">{{$t('accountList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('accountList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="type" :label="$t('accountList.type')"></el-table-column>
        <el-table-column column-key="leaderId" prop="loginName" :label="$t('accountList.loginName')"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('accountList.employeeName')"></el-table-column>
        <el-table-column prop="leaderName" sortable :label="$t('accountList.leader')" width="120"></el-table-column>
        <el-table-column prop="officeName" :label="$t('accountList.officeName')"></el-table-column>
        <el-table-column prop="dataScopeOfficeName" :label="$t('accountList.dataScopeOfficeName')"></el-table-column>
        <el-table-column prop="positionName" :label="$t('accountList.positionName')"></el-table-column>
        <el-table-column prop="positionNames" :label="$t('accountList.positionNames')"></el-table-column>
        <el-table-column prop="employeeStatus" :label="$t('accountList.employeeStatus')"></el-table-column>
        <el-table-column fixed="right" :label="$t('accountList.operation')" width="140">
          <template scope="scope">
            <div  class="active"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'hr:account:edit'">{{$t('accountList.edit')}}</el-button></div>
            <div  class="active"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'hr:account:delete'">{{$t('accountList.delete')}}</el-button></div>
            <div  class="active"><el-button size="small" @click.native="itemAction(scope.row.id,'roleEdit')" v-permit="'hr:accountPosition:edit'">{{$t('accountList.roleEdit')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'

  export default {
    components:{officeSelect},
    data() {
      return {
        page:{},
        formData:{
          extra:{}
        },
        searchText:"",
        initPromise:{},
        offices:[],
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
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
        axios.get('/api/basic/hr/account?'+qs.stringify(submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'accountForm'})
      },exportData(){
        this.formVisible = false;
        var submitData = util.deleteExtra(this.formData);
        util.confirmBeforeExportData(this).then(() => {
          window.location.href="/api/basic/hr/account/export?"+qs.stringify(submitData);
        }).catch(()=>{});
      },itemAuthAdd(){
        this.$router.push({name:"accountAuthorityForm"})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'accountForm', query: { id: id }})
        } else  if(action=="roleEdit") {
          this.$router.push({ name: 'accountPositionForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/basic/hr/account/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      var that=this;
      that.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/account/getQuery').then((response) =>{
        that.formData=response.data;
      });
    },activated(){
      this.initPromise.then(() => {
        this.pageRequest();
      });
    }
  };
</script>

