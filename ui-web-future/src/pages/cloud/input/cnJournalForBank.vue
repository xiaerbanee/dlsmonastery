<template>
  <div>
    <head-tab active="cnJournalForBank"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item label="科目"   prop="accountNumber">
          <el-select v-model="formData.accountNumber" filterable placeholder="请选择">
            <el-option v-for="item in accountForBankList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
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
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table = null;
  export default {
    data() {
      return {
        table:null,
        accountForBankList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 1,
          colHeaders: ["对方科目编码", "结算方式","借方金额", "贷方金额","银行账户", "摘要", "对方科目名称", "员工","部门","其他类","费用类"],
          columns: [
            {type: "autocomplete", strict: true, allowEmpty: false, accountNumber:[],source: this.accountNumber},
            {type: "autocomplete", strict: true, allowEmpty: false, settleTypeName:[],source: this.settleTypeName},
            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
            {type: "autocomplete", strict: true, allowEmpty: false, settleTypeName:[],source: this.settleTypeName},
            {type: "text", allowEmpty: true, strict: true},
            {type: "text", readOnly: true, allowEmpty: true, strict: true},
            {type: "autocomplete", strict: true, allowEmpty: false, staffName:[],source: this.staffName},
            {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
            {type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName},
            {type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName},
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if (column === 0) {
                  let accountNumber = changes[i][3];
                  if (util.isNotBlank(accountNumber)) {
                    axios.get('/api/global/cloud/kingdee/bdAccount/findByNumber?number=' + accountNumber).then((response) => {
                      let account = response.data;
                      table.setDataAtCell(row, 6, account.fname);
                    });
                  } else {
                    table.setDataAtCell(row, 6, null);
                  }
                }
              }
              let data=table.getData();
              for(let i=0;i<data.length; i++) {
                let otherTypeName = "";
                let accountNumber = "";
                if(data[i][0]) {
                  accountNumber =data[i][0];
                }
                if(data[i][9]) {
                  otherTypeName = data[i][9];
                }
                if(otherTypeName !== "" && accountNumber !== "" && otherTypeName!== '无'){
                  axios.get('/api/global/cloud/kingdee/basAssistant/findNumberSubByName?name=' + otherTypeName).then((response) => {
                    let number = response.data;
                    if (accountNumber !== number){
                      table.setDataAtCell(i, 9, '');
                      let j = i=1;
                      alert('第'+j+"其他类的编码前4位必须和对应科目的编码一致");;
                    }
                  });
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          accountNumber:'',
          json:[],
        },rules: {
          billDate: [{ required: true, message: '必填项'}],
          accountNumber: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/cnJournalForBank/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.accountNumberList;
        this.settings.columns[1].source = extra.settleTypeNameList;
        this.settings.columns[4].source = extra.bankAcntNameList;
        this.settings.columns[7].source = extra.empInfoNameList;
        this.settings.columns[8].source = extra.departmentNameList;
        this.settings.columns[9].source = extra.otherTypeNameList;
        this.settings.columns[10].source = extra.expenseTypeNameList;
        let flag = extra.customerForFlag;
        if(flag === true){
          this.settings.colHeaders.push("对方关联客户");
          this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, customerNameFor:[],source: this.customerNameFor});
          this.settings.columns[11].source = extra.customerNameForList;
        }
        table = new Handsontable(this.$refs["handsontable"], this.settings);
        this.accountForBankList = extra.accountForBankList;
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
            axios.post('/api/global/cloud/input/cnJournalForBank/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
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
