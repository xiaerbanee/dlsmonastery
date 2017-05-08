<template>
  <div>
    <head-tab active="batchCashDeliveryForm"></head-tab>
    <div>
      <el-form :model="formData" method="get">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="formLabel.billDate.label" :label-width="formLabelWidth">
              <el-date-picker v-model="formData.billDate" type="date" align="right" placeholder="请选择时间" :picker-options="pickerDateOption"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          </el-col>
        </el-row>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          colHeaders: ["对方科目","对方科目全称","员工","部门","其他类","费用类","借方金额","贷方金额","摘要",],
          columns:[
            {type: "autocomplete", allowEmpty: false, strict: true, accountNumber:[], source: this.accountNumber, width:100},
            {type:"text", allowEmpty: false, strict: true, readOnly: true, width: 100},
            {type: "autocomplete", allowEmpty: false, strict: true, empInfoName:[], source: this.empInfoName, width:80},
            {type: "autocomplete", allowEmpty: false, strict: true, departmentName:[],source: this.departmentName, width: 150},
            {type: "autocomplete", allowEmpty: false, strict: true, otherType:[], source: this.otherType, width: 150},
            {type: "autocomplete", allowEmpty: false, strict: true, expenseType:[], source: this.expenseType, width: 150},
            {type: 'numeric', strict: true, format:"0,0.00", width:100},
            {type: 'numeric', strict: true, format:"0,0.00", width: 100},
            {type:"text", strict: true, width: 100},
          ],
          afterChange: function (changes, source) {

          }
        },
        formData:{
          billDate:'',
          data:[],
        },formLabel:{
          billDate:{label:"日期"},
        },
        pickerDateOption:'',
        submitDisabled:false,
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/batchCashDelivery/form').then((response)=>{
          if("WZOPPO" === response.data.kingdeeName) {
            this.settings.columns.push({type: "autocomplete", allowEmpty: true, strict: true, customerList:[],source: this.customerList});
            this.setting.colHeaders.push("对方关联客户");
          }
          this.table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },
    methods: {
      formSubmit(){
        this.formData.data =new Array();
        let list = this.table.getData();
        for(let item in list){
          if(!this.table.isEmptyRow(item)){
            this.formData.data.push(list[item]);
          }
        }
        this.formData.data = JSON.stringify(this.formData.data);
        axios.post('/api/global/cloud/input/batchCashDelivery/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
        });
      },
    }
  }
</script>
