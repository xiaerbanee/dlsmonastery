<template>
  <div>
    <head-tab active="voucherForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" label-width="80px" :inline="true">
        <el-form-item label="凭证日期"  prop="fdate">
          <date-picker v-model="formData.fdate"></date-picker>
        </el-form-item>
        <el-form-item label="备注"  prop="remarks">
          <el-input v-model="formData.remarks" type="textarea" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitAuditDisabled" @click="formSubmit" icon="check">保存</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmitAndAudit" icon="check">保存且审核</el-button>
        </el-form-item>
      </el-form>
      <div style="width:100%;margin-bottom:20px;">借方金额: <span id="debit" style="color:red"></span></div>
      <div style="width:100%;margin-bottom:20px;">贷方金额: <span  id="credit" style="color:red"></span></div>
      <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  var accountNameToFlexGroupNamesMap = {};
  var headers = [];
  var setCreditAndDebit = function (datas) {
    let debitColumn =headers.length - 2;
    let creditColumn = headers.length - 1;
    var debit=0;
    var credit=0;
    for(let i=0;i<datas.length; i++) {
      if(datas[i][debitColumn]) {
        debit = util.numTofixed(debit + datas[i][debitColumn]*1);
      }
      if(datas[i][creditColumn]) {
        credit = util.numTofixed(credit + datas[i][creditColumn]*1);
      }
    }
    document.getElementById("debit").innerHTML = debit;
    document.getElementById("credit").innerHTML = credit;
  };
  export default {
    data() {
      return {
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          manualColumnResize:true,
          minRows: 10,
          height: 650,
          colHeaders: [],
          columns: [],
          data:{},
           contextMenu: util.contextMenu(this.$store.state.global.lang),
          fixedRowsTop:0,
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if (column === headers.length - 1) {//贷方金额
                  if (changes[i][3] !== '') {
                    table.setDataAtCell(row, headers.length - 2, '');
                    setCreditAndDebit(table.getData());
                  }
                }else if (column === headers.length - 2) {//借方金额
                  if (changes[i][3] !== '') {
                    table.setDataAtCell(row, headers.length - 1, '');
                    setCreditAndDebit(table.getData());
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
            }else if (source === 'loadData'){

            }
          }
        },
        formData:{
          fdate:new Date().toLocaleDateString()
        },
        rules: {
          fdate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        submitAuditDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/sys/voucher/form',{params:{id:this.$route.query.id}}).then((response)=>{
        let extra = response.data.extra;
        this.settings.colHeaders = extra.headerList;
        accountNameToFlexGroupNamesMap = extra.accountNameToFlexGroupNamesMap;
        this.settings.columns.push({type: 'text', strict: true, allowEmpty: false,width:150});
        this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, width:100, accountName:[],source: this.accountName});
        this.settings.columns[1].source = extra.accountNameList;
        let colHeaders = extra.headerList;
        headers = extra.headerList;
        for (let i=0;i<colHeaders.length;i++){
            if(colHeaders[i] === "供应商") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100,supplierName:[],source: this.supplierName});
              this.settings.columns[i].source = extra.supplierNameList;
            }else if(colHeaders[i] === "客户"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, customerName:[],source: this.customerName});
              this.settings.columns[i].source = extra.customerNameList;
            }else if(colHeaders[i] === "银行账号"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, bankAccountName:[],source: this.bankAccountName});
              this.settings.columns[i].source = extra.bankAcntNameList;
            }else if(colHeaders[i] === "其他类"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, otherTypeName:[],source: this.otherTypeName});
              this.settings.columns[i].source = extra.otherTypeNameList;
            }else if(colHeaders[i] === "部门") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, departmentName: [], source: this.departmentName});
              this.settings.columns[i].source = extra.departmentNameList;
            }else if(colHeaders[i] === "费用类") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, expenseTypeName:[],source: this.expenseTypeName});
              this.settings.columns[i].source = extra.expenseTypeNameList;
            }else if(colHeaders[i] === "员工") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100,empInfoName:[],source: this.empInfoName});
              this.settings.columns[i].source = extra.empInfoNameList;
            }else if(colHeaders[i] === "资产类别") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100, assetTypeName:[],source: this.assetTypeName});
              this.settings.columns[i].source = extra.assetTypeNameList;
            }else if(colHeaders[i] === "费用项目") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, width:100,expenseName:[],source: this.expenseName});
              this.settings.columns[i].source = extra.expenseNameList;
            }
        }
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, width:100, strict: true});
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, width:100, strict: true});
        this.settings.data = extra.data;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
        setCreditAndDebit(table.getData());
      });
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.formData.json = new Array();
            let list = table.getData();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                this.formData.json.push(list[item]);
              }
            }
            this.formData.json = JSON.stringify(this.formData.json);
            this.formData.fdate = util.formatLocalDate(this.formData.fdate);
            axios.post('/api/global/cloud/sys/voucher/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success === true){
                this.$message(response.data.message);
                this.$router.push({name:'voucherList',query:util.getQuery("voucherList"), params:{_closeFrom:true}})
              }else {
                this.$alert(response.data.message);
                this.submitDisabled = false;
                this.submitAuditDisabled = false;
              }
            })
              .catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      formSubmitAndAudit() {
        this.submitAuditDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.formData.json = new Array();
            let list = table.getData();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                this.formData.json.push(list[item]);
              }
            }
            this.formData.json = JSON.stringify(this.formData.json);
            this.formData.fdate = util.formatLocalDate(this.formData.fdate);
            axios.post('/api/global/cloud/sys/voucher/audit', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.$router.push({name:'voucherList',query:util.getQuery("voucherList"), params:{_closeFrom:true}})
              }else{
                this.$alert(response.data.message);
                this.submitDisabled = false;
                this.submitAuditDisabled = false;
              }
            }).catch(function () {
              this.submitAuditDisabled = false;
            });
          }else{
            this.submitAuditDisabled = false;
          }
        })
      },initPage(){
        if (this.$route.query.id){
          axios.get('/api/global/cloud/sys/voucher/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.formData = response.data;
          });
        }
    }
  },created(){
    this.initPage();
  }
  }
</script>
