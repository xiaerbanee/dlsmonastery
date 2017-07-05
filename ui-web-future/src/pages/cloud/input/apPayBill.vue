<template>
  <div>
    <head-tab active="apPayBill"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
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
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 1,
          colHeaders: ["往来单位", "部门", "银行账户", "结算方式", "金额", "备注","对方科目"],
          columns: [
            {type: "autocomplete", strict: true, allowEmpty: false, supplierName:[],source: this.supplierName},
            {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
            {type: "autocomplete", strict: true, allowEmpty: false, bankAcntName:[],source: this.bankAcntName},
            {type: "autocomplete", strict: true, allowEmpty: false, settleTypeName:[],source: this.settleTypeName},
            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
            {type: "text", allowEmpty: true, strict: true},
            {type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName},
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          json:[],
        },rules: {
          billDate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/apPayBill/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.supplierNameList;
        this.settings.columns[1].source = extra.departmentNameList;
        this.settings.columns[2].source = extra.bankAcntNameList;
        this.settings.columns[3].source = extra.settleTypeNameList;
        this.settings.columns[6].source = extra.accountNameList;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.formData.json =new Array();
            let list = table.getData();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                this.formData.json.push(list[item]);
              }
            }
            this.formData.json = JSON.stringify(this.formData.json);
            this.formData.billDate = util.formatLocalDate(this.formData.billDate);
            axios.post('/api/global/cloud/input/apPayBill/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    }
  }
</script>
