<template>
  <div>
    <head-tab active="adPricesystemChangeForm"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemChangeForm.filter')}}</el-button>
      <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
    </el-row>
    <el-dialog :title="$t('adPricesystemChangeForm.filter')"  v-model="formVisible"  size="tiny" class="search-form">
      <el-form :model="formData">
        <el-form-item :label="formLabel.productName.label">
          <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adPricesystemChangeForm.likeSearch')"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('adPricesystemChangeForm.sure')}}</el-button>
      </div>
    </el-dialog>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top:20px"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  import productSelect from 'components/future/product-select';

  export default{
    components:{productSelect},
    data(){
      return this.getData();

    },
    methods: {
      getData(){
        return {
          isInit: false,
          formData: {},
          submitData: {
            productName: '',
          },
          formLabel: {
            productName: {label: this.$t('adPricesystemChangeForm.productName')},
            productCode: {label: this.$t('adPricesystemChangeForm.productCode')}
          },
          inputForm: {
            data: []
          },
          rules: {},
          productTypes: [],
          adPricesystem: {},
          formVisible: false,
          submitDisabled: false,
          table: null,
          settings: {
            colHeaders: [this.$t('adPricesystemChangeForm.id'), this.$t('adPricesystemChangeForm.productCode'), this.$t('adPricesystemChangeForm.productName'), this.$t('adPricesystemChangeForm.volume'), this.$t('adPricesystemChangeForm.shouldGet')],
            rowHeaders: true,
            autoColumnSize: true,
            allowInsertRow: false,
            maxRows: 10000,
            columns: [{
              readOnly: true,
              width: 100
            }, {
              readOnly: true,
              width: 150
            }, {
              readOnly: true,
              width: 300
            }, {
              readOnly: true,
              type: "numeric",
              width: 150
            }, {
              readOnly: true,
              type: "numeric",
              width: 150
            }]
          },
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for (var item in list) {
          if (!this.table.isEmptyRow(item)) {
            this.inputForm.data.push(list[item]);
          }
        }
        this.inputForm.data = JSON.stringify(this.inputForm.data);
        axios.post('/api/ws/future/layout/adPricesystemChange/save', qs.stringify(this.inputForm, {allowDots: true})).then((response) => {
          this.$message(response.data.message);
          if(response.data.success){
            Object.assign(this.$data, this.getData());
          }
        }).catch(function () {
          that.submitDisabled = false;
        });
      }, search() {
        this.formVisible = false;
        this.getTableData();
      }, getTableData(){
        util.copyValue(this.formData, this.submitData);
        axios.get('/api/ws/future/layout/adPricesystemChange/findFilter', {params: this.submitData}).then((response) => {
          this.settings.data = response.data;
          this.table.loadData(this.settings.data);
        });
      }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
        axios.get('/api/ws/future/layout/adPricesystemChange/getQuery').then((response)=>{
          this.formData=response.data;
          util.copyValue(this.$route.query,this.formData);
          this.getTableData();
        });
        axios.get('/api/ws/future/layout/adPricesystemChange/findAdPricesystem').then((response)=>{
          this.adPricesystem = response.data;
          for(let key in this.adPricesystem){
            this.settings.colHeaders.push(this.adPricesystem[key].name);
            this.settings.columns.push({
              type:"numeric",
              width:300
            })
          }
        })
      }
        this.isInit = true;

      }
  }
</script>

<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>


