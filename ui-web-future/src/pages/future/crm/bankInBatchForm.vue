<template>
  <div>
    <head-tab active="bankInBatchForm"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
      </el-row>
      <el-form  :model="inputForm"  ref="inputForm">
        <el-row>
          <el-col :span="24">
            <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  let table = null;
  export default{
    data(){
      return this.getData()
    },
    methods: {
      getData() {
        return {
          inputForm: {
            data:""
          },
          submitDisabled: false,
          settings: {
            rowHeaders: true,
            minSpareRows: 100,
            startRows: 100,
            startCols: 6,
            colHeaders: [ '门店', '银行', '到账日期',  '到账金额','类型','备注'],
            columns: [{
              data:"depotName",
              type: 'text',
              strict: true,
              allowEmpty:false,
              width:150
            }, {
              data:"bank",
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width:200
            }, {
              data:'billDate',
              type:'date',
              dateFormat:'YYYY-MM-DD',
              correctFormat: true,
              allowEmpty:false,
              width:150
            }, {
              data:"amount",
              type:'text',
              allowEmpty:false,
              strict: true,
              width: 200
            }, {
              data:"type",
              type: 'autocomplete',
              strict: true,
              allowEmpty:false,
              width: 200
            }, {
              data:"remarks",
              type:"text",
              width: 200
            }],
          },
        };
      },
      formSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            let tableData = [];

            let list = table.getData();
            for (let item in list) {
              if (!table.isEmptyRow(item)) {
                let row = list[item];
                let bankInBatchDetailForm = {};
                bankInBatchDetailForm.shopName = row[0];
                bankInBatchDetailForm.bankName = row[1];
                bankInBatchDetailForm.inputDate = row[2];
                bankInBatchDetailForm.amount = row[3];
                bankInBatchDetailForm.type = row[4];
                bankInBatchDetailForm.remarks = row[5];
                tableData.push(bankInBatchDetailForm);
              }
            }
            this.inputForm.bankInBatchDetailFormList = tableData;

            axios.post('/api/ws/future/crm/bankIn/batchAdd', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              this.initPage();

            }).catch( () => {
              this.submitDisabled = false;
            });

          }
        })
      },initPage(){
        axios.get('/api/ws/future/crm/bankIn/getBatchForm').then((response)=>{
          this.inputForm = response.data;
          this.settings.columns[1].source=response.data.extra.bankNameList;
          this.settings.columns[4].source=response.data.extra.typeList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    created() {
      this.initPage();
    },
  }
</script>
