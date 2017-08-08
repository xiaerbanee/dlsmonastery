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
            colHeaders: ["往来单位", "部门", "银行账户", "结算方式", "金额", "备注","对方科目"],
            columns: [
              {type: "autocomplete", strict: true, allowEmpty: false, supplierName:[],source: this.supplierName},
              {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
              {type: "autocomplete", strict: true, allowEmpty: true, bankAcntName:[],source: this.bankAcntName},
              {type: "autocomplete", strict: true, allowEmpty: false, settleTypeName:[],source: this.settleTypeName},
              {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
              {type: "text", allowEmpty: true, strict: true},
              {type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName},
            ],
            contextMenu: true,
            afterChange: function (changes, source) {
              if (source !== 'loadData') {
                for (let i = changes.length - 1; i >= 0; i--) {
                  let row = changes[i][0];
                  let column = changes[i][1];
                  if(column === 3 &&　changes[i][3] === '现金') {
                    table.setDataAtCell(row, 2, '');
                    table.setDataAtCell(row, 5, '批量开单');
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
            axios.post('/api/global/cloud/input/apPayBill/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
                Object.assign(this.$data,this.getData());
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
      axios.get('/api/global/cloud/input/apPayBill/form').then((response)=>{
        this.formData = response.data;
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.supplierNameList;
        this.settings.columns[1].source = extra.departmentNameList;
        this.settings.columns[2].source = extra.bankAcntNameList;
        this.settings.columns[3].source = extra.settleTypeNameList;
        this.settings.columns[6].source = extra.accountNameList;
        this.initPage();
      });
    },
  }
</script>
