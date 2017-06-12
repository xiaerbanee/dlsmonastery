<template>
  <div>
    <head-tab active="shopBuildList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopBuild:view'">{{$t('shopBuildList.filterOrExport')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchBlack')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('shopBuildList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopBuildList.officeName')" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.auditType')" :label-width="formLabelWidth">
                <el-select v-model="formData.auditType" filterable clearable :placeholder="$t('shopBuildList.inputKey')">
                  <el-option v-for="(value,key) in auditTypes" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="adShop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.processFlow')" :label-width="formLabelWidth">
                <process-status-select v-model="formData.processStatus" type="门店建设申请"></process-status-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.fixtureType')" :label-width="formLabelWidth">
                <dict-enum-select v-model="formData.fixtureType" category="装修类别"></dict-enum-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy"></account-select>
              </el-form-item>
              <el-form-item :label="$t('shopBuildList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()" v-permit="'crm:shopBuild:view'" icon="upload">{{$t('shopBuildList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('shopBuildList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopBuildList.loading')" @sort-change="sortChange" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="id" :label="$t('shopBuildList.code')" sortable></el-table-column>
        <el-table-column column-key="officeId" prop="officeName" :label="$t('shopBuildList.officeName')" sortable></el-table-column>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('shopBuildList.shopName')" sortable></el-table-column>
        <el-table-column prop="fixtureType" :label="$t('shopBuildList.fixtureType')" sortable></el-table-column>
        <el-table-column prop="content" :label="$t('shopBuildList.content')" width="150" sortable></el-table-column>
        <el-table-column prop="oldContents"  :label="$t('shopBuildList.oldContents')" sortable></el-table-column>
        <el-table-column prop="newContents" :label="$t('shopBuildList.newContents')" sortable></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopBuildList.processFlow')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.processStatus === '已通过' ? 'primary' : 'danger'"  close-transition>{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('shopBuildList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopBuildList.createdDate')" sortable ></el-table-column>
        <el-table-column column-key="lastModifiedBy" prop="lastModifiedByName" :label="$t('shopBuildList.lastModifiedBy')" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopBuildList.lastModifiedDate')"  sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopBuildList.remarks')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('shopBuildList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopBuild:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopBuildList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.isAuditable&&scope.row.processStatus.indexOf('通过')<0" v-permit="'crm:shopBuild:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopBuildList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable" v-permit="'crm:shopBuild:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopBuildList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable" v-permit="'crm:shopBuild:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopBuildList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import dictEnumSelect from 'components/basic/dict-enum-select';
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';
  import processStatusSelect from 'components/general/process-status-select';
  export default {
    components:{officeSelect,dictEnumSelect,accountSelect,depotSelect,processStatusSelect},
    data() {
      return {
        searchText:"",
        pageLoading: false,
        page:{},
        formData:{
          auditType:1,
        },
        auditTypes:{
          0:this.$t('shopBuildList.all'),
          1:this.$t('shopBuildList.waitAudit')
        },
        multipleSelection:[],
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption
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
        util.setQuery("shopBuildList",submitData);
        axios.get('/api/ws/future/layout/shopBuild',{params:submitData}).then((response) => {
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
      },exportData(){
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/ws/future/layout/shopBuild/export?'+qs.stringify(this.submitData)).then((response)=> {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
      },itemAdd(){
        this.$router.push({ name: 'shopBuildForm'});
      },itemAction:function(id,action){
        if(action=="detail") {
          this.$router.push({ name: 'shopBuildDetail', query:{id: id}});
        }else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopBuild/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }else if(action == "edit"){
          this.$router.push({name: 'shopBuildForm', query:{id: id}});
        }else if(action == "audit"){
          this.$router.push({name: 'shopBuildDetail', query:{id: id,action:action}});
        }
     },handleSelectionChange(val) {
        this.multipleSelection = new Array();
         for(var key in val){
           this.multipleSelection.push(val[key].id);
        }
    },batchPass(){
        if(!this.multipleSelection || this.multipleSelection.length < 1){
          this.$message(this.$t('shopBuildList.noSelectionFound'));
          return ;
        }
        axios.get('/api/ws/future/layout/shopBuild/batchAudit',{params:{ids:this.multipleSelection,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
    },batchBack(){
        if(!this.multipleSelection || this.multipleSelection.length < 1){
          this.$message(this.$t('shopBuildList.noSelectionFound'));
          return ;
        }
        axios.get('/api/ws/future/layout/shopBuild/batchAudit',{params:{ids:this.multipleSelection,pass:false}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
    },
      checkSelectable(row) {
        return row.processStatus.indexOf('通过')<0;
      }
    },
    created () {
        var that = this;
        that.pageHeight = window.outerHeight -320;
        axios.get('/api/ws/future/layout/shopBuild/getQuery',{params:this.formData}).then((response) =>{
           that.formData = response.data;
           util.copyValue(that.$route.query,that.formData);
           that.pageRequest();
        });
    }
  };
</script>
