<template>
  <div>
    <head-tab active="shopDepositBatchForm"></head-tab>
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
  var table = null;
  export default{
    data(){
      return this.getData()
    },
    mounted () {
      table = new Handsontable(this.$refs["handsontable"], this.settings);
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
            colHeaders: [ '门店', '开单类型', '银行',  '部门','开单时间','市场保证金','形象保证金','演示机押金','备注'],
            columns: [{
              data:"shopName",
              type: 'autocomplete',
              strict: true,
              allowEmpty:false,
              tempShopNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempShopNames.indexOf(query)>=0) {
                  process(that.tempShopNames);
                } else {
                  var shopNames = new Array();
                  if(query.length>=2) {
                    axios.get('/api/ws/future/basic/depot/shop?name='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(var index in response.data) {
                          var shopName = response.data[index].name;
                          shopNames.push(shopName);
                          if(that.tempShopNames.indexOf(shopName)<0) {
                            that.tempShopNames.push(shopName);
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
              width:150
            }, {
              data:"billType",
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              width:200
            },{
              data:"bankName",
              type: "autocomplete",
              allowEmpty: true,
              strict: true,
              tempBankNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempBankNames.indexOf(query)>=0) {
                  process(that.tempBankNames);
                } else {
                  var bankNames = new Array();
                  if(query.length>=2) {
                    axios.get('/api/ws/future/basic/bank/search?key='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(var index in response.data) {
                          var bankName = response.data[index].name;
                          bankNames.push(bankName);
                          if(that.tempBankNames.indexOf(bankName)<0) {
                            that.tempBankNames.push(bankName);
                          }
                        }
                      }
                      process(bankNames);
                    });
                  } else {
                    process(bankNames);
                  }
                }
              },
              width:200
            },{
              data:'departMentName',
              type: 'autocomplete',
              strict: true,
              allowEmpty:false,
              width:200
            }, {
              data:'billDate',
              type:'date',
              dateFormat:'YYYY-MM-DD',
              correctFormat: true,
              allowEmpty:false,
              width:150
            }, {
              data:"marketAmount",
              type:'text',
              allowEmpty:false,
              width:120
            }, {
              data:'imageAmount',
              type:'text',
              allowEmpty:false,
              width:120
            }, {
              data:'demoPhoneAmount',
              type:'text',
              allowEmpty:false,
              width:120
            },{
              data:"remarks",
              type:"text",
              width: 120
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
                let shopDepositBatchDetailForm = {};
                shopDepositBatchDetailForm.shopName = row[0];
                shopDepositBatchDetailForm.outBillType = row[1];
                shopDepositBatchDetailForm.bankName = row[2];
                shopDepositBatchDetailForm.departMentName = row[3];
                shopDepositBatchDetailForm.billDate = row[4];
                shopDepositBatchDetailForm.marketAmount = row[5];
                shopDepositBatchDetailForm.imageAmount = row[6];
                shopDepositBatchDetailForm.demoPhoneAmount = row[7];
                shopDepositBatchDetailForm.remarks = row[8];
                tableData.push(shopDepositBatchDetailForm);
              }
            }
            this.inputForm.shopDepositBatchDetailFormList = tableData;
            axios.post('/api/ws/future/layout/shopDeposit/batchSave', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {

              if (response.data.success) {
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
        axios.get('/api/ws/future/layout/shopDeposit/getBatchForm').then((response)=>{
          this.inputForm = response.data;
          let departmentNameList=[];
          for(let each of response.data.extra.departMentList){
            departmentNameList.push(each.ffullName);
          }
          this.settings.columns[1].source=response.data.extra.outBillTypeList;
          this.settings.columns[3].source=departmentNameList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    created() {
      this.initPage();
    },
  }
</script>
