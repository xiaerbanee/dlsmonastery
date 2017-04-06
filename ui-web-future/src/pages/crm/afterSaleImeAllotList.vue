<template>
  <div>
    <head-tab :active="$t('afterSaleImeAllotList.afterSaleImeAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleImeAllotList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleImeAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.fromDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromDepotName" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toDepotName" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('afterSaleImeAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.afterSaleId.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.afterSaleId" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.blankOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleImeAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleImeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleImeAllotList.bill')"sortable ></el-table-column>
        <el-table-column prop="afterSale.id" :label="$t('afterSaleImeAllotList.afterSaleBill')" sortable></el-table-column>
        <el-table-column prop="productIme.ime" :label="$t('afterSaleImeAllotList.ime')" sortable></el-table-column>
        <el-table-column prop="fromDepot.name" :label="$t('afterSaleImeAllotList.fromDepot')"></el-table-column>
        <el-table-column prop="toDepot.name" :label="$t('afterSaleImeAllotList.toDepot')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('afterSaleImeAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('afterSaleImeAllotList.createdDate')" sortable></el-table-column>
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
          fromDepotName:'',
          toDepotName:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          afterSaleId:''
        },formLabel:{
          fromDepotName:{label:this.$t('afterSaleImeAllotList.fromDepot')},
          toDepotName:{label:this.$t('afterSaleImeAllotList.toDepot')},
          createdBy:{label:this.$t('afterSaleImeAllotList.createdBy')},
          createdDateBTW:{label:this.$t('afterSaleImeAllotList.createdDate')},
          afterSaleId:{label:this.$t('afterSaleImeAllotList.afterSaleBill')},
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
        util.setQuery("afterSaleList",this.formData);
        axios.get('/api/crm/afterSaleImeAllot',{params:this.formData}).then((response) => {
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

