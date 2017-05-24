<template>
  <div>
    <head-tab active="depotAccountList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="formVisible = true" icon="search">{{$t('depotAccountList.filter')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportDetail"  >{{$t('depotAccountList.exportDetail')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportConfirmation" >{{$t('depotAccountList.exportConfirmation')}}</el-button>
        <el-button type="primary" v-permit="'crm:depot:depotAccountData'"  @click="exportAllDepots"  >{{$t('depotAccountList.exportAllDepots')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('depotAccountList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('depotAccountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.dutyDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"  ></office-select>
              </el-form-item>
              <el-form-item :label="formLabel.specialityStore.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.specialityStore" ></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('depotAccountList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotAccountList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="officeName" :label="$t('depotAccountList.officeName')"></el-table-column>
        <el-table-column prop="areaName" :label="$t('depotAccountList.areaName')" ></el-table-column>
        <el-table-column  prop="name" :label="$t('depotAccountList.name')" width="250px"></el-table-column>
        <el-table-column prop="qcys" :label="$t('depotAccountList.qcys')"></el-table-column>
        <el-table-column prop="qmys" :label="$t('depotAccountList.qmys')" ></el-table-column>
        <el-table-column prop="xxbzj" :label="$t('depotAccountList.xxbzj')"></el-table-column>
        <el-table-column prop="scbzj" :label="$t('depotAccountList.scbzj')"></el-table-column>
        <el-table-column prop="ysjyj" :label="$t('depotAccountList.ysjyj')"></el-table-column>
        <el-table-column fixed="right" :label="$t('depotAccountList.operation')" width="140">
          <template scope="scope">
            <el-button size="small"  type="text"  v-permit="'crm:depot:depotAccountData'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('depotAccountList.detail')}}</el-button>
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
        formData:{},
        submitData: {
          page: 0,
          size: 25,
          name: '',
          dutyDateRange: '',
          officeId: '',
          specialityStore: ''
        }, formLabel: {
          name: {label: this.$t('depotAccountList.name')},
          dutyDateRange: {label: this.$t('depotAccountList.dateRange')},
          officeId: {label: this.$t('depotAccountList.officeName')},
          specialityStore: {label: this.$t('depotAccountList.isSpecialityStore')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData, this.submitData);
        util.setQuery("depotAccountList", this.submitData);
        axios.get('/api/ws/future/basic/depot/findDepotAccountList?' + qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
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
      }, itemAction: function (id, action) {
        if(action=="detail") {
          this.$router.push({name: 'depotAccountDetail', query: {id: id, dateRange: this.submitData.dateRange}});
        }
      }, exportAllDepots() {

      util.confirmBeforeExportData(this).then(() => {
        axios.get('/api/ws/future/basic/depot/depotAccountExportAllDepots', {params:{dutyDateRange:this.submitData.dutyDateRange}}).then((response)=> {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
      }).catch(()=>{});

    }, exportConfirmation(){
      util.confirmBeforeExportData(this).then(() => {
        axios.get('/api/ws/future/basic/depot/depotAccountExportConfirmation', {params:{dutyDateRange:this.submitData.dutyDateRange,specialityStore: this.submitData.specialityStore}}).then((response) => {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
      }).catch(()=>{});

    }, exportDetail(){
      util.confirmBeforeExportData(this).then(() => {
        axios.get('/api/ws/future/basic/depot/depotAccountExportDetail', {params:{dutyDateRange:this.submitData.dutyDateRange,specialityStore: this.submitData.specialityStore}}).then((response) => {
          window.location.href = "/api/general/sys/folderFile/download?id=" + response.data;
        });
      }).catch(()=>{});
    }
  }, created (){
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/depot/getDepotAccountQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

