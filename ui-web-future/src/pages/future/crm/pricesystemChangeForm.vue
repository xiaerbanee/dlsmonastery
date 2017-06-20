<template>
  <div>
    <head-tab active="pricesystemChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('pricesystemChangeForm.product')" prop="productId">
              <product-select v-model="inputForm.productIds" :multiple=true @input="handleChange"></product-select>
            </el-form-item>
            <el-form-item :label="$t('pricesystemChangeForm.remarks')" prop="remarks">
              <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('pricesystemChangeForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-row>
        <el-col :span="24">
          <div ref="handsontable"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  import productSelect from 'components/future/product-select'
  export default{
    components:{
      productSelect,
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        submitDisabled:false,
        table:null,
        settings: {
          cells: function (row, col, prop) {
            var cellProperties = {width:150};
            if (col==0) {
              cellProperties.readOnly = true;
            } else {
              cellProperties.type = 'numeric';
            }
            return cellProperties;
          },
          fixedColumnsLeft:1,
          fixedRowsTop:0,
          colHeaders: [],
          data:[],
          columns:[]
        },
        pricesystem:{},
        formProperty:{},
        remoteLoading:false,
        inputForm:{
          productIds:"",
          data:[],
          remarks:''
        },
        rules: {}
      }
    },
      formSubmit(){
        var that = this;
        that.submitDisabled = true;
        that.table.validateCells(function(valid){
          if(valid) {
            var form = that.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                that.inputForm.data = JSON.stringify(that.settings.data);
                axios.post('/api/ws/future/crm/pricesystemChange/save',qs.stringify(that.inputForm, {allowDots: true})).then((response)=> {
                  this.$message(response.data.message);
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }).catch(function () {
                  that.submitDisabled = false;
                });
              }
            })
          } else {
              that.$message.error("表格中有错误数据");
              that.submitDisabled = false;
          }
        });
      },handleChange(){
          if(this.inputForm.productIds.length == 0){
            this.table = null;
          }else{
            axios.get('/api/ws/future/basic/pricesystemDetail/filter',{params:{productIds:this.inputForm.productIds}}).then((response)=>{
              this.settings.data = response.data;
              this.table.loadData(this.settings.data);
            });
          }
      },
      tableInit(){
        axios.get('/api/ws/future/basic/pricesystem/filter').then((response)=>{
          this.pricesystem=response.data;
          this.settings.colHeaders.push("");
          this.settings.columns.push({readonly:true});
          for(let key in this.pricesystem){
            this.settings.colHeaders.push(this.pricesystem[key].name);
            this.settings.columns.push({type: "numeric"});
          }
          this.table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      },initPage(){

        this.tableInit();

        axios.get('/api/ws/future/crm/pricesystemChange/getForm').then((response)=>{
          this.formProperty=response.data;
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
<style>
@import "~handsontable/dist/handsontable.full.css";
</style>
