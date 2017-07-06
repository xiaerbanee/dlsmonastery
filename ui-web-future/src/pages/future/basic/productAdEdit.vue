<template>
  <div>
    <head-tab active="productAdEdit"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formSubmit()" icon="check">{{$t('productAdEdit.save')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('productAdEdit.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('productAdEdit.filter')" v-model="formVisible"  size="small" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData"  ref="inputForm" >
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="$t('productAdEdit.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.hasIme')" :label-width="formLabelWidth">
                <bool-select v-model="formData.hasIme"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.code')" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.visible')" :label-width="formLabelWidth">
                <bool-select v-model="formData.visible"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('productAdEdit.productType')" :label-width="formLabelWidth">
                <product-type-select v-model="formData.productTypeId"></product-type-select>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.allowOrder')" :label-width="formLabelWidth">
                <bool-select v-model="formData.allowOrder"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.outGroupName')" :label-width="formLabelWidth">
                <el-select v-model="formData.outGroupName" filterable clearable :placeholder="$t('productAdEdit.inputWord')">
                  <el-option v-for="item in formData.extra.outGroupNameList"  :key="item"   :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.netType')" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" filterable clearable :placeholder="$t('productAdEdit.inputKey')">
                  <el-option v-for="netType in formData.extra.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productAdEdit.sure')}}</el-button>
        </div>
      </search-dialog>
        <div ref="handsontable" style="height:1200px;overflow:hidden;margin-top:20px"></div>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  import boolSelect from 'components/common/bool-select';
  import productTypeSelect from 'components/future/product-type-select';
  var table = null;

  export default {
    components:{
      boolSelect,
      productTypeSelect
    },
    data() {
      return this.getData();
    },mounted() {
      //Handsontable初始化操作
      this.search();
      table = new Handsontable(this.$refs["handsontable"], this.settings);

    }, methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
        getData(){
          return {
            submitDisabled:false,
            searchText:'',
            settings: {
              data:[],
              colHeaders: ["id",this.$t('productAdEdit.code'),this.$t('productAdEdit.name'),this.$t('productAdEdit.visible'),this.$t('productAdEdit.allowOrder'),this.$t('productAdEdit.price2'),this.$t('productAdEdit.expiryDateRemarks'),this.$t('productAdEdit.volume'),this.$t('productAdEdit.remarks')],
              rowHeaders:true,
              height: 720,
              allowInsertRow:false,
              autoColumnSize:true,
              columns: [
                {data:"id",readOnly: true, strict: true ,width: 50 },
                {data:"code",readOnly: true, strict: true, width: 300 },
                {data:"name",readOnly: true, strict: true, width: 450 },
                {data:"visible", type: "autocomplete", strict: true, source:['是','否'], width: 70},
                {data:"allowOrder", type: "autocomplete", strict: true, source:['是','否'], width: 80},
                {data:"price2", type: "numeric",format:"0.00", width: 70},
                {data:"expiryDateRemarks", width:150},
                {data:"volume", type: "numeric", format: '0.00', width: 50},
                {data:"remarks", width:150},
              ]
            },
            formData:{
              extra:{}
            },
            inputForm:{
              productList:[],
            },
            formLabelWidth: '120px',
            formVisible: false,
          };
        },
      search() {
        this.formVisible = false;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("productList",submitData);
        axios.get('/api/ws/future/basic/product/filter',{params:submitData}).then((response) => {
          this.settings.data  = response.data;
          for (let index in this.settings.data) {
            this.settings.data[index].visible = this.settings.data[index].visible ? '是' : '否';
            this.settings.data[index].allowOrder = this.settings.data[index].allowOrder ? '是' : '否';
          }
          table.loadData(this.settings.data);
        });
      },formSubmit(){
            this.submitDisabled = true;
            table.validateCells((valid)=> {
            if(valid) {
              this.inputForm.productList = JSON.stringify(table.getData());
              axios.post('/api/ws/future/basic/product/batchSave', qs.stringify(this.inputForm, {allowDots: true})).then((response) => {
                this.$message(response.data.message);
              }).catch( ()=> {
                this.submitDisabled = false;
              });
            }
        })
      },initPage() {
        //页面初始化事件
        this.pageHeight = window.outerHeight - 320;
        axios.get('/api/ws/future/basic/product/getQuery').then((response) => {
          this.formData = response.data;
          util.copyValue(this.$route.query, this.formData);
        });
      }
    },created() {
      this.initPage();
    }
  };
</script>


