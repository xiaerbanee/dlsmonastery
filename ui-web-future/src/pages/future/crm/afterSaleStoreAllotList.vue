<template>
  <div>
    <head-tab active="afterSaleStoreAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">过滤或导出</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('afterSaleStoreAllotList.filter')" v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('afterSaleStoreAllotList.fromStore')" >
                <el-input v-model="formData.fromStoreName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleStoreAllotList.toStore')">
                <el-input v-model="formData.toStoreName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleStoreAllotList.afterSaleBill')" >
                <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleStoreAllotList.productName')" >
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleStoreAllotList.createdDate')" >
                <date-range-picker v-model="formData.createdDate" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('afterSaleStoreAllotList.outCode')" >
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData()">导出</el-button>
          <el-button type="primary" @click="search()">{{$t('afterSaleStoreAllotList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleStoreAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleStoreAllotList.bill')" sortable  ></el-table-column>
        <el-table-column prop="businessId" :label="$t('afterSaleStoreAllotList.afterSaleBill')" sortable></el-table-column>
        <el-table-column prop="productName" :label="$t('afterSaleStoreAllotList.productName')" ></el-table-column>
        <el-table-column prop="fromStoreName" :label="$t('afterSaleStoreAllotList.fromStore')"></el-table-column>
        <el-table-column prop="toStoreName" :label="$t('afterSaleStoreAllotList.toStore')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('afterSaleStoreAllotList.outCode')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('afterSaleStoreAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('afterSaleStoreAllotList.createdDate')" sortable></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '28%',
        formVisible: false,
        pageLoading: false,
        afterSaleStoreAllotList:{}
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
        util.setQuery("afterSaleStoreAllotList",submitData);
        axios.get('/api/ws/future/crm/afterSaleStoreAllot',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },exportData(){
        this.formVisible = false;
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/afterSaleStoreAllot/export?'+qs.stringify(util.deleteExtra(this.formData));
          this.pageRequest();
        }).catch(()=>{});
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/crm/afterSale/getQuery').then((response) =>{
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

