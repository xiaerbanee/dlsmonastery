<template>
  <div>
    <head-tab active="reportScoreOfficeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="share">{{$t('reportScoreOfficeList.officeRank')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('reportScoreOfficeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('reportScoreOfficeList.filter')"  v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('reportScoreOfficeList.scoreDate')" :label-width="formLabelWidth">
                <date-picker v-model="formData.scoreDate"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('reportScoreOfficeList.officeName')" :label-width="formLabelWidth">
                 <office-select v-model="formData.officeId" @afterInit="setSearchText"></office-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressOrderList.sure')}}</el-button>
        </div>
      </search-dialog>
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
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        productList:[],
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading: false
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("reportScoreOfficeList",submitData);
        axios.get('/api/ws/future/crm/reportScoreOffice',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'reportScoreAreaList'});
      }
    },created () {
      const that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise=axios.get('/api/ws/future/crm/reportScoreOffice/getQuery').then((response) =>{
        that.formData=response.data;
        console.log(that.formData)
        util.copyValue(that.$route.query,that.formData);
    });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

