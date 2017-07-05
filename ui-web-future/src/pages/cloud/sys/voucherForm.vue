<template>
  <div>
    <head-tab active="voucherForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" label-width="80px">
        <el-form-item label="凭证日期"  prop="fdate">
          <date-picker v-model="formData.fdate"></date-picker>
        </el-form-item>
        <el-form-item label="备注"  prop="remarks">
          <el-input v-model="formData.remarks" type="textarea" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="借方金额" >
          <span id="debit"></span>
        </el-form-item>
        <el-form-item label="贷方金额">
          <span  id="credit"></span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
          <el-button type="primary" @click="formSubmitAndAudit" icon="check">保存且审核</el-button>
        </el-form-item>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
  </div>
</template>
<script>
  var table = null;
  var accountNumberNameToFlexGroupNamesMap = {};
  var headers = [];
  var debit = 0;
  var credit = 0;
  var setDebit = function (value) {
    if (value) {
      debit = debit + value;
      document.getElementById("debit").innerHTML = debit
    }
  };
  var setCredit = function (value) {
    if (value) {
      credit = credit + value;
      document.getElementById("credit").innerHTML = credit
    }
  };
  export default {
    data() {
      return {
        table:null,
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
                    setCredit(changes[i][3]);
                  }
                }else if (column === headers.length - 2) {//借方金额
                  if (changes[i][3] !== '') {
                    table.setDataAtCell(row, headers.length - 1, '');
                    setDebit(changes[i][3]);
                  }
                }

                if (column === 1) {//科目名称
                  for(let j=2;j<headers.length-2;j++) {
                    let flexGroupNames = accountNumberNameToFlexGroupNamesMap[changes[i][3]];
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
          fdate:new Date().toLocaleDateString()
        },
        rules: {
          fdate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/sys/voucher/form',{params:{id:this.$route.query.id}}).then((response)=>{
        let extra = response.data.extra;
        this.settings.colHeaders = extra.headerList;
        accountNumberNameToFlexGroupNamesMap = extra.accountNumberNameToFlexGroupNamesMap;
        this.settings.columns.push({type: 'text', strict: true, allowEmpty: false});
        this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName});
        this.settings.columns[1].source = extra.accountNumberNameList;
        let colHeaders = extra.headerList;
        headers = extra.headerList;
        for (let i=0;i<colHeaders.length;i++){
            if(colHeaders[i] === "供应商") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, supplierName:[],source: this.supplierName});
              this.settings.columns[i].source = extra.supplierNumberNameList;
            }else if(colHeaders[i] === "客户"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, customerName:[],source: this.customerName});
              this.settings.columns[i].source = extra.customerNumberNameList;
            }else if(colHeaders[i] === "银行账号"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, bankAccountName:[],source: this.bankAccountName});
              this.settings.columns[i].source = extra.bankAcntNumberNameList;
            }else if(colHeaders[i] === "其他类"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, otherTypeName:[],source: this.otherTypeName});
              this.settings.columns[i].source = extra.otherTypeNumberNameList;
            }else if(colHeaders[i] === "部门") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, departmentName: [], source: this.departmentName});
              this.settings.columns[i].source = extra.departmentNumberNameList;
            }else if(colHeaders[i] === "费用类") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, expenseTypeName:[],source: this.expenseTypeName});
              this.settings.columns[i].source = extra.expenseTypeNumberNameList;
            }else if(colHeaders[i] === "员工") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, empInfoName:[],source: this.empInfoName});
              this.settings.columns[i].source = extra.empInfoNumberNameList;
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
                this.$message.error(response.data.message);
                this.submitDisabled = false;
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      formSubmitAndAudit() {
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
