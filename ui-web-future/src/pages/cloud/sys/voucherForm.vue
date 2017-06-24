<template>
  <div>
    <head-tab active="voucherForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" label-width="80px">
        <el-form-item label="凭证日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item label="备注"  prop="remarks">
          <el-input v-model="formData.remarks" type="textarea" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="借方金额">
          {{debit}}
        </el-form-item>
        <el-form-item label="贷方金额" >
          {{credit}}
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
  var accountNameToFlexGroupNamesMap = {};
  var headers = [];
  export default {
    data() {
      return {
        table:null,
        debit:0,
        credit:0,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          minSpareRows: 10,
          height: 650,
          colHeaders: [],
          columns: [],
          data:{},
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if (column === headers.length - 1) {//贷方金额
                  if (changes[i][3] !== '') {
                    table.setDataAtCell(row, headers.length - 2, '');
                  }
                }else if (column === headers.length - 2) {//借方金额
                  if (changes[i][3] !== '') {
                    table.setDataAtCell(row, headers.length - 1, '');
                  }
                }
                if (column === 1) {//科目名称
                  for(let j=2;j<headers.length-2;j++) {
                    let flexGroupNames = accountNameToFlexGroupNamesMap[changes[i][3]];
                    if(flexGroupNames) {
                      //不包含
                      if(flexGroupNames.indexOf(headers[j]) === -1) {
                        table.setDataAtCell(row, j, '');
                        table.setCellMeta(row, j, 'readOnly', true);
                      } else {
                        table.setCellMeta(row, j, 'readOnly', false);
                      }
                    } else {
                      table.setDataAtCell(row, j, '');
                      table.setCellMeta(row, j, 'readOnly', true);
                    }
                  }
                }
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
        accountNameToFlexGroupNamesMap = extra.accountNameToFlexGroupNamesMap;
        this.settings.columns.push({type: 'text', strict: true, allowEmpty: false});
        this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName});
        this.settings.columns[1].source = extra.accountNameList;
        let colHeaders = extra.headerList;
        headers = extra.headerList;
        for (let i=0;i<colHeaders.length;i++){
            if(colHeaders[i] === "供应商") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, supplierName:[],source: this.supplierName});
              this.settings.columns[i].source = extra.supplierNameList;
            }else if(colHeaders[i] === "客户"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, customerName:[],source: this.customerName});
              this.settings.columns[i].source = extra.customerNameList;
            }else if(colHeaders[i] === "银行账号"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, bankAccountName:[],source: this.bankAccountName});
              this.settings.columns[i].source = extra.bankAcntNameList;
            }else if(colHeaders[i] === "其他类"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, otherTypeName:[],source: this.otherTypeName});
              this.settings.columns[i].source = extra.otherTypeNameList;
            }else if(colHeaders[i] === "部门") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, departmentName: [], source: this.departmentName});
              this.settings.columns[i].source = extra.departmentNameList;
            }else if(colHeaders[i] === "费用类") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, expenseTypeName:[],source: this.expenseTypeName});
              this.settings.columns[i].source = extra.expenseTypeNameList;
            }else if(colHeaders[i] === "员工") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, empInfoName:[],source: this.empInfoName})
              this.settings.columns[i].source = extra.empInfoNameList;
            }
        }
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, strict: true});
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, strict: true});
        this.settings.data = extra.data;
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
