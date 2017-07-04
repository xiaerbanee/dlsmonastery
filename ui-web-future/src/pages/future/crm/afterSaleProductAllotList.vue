<template>
  <div>
    <head-tab active="afterSaleProductAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleProductAllotList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <el-dialog :title="$t('afterSaleProductAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData"  :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('afterSaleProductAllotList.fromProductName')">
                <el-input v-model="formData.fromProductName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.toProductName')">
                <el-input v-model="formData.toProductName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.createdBy')" >
                <el-input v-model="formData.afterSaleId" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.createdDate')">
                <el-input v-model="formData.storeName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.afterSaleBill')">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.toOutCode')" >
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.fromOutCode')">
                <el-input type="textarea" v-model="formData.toOutCode" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleProductAllotList.storeName')" >
                <el-input type="textarea" v-model="formData.fromOutCode" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.blankOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleProductAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleProductAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleProductAllotList.bill')" sortable ></el-table-column>
        <el-table-column prop="afterSale.id" :label="$t('afterSaleProductAllotList.afterSaleBill')" sortable></el-table-column>
        <el-table-column prop="store.name" :label="$t('afterSaleProductAllotList.storeName')" ></el-table-column>
        <el-table-column prop="fromProduct.name" :label="$t('afterSaleProductAllotList.fromProductName')"></el-table-column>
        <el-table-column prop="toProduct.name" :label="$t('afterSaleProductAllotList.toProductName')"></el-table-column>
        <el-table-column prop="toOutCode" :label="$t('afterSaleProductAllotList.toOutCode')" sortable></el-table-column>
        <el-table-column prop="fromOutCode" :label="$t('afterSaleProductAllotList.fromOutCode')" sortable></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('afterSaleProductAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('afterSaleProductAllotList.createdDate')" sortable></el-table-column>
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
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
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
        util.setQuery("afterSaleProductAllotList",submitData);
        axios.get('/api/crm/afterSaleProductAllot',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
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

