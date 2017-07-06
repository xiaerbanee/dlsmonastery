<template>
  <div>
    <head-tab active="reportScoreList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"  v-permit="'crm:reportScore:edit'" @click="itemAdd" icon="plus" >{{$t('reportScoreList.add')}}</el-button>
        <el-button type="primary"  v-permit="'crm:reportScore:view'"@click="formVisible = true" icon="search">{{$t('reportScoreList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('reportScoreList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('reportScoreList.scoreDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.scoreDateRange"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('reportScoreList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('reportScoreList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="scoreDate" :label="$t('reportScoreList.scoreDate')" sortable></el-table-column>
        <el-table-column prop="companyScore" :label="$t('reportScoreList.companyScore')"></el-table-column>
        <el-table-column prop="companyMonthScore" :label="$t('reportScoreList.companyMonthScore')"></el-table-column>
        <el-table-column prop="score" :label="$t('reportScoreList.score')"></el-table-column>
        <el-table-column prop="monthScore"  :label="$t('reportScoreList.monthScore')"></el-table-column>
        <el-table-column prop="cardQty" :label="$t('reportScoreList.cardQty')"></el-table-column>
        <el-table-column prop="monthCardQty" :label="$t('reportScoreList.monthCardQty')"></el-table-column>
        <el-table-column prop="rank" :label="$t('reportScoreList.rank')"></el-table-column>
        <el-table-column prop="saleQty" :label="$t('reportScoreList.saleQty')"></el-table-column>
        <el-table-column prop="monthSaleQty" :label="$t('reportScoreList.monthSaleQty')"></el-table-column>
        <el-table-column prop="recentMonthSaleQty" :label="$t('reportScoreList.recentMonthSaleQty')"></el-table-column>
        <el-table-column v-permit="'crm:reportScore:moneyView'"  prop="saleMoney" :label="$t('reportScoreList.saleMoney')"></el-table-column>
        <el-table-column v-permit="'crm:reportScore:moneyView'"  prop="monthSaleMoney" :label="$t('reportScoreList.monthSaleMoney')"></el-table-column>
        <el-table-column fixed="right" :label="$t('reportScoreList.operation')" width="140">
          <template scope="scope">
            <div class="action"  v-permit="'crm:reportScore:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')"> {{$t('reportScoreList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  export default{

    data() {
      return {
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("reportScoreList",submitData);
        axios.get('/api/ws/future/crm/reportScore?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        });
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();

      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'reportScoreForm'})
      },itemAction:function(id,action){
        if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/reportScore/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      const that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise=axios.get('/api/ws/future/crm/reportScore/getQuery').then((response) =>{
      that.formData=response.data;
        that.formData.sort="scoreDate,desc"
      util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

