<template>
  <div>
    <head-tab active="arReceiveBill"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 25px"></div>
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
            colHeaders: ["往来单位", "我方银行账号", "业务日期", "收款金额", "结算方式", "备注"],
            columns: [
              {type: "autocomplete", strict: true, allowEmpty: false, customerName:[],source: this.customerName},
              {type: "autocomplete", strict: true, allowEmpty: true, bankAcntName:[],source: this.bankAcntName},
              {type: 'date',strict: true, allowEmpty: false,dateFormat:'YYYY-MM-DD',correctFormat: true},
              {type: "numeric",strict: true,allowEmpty: false, format:"0,0.00"},
              {type: "autocomplete",strict: true, allowEmpty: false, settleTypeName:[],source: this.settleTypeName},
              {type: "text",strict: true, allowEmpty: false }
            ],
            contextMenu: true,
            afterChange: function (changes, source) {
              if (source !== 'loadData') {
                for (let i = changes.length - 1; i >= 0; i--) {
                  let row = changes[i][0];
                  let column = changes[i][1];
                  if(column === 4 &&　changes[i][3] === '现金') {
                    table.setDataAtCell(row, 1, '');
                    table.setDataAtCell(row, 5, '批量开单');
                  }
                }
              }
            }
          },
          formData:{
          },rules: {},
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
            axios.post('/api/global/cloud/input/arReceiveBill/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
                this.formData = {};
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
      axios.get('/api/global/cloud/input/arReceiveBill/form').then((response)=>{
        this.formData = response.data;
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.customerNameList;
        this.settings.columns[1].source = extra.banAcntNameList;
        this.settings.columns[4].source = extra.settleTypeNameList;
        this.initPage();
      });
    },
  }
</script>
