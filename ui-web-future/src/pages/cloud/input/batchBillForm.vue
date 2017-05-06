<template>
  <div>
    <head-tab active="batchBillForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" >
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="formLabel.storeNumber.label"  :label-width="formLabelWidth">
              <el-select v-model="formData.storeNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteStore" :loading="remoteLoading">
                <el-option v-for="item in storeList" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="formLabel.billDateBTW.label" :label-width="formLabelWidth">
              <el-billDate-picker v-model="formData.billDate" type="billDate" align="right" placeholder="请选择时间" :picker-options="pickerDateOption"></el-billDate-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          </el-col>
        </el-row>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        table:null,
        storeList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          product:'',
          colHeaders: ["门店","编码","货品","价格","数量","类型","备注"],
          columns: [
            {type: "autocomplete", allowEmpty: false, strict: true, tempCustomerNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempCustomerNames.indexOf(query)>=0) {
                  process(that.tempCustomerNames);
                } else {
                  var customerNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/input/bdCustomer/getNameByNameLike?name='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          var shopName = response.data[index];
                          customerNames.push(shopName);
                          if(that.tempCustomerNames.indexOf(shopName)<0) {
                            that.tempCustomerNames.push(shopName);
                          }
                        }
                      }
                      process(customerNames);
                    });
                  } else {
                    process(customerNames);
                  }
                }
              }
            },
            {type:"text", allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: true, strict: true,tempProductNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempProductNames.indexOf(query)>=0) {
                  process(that.tempProductNames);
                } else {
                  var productNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/input/bdMaterial/getNameByNameLike?name='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          var productName = response.data[index];
                          productNames.push(productName);
                          if(that.tempProductNames.indexOf(index)<0) {
                            that.tempProductNames.push(productName);
                          }
                        }
                      }
                      process(productNames);
                    });
                  } else {
                    process(productNames);
                  }
                }
              }
            },
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true,tempBillTypes:[], source: this.tempBillTypes},
            {type: "text", allowEmpty: true, strict: true }
          ],
          afterChange: function (changes, source) {
            var tbody = this.table.lastChild;
            if (source === 'edit') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1]==2;
                var tr = tbody.childNodes[row];
                var td = tr.childNodes[2];
                if(column){
                  var name = changes[i][3];
                  var productMap;
                  axios.get('/api/global/cloud/input/bdMaterial/getNameAndNumber').then((response) =>{
                    productMap = response.data;
                    for (let index in productMap){
                      if(productMap[index].name === name){
                        td.innerHTML = productMap[index].value;
//                            this.table.setDataAtCell(row,1,productMap[index].value,source);
                      }
                    }
//                    this.table.render();
                  });

//                  if(this.productMap[name]!=null) {
//                    console.log(this.productMap[name]);
////                    $('#grid').handsontable('setDataAtCell', row, 0, codeMap[name]);
//                  }
                }
              }
            }
          }
        },
        formData:{
          billDate:'',
          billDateBTW:'',
          storeNumber:'',
          data:[],
        },formLabel:{
          billDateBTW:{label:"日期"},
          storeNumber:{label:"仓库"},
        },
        pickerDateOption:'',
        submitDisabled:false,
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/batchBill/form').then((response)=>{
        this.settings.columns[5].source = response.data.typeList;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },
    methods: {
      formSubmit(){
        this.formData.data =new Array();
        let list = this.table.getData();
        for(let item in list){
          if(!this.table.isEmptyRow(item)){
            this.formData.data.push(list[item]);
          }
        }
        this.formData.data = JSON.stringify(this.formData.data);
        this.formData.billDateBTW = util.formatDateRange(this.formData.billDate);
        axios.post('/api/global/cloud/input/batchMaterial/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
        });
      },
      remoteStore(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/input/bdStock/getNameAndNumber',{params:{name:query}}).then((response)=>{
            this.storeList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.storeList = {};
        }
      }
    }
  }
</script>
