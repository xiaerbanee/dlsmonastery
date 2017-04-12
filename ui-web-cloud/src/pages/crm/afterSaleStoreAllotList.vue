<template>
  <div>
    <head-tab :active="$t('afterSaleStoreAllotList.afterSaleStoreAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleStoreAllotList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleStoreAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.fromStoreName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromStoreName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toStoreName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toStoreName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.afterSaleId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.afterSaleId" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('afterSaleStoreAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.badProductIme.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.badProductIme" auto-complete="off" :placeholder="$t('afterSaleStoreAllotList.blankOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleStoreAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleStoreAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleStoreAllotList.bill')" sortable  ></el-table-column>
        <el-table-column prop="afterSale.id" :label="$t('afterSaleStoreAllotList.afterSaleBill')" sortable></el-table-column>
        <el-table-column prop="product.name" :label="$t('afterSaleStoreAllotList.productName')" ></el-table-column>
        <el-table-column prop="fromStore.name" :label="$t('afterSaleStoreAllotList.fromStore')"></el-table-column>
        <el-table-column prop="toStore.name" :label="$t('afterSaleStoreAllotList.toStore')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('afterSaleStoreAllotList.outCode')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('afterSaleStoreAllotList.createdBy')"></el-table-column>
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          pageNumber:0,
          pageSize:25,
          fromStoreName:'',
          toStoreName:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          afterSaleId:'',
          outCode:'',
          productName:'',
          badProductIme:''
        },formLabel:{
          fromStoreName:{label:this.$t('afterSaleStoreAllotList.fromStore')},
          toStoreName:{label:this.$t('afterSaleStoreAllotList.toStore')},
          createdBy:{label:this.$t('afterSaleStoreAllotList.createdBy')},
          createdDateBTW:{label:this.$t('afterSaleStoreAllotList.createdDate')},
          afterSaleId:{label:this.$t('afterSaleStoreAllotList.afterSaleBill')},
          outCode:{label:this.$t('afterSaleStoreAllotList.outCode')},
          productName:{label:this.$t('afterSaleStoreAllotList.productName')},
          badProductIme:{label:this.$t('afterSaleStoreAllotList.badProductIme')},
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
        util.setQuery("afterSaleStoreAllotList",this.formData);
        axios.get('/api/crm/afterSaleStoreAllot',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
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

