<template>
  <div>
    <head-tab :active="$t('adApplyList.adApplyList')"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adApply:edit'">{{$t('adApplyList.adApplyForm')}}</el-button>
        <el-button type="primary" @click="itemBillAdd" icon="plus" v-permit="'crm:adApply:edit'">{{$t('adApplyList.adApplyBillForm')}}</el-button>
        <el-button type="primary" @click="grain" icon="plus" v-permit="'crm:adApply:goods'">{{$t('adApplyList.adApplyGoods')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adApply:view'">{{$t('adApplyList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('adApplyList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.productCodeList.label" :label-width="formLabelWidth">
                <el-select v-model="formData.productCodeList"  multiple filterable remote :placeholder="$t('adApplyList.inputWord')" :remote-method="getProductList" :loading="remoteLoading" :clearable=true >
                  <el-option v-for="product in productList" :key="product.id" :label="product.code" :value="product.code"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdLoginName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdLoginName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('adApplyList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.isBilled.label" :label-width="formLabelWidth">
                <el-radio-group v-model="formData.isBilled">
                  <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()">{{$t('adApplyList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('adApplyList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adApplyList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="shop.name" :label="$t('adApplyList.shopName')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adApplyList.createdDate')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('adApplyList.createdBy')"></el-table-column>
        <el-table-column prop="product.code" :label="$t('adApplyList.productCode')"></el-table-column>
        <el-table-column prop="expiryDateRemarks" :label="$t('adApplyList.expiryDateRemarks')"></el-table-column>
        <el-table-column prop="product.name" :label="$t('adApplyList.product')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyList.applyQty')"></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adApplyList.confirmQty')"></el-table-column>
        <el-table-column prop="billedQty" :label="$t('adApplyList.billedQty')"></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyList.leftQty')"></el-table-column>
        <el-table-column prop="orderId" :label="$t('adApplyList.orderId')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adApplyList.remarks')"></el-table-column>
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
          shopName:'',
          productCodeList:'',
          createdLoginName:'',
          createdDate:'',
          productName:'',
          isBilled:''
        },formLabel:{
          shopName:{label:this.$t('adApplyList.shopName')},
          productCodeList:{label:this.$t('adApplyList.productCode')},
          createdLoginName:{label:this.$t('adApplyList.createdBy')},
          createdDateBTW:{label:this.$t('adApplyList.createdDate')},
          productName:{label:this.$t('adApplyList.productName')},
          isBilled:{label:this.$t('adApplyList.isBilled'),value:""}
        },
        formProperty:{bools:[]},
        pickerDateOption:util.pickerDateOption,
        productList:[],
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.isBilled.value = util.bool2str(this.formData.isBilled);
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        axios.get('/api/crm/adApply',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'adApplyForm'});
      },itemBillAdd(){
        this.$router.push({ name: 'adApplyBillForm'});
      },grain(){
        this.$router.push({name: 'adApplyGoods'});
      },exportData(){
				window.location.href= "/api/crm/adApply/export?"+qs.stringify(this.formData);
      },getProductList(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/product/search',{params:{code:query}}).then((response)=>{
            this.productList = response.data;
            this.remoteLoading = false;
            })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/crm/adApply/getListProperty').then((response)=>{
        this.formProperty = response.data;
      });
      this.pageRequest();
    }
  };
</script>

