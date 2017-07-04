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
<script>
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
            colHeaders: [ '门店', '银行', '开单日期',  '到账金额','类型','备注'],
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
            this.inputForm.data = new Array();
            let list = table.getData();
            for (var item in list) {
              if (!table.isEmptyRow(item)) {
                this.inputForm.data.push(list[item]);
              }
            }
            this.inputForm.data = JSON.stringify(this.inputForm.data);
            axios.post('/api/ws/future/crm/bankIn/batchAllot', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              if (response.data.success) {
                Object.assign(this.$data, this.getData());
              }
              this.submitDisabled = false;
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/crm/bankIn/getForm').then((response)=>{
          this.inputForm = response.data;
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
