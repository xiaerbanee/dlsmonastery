<template>
  <div>
    <head-tab active="employeePhoneDepositList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:employeePhoneDeposit:edit'">{{$t('employeePhoneDepositList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:employeePhoneDeposit:view'">{{$t('employeePhoneDepositList.filter')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'hr:employeePhoneDeposit:audit'">{{$t('employeePhoneDepositList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchNoPass" icon="close" v-permit="'hr:employeePhoneDeposit:audit'">{{$t('employeePhoneDepositList.batchNoPass')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('employeePhoneDepositList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.employeeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.employeeName" auto-complete="off" :placeholder="$t('employeePhoneDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.depotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('employeePhoneDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('employeePhoneDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable :placeholder="$t('employeePhoneDepositList.selectStatus')">
                  <el-option  v-for="item in searchProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('employeePhoneDepositList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange" :element-loading-text="$t('employeePhoneDepositList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column type="selection" width="55" disabled="true" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="employee.name" :label="$t('employeePhoneDepositList.employeeName')" width="150"></el-table-column>
        <el-table-column prop="depot.name" :label="$t('employeePhoneDepositList.shopName')" ></el-table-column>
        <el-table-column prop="areaName"  :label="$t('employeePhoneDepositList.areaName')"  width="100"></el-table-column>
        <el-table-column prop="department" :label="$t('employeePhoneDepositList.department')"   width="100"></el-table-column>
        <el-table-column prop="bank.name" :label="$t('employeePhoneDepositList.bank')"   width="100"></el-table-column>
        <el-table-column prop="amount"  :label="$t('employeePhoneDepositList.amount')" ></el-table-column>
        <el-table-column prop="outCode" :label="$t('employeePhoneDepositList.outCode')"  width="120"></el-table-column>
        <el-table-column prop="billDate" :label="$t('employeePhoneDepositList.billDate')" ></el-table-column>
        <el-table-column prop="product.name" :label="$t('employeePhoneDepositList.productName')"  width="120"></el-table-column>
        <el-table-column prop="status" :label="$t('employeePhoneDepositList.status')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('employeePhoneDepositList.remarks')"></el-table-column>
        <el-table-column prop="lastModified.fullName" :label="$t('employeePhoneDepositList.lastModifiedBy')" ></el-table-column>
        <el-table-column prop="lastModifiedByDate" :label="$t('employeePhoneDepositList.lastModifiedDate')"  width="120"></el-table-column>
        <el-table-column prop="locked" :label="$t('employeePhoneDepositList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('employeePhoneDepositList.operation')" width="140">
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
        page:{},
        formData:{
          page:0,
          size:25,
          createdDate:'',
          employeeName:'',
          status:'',
          remarks:'',
          depotName:""
        },formLabel:{
          employeeName:{label:this.$t('employeePhoneDepositList.employeeName')},
          status:{label:this.$t('employeePhoneDepositList.status')},
          remarks:{label:this.$t('employeePhoneDepositList.remarks')},
          depotName:{label:this.$t('employeePhoneDepositList.shopName')}
        },
        searchProperty:{},
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("employeePhoneDepositList",this.formData);
        axios.get('/api/basic/hr/employeePhoneDeposit',{params:this.formData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'employeePhoneDepositForm'})
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
        axios.get('/api/basic/hr/employeePhoneDeposit/batchAudit',{params:{ids:this.selects,pass:true}}).then((response) =>{
        this.$message(response.data.message);
      });
      },batchNoPass(){
        axios.get('/api/basic/hr/employeePhoneDeposit/batchAudit',{params:{ids:this.selects,pass:false}}).then((response) =>{
        this.$message(response.data.message);
      });
      },checkSelectable(row) {
        return row.status === '省公司审核'
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/basic/hr/employeePhoneDeposit/getQuery').then((response) =>{
        this.searchProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

