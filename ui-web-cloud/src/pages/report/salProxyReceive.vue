<template>
  <div>
    <head-tab active="salProxyReceive"></head-tab>
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
                  <el-button type="primary" @click="exportData()" icon="upload">导出</el-button>
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
          stretchH: 'all',
          height: 650,
          fixedRowsTop:0,
          colHeaders:  ['分组名称','制式','销售金额','银行名称','收款金额','收款占比'],
          columns: [
            {data:'groupName', type: 'text'},
            {data:'flx', type: 'text'},
            {data:'fallAmount', type: 'numeric',format:"0,0.00"},
            {data:'bankName', type: 'text'},
            {data:'skAmount', type: 'numeric',format:"0,0.00"},
            {data:'backLV', type: 'numeric',format: "0.00%"}
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
        axios.post("/api/global/cloud/report/salProxyReceive/list",qs.stringify(this.formData,{allowDots:true})).then((response)=>{
          this.settings.data = response.data;
          table = new Handsontable(this.$refs["handsontable"], this.settings)
        })
      },
      exportData(){
        this.formData.dateStart = util.formatLocalDate(this.formData.dateStart);
        this.formData.dateEnd = util.formatLocalDate(this.formData.dateEnd);
        window.location.href = '/api/global/cloud/report/salProxyReceive/export?dateStart='+this.formData.dateStart+"&dateEnd="+this.formData.dateEnd;
      }
    },created () {
      this.initPage();
    }
  };
</script>

