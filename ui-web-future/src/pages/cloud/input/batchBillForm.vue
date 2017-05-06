<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="batchBillForm"></head-tab>
    <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        submitDisabled:false,
        table:null,
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
            {type: "autocomplete", allowEmpty: false, strict: true,tempBillTypes:[],
              source:function (query, process) {
                var that = this;
                if(that.tempBillTypes.indexOf(query)>0) {
                  process(that.tempBillTypes);
                } else {
                    axios.get('/api/global/cloud/input/batchBill/getBillTypeEnum').then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          if(that.tempBillTypes.indexOf(index)<0) {
                            that.tempBillTypes.push(index);
                          }
                        }
                      }
                      process(response.data);
                    });
                }
              }
            },
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
                    this.table.render();
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

        },formLabel:{
        },
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    mounted () {
        this.table = new Handsontable(this.$refs["handsontable"], this.settings);
    },
    methods: {

    },created () {

    }
  };
</script>
