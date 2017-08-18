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
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  export default {
    data(){
      return this.getData()
    },mounted () {
      table = new Handsontable(this.$refs["handsontable"], this.settings);
    },
    methods:{
      formSubmit(){
        var that=this;
        this.inputForm.data =new Array();
        let list=table.getData();
        for(var item in list){
          if(!table.isEmptyRow(item)){
            this.inputForm.data.push(list[item]);
          }
        }
        this.inputForm.data = JSON.stringify(this.inputForm.data);
        axios.post('/api/ws/future/api/carrierOrder/ship',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
          if(response.data.success){
            this.$message(response.data.message);
            if (this.isCreate) {
              Object.assign(this.$data,this.getData());
              this.initPage();
            }else {
              this.$router.push({name: 'carrierOrderList', query: util.getQuery("carrierOrderList"),params:{_closeFrom:true}})
            }
          }else {
            that.submitDisabled = false;
            this.$message({
              showClose: true,
              message: response.data.message,
              type: 'error'
            });
          }
        });
      },getData(){
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
      }
    }
  }
</script>
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
