<template>
  <div>
    <head-tab :active="$t('reportScoreOfficeList.reportScoreOfficeList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="share">{{$t('reportScoreOfficeList.officeRank')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('reportScoreOfficeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('reportScoreOfficeList.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.scoreDate.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.scoreDate" type="date" align="right"  :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" clearable filterable >
                  <el-option v-for="item in formProperty.offices" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable filterable >
                  <el-option v-for="item in formProperty.areas" key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressOrderList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="scoreDate" :label="$t('reportScoreOfficeList.scoreDate')" sortable></el-table-column>
        <el-table-column prop="office.name" :label="$t('reportScoreOfficeList.officeName')"  width="200"></el-table-column>
        <el-table-column prop="area.name" :label="$t('reportScoreOfficeList.areaName')"  width="180"></el-table-column>
        <el-table-column prop="score" :label="$t('reportScoreOfficeList.score')"></el-table-column>
        <el-table-column prop="monthScore" :label="$t('reportScoreOfficeList.monthScore')"></el-table-column>
        <el-table-column prop="dateRank" :label="$t('reportScoreOfficeList.dateRank')"></el-table-column>
        <el-table-column prop="monthRank" :label="$t('reportScoreOfficeList.monthRank')"></el-table-column>
        <el-table-column prop="saleQty" :label="$t('reportScoreOfficeList.saleQty')"></el-table-column>
        <el-table-column prop="monthSaleQty" :label="$t('reportScoreOfficeList.monthSaleQty')"></el-table-column>
        <el-table-column prop="recentMonthSaleQty" :label="$t('reportScoreOfficeList.recentMonthSaleQty')"></el-table-column>
        <el-table-column prop="office.point" :label="$t('reportScoreOfficeList.point')"></el-table-column>
        <el-table-column prop="monthSaleMoney"  :label="$t('reportScoreOfficeList.monthSaleMoney')"></el-table-column>
        <el-table-column prop="pointMonthSaleMoney" :label="$t('reportScoreOfficeList.pointMonthSaleMoney')" width="180"></el-table-column>
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
          scoreDate:new Date(),
          officeId:'',
          areaId:''
        },formLabel:{
          scoreDate:{label:this.$t('reportScoreOfficeList.scoreDate')},
          officeId:{label:this.$t('reportScoreOfficeList.officeName')},
          areaId:{label:this.$t('reportScoreOfficeList.areaName')}
        },
        formProperty:{},
        pickerDateOption:util.pickerDateOption,
        productList:[],
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("reportScoreOfficeList",this.formData);
        this.formData.scoreDate = util.formatLocalDate(this.formData.scoreDate);
        axios.get('/api/crm/reportScoreOffice',{params:this.formData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'reportScoreAreaList'});
      }
    },created () {
      this.pageHeight = window.outerHeight -325;
      axios.get('/api/crm/reportScoreOffice/getListProperty').then((response)=>{
        this.formProperty = response.data;
      });
      this.pageRequest();
    }
  };
</script>

