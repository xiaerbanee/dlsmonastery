<template>
  <div>
    <head-tab active="depotAccountList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="formVisible = true" icon="search">{{$t('depotAccountList.filter')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportDetail"  >{{$t('depotAccountList.exportDetail')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportConfirmation" >{{$t('depotAccountList.exportConfirmation')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportAllDepots"  >{{$t('depotAccountList.exportAllDepots')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('depotAccountList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('depotAccountList.name')">
                <el-input v-model="formData.name" :placeholder="$t('depotAccountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotAccountList.dateRange')">
                <date-range-picker v-model="formData.dutyDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('depotAccountList.officeName')">
                <office-select v-model="formData.officeId" @afterInit="setSearchText" ></office-select>
              </el-form-item>
              <el-form-item :label="$t('depotAccountList.areaName')">
                <el-select v-model="formData.areaId" @afterInit="setSearchText" >
                  <el-option v-for="area in formData.extra.areaList" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotAccountList.isSpecialityStore')">
                <bool-select v-model="formData.specialityStore" ></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('depotAccountList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotAccountList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="officeName" column-key="officeId" :label="$t('depotAccountList.officeName')" sortable></el-table-column>
        <el-table-column prop="areaName" column-key="areaId" :label="$t('depotAccountList.areaName')" sortable></el-table-column>
        <el-table-column prop="name" :label="$t('depotAccountList.name')" sortable></el-table-column>
        <el-table-column prop="qcys" :label="$t('depotAccountList.qcys')"></el-table-column>
        <el-table-column prop="qmys" :label="$t('depotAccountList.qmys')" ></el-table-column>
        <el-table-column prop="xxbzj" :label="$t('depotAccountList.xxbzj')"></el-table-column>
        <el-table-column prop="scbzj" :label="$t('depotAccountList.scbzj')"></el-table-column>
        <el-table-column :label="$t('depotAccountList.operation')" >
          <template scope="scope">
            <div class="action" v-permit="'crm:depot:depotAccountData'"><el-button size="small"  @click.native="itemDetail(scope.row.clientOutId)">{{$t('depotAccountList.detail')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
  </div>
</div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  import boolSelect from 'components/common/bool-select'
  export default{
    components:{
      officeSelect,
      boolSelect,
    },
    data() {
      return {
        page: {},
        pageHeight: 600,
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formVisible: false,
        pageLoading: false,
        accountTaxPermitted:false,
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
        //判断是否有tax权限
        submitData.accountTaxPermitted = util.isPermit("crm:depot:depotAccountTax");
        util.setQuery("depotAccountList", submitData);
        axios.get('/api/ws/future/basic/depot/findDepotAccountList?' + qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        }).catch(()=>{
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
      }, itemDetail(clientOutId) {
          this.$router.push({name: 'depotAccountDetail', query: {clientOutId: clientOutId, dateRange:  this.formData.dutyDateRange }});
      }, exportAllDepots() {
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/basic/depot/depotAccountExportAllDepots?'+qs.stringify({dutyDateRange:this.formData.dutyDateRange, accountTaxPermitted:this.accountTaxPermitted});
      }).catch(()=>{});
    }, exportConfirmation(){
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/basic/depot/depotAccountExportConfirmation?'+qs.stringify({dutyDateRange:this.formData.dutyDateRange,specialityStore: this.formData.specialityStore, accountTaxPermitted:this.accountTaxPermitted});
      }).catch(()=>{});
    }, exportDetail(){
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/basic/depot/depotAccountExportDetail?'+qs.stringify({dutyDateRange:this.formData.dutyDateRange,specialityStore: this.formData.specialityStore, accountTaxPermitted:this.accountTaxPermitted});
      }).catch(()=>{});
    }
  }, created (){
      this.accountTaxPermitted = util.isPermit("crm:depot:depotAccountTax");
      this.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/basic/depot/getDepotAccountQuery').then((response) =>{
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

