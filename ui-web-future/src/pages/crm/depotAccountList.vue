<template>
  <div>
    <head-tab :active="$t('depotAccountList.depotAccountList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('depotAccountList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
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
        page:{},
        formData:{
          page:0,
          size:25,
          name:'',
          dateRange:'',
          dutyDateBTW:'',
          areaId:'',
          officeId:'',
          specialityStore:''
        },formLabel:{
          name:{label:this.$t('depotAccountList.name')},
          dutyDateBTW:{label:this.$t('depotAccountList.dateRange')},
          officeId:{label: this.$t('depotAccountList.officeName'),value:""},
          areaId:{label: this.$t('depotAccountList.areaName'),value:""},
          specialityStore:{label: this.$t('depotAccountList.isSpecialityStore'),value:""}
        },
        offices:[],
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      request(){
        this.pageLoading = true;
        util.setQuery("depotAccountList",this.formData);
        if(this.formProperty.dateRange==this.formData.dateRange){
          this.formData.dutyDateBTW = this.formData.dateRange;
        }else{
          this.formData.dutyDateBTW = util.formatDateRange(this.formData.dateRange);
        }
        this.formLabel.specialityStore.value = util.bool2str(this.formData.specialityStore);
        this.formLabel.officeId.value = util.getLabel(this.offices, this.formData.officeId);
        this.formLabel.areaId.value=util.getLabel(this.formProperty.areas, this.formData.areaId);
      },
      pageRequest() {
          this.request();
          axios.get('/api/crm/depot/depotAccountData',{params:this.formData}).then((response) => {
            this.page = response.data;
          })
      },shopExportRequest() {
          this.request();
          window.location.href= "/api/crm/depot/shopExport?"+qs.stringify(this.formData);
      },shopExportDataRequest() {
          this.request();
          window.location.href= "/api/crm/depot/accountExport?"+qs.stringify(this.formData);
    },pageChange(pageNumber,pageSize) {
        this.pageLoading = false;
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
      },exportData(){
        this.formVisible = false;
        this.shopExportRequest();
      },exportDataDetail(){
        this.formVisible = false;
        this.shopExportDataRequest();
      },itemAction:function(id,action){
        if(action=="详细") {
          if(this.formProperty.dateRange==this.formData.dateRange){
            this.formData.dateRange = this.formData.dateRange;
          }else{
            this.formData.dateRange = util.formatDateRange(this.formData.dateRange);
          }
            console.log(this.formData.dateRange);
          this.$router.push({ name: 'depotAccountDetail', query: { id: id, dateRange:this.formData.dateRange}})
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.offices = [];
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/depot/getListProperty').then((response) =>{
        this.formProperty=response.data;
        this.formData.dateRange=this.formProperty.dateRange;
        this.pageRequest();
      });
    }
  };
</script>

