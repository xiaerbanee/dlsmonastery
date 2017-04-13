<template>
  <div>
    <head-tab :active="$t('shopGoodsDepositList.shopGoodsDepositList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopGoodsDeposit:edit'">{{$t('shopGoodsDepositList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopGoodsDeposit:audit'">{{$t('shopGoodsDepositList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopGoodsDeposit:view'">{{$t('shopGoodsDepositList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopGoodsDepositList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.bankName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.bankName" auto-complete="off" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('shopGoodsDepositList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('shopGoodsDepositList.inputStatus')">
                  <el-option v-for="item in formProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.amount.label" :label-width="formLabelWidth">
                <el-input v-model="formData.amount" auto-complete="off" :placeholder="$t('shopGoodsDepositList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.outBillType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outBillType" clearable filterable :placeholder="$t('shopGoodsDepositList.inputStatus')">
                  <el-option v-for="item in formProperty.outBillTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopGoodsDepositList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('shopGoodsDepositList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="formatId" :label="$t('shopGoodsDepositList.code')" width="150" ></el-table-column>
        <el-table-column prop="shop.name" :label="$t('shopGoodsDepositList.shopName')" ></el-table-column>
        <el-table-column prop="shop.area.name" :label="$t('shopGoodsDepositList.areaName')"  ></el-table-column>
        <el-table-column prop="departMent" :label="$t('shopGoodsDepositList.department')"></el-table-column>
        <el-table-column prop="bank.name" :label="$t('shopGoodsDepositList.bank')"  ></el-table-column>
        <el-table-column prop="amount" :label="$t('shopGoodsDepositList.amount')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('shopGoodsDepositList.outCode')"  ></el-table-column>
        <el-table-column prop="outBillType" :label="$t('shopGoodsDepositList.outBillType')"></el-table-column>
        <el-table-column prop="billDate"  :label="$t('shopGoodsDepositList.billDate')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('shopGoodsDepositList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopGoodsDepositList.createdDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopGoodsDepositList.remarks')"></el-table-column>
        <el-table-column prop="status" :label="$t('shopGoodsDepositList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status==='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="locked" :label="$t('shopGoodsDepositList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('shopGoodsDepositList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          page:0,
          size:25,
          shopName:'',
          createdBy:'',
          remarks:'',
          bankName:'',
          createdDateBTW:'',
          createdDate:'',
          status:'',
          amount:'',
          outBillType:''
        },formLabel:{
          createdDateBTW:{label: this.$t('shopGoodsDepositList.createdDate')},
          shopName:{label:this.$t('shopGoodsDepositList.shopName')},
          createdBy:{label: this.$t('shopGoodsDepositList.createdBy')},
          remarks:{label:this.$t('shopGoodsDepositList.remarks')},
          bankName:{label:this.$t('shopGoodsDepositList.bank')},
          status:{label:this.$t('shopGoodsDepositList.status')},
          amount:{label:this.$t('shopGoodsDepositList.Deposit')},
          outBillType:{label:this.$t('shopGoodsDepositList.outBillType')}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        util.setQuery("shopGoodsDepositList",this.formData);
        axios.get('/api/crm/shopGoodsDeposit',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'shopGoodsDepositForm'})
      },itemAction:function(id,action){
        if(action=='删除'){
          axios.get('/api/crm/shopGoodsDeposit/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchPass(){
        axios.get('/api/crm/shopGoodsDeposit/audit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },checkSelectable(row) {
        return row.status !== '已通过'
      },getListProperty(){
        axios.get('/api/crm/shopGoodsDeposit/getListProperty').then((response) =>{
          this.formProperty=response.data;
        this.pageRequest();
      });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getListProperty();
    }
  };
</script>

