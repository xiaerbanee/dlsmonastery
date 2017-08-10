<template>
  <div>
    <head-tab active="arOtherRecAble"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit" icon="check">保存</el-button>
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
            stretchH: 'all',
            height: 650,
            minSpareRows: 1,
            colHeaders: ["客户", "用承担部门","对方科目编码(4)", "对方科目", "金额", "备注", '员工', "其他类", "费用类", '对方关联客户'],
            columns: [
              {type: "autocomplete", strict: true, allowEmpty: false, customerName:[],source: this.customerName},
              {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
              {type: "text", strict: true, allowEmpty: false, readOnly:true},
              {type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName},
              {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
              {type: "text", allowEmpty: true, strict: true},
              {type: "autocomplete", strict: true, allowEmpty: false, staffName:[],source: this.staffName},
              {type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName},
              {type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName},
              {type: "autocomplete", strict: true, allowEmpty: false, customerForName:[],source: this.customerForName},
            ],
            contextMenu: true,
            afterChange: function (changes, source) {
              if (source !== 'loadData') {
                var data = table.getData();
                for (let i = changes.length - 1; i >= 0; i--) {
                  let row = changes[i][0];
                  let column = changes[i][1];
                  if(column === 3) {
                    let accountName = changes[i][3];
                    axios.get('/api/global/cloud/kingdee/bdAccount/findNumberSubByFullName',{params:{fullName:accountName}}).then((response) => {
                      table.setDataAtCell(row, 2, response.data);
                    });
                  }
                  if(column === 4 &&　changes[i][4] === 0) {
                    let n = row +1;
                    alert('第'+n+"行，金额不能为零");
                    table.setDataAtCell(row, 3, '');
                  }
                }
                for(let i=0;i<data.length; i++) {
                  let otherTypeName = "";
                  let accountNumberSub = "";
                  if(data[i][2]) {
                    accountNumberSub =data[i][2];
                  }
                  if(data[i][7]) {
                    otherTypeName = data[i][7];
                  }
                  if(otherTypeName !== "" && accountNumberSub !== "" && otherTypeName!== '无'){
                    axios.get('/api/global/cloud/kingdee/basAssistant/findNumberSubByName',{params:{name:otherTypeName}}).then((response) => {
                      let number = response.data;
                      if (accountNumberSub !== number){
                        table.setDataAtCell(i, 7, '');
                        let j = i=1;
                        alert('第'+j+"行,其他类的编码前4位必须和对方科目编码前4位一致");
                      }
                    });
                  }
                }
              }
            }
          },
          formData:{
          },rules: {
            billDate: [{ required: true, message: '必填项'}],
          },
          submitDisabled:false
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
            axios.post('/api/global/cloud/input/arOtherRecAble/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
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
      axios.get('/api/global/cloud/input/arOtherRecAble/form').then((response)=>{
        this.formData = response.data;
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.customerNameList;
        this.settings.columns[1].source = extra.departmentNameList;
        this.settings.columns[3].source = extra.accountFullNameList;
        this.settings.columns[6].source = extra.staffNameList;
        this.settings.columns[7].source = extra.otherTypeNameList;
        this.settings.columns[8].source = extra.expenseTypeNameList;
        this.settings.columns[9].source = extra.customerNameList;
        this.initPage();
      });
    },
  }
</script>
