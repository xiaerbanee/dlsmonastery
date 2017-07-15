<template>
  <div>
    <head-tab active="employeePhoneDepositList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:employeePhoneDeposit:edit'">{{$t('employeePhoneDepositList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'hr:employeePhoneDeposit:view'">{{$t('employeePhoneDepositList.filter')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'hr:employeePhoneDeposit:audit'">{{$t('employeePhoneDepositList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchNoPass" icon="close" v-permit="'hr:employeePhoneDeposit:audit'">{{$t('employeePhoneDepositList.batchNoPass')}}</el-button>
        <el-button type="primary" @click="itemBatchAdd" icon="plus" v-permit="'hr:employeePhoneDeposit:edit'">批量添加</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('employeePhoneDepositList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('employeePhoneDepositList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('employeePhoneDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('employeePhoneDepositList.remarks')" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('employeePhoneDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('employeePhoneDepositList.status')" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('employeePhoneDepositList.selectStatus')">
                  <el-option  v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData()">{{$t('accountList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('employeePhoneDepositList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange" :element-loading-text="$t('employeePhoneDepositList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column type="selection" width="55" disabled="true" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="employeeName" :label="$t('employeePhoneDepositList.employeeName')" width="150"></el-table-column>
        <el-table-column prop="depotName" :label="$t('employeePhoneDepositList.shopName')" ></el-table-column>
        <el-table-column prop="areaName"  :label="$t('employeePhoneDepositList.areaName')"  width="100"></el-table-column>
        <el-table-column prop="department" :label="$t('employeePhoneDepositList.department')"   width="100"></el-table-column>
        <el-table-column prop="bankName" :label="$t('employeePhoneDepositList.bank')"   width="100"></el-table-column>
        <el-table-column prop="amount"  :label="$t('employeePhoneDepositList.amount')" ></el-table-column>
        <el-table-column prop="outCode" :label="$t('employeePhoneDepositList.outCode')"  width="120"></el-table-column>
        <el-table-column prop="billDate" :label="$t('employeePhoneDepositList.billDate')" ></el-table-column>
        <el-table-column prop="productName" :label="$t('employeePhoneDepositList.productName')"  width="120"></el-table-column>
        <el-table-column prop="status" :label="$t('employeePhoneDepositList.status')" ></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('employeePhoneDepositList.lastModifiedBy')" ></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('employeePhoneDepositList.lastModifiedDate')"  width="120"></el-table-column>
          <el-table-column prop="remarks" :label="$t('employeePhoneDepositList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('employeePhoneDepositList.locked')" width="120">
        <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('employeePhoneDepositList.operation')" width="140">
          <template scope="scope" >
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')" v-if="scope.row.isEdit">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'删除')" v-if="!scope.row.locked">删除</el-button>
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
        page:{},
        searchText:"",
        formData:{
          extra:{}
          },
        selects:new Array(),
        formLabelWidth: '120px',
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
        util.setQuery("bankList",submitData);
        axios.get('/api/ws/future/basic/employeePhoneDeposit?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'employeePhoneDepositForm'})
      },itemBatchAdd(){
        this.$router.push({ name: 'employeePhoneDepositBatchForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'employeePhoneDepositForm', query: { id: id }})
        }
      },selectionChange(selection){
       this.selects=new Array();
        for(var key in selection){
           this.selects.push(selection[key].id)
        }
      },batchPass(){
        axios.get('/api/ws/future/basic/employeePhoneDeposit/batchAudit',{params:{ids:this.selects,pass:true}}).then((response) =>{
        this.$message(response.data.message);
          this.pageRequest();
      });
      },batchNoPass(){
        axios.get('/api/ws/future/basic/employeePhoneDeposit/batchAudit',{params:{ids:this.selects,pass:false}}).then((response) =>{
        this.$message(response.data.message);
          this.pageRequest();
      });
      },checkSelectable(row) {
        return row.status === '省公司审核'
      },exportData(){
        this.formVisible = false;
        var submitData = util.deleteExtra(this.formData);
        axios.get('/api/ws/future/basic/employeePhoneDeposit/export?'+qs.stringify(submitData)).then((response)=> {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      axios.get('/api/ws/future/basic/employeePhoneDeposit/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

