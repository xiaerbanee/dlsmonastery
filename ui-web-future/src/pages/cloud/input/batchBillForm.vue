<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="batchBillForm"></head-tab>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        submitDisabled:false,
        table:null,
        customerNameList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          colHeaders: ["编码","门店","货品","价格","数量","类型","备注"],
          columns: [
            {type: "text", allowEmpty: false, strict: true },
            {
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              customerNameList: [],
              source: function (query, process) {
                var that = this;
                  var shopNames = new Array();
                    axios.get('/api/global/cloud/input/batchBill/form').then((response) => {
                        console.log(response.data.customerNameList);
                      if (response.data.customerNameList > 0) {
                        for (var index in response.data.customerNameList) {
                          var shopName = response.data[index];
                          shopNames.push(shopName);
                          if (that.customerNameList.indexOf(shopName) < 0) {
                            that.customerNameList.push(shopName);
                          }
                        }
                      }
                      process(shopNames);
                    });
              },
            },
            {type: "autocomplete", allowEmpty: true, strict: true},
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true },
            {type: "text", allowEmpty: true, strict: true }
          ],
        },
        formData:{

        },formLabel:{
        },
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    mounted () {
      axios.get('/api/global/cloud/input/batchBill/form').then((response) =>{
        this.customerNameList = response.data.customerNameList;

        console.log(this.customerNameList);
        this.table = new Handsontable(this.$refs["handsontable"], this.settings);
      });

    },
    methods: {
      search() {
        this.formVisible = false;
        this.formLabel.hasIme.value = util.bool2str(this.formData.hasIme);
        this.formLabel.allowBill.value = util.bool2str(this.formData.allowBill);
        this.formLabel.allowOrder.value =  util.bool2str(this.formData.allowOrder);
        this.formLabel.productType.value = util.getLabel(this.formProperty.productTypes, this.formData.productType);
        util.setQuery("productList",this.formData);
        axios.get('/api/crm/product/filter',{params:this.formData}).then((response) => {
          this.settings.data  = response.data;
          this.table.loadData(this.settings.data);
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/global/cloud/input/batchBill/form').then((response) =>{
        this.formProperty = response.data;
      });
    }
  };
</script>
