<template>
  <div>
    <head-tab active="afterSaleProductAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleProductAllotList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleProductAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.fromProductName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromProductName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toProductName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toProductName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.afterSaleId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.afterSaleId" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.storeName" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('afterSaleProductAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.toOutCode.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.toOutCode" auto-complete="off" :placeholder="$t('afterSaleProductAllotList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.fromOutCode.label" :label-width="formLabelWidth">
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          page:0,
          size:25,
          fromProductName:'',
          toProductName:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          afterSaleId:'',
          toOutCode:'',
          fromOutCode:'',
          storeName:''
        },formLabel:{
          fromProductName:{label:this.$t('afterSaleProductAllotList.fromProductName')},
          toProductName:{label:this.$t('afterSaleProductAllotList.toProductName')},
          createdBy:{label:this.$t('afterSaleProductAllotList.createdBy')},
          createdDateBTW:{label:this.$t('afterSaleProductAllotList.createdDate')},
          afterSaleId:{label:this.$t('afterSaleProductAllotList.afterSaleBill')},
          toOutCode:{label:this.$t('afterSaleProductAllotList.toOutCode')},
          fromOutCode:{label:this.$t('afterSaleProductAllotList.fromOutCode')},
          storeName:{label:this.$t('afterSaleProductAllotList.storeName')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("afterSaleProductAllotList",this.formData);
        axios.get('/api/crm/afterSaleProductAllot',{params:this.formData}).then((response) => {
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
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

