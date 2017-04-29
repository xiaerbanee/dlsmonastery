<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="batchBillForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
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
          allowInsertRow:false,
          autoColumnSize:true,
          stretchH: 'all',
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          data:[],
          colHeaders: ["编码","门店","货品","价格","数量","类型","备注"],
          columns: [
            {type: "text", allowEmpty: false, strict: true },
            {type: "autocomplete", allowEmpty: false, strict: true, source: this.formProperty.customerNameList},
            {type: "autocomplete", allowEmpty: true, strict: true, source: this.formProperty.productNameList},
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true, source: this.formProperty.typeList},
            {type: "text", allowEmpty: true, strict: true }
          ],
        },
        formData:{

        },formLabel:{

        },
        formLabelWidth: '120px',
        formProperty:{
          customerNameList:[],
          productNameList:[],
          typeList:[],
        },
        formVisible: false
      };
    },
    mounted () {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings)
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
