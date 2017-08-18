<template>
  <div>
    <head-tab active="receivableList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" v-permit="'crm:receivable:view'" @click="formVisible = true" icon="search">{{$t('receivableList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('receivableList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="28%">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('receivableList.name')">
                <el-input v-model="formData.clientName" :placeholder="$t('receivableList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('receivableList.dateRange')">
                <date-range-picker v-model="formData.dutyDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('receivableList.officeName')">
                <office-select v-model="formData.officeIds" @afterInit="setSearchText" multiple></office-select>
              </el-form-item>
              <el-form-item :label="$t('receivableList.areaName')">
                <office-select v-model="formData.areaId" :remote="false" officeRuleName="办事处" @afterInit="setSearchText"></office-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('receivableList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('receivableList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="depotAreaNames" :label="$t('receivableList.areaName')"></el-table-column>
        <el-table-column prop="depotOfficeNames" :label="$t('receivableList.officeName')"></el-table-column>
        <el-table-column prop="name" :label="$t('receivableList.name')" sortable></el-table-column>
        <el-table-column prop="qcys" :label="$t('receivableList.qcys')"></el-table-column>
        <el-table-column prop="qmys" :label="$t('receivableList.qmys')" ></el-table-column>
        <el-table-column :label="$t('receivableList.operation')" >
          <template scope="scope">
            <div class="action" v-permit="'crm:receivable:view'"><el-button size="small"  @click.native="itemDetail(scope.row.outId)">{{$t('receivableList.detail')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
  </div>
</div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  export default{
    components:{
      officeSelect,
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
        axios.get('/api/ws/future/basic/client/findReceivableList?' + qs.stringify(submitData)).then((response) => {
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
        window.location.href='/api/ws/future/basic/client/depotAccountExportAllDepots?'+qs.stringify(util.deleteExtra(this.formData));
      }).catch(()=>{});
    }, exportConfirmation(){
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/basic/depot/depotAccountExportConfirmation?'+qs.stringify(util.deleteExtra(this.formData));
      }).catch(()=>{});
    }, exportDetail(){
      util.confirmBeforeExportData(this).then(() => {
        window.location.href='/api/ws/future/basic/depot/depotAccountExportDetail?'+qs.stringify(util.deleteExtra(this.formData));
      }).catch(()=>{});
    }
  }, created (){
      this.pageHeight = 0.74*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/basic/client/getReceivableQuery').then((response) =>{
        this.formData=response.data;
        this.formData.accountTaxPermitted = util.isPermit("crm:depot:depotAccountTax");
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

