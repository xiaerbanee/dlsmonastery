<template>
  <div>
    <head-tab active="cnJournalForCash"></head-tab>
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
    data:function () {
      return this.getData();
    },
    methods: {
      getData() {
        return {
          settings: {
            rowHeaders:true,
            autoColumnSize:true,
            manualColumnResize:true,
            height: 650,
            minSpareRows: 1,
            fixedRowsTop:0,
            colHeaders: ["对方科目编码", "借方金额", "贷方金额", "摘要", "对方科目名称", "员工","部门","其他类","费用类"],
            columns: [
              {type: "autocomplete", strict: true, allowEmpty: false, accountNumber:[],source: this.accountNumber, width:100},
              {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true, width:100},
              {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true, width:100},
              {type: "text", allowEmpty: true, strict: true, width:100},
              {type: "text", readOnly: true, allowEmpty: true, strict: true, width:100},
              {type: "autocomplete", strict: true, allowEmpty: false, staffName:[],source: this.staffName, width:80},
              {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName, width:150},
              {type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName, width:150},
              {type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName, width:150},
            ],
           contextMenu: util.contextMenu(this.$store.state.global.lang),
            afterChange: function (changes, source) {
              if (source !== 'loadData') {
                for (let i = changes.length - 1; i >= 0; i--) {
                  let row = changes[i][0];
                  let column = changes[i][1];
                  if(column === 0) {
                    let accountNumber = changes[i][3];
                    if (util.isNotBlank(accountNumber)) {
                      axios.get('/api/global/cloud/kingdee/bdAccount/findByNumber',{params:{number:accountNumber}}).then((response) => {
                        let account = response.data;
                        table.setDataAtCell(row, 4, account.fname);
                      });
                    } else {
                      table.setDataAtCell(row, 4, null);
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
                      axios.get('/api/global/cloud/kingdee/basAssistant/findNumberSubByName',{params:{name:otherTypeName}}).then((response) => {
                        let number = response.data;
                        if (accountNumber !== number){
                          table.setDataAtCell(i, 9, '');
                          let j = i=1;
                          alert('第'+j+"其他类的编码前4位必须和对应科目的编码一致");
                        }
                      });
                    }
                  }
                }
              }
            }
          },
          formData:{
          },rules: {
            billDate: [{ required: true, message: '必填项'}],
          },
          submitDisabled:false,
        };
      },
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
            var submitData = util.deleteExtra(this.formData);
            axios.post('/api/global/cloud/input/cnJournalForCash/save', qs.stringify(submitData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
                form.resetFields();
              }else{
                this.$alert(response.data.message);
              }
              this.submitDisabled = false;
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage() {
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      },
    },
    created() {
      axios.get('/api/global/cloud/input/cnJournalForCash/form').then((response)=>{
        this.formData = response.data;
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.accountNumberList;
        this.settings.columns[5].source = extra.staffNameList;
        this.settings.columns[6].source = extra.departmentNameList;
        this.settings.columns[7].source = extra.otherTypeNameList;
        this.settings.columns[8].source = extra.expenseTypeNameList;
        let flag = extra.customerForFlag;
        if(flag === true){
          this.settings.colHeaders.push("对方关联客户");
          this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, customerNameFor:[],source: this.customerNameFor, width:150});
          this.settings.columns[9].source = extra.customerNameForList;
        }
        this.initPage();
      });
    },
  }
</script>
