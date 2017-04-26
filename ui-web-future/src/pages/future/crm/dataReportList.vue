<template>
  <div>
    <head-tab active="dataReportList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="preRequest" icon="left">{{$t('dataReportList.black')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dataReportList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dataReportList.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.reportDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.reportDate" type="daterange" align="right" :placeholder="$t('dataReportList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dataReportList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="data" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dataReportList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column :label="$t('dataReportList.name')" prop="name">
          <template scope="scope">
            <el-button type="primary"  size="small" @click="nextRequest(scope.row.id)">{{scope.row.name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="baokaTotalImeSaleReport.saleQty" :label="$t('dataReportList.saleQtyForBaoka')" ></el-table-column>
        <el-table-column prop="baokaTotalImeSaleReport.extendMap.baokaSalePercentage" :label="$t('dataReportList.baokaSalePercentage')" ></el-table-column>
        <el-table-column prop="baokaTotalImeStockReport.saleStock" :label="$t('dataReportList.saleStockForBaoka')" ></el-table-column>
        <el-table-column prop="baokaTotalImeStockReport.extendMap.baokaStockPercentage" :label="$t('dataReportList.baokaStockPercentage')" ></el-table-column>
        <el-table-column prop="totalImeSaleReport.saleQty" :label="$t('dataReportList.saleQtyForSale')"  sortable></el-table-column>
        <el-table-column prop="totalImeSaleReport.extendMap.salePercentage" :label="$t('dataReportList.salePercentage')" ></el-table-column>
        <el-table-column prop="totalImeStockReport.saleStock" :label="$t('dataReportList.saleStockForSale')"  sortable></el-table-column>
        <el-table-column prop="totalImeStockReport.extendMap.saleStockPercentage" :label="$t('dataReportList.saleStockPercentage')" ></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{
          inventoryDetailModeList:[]
        },
        total:{},
        data:[],
        parentIdArr:[],
        total:{
          name:this.$t('dataReportList.total'),
          baokaTotalImeSaleReport:{saleQty:""},
          baokaTotalImeStockReport:{saleStock:""},
          totalImeSaleReport:{saleQty:""},
          totalImeStockReport:{saleStock:""}
        },
        formData:{
          parentOfficeId:"",
          reportDateStart: util.formatLocalDate(util.getFirstDayOfMonth(new Date())),
          reportDateEnd:util.formatLocalDate(new Date()),
          productName: "",
          reportDateBTW:"",
          reportDate:""
        },formLabel:{
          reportDateBTW:{label:this.$t('dataReportList.dateRange')},
        },actionButton:{
          hasEdit:false
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption,

      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.reportDateBTW = util.formatDateRange(this.formData.reportDate);
        util.setQuery("dataReportList",this.formData);
        if(this.parentIdArr.length!=0){
            this.formData.parentOfficeId=this.parentIdArr[this.parentIdArr.length-1]
        }else{
          this.formData.parentOfficeId=""
        }
        axios.get('/api/crm/imeSaleReport/officeInventory',{params:this.formData}).then((response) => {
          var arr=[];
          this.page =response.data;
          this.pageLoading = false;
          arr=response.data.inventoryDetailModelList;
          this.total.baokaTotalImeSaleReport.saleQty=this.page.baokaTotalImeSaleReport.saleQty;
          this.total.baokaTotalImeStockReport.saleStock=this.page.baokaTotalImeStockReport.saleStock;
          this.total.totalImeSaleReport.saleQty=this.page.totalImeSaleReport.saleQty;
          this.total.totalImeStockReport.saleStock=this.page.totalImeStockReport.saleStock;
          arr.push(this.total);
          this.data=arr;
        })
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },nextRequest: function (id) {
          if(this.page.hasNext){
              this.parentIdArr.push(id);
              this.pageRequest();
          }else{
            this.$message('不能再往下点了哟^o^');
          }
      },preRequest:function(){
           this.parentIdArr=this.parentIdArr.slice(0, this.parentIdArr.length - 1);
           this.pageRequest();
      },getQuery(){
        axios.get('/api/crm/product/getQuery').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.actionButton.hasEdit = util.isPermit("crm:demoPhone:edit");
      this.pageRequest();
    }
  };
</script>


