<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="productAdEdit"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="" icon="check">{{$t('productAdEdit.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('productAdEdit.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productAdEdit.filter')" v-model="formVisible"  size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.hasIme.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.hasIme"></bool-select>
              </el-form-item>
              <el-form-item :label="formLabel.code.label" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.allowBill.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.allowBill"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.productType.label" :label-width="formLabelWidth">
                <product-type-select v-model="formData.productTypeId"></product-type-select>
              </el-form-item>
              <el-form-item :label="formLabel.allowOrder.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.allowOrder"></bool-select>
              </el-form-item>
              <el-form-item :label="formLabel.outGroupName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outGroupName" filterable clearable :placeholder="$t('productAdEdit.inputWord')">
                  <el-option v-for="item in formData.outGroupNameList"  :key="item"   :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.netType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" filterable clearable :placeholder="$t('productAdEdit.inputKey')">
                  <el-option v-for="netType in formData.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productAdEdit.sure')}}</el-button>
        </div>
      </el-dialog>
        <div ref="handsontable" style="height:1200px;overflow:hidden;margin-top:20px"></div>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  import boolSelect from 'components/common/bool-select'
  import productTypeSelect from 'components/future/product-type-select'
  export default {
    components:{
      boolSelect,
      productTypeSelect
    },
    data() {
      return {
        submitDisabled:false,
        table:null,
        settings: {
          data:[],
          colHeaders: ["id",this.$t('productAdEdit.code'),this.$t('productAdEdit.name'),this.$t('productAdEdit.visible'),this.$t('productAdEdit.allowOrder'),this.$t('productAdEdit.price2'),this.$t('productAdEdit.expiryDateRemarks'),this.$t('productAdEdit.volume'),this.$t('productAdEdit.remarks')],
          rowHeaders:true,
          allowInsertRow:false,
          autoColumnSize:true,
          columns: [
            {data:"id", type: "autocomplete", readOnly: true, strict: true },
            {data:"code", type: "autocomplete", readOnly: true, strict: true },
            {data:"name", type: "autocomplete", readOnly: true, strict: true },
            {data:"visible", type: "autocomplete",  strict: true, source:["true","false"]},
            {data:"allowOrder", type: "autocomplete", strict: true, source:["true","false"]},
            {data:"price2", type: "numeric"},
            {data:"expiryDateRemarks", width:150},
            {data:"volume", type: "numeric", format: '0.00'},
            {data:"remarks", width:150},
          ]
        },
        formData:{},
        submitData:{
          name:'',
          type:'',
          hasIme:'',
          allowBill:'',
          productTypeId:'',
          allowOrder:'',
          outGroupName:'',
          netType:'',
        },
        formLabel:{
          name:{label:this.$t('productAdEdit.name')},
          hasIme:{label:this.$t('productAdEdit.hasIme'),value:""},
          code:{label:this.$t('productAdEdit.code')},
          allowBill:{label:this.$t('productAdEdit.allowBill'),value:""},
          productType:{label:this.$t('productAdEdit.productType'),value:""},
          allowOrder:{label:this.$t('productAdEdit.allowOrder'),value:""},
          outGroupName:{label:this.$t('productAdEdit.outGroupName'),value:""},
          netType:{label:this.$t('productAdEdit.netType'),value:""}
        },
        formLabelWidth: '120px',
        formProperty:{},
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
        util.copyValue(this.formData,this.submitData);
        util.setQuery("productList",this.submitData);
        axios.get('/api/ws/future/basic/product/filter',{params:this.submitData}).then((response) => {
          this.settings.data  = response.data;
          this.table.loadData(this.settings.data);
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/product/getQuery').then((response) =>{
        this.formData = response.data;
        console.log(this.formData);
        util.copyValue(this.$route.query,this.formData);
        this.search();
      });
    }
  };
</script>

