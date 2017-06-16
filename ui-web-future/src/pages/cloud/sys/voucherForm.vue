<template>
  <div>
    <head-tab active="voucherForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" label-width="80px">
        <el-form-item label="凭证日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item label="备注"  prop="remarks">
          <el-input v-model="formData.remarks"></el-input>
        </el-form-item>
        <el-form-item label="借方金额">
          <el-input v-model="debit"></el-input>
        </el-form-item>
        <el-form-item label="贷方金额">
          <el-input v-model="credit"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          <el-button type="primary" @click="formSubmitAndAudit" icon="check">保存且审核</el-button>
        </el-form-item>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  import ElInput from "../../../../node_modules/element-ui/packages/input/src/input";
  import ElInputNumber from "../../../../node_modules/element-ui/packages/input-number/src/input-number";
  import ElFormItem from "../../../../node_modules/element-ui/packages/form/src/form-item";
  var table = null;
  export default {
    data() {
      return {
        table:null,
        debit:'',
        credit:'',
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          minSpareRows: 1,
          height: 650,
          colHeaders: [
//              '摘要','科目名称','供应商','客户','银行账号','其他类','部门','费用类','员工','借方金额','贷方金额'
          ],
          columns: [
//            {type: 'text', allowEmpty: false, strict: true},
//            {type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName},
//            {type: "autocomplete", strict: true, allowEmpty: false, supplierName:[],source: this.supplierName},
//            {type: "autocomplete", strict: true, allowEmpty: false, customerName:[],source: this.customerName},
//            {type: "autocomplete", strict: true, allowEmpty: false, bankAccountName:[],source: this.bankAccountName},
//            {type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName},
//            {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
//            {type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName},
//            {type: "autocomplete", strict: true, allowEmpty: false, empInfoName:[],source: this.empInfoName},
//            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
//            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString()
        },
        rules: {
          billDate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/sys/voucher/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.colHeaders = extra.headerList;
        this.settings.columns.push({type: 'text', allowEmpty: false, strict: true});
        this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName});
        this.settings.columns[1].source = extra.accountNameList;
        let colHeaders = extra.headerList;
        for (let i=0;i<colHeaders.length;i++){
            switch (colHeaders[i]){
              case "供应商":
                  this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, supplierName:[],source: this.supplierName});
                  this.settings.columns[i+2].source = extra.supplierNameList;
              case "客户":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, customerName:[],source: this.customerName});
                this.settings.columns[i+2].source = extra.customerNameList;
              case "银行账号":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, bankAccountName:[],source: this.bankAccountName});
                this.settings.columns[i+2].source = extra.bankAcntNameList;
              case "其他类":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName});
                this.settings.columns[i+2].source = extra.otherTypeNameList;
              case "部门":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName});
                this.settings.columns[i+2].source = extra.departmentNameList;
              case "费用类":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName});
                this.settings.columns[i+2].source = extra.expenseTypeNameList;
              case "员工":
                this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, empInfoName:[],source: this.empInfoName});
                this.settings.columns[i+2].source = extra.empInfoNameList;
            }
        }
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true});
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true});
//        this.settings.columns[2].source = extra.supplierNameList;
//        this.settings.columns[3].source = extra.customerNameList;
//        this.settings.columns[4].source = extra.bankAcntNameList;
//        this.settings.columns[5].source = extra.otherTypeNameList;
//        this.settings.columns[6].source = extra.departmentNameList;
//        this.settings.columns[7].source = extra.expenseTypeNameList;
//        this.settings.columns[8].source = extra.empInfoNameList;
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
            axios.post('/api/global/cloud/input/stkInStock/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      formSubmitAndAudit() {
      },

    }
  }
</script>
