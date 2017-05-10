<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="consignmentReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dateRange"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        submitDisabled:false,
        table:null,
        settings: {
          data:{},
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          filters: true,
          dropdownMenu: true,
          contextMenu: true,
          colHeaders: ["客户代码","客户名称","商品代码","商品名称","寄售期初数量","寄售期初单价","寄售期初金额","寄售发出数量","寄售发出单价","寄售发出金额",
            "寄售结算数量","寄售结算单价","寄售结算金额","寄售未结算数量","寄售未结算单价","寄售未结算金额"],
          columns: [
            {data:'customerCode', type: 'text'},
            {data:'customerName', type: 'text'},
            {data:'goodsCode', type: 'text'},
            {data:'goodsName', type: 'text'},
            {data:'consignmentInitialQuantity', type: 'numeric'},
            {data:'consignmentInitialPrice', type: 'numeric',format:"0,0.00"},
            {data:'consignmentInitialAmount', type: 'numeric',format:"0,0.00"},
            {data:'consignmentSendQuantity', type: 'numeric'},
            {data:'consignmentSendPrice', type: 'numeric',format:"0,0.00"},
            {data:'consignmentSendAmount', type: 'numeric',format:"0,0.00"},
            {data:'consignmentSettlementQuantity', type: 'numeric'},
            {data:'consignmentSettlementPrice', type: 'numeric',format:"0,0.00"},
            {data:'consignmentSettlementAmount', type: 'numeric',format:"0,0.00"},
            {data:'consignmentNotSettledQuantity', type: 'numeric'},
            {data:'consignmentNotSettledPrice', type: 'numeric',format:"0,0.00"},
            {data:'consignmentNotSettledAmount', type: 'numeric',format:"0,0.00"}
          ],
        },
        formData: {
          dateRange: '',
        },
        formLabel:{
          dateRange:{label:"日期"},
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    mounted () {
      axios.get("/api/global/cloud/report/consignmentReport/report").then((response)=>{
        this.settings.data = response.data.consignmentDtoList;
        this.formData.dateRange = response.data.dateRange;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods: {
      search(){
        this.formVisible = false;
        util.getQuery("consignmentReport");
        util.setQuery("consignmentReport",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get("/api/global/cloud/report/consignmentReport/report",{params:this.submitData}).then((response)=>{
          this.settings.data = response.data.consignmentDtoList;
          this.formData.dateRange = response.data.dateRange;
        })
      }
    },created () {

    }
  };
</script>

