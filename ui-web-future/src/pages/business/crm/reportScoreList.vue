<template>
  <div>
    <head-tab active="reportScoreList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >{{$t('reportScoreList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('reportScoreList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('reportScoreList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.scoreDate.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.scoreDate" type="date" align="right"  :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('reportScoreList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('reportScoreList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="scoreDate" :label="$t('reportScoreList.scoreDate')" sortable></el-table-column>
        <el-table-column prop="companyScore" :label="$t('reportScoreList.companyScore')"></el-table-column>
        <el-table-column prop="companyMonthScore" :label="$t('reportScoreList.companyMonthScore')"></el-table-column>
        <el-table-column prop="score" :label="$t('reportScoreList.score')"></el-table-column>
        <el-table-column prop="monthScore"  :label="$t('reportScoreList.monthScore')"></el-table-column>
        <el-table-column prop="cardQty" :label="$t('reportScoreList.cardQty')"></el-table-column>
        <el-table-column prop="monthCardQty" :label="$t('reportScoreList.monthCardQty')"></el-table-column>
        <el-table-column prop="rank":label="$t('reportScoreList.rank')"></el-table-column>
        <el-table-column prop="saleQty" :label="$t('reportScoreList.saleQty')"></el-table-column>
        <el-table-column prop="monthSaleQty" :label="$t('reportScoreList.monthSaleQty')"></el-table-column>
        <el-table-column prop="recentMonthSaleQty" :label="$t('reportScoreList.recentMonthSaleQty')"></el-table-column>
        <el-table-column prop="saleMoney" :label="$t('reportScoreList.saleMoney')"></el-table-column>
        <el-table-column prop="monthSaleMoney" :label="$t('reportScoreList.monthSaleMoney')"></el-table-column>
        <el-table-column fixed="right" :label="$t('reportScoreList.operation')" width="140">
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
          scoreDate:''
        },formLabel:{
          scoreDate:{label:this.$t('reportScoreList.scoreDate')}
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
        util.setQuery("reportScoreList",this.formData);
        axios.get('/api/crm/reportScore',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'reportScoreForm'})
      },itemAction:function(id,action){
        if(action=="删除") {
          axios.get('/api/crm/reportScore/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -325;
      axios.get('/api/crm/reportScore/getQuery').then((response)=>{
        this.formProperty = response.data;
      });
      this.pageRequest();
    }
  };
</script>

