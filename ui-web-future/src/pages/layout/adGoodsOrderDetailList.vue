<template>
  <div>
    <head-tab active="adGoodsOrderDetailList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderDetailList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderDetailList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('adGoodsOrderDetailList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item :label="$t('adGoodsOrderDetailList.adGoodsOrderIdStr')">
                <el-input v-model="formData.adGoodsOrderIdStr" :placeholder="$t('adGoodsOrderDetailList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.createdDateRange')">
                <date-range-picker v-model="formData.adGoodsOrderCreatedDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.billDateRange')">
                <date-range-picker v-model="formData.adGoodsOrderBillDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.processStatus')">
                <el-select v-model="formData.adGoodsOrderProcessStatus" multiple clearable filterable>
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.remarks')">
                <el-input v-model="formData.adGoodsOrderRemarks" :placeholder="$t('adGoodsOrderDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.productId')">
                <el-input v-model="formData.productName" :placeholder="$t('adGoodsOrderDetailList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('adGoodsOrderDetailList.createdBy')">
                <account-select v-model="formData.adGoodsOrderCreatedBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.storeName')">
                <depot-select v-model="formData.adGoodsOrderStoreId" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.shopName')">
                <depot-select v-model="formData.adGoodsOrderShopId" category="adShop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.areaName')">
                <el-select v-model="formData.adGoodsOrderShopAreaId" clearable>
                  <el-option v-for="item in formData.extra.adGoodsOrderShopAreaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetailList.billType')">
                <el-select v-model="formData.adGoodsOrderBillType" filterable clearable :placeholder="$t('adGoodsOrderDetailList.inputKey')">
                  <el-option v-for="item in formData.extra.adGoodsOrderBillTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adGoodsOrderDetailList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderDetailList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="adGoodsOrderFormatId" column-key="adGoodsOrderId" :label="$t('adGoodsOrderDetailList.adGoodsOrderFormatId')" sortable>
          <template scope="scope">
            <div class="action" v-permit="'crm:adGoodsOrder:view'"><el-button type="text" size="small" @click.native="itemAction(scope.row.adGoodsOrderId,'detail')">{{scope.row.adGoodsOrderFormatId}}</el-button></div>
          </template>
        </el-table-column>
        <el-table-column prop="adGoodsOrderCreatedDate" :label="$t('adGoodsOrderDetailList.createdDate')" ></el-table-column>
        <el-table-column prop="adGoodsOrderBillDate" :label="$t('adGoodsOrderDetailList.billDate')" ></el-table-column>
        <el-table-column prop="adGoodsOrderBillType" :label="$t('adGoodsOrderDetailList.type')" ></el-table-column>
        <el-table-column prop="adGoodsOrderProcessStatus" :label="$t('adGoodsOrderDetailList.processStatus')" ></el-table-column>
        <el-table-column prop="adGoodsOrderShopAreaName" :label="$t('adGoodsOrderDetailList.areaName')" ></el-table-column>
        <el-table-column prop="adGoodsOrderShopName"  :label="$t('adGoodsOrderDetailList.shopName')" ></el-table-column>
        <el-table-column prop="productCode" :label="$t('adGoodsOrderDetailList.productCode')"  ></el-table-column>
        <el-table-column prop="productName" :label="$t('adGoodsOrderDetailList.productName')" ></el-table-column>
        <el-table-column prop="productPrice2" :label="$t('adGoodsOrderDetailList.productPrice2')"></el-table-column>
        <el-table-column prop="qty" :label="$t('adGoodsOrderDetailList.qty')+'('+totalQty+')'"></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adGoodsOrderDetailList.confirmQty')+'('+totalConfirmQty+')'"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderDetailList.billQty')+'('+totalBillQty+')'"></el-table-column>
        <el-table-column prop="productPrice2" :label="$t('adGoodsOrderDetailList.price')"></el-table-column>
        <el-table-column prop="adGoodsOrderRemarks" :label="$t('adGoodsOrderDetailList.adGoodsOrderRemarks')" ></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';

  export default {
    components:{
      accountSelect,
      depotSelect,
    },
    data() {
      return {
        pageLoading: false,
        page:{},
        searchText:'',
        formData:{
            extra:{},
        },
        initPromise:{},
        formVisible: false,
        pageHeight:600,
        totalQty:0,
        totalConfirmQty:0,
        totalBillQty:0,
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
        let submitData = util.deleteExtra(this.formData);
        axios.get('/api/ws/future/layout/adGoodsOrderDetail?'+qs.stringify(submitData)).then((response) => {
          this.getTotalQty(response.data.content);
          this.page = response.data;
          this.pageLoading = false;
        });
      }, pageChange(pageNumber, pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      }, sortChange(column) {
        this.formData.sort = util.getSort(column);
        this.formData.page = 0;
        this.pageRequest();
      }, search() {
        this.formVisible = false;
        this.pageRequest();
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/layout/adGoodsOrderDetail/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }, itemAction: function (id, action) {
        if (action === "detail") {
          this.$router.push({name: 'adGoodsOrderDetail', query: {id: id, action: "detail"}})
        }
      },getTotalQty(content){
        if(content == null){
          return;
        }
        let tempQty = 0;
        let tempConfirmQty = 0;
        let tempBillQty = 0;
        for(let index of content){
          if(util.isNotBlank(index.qty)){
            tempQty += index.qty;
          }
          if(util.isNotBlank(index.confirmQty)){
            tempConfirmQty += index.confirmQty;
          }
          if(util.isNotBlank(index.billQty)){
            tempBillQty += index.billQty;
          }
        }
        this.totalQty = tempQty;
        this.totalConfirmQty = tempConfirmQty;
        this.totalBillQty = tempBillQty;
      }
    },created () {
       this.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/layout/adGoodsOrderDetail/getQuery').then((response) =>{
        this.formData=response.data;
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  }
</script>

