<template>
  <div>
    <head-tab active="goodsOrderImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{ $t('goodsOrderImeList.filter') }}</el-button>
        <el-button type="primary" @click="exportData">{{ $t('goodsOrderImeList.exportData') }}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('goodsOrderImeList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('goodsOrderImeList.createdDate')">
                <date-range-picker  v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.billDate')">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.shipDate')">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.status')">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderImeList.selectStatus')">
                  <el-option v-for="status in formData.extra.statusList" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.remarks')">
                <el-input v-model="formData.remarks" :placeholder="$t('goodsOrderImeList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.imes')">
                <el-input type="textarea" v-model="formData.imes" :placeholder="$t('goodsOrderImeList.multiEnter')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('goodsOrderImeList.createdBy')">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"  ></account-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.store')">
                <el-input v-model="formData.storeName" :placeholder="$t('goodsOrderImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.shop')">
                <el-input v-model="formData.shopName" :placeholder="$t('goodsOrderImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.productName')">
                <el-input v-model="formData.productName" :placeholder="$t('goodsOrderImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.formatId')">
                <el-input v-model="formData.businessId" :placeholder="$t('goodsOrderImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderImeList.lxMallOrder')">
                <bool-select v-model="formData.lxMallOrder"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderImeList.sure') }}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('goodsOrderImeList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column prop="goodsOrderFormatId" :label="$t('goodsOrderImeList.formatId')"></el-table-column>
        <el-table-column prop="goodsOrderCreatedDate" :label="$t('goodsOrderImeList.createdDate')"></el-table-column>
        <el-table-column prop="goodsOrderStoreName" :label="$t('goodsOrderImeList.store')" ></el-table-column>
        <el-table-column prop="goodsOrderShopName" :label="$t('goodsOrderImeList.shop')"></el-table-column>
        <el-table-column prop="productName" :label="$t('goodsOrderImeList.productName')"></el-table-column>
        <el-table-column prop="productImeIme" :label="$t('goodsOrderImeList.productImeIme')"></el-table-column>
        <el-table-column prop="goodsOrderStatus" :label="$t('goodsOrderImeList.status')" ></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import boolSelect from 'components/common/bool-select'
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      boolSelect,
      accountSelect,
    },
    data() {
    return {
      page:{},
      formData:{
          extra:{}
      },
      initPromise:{},
      pageHeight:600,
      searchText:"",
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
      let submitData = util.deleteExtra(this.formData);
      util.setQuery("goodsOrderImeList",submitData);
      axios.get('/api/ws/future/crm/goodsOrderIme?'+qs.stringify(submitData)).then((response) => {
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

    },
    exportData() {
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/crm/goodsOrderIme/export?'+qs.stringify(util.deleteExtra(this.formData));
      }).catch(()=>{});
    }
 },created () {
    this.pageHeight = window.outerHeight -320;
    this.initPromise=axios.get('/api/ws/future/crm/goodsOrderIme/getQuery').then((response) =>{
      this.formData=response.data;
      util.copyValue(this.$route.query,this.formData);
    });
  },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
};
</script>

