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
           contextMenu: util.contextMenu(this.$store.state.global.lang),
            manualColumnResize:true,
            minSpareRows: 100,
            startRows: 100,
            startCols: 7,
            colHeaders: [ '门店', '银行', '到账日期',  '到账金额','类型','备注', '转账类型'],
            columns: [{
              data:"depotName",
              type: "autocomplete",
              strict: true,
              tempShopNames: [],
              source: function (query, process) {
                if (this.tempShopNames.indexOf(query) >= 0) {
                  process(this.tempShopNames);
                } else {
                  let shopNames = [];
                  if (query.length >= 2) {
                    axios.get('/api/ws/future/basic/depot/shop?name=' + query).then((response) => {
                      if (response.data.length > 0) {
                        for (let row of response.data) {
                          let shopName = row.name;
                          shopNames.push(shopName);
                          if (this.tempShopNames.indexOf(shopName) < 0) {
                            this.tempShopNames.push(shopName);
                          }
                        }
                      }
                      process(shopNames);
                    });
                  } else {
                    process(shopNames);
                  }
                }
              },
              allowEmpty:false,
              width:300
            }, {
              data:"bank",
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width:150
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
              width: 150
            }, {
              data:"type",
              type: 'autocomplete',
              strict: true,
              allowEmpty:false,
              width: 100
            }, {
              data:"remarks",
              type:"text",
              width: 200
            }, {
              data:"transferType",
              type: 'autocomplete',
              strict: true,
              allowEmpty:false,
              width: 100
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
                bankInBatchDetailForm.transferType = row[6];
                tableData.push(bankInBatchDetailForm);
              }
            }
            this.inputForm.bankInBatchDetailFormList = tableData;

            axios.post('/api/ws/future/crm/bankIn/batchAdd', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {

              if(response.data.success){
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$alert(response.data.message);
                this.submitDisabled = false;
              }
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
          this.settings.columns[6].source=response.data.extra.transferTypeList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    created() {
      this.initPage();
    },
  }
</script>
