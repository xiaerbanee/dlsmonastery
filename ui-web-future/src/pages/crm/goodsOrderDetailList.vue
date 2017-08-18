<template>
  <div>
    <head-tab active="goodsOrderDetailList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{ $t('goodsOrderDetailList.filter') }}</el-button>
        <el-button type="primary" @click="exportData">{{ $t('goodsOrderDetailList.exportData') }}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('goodsOrderDetailList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('goodsOrderDetailList.productName')">
                <el-input v-model="formData.productName" :placeholder="$t('goodsOrderDetailList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderDetailList.depotName')">
                <el-input v-model="formData.depotName" :placeholder="$t('goodsOrderDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderDetailList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderDetailList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderDetailList.sure') }}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('goodsOrderDetailList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column prop="bussinessId" :label="$t('goodsOrderDetailList.bussinessId')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('goodsOrderDetailList.createdDate')"></el-table-column>
        <el-table-column prop="storeName" :label="$t('goodsOrderDetailList.storeName')" ></el-table-column>
        <el-table-column prop="depotName" :label="$t('goodsOrderDetailList.depotName')"></el-table-column>
        <el-table-column prop="productName" :label="$t('goodsOrderDetailList.productName')"></el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderDetailList.price')"></el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderDetailList.qty')" ></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderDetailList.billQty')" ></el-table-column>
        <el-table-column prop="shippedQty" :label="$t('goodsOrderDetailList.shippedQty')" ></el-table-column>
        <el-table-column prop="amount" :label="$t('goodsOrderDetailList.totalPrice')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderDetailList.status')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderDetailList.remarks')"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
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
        axios.get('/api/ws/future/crm/goodsOrderDetail?'+qs.stringify(submitData)).then((response) => {
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
          window.location.href='/api/ws/future/crm/goodsOrderDetail/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    },created () {
      this.pageHeight = 0.74*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/crm/goodsOrderDetail/getQuery').then((response) =>{
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

