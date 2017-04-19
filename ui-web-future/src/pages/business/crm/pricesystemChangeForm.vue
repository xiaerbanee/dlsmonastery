<template>
  <div>
    <head-tab active="pricesystemChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('pricesystemChangeForm.product')" prop="productIdList">
              <el-select v-model="inputForm.productIdList" multiple filterable  :placeholder="$t('pricesystemChangeForm.inputWord')" @change="onchange" :clearable=true >
                <el-option v-for="item in formProperty.products" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
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
  export default{
    data(){
      return{
        submitDisabled:false,
        table:null,
        settings: {
          cells: function (row, col, prop) {
            var cellProperties = {width:100};
            if(col==0) {
              cellProperties.width = 100;
            }
            if (col==0 || row ==0) {
              cellProperties.readOnly = true;
            } else {
              cellProperties.type = 'numeric';
            }
            return cellProperties;
          },
          data:[],
          rowHeaders:false,
				  colWidth:70
        },
        formProperty:{},
        remoteLoading:false,
        inputForm:{
          productIdList:"",
          data:[],
          remarks:''
        },
        rules: {}
      }
    },
    mounted () {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings)
    },
    methods:{
      formSubmit(){
        var that = this;
        that.submitDisabled = true;
        that.table.validateCells(function(valid){
          if(valid) {
            var form = that.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                that.inputForm.data = that.settings.data;
                axios.post('/api/crm/pricesystemChange/save',qs.stringify(that.inputForm)).then((response)=> {
                  that.$message(response.data.message);
                  form.resetFields();
                  that.submitDisabled = false;
                });
              }
            })
          } else {
              that.$message.error("表格中有错误数据");
              that.submitDisabled = false;
          }
        });
      },onchange(){
        if(this.inputForm.productIdList.length>0){
          axios.get('/api/crm/pricesystemChange/getPricesystemDetail',{params:{productIdList:this.inputForm.productIdList}}).then((response)=>{
            this.settings.data=response.data;
            this.table.loadData(this.settings.data);
          });
        } else {
            this.settings.data=[];
            this.table.loadData(this.settings.data);
        }
      }
    },created(){
      axios.get('/api/crm/pricesystemChange/getFormProperty').then((response)=>{
        this.formProperty=response.data;
      });
    }
  }
</script>
<style>
@import "~handsontable/dist/handsontable.full.css";
</style>
