<style>
</style>
<template>
  <div>
    <head-tab active="consignmentReport"></head-tab>
    <el-row :gutter="20">
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules">
        <el-col :span="6">
          <el-form-item label="开始日期"  prop="dateStart">
            <date-picker v-model="formData.dateStart"></date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="结束日期"  prop="dateEnd">
            <date-picker v-model="formData.dateEnd"></date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="search()" icon="search">搜索</el-button>
          <el-button type="primary" @click="exportData()" icon="export">导出</el-button>
        </el-col>
      </el-form>
    </el-row>
    <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  export default {
    data() {
      return {
        settings: {
          data:{},
          rowHeaders:true,
          autoColumnSize:true,
          manualColumnResize:true,
          filters: true,
          dropdownMenu: true,
           contextMenu: util.contextMenu(this.$store.state.global.lang),
          stretchH: 'all',
          height: 650,
          fixedRowsTop:0,
          colHeaders: ["客户代码","客户名称","商品代码","商品名称","寄售期初数量","寄售期初单价","寄售期初金额","寄售发出数量","寄售发出单价","寄售发出金额",
            "寄售结算数量","寄售结算单价","寄售结算金额","寄售未结算数量","寄售未结算单价","寄售未结算金额"],
          columns: [
            {data:'customerCode', type: 'text', readOnly:true},
            {data:'customerName', type: 'text', readOnly:true},
            {data:'goodsCode', type: 'text', readOnly:true},
            {data:'goodsName', type: 'text', readOnly:true},
            {data:'consignmentInitialQuantity', type: 'numeric', readOnly:true},
            {data:'consignmentInitialPrice', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentInitialAmount', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentSendQuantity', type: 'numeric', readOnly:true},
            {data:'consignmentSendPrice', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentSendAmount', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentSettlementQuantity', type: 'numeric', readOnly:true},
            {data:'consignmentSettlementPrice', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentSettlementAmount', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentNotSettledQuantity', type: 'numeric', readOnly:true},
            {data:'consignmentNotSettledPrice', type: 'numeric',format:"0,0.00", readOnly:true},
            {data:'consignmentNotSettledAmount', type: 'numeric',format:"0,0.00", readOnly:true}
          ],
        },
        formData: {
          dateStart: new Date().toLocaleDateString(),
          dateEnd:new Date().toLocaleDateString(),
        },
        rules: {
          dateStart: [{ required: true, message: '必填项'}],
          dateEnd: [{ required: true, message: '必填项'}],
        },
      };
    },
    methods: {
      search(){
        this.initPage();
      },
      initPage() {
        this.formData.dateStart = util.formatLocalDate(this.formData.dateStart);
        this.formData.dateEnd = util.formatLocalDate(this.formData.dateEnd);
        axios.post("/api/global/cloud/report/consignmentWZ/list",qs.stringify(this.formData,{allowDots:true})).then((response)=>{
          this.settings.data = response.data;
          table = new Handsontable(this.$refs["handsontable"], this.settings)
        })
      },
      exportData(){
        this.formData.dateStart = util.formatLocalDate(this.formData.dateStart);
        this.formData.dateEnd = util.formatLocalDate(this.formData.dateEnd);
        window.location.href = '/api/global/cloud/report/consignmentWZ/export?dateStart='+this.formData.dateStart+"&dateEnd="+this.formData.dateEnd;
      }
    },created () {
      this.initPage();
    }
  };
</script>

