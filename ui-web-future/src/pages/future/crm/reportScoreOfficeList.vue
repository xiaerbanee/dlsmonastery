<template>
  <div>
    <head-tab active="reportScoreOfficeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="share">{{$t('reportScoreOfficeList.officeRank')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('reportScoreOfficeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('reportScoreOfficeList.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.scoreDate.label" :label-width="formLabelWidth">
                <date-picker v-model="formData.scoreDate"></date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                 <office-select v-model="formData.officeId"></office-select>
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
        <el-table-column prop="officeName" :label="$t('reportScoreOfficeList.officeName')"  width="200"></el-table-column>
        <el-table-column prop="areaName" :label="$t('reportScoreOfficeList.areaName')"  width="180"></el-table-column>
        <el-table-column prop="score" :label="$t('reportScoreOfficeList.score')"></el-table-column>
        <el-table-column prop="monthScore" :label="$t('reportScoreOfficeList.monthScore')"></el-table-column>
        <el-table-column prop="dateRank" :label="$t('reportScoreOfficeList.dateRank')"></el-table-column>
        <el-table-column prop="monthRank" :label="$t('reportScoreOfficeList.monthRank')"></el-table-column>
        <el-table-column prop="saleQty" :label="$t('reportScoreOfficeList.saleQty')"></el-table-column>
        <el-table-column prop="monthSaleQty" :label="$t('reportScoreOfficeList.monthSaleQty')"></el-table-column>
        <el-table-column prop="recentMonthSaleQty" :label="$t('reportScoreOfficeList.recentMonthSaleQty')"></el-table-column>
        <el-table-column prop="officePoint" :label="$t('reportScoreOfficeList.point')"></el-table-column>
        <el-table-column prop="monthSaleMoney"  :label="$t('reportScoreOfficeList.monthSaleMoney')"></el-table-column>
        <el-table-column prop="pointMonthSaleMoney" :label="$t('reportScoreOfficeList.pointMonthSaleMoney')" width="180"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import datePicker from 'components/common/date-picker'
  import officeSelect from 'components/basic/office-select'
  export default {
    components:{
      datePicker,
      officeSelect
      },
    data() {
      return {
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          scoreDate:'',
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
        util.copyValue(this.formData,this.submitData);
        util.setQuery("reportScoreOfficeList",this.submitData);
        this.formData.scoreDate = util.formatLocalDate(this.submitData.scoreDate);
        axios.get('/api/ws/future/crm/reportScoreOffice',{params:this.submitData}).then((response) => {
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
      axios.get('/api/ws/future/crm/reportScoreOffice/getQuery').then((response)=>{
        this.formProperty = response.data;
      });
      this.pageRequest();
    }
  };
</script>

