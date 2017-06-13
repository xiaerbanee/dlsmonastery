<template>
  <div>
    <head-tab active="adApplyList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adApply:edit'">{{$t('adApplyList.adApplyForm')}}</el-button>
        <el-button type="primary" @click="itemBillAdd" icon="plus" v-permit="'crm:adApply:edit'">{{$t('adApplyList.adApplyBillForm')}}</el-button>
        <el-button type="primary" @click="grain" icon="plus" v-permit="'crm:adApply:goods'">{{$t('adApplyList.adApplyGoods')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adApply:view'">{{$t('adApplyList.filterOrExport')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('adApplyList.filter')" v-model="formVisible" size="tiny" class="search-form" ref="searchDialog"  z-index="1500">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('adApplyList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="adShop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.productCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.productCode" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.productName')" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.isBilled')" :label-width="formLabelWidth">
                <bool-select v-model="formData.isBilled"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()">{{$t('adApplyList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('adApplyList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adApplyList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('adApplyList.shopName')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adApplyList.createdDate')" sortable></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('adApplyList.createdBy')" sortable></el-table-column>
        <el-table-column column-key="productId" prop="productCode" :label="$t('adApplyList.productCode')" sortable></el-table-column>
        <el-table-column prop="expiryDateRemarks" :label="$t('adApplyList.expiryDateRemarks')"></el-table-column>
        <el-table-column column-key="productId" prop="productName" :label="$t('adApplyList.product')" sortable></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyList.applyQty')" sortable></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adApplyList.confirmQty')" sortable></el-table-column>
        <el-table-column prop="billedQty" :label="$t('adApplyList.billedQty')" sortable></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyList.leftQty')" sortable></el-table-column>
        <el-table-column prop="orderId" :label="$t('adApplyList.orderId')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adApplyList.remarks')"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';
  import boolSelect from 'components/common/bool-select';
  export default {
    components:{
      accountSelect,depotSelect,boolSelect
    },
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        pageHeight:600,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading: false
      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("adApplyList", submitData);
        axios.get('/api/ws/future/layout/adApply',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'adApplyForm'});
      },itemBillAdd(){
        this.$router.push({ name: 'adApplyBillForm'});
      },grain(){
        this.$router.push({name: 'adApplyGoods'});
      },exportData(){
				window.location.href= "/api/crm/adApply/export?"+qs.stringify(this.formData);
      }
    },created () {
        let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/layout/adApply/getQuery').then((response)=>{
        that.formData = response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

