<template>
  <div>
    <head-tab active="afterSaleHeadInput"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemChangeForm.filter')}}</el-button>
    </el-row>
    <su-alert :text="shipResult.errorMsg" type="danger"></su-alert>
    <search-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
      <el-form :model="formData">
        <el-form-item label="串码" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="formData.imeStr" auto-complete="off" placeholder="请输入串码，逗号或换行隔开"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('adPricesystemChangeForm.sure')}}</el-button>
      </div>
    </search-dialog>
    <div class="position:relative;" style="margin-top:20px;margin-left:50px">
      <el-form>
        <el-form-item label="类型" :label-width="formLabelWidth">
          <el-select v-model="type" placeholder="请选择" @change="onchange(type)">
            <el-option v-for="item in options" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top:30px;margin-left:50px"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table = null;
  export default{
    data(){
      return {
        formData: {
          imeStr: '',
          type:'售后机',
          action:this.$route.query.action
        },
        inputForm: {
          data: ''
        },
        rules: {},
        type: '售后机',
        options: ['售后机', '窜货机'],
        formLabelWidth: '120px',
        formVisible: false,
        shipResult:"",
        submitDisabled: false,
        settings: {
          colHeaders: ['坏机串码', '坏机型号', '坏机门店', '退机类型', '包装', '内存', '坏机来源', '坏机所在库', '替换机串码', '替换机型号', '返还金额','备注'],
          rowHeaders: true,
          autoColumnSize: true,
          allowInsertRow: false,
          maxRows: 1000,
          columns: [{
            data: "badProductIme",
            width: 100
          }, {
            data: "badProductName",
            type: "autocomplete",
            allowEmpty:false ,
            strict: true,
            badProductNames: [],
            source: function (query, process) {
              var that = this;
              if (that.badProductNames.indexOf(query) >= 0) {
                process(that.badProductNames);
              } else {
                var productNames = new Array();
                if (query.length >= 2) {
                  axios.get('/api/ws/future/basic/product/search?name=' + query).then((response) => {
                    if (response.data.length > 0) {
                      for (var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if (that.badProductNames.indexOf(productName) < 0) {
                          that.badProductNames.push(productName);
                        }
                      }
                    }
                    process(productNames);
                  });
                } else {
                  process(productNames);
                }
              }
            },
            width: 100
          }, {
            data: "badDepotName",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            badDepotNames: [],
            source: function (query, process) {
              var that = this;
              if (that.badDepotNames.indexOf(query) >= 0) {
                process(that.badDepotNames);
              } else {
                var productNames = new Array();
                if (query.length >= 2) {
                  axios.get('/api/ws/future/basic/depot/shop?name=' + query).then((response) => {
                    if (response.data.length > 0) {
                      for (var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if (that.badDepotNames.indexOf(productName) < 0) {
                          that.badDepotNames.push(productName);
                        }
                      }
                    }
                    process(productNames);
                  });
                } else {
                  process(productNames);
                }
              }
            },
            width: 120
          }, {
            data: "badType",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            width: 100
          }, {
            data: "packageStatus",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            width: 100
          }, {
            data: "memory",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            width: 100
          }, {
            data: "fromDepotName",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            fromDepotNames: [],
            source: function (query, process) {
              var that = this;
              if (that.fromDepotNames.indexOf(query) >= 0) {
                process(that.fromDepotNames);
              } else {
                var productNames = new Array();
                if (query.length >= 2) {
                  axios.get('/api/ws/future/basic/depot/store?name=' + query).then((response) => {
                    if (response.data.length > 0) {
                      for (var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if (that.fromDepotNames.indexOf(productName) < 0) {
                          that.fromDepotNames.push(productName);
                        }
                      }
                    }
                    process(productNames);
                  });
                } else {
                  process(productNames);
                }
              }
            },
            width: 120
          }, {
            data: "toDepotName",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            toDepotNames: [],
            source: function (query, process) {
              var that = this;
              if (that.toDepotNames.indexOf(query) >= 0) {
                process(that.toDepotNames);
              } else {
                var productNames = new Array();
                if (query.length >= 2) {
                  axios.get('/api/ws/future/basic/depot/store?name=' + query).then((response) => {
                    if (response.data.length > 0) {
                      for (var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if (that.toDepotNames.indexOf(productName) < 0) {
                          that.toDepotNames.push(productName);
                        }
                      }
                    }
                    process(productNames);
                  });
                } else {
                  process(productNames);
                }
              }
            },
            width: 120
          }, {
            data: "replaceProductIme",
            width: 100
          }, {
            data: "replaceProductName",
            type: "autocomplete",
            allowEmpty: false,
            strict: true,
            replaceProductNames: [],
            source: function (query, process) {
              var that = this;
              if (that.replaceProductNames.indexOf(query) >= 0) {
                process(that.replaceProductNames);
              } else {
                var productNames = new Array();
                if (query.length >= 2) {
                  axios.get('/api/ws/future/basic/product/search?name=' + query).then((response) => {
                    if (response.data.length > 0) {
                      for (var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if (that.replaceProductNames.indexOf(productName) < 0) {
                          that.replaceProductNames.push(productName);
                        }
                      }
                    }
                    process(productNames);
                  });
                } else {
                  process(productNames);
                }
              }
            },
            width: 100
          }, {
            data: "replaceAmount",
            width: 100
          }, {
            data: "remarks",
            width: 100
          }],
        },
      }
    },
    mounted () {
      this.onchange(this.type);
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for (var item in list) {
          if (!this.table.isEmptyRow(item)) {
            this.inputForm.data.push(list[item]);
          }
        }
        this.inputForm.data = JSON.stringify(this.inputForm.data);
        axios.post('/api/ws/future/crm/afterSale/saveHead', qs.stringify({data: this.inputForm.data,type:this.type,action:this.$route.query.action}, {allowDots: true})).then((response) => {
          this.$message(response.data.message);
          this.settings.data = [];
          this.table.loadData(this.settings.data);
          this.submitDisabled = false;
        }).catch(function () {
          this.submitDisabled = false;
        });
      }, search() {
        this.formVisible = false;
        this.getData();
      }, getData(){
        console.log(this.formData)
        axios.get('/api/ws/future/crm/afterSale/headInputData', {params: this.formData}).then((response) => {
          this.settings.data = response.data.afterSaleInputList;
          this.table.loadData(this.settings.data);
          this.shipResult = response.data.restResponse;
          //错误信息
          var errorMsg = "";
          for(var index in this.shipResult.errors) {
            errorMsg = errorMsg + this.shipResult.errors[index].message + "<br/>";
          }
          this.shipResult.errorMsg = errorMsg;
        })
      }, onchange(type){
        if (this.type == '窜货机') {
          this.$router.push({ name: 'afterSaleHeadFleeInput',query:{action:this.$route.query.action}})
        }else {
          let categoryList=new Array();
          categoryList.push("退机类型")
          categoryList.push("内存")
          categoryList.push("包装")
          axios.get('/api/basic/sys/dictEnum/findByCategoryList',{params:{categoryList:categoryList}}).then((response)=> {
            this.settings.columns[4].source=util.getLabelList(response.data.PACKAGES_STATUS,'value');
            this.settings.columns[3].source=util.getLabelList(response.data.TOS_TORE_TYPE,'value');
            this.settings.columns[5].source=util.getLabelList(response.data.MEMORY,'value');
            this.table = new Handsontable(this.$refs["handsontable"], this.settings)
          })
        }
      }
    }, created(){

    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>


