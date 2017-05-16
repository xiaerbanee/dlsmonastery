<template>
  <div>
    <head-tab active="depotAccountList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('depotAccountList.filterOrExport')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('depotAccountList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('depotAccountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dateRange" type="daterange" align="right" :placeholder="$t('depotAccountList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('depotAccountList.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable  filterable :placeholder="$t('depotAccountList.inputKey')">
                  <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.specialityStore.label" :label-width="formLabelWidth">
                <el-select v-model="formData.specialityStore" filterable clearable :placeholder="$t('depotAccountList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData">{{$t('depotAccountList.exportData')}}</el-button>
          <el-button @click="exportDataDetail">{{$t('depotAccountList.exportDataDetail')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('depotAccountList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotAccountList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="office.name" :label="$t('depotAccountList.officeName')"></el-table-column>
        <el-table-column prop="area.name" :label="$t('depotAccountList.areaName')" ></el-table-column>
        <el-table-column  prop="name" :label="$t('depotAccountList.name')" width="250px"></el-table-column>
        <el-table-column prop="depositMap.qcys" :label="$t('depotAccountList.qcys')"></el-table-column>
        <el-table-column prop="depositMap.qmys" :label="$t('depotAccountList.qmys')" ></el-table-column>
        <el-table-column prop="depositMap.xxbzj" :label="$t('depotAccountList.xxbzj')"></el-table-column>
        <el-table-column prop="depositMap.scbzj" :label="$t('depotAccountList.scbzj')"></el-table-column>
        <el-table-column prop="depositMap.ysjyj" :label="$t('depotAccountList.ysjyj')"></el-table-column>
        <el-table-column fixed="right" :label="$t('depotAccountList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
  </div>
</div>
</template>
<script>
  export default {
    data() {
      return {
        page: {},
        formData:{},
        submitData: {
          page: 0,
          size: 25,
          name: '',
          dateRange: '',
          dutyDateRange: '',
          areaId: '',
          officeId: '',
          specialityStore: ''
        }, formLabel: {
          name: {label: this.$t('depotAccountList.name')},
          dutyDateRange: {label: this.$t('depotAccountList.dateRange')},
          officeId: {label: this.$t('depotAccountList.officeName'), value: ""},
          areaId: {label: this.$t('depotAccountList.areaName'), value: ""},
          specialityStore: {label: this.$t('depotAccountList.isSpecialityStore'), value: ""}
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
        axios.get('/api/ws/future/crm/depot/depotAccountData?' + qs.stringify(this.submitData)).then((response) => {
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
      }, itemAdd(){
        this.$router.push({name: 'dictEnumForm'})
      }, itemAction: function (id, action) {
        if (action == "detail") {
          this.$router.push({name: 'depotAccountDetail', query: {id: id, dateRange: this.formData.dateRange}})
        }
      }

    , shopExportRequest() {
      this.request();
      window.location.href = "/api/crm/depot/shopExport?" + qs.stringify(this.formData);
    }, shopExportDataRequest() {
      this.request();
      window.location.href = "/api/crm/depot/accountExport?" + qs.stringify(this.formData);
    }, exportData(){
      this.formVisible = false;
      this.shopExportRequest();
    }, exportDataDetail(){
      this.formVisible = false;
      this.shopExportDataRequest();
    }, itemAction: function (id, action) {

        this.$router.push({name: 'depotAccountDetail', query: {id: id, dateRange: this.formData.dateRange}})

    }
  },
    created (){
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/depot/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

