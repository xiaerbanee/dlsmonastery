<template>
  <div>
    <head-tab active="carrierOrderShip"></head-tab>
    <div class="outer">
      <div class="header">
        <h1>商城发货</h1>
      </div>
      <el-form :model="inputForm" ref="inputForm"  label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="24">
            <div ref="handsontable" style="width:800px;height:603px;overflow:hidden;"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item>
              <el-button type="primary" class="saveBtn" icon="check" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBatchAdd.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  export default {
    data(){
      return {
        inputForm:{},
        submitDisabled:false,
          settings:{
            colHeaders:[this.$t('carrierOrderShip.billCode'),this.$t('carrierOrderShip.shipCode')],
            rowHeaders: true,
            startCols:3,
            startRows:100,
            columns:[
              {
                width:350
              },{
                width:350
              }
            ]
          }
      }
    },mounted () {
      table = new Handsontable(this.$refs["handsontable"], this.settings);
    },
    methods:{

    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<style>
  .outer{
    width: 800px;
    margin: 0 auto;
  }
  .header{
    text-align: center;
  }
  button.saveBtn{
    margin-top: 20px;
  }
  .el-col{
    text-align: center;
    margin-top: 20px;
  }
</style>
