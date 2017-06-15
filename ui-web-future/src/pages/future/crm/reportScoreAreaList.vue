<template>
  <div>
    <head-tab active="reportScoreAreaList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="share">{{$t('reportScoreAreaList.reportScoreOfficeRank')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('reportScoreAreaList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('reportScoreAreaList.filter')" v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('reportScoreAreaList.scoreDate')" :label-width="formLabelWidth">
                <date-picker v-model="formData.scoreDate"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('reportScoreAreaList.officeName')" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable filterable >
                  <el-option v-for="item in formData.extra.areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('reportScoreAreaList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('reportScoreAreaList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="scoreDate" :label="$t('reportScoreAreaList.scoreDate')" sortable></el-table-column>
        <el-table-column prop="officeName" :label="$t('reportScoreAreaList.officeName')" width="180"></el-table-column>
        <el-table-column prop="score" :label="$t('reportScoreAreaList.score')"></el-table-column>
        <el-table-column prop="monthScore" :label="$t('reportScoreAreaList.monthScore')"></el-table-column>
        <el-table-column prop="dateRank" :label="$t('reportScoreAreaList.dateRank')"></el-table-column>
        <el-table-column prop="monthRank" :label="$t('reportScoreAreaList.monthRank')"></el-table-column>
        <el-table-column prop="saleQty" :label="$t('reportScoreAreaList.saleQty')"></el-table-column>
        <el-table-column prop="monthSaleQty" :label="$t('reportScoreAreaList.monthSaleQty')"></el-table-column>
        <el-table-column prop="recentMonthSaleQty" :label="$t('reportScoreAreaList.recentMonthSaleQty')"></el-table-column>
        <el-table-column prop="officePoint"  :label="$t('reportScoreAreaList.point')"></el-table-column>
        <el-table-column prop="monthSaleMoney" :label="$t('reportScoreAreaList.monthSaleMoney')" width="180"></el-table-column>
        <el-table-column prop="pointMonthSaleMoney" :label="$t('reportScoreAreaList.pointMonthSaleMoney')" width="180"></el-table-column>
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
        searchText:"",
        formData:{
          extra:{}
        },
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
        util.setQuery("reportScoreAreaList",submitData);
        axios.get('/api/ws/future/crm/reportScoreArea',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'reportScoreOfficeList'});
      }
    },created () {
      this.pageHeight = window.outerHeight -325;
      axios.get('/api/ws/future/crm/reportScoreArea/getQuery').then((response)=>{
        this.formData = response.data;
      });
      this.pageRequest();
    }
  };
</script>

