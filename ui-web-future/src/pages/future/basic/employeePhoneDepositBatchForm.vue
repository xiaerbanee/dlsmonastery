<template>
  <div>
    <head-tab active="employeePhoneDepositBatchForm"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleFromCompany.save')}}</el-button>
    </el-row>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="24">
            <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table=null
    export default{
    data(){
      return{
        formData:{},
        inputForm:{
          data:null
        },
        rules:{},
        settings: {
          colHeaders: ["员工","门店","收款金额","部门","手机型号","备注"],
          rowHeaders:true,
          maxRows:1000,
          columns: [{
            data:"accountName",
            type:'text',
            strict:true,
            width:150
          },{
            data:"depot",
            type: "autocomplete",
            allowEmpty: true,
            strict: true,
            tempDepotNames:[],
            source:function (query, process) {
              var that = this;
              if(that.tempDepotNames.indexOf(query)>=0) {
                process(that.tempDepotNames);
              } else {
                var depotNames = new Array();
                if(query.length>=2) {
                  axios.get('/api/ws/future/basic/depot/directShop?name='+query).then((response)=>{
                    if(response.data.length>0) {
                      for(var index in response.data) {
                        var depotName = response.data[index].name;
                        depotNames.push(depotName);
                        if(that.tempDepotNames.indexOf(depotName)<0) {
                          that.tempDepotNames.push(depotName);
                        }
                      }
                    }
                    process(depotNames);
                  });
                } else {
                  process(depotNames);
                }
              }
            },
            width:200
          },{
            data:"amount",
            type:'text',
            allowEmpty:false,
            width:150
          },{
            data:"department",
            type: 'autocomplete',
            strict: true,
            allowEmpty:false,
            width:200

          },{
            data:"productName",
            type: "autocomplete",
            allowEmpty: true,
            strict: true,
            tempProductNames:[],
            source:function (query, process) {
              var that = this;
              if(that.tempProductNames.indexOf(query)>=0) {
                process(that.tempProductNames);
              } else {
                var productNames = new Array();
                if(query.length>=2) {
                  axios.get('/api/ws/future/basic/product/searchFullText?key='+query).then((response)=>{
                    if(response.data.length>0) {
                      for(var index in response.data) {
                        var productName = response.data[index].name;
                        productNames.push(productName);
                        if(that.tempProductNames.indexOf(productName)<0) {
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
            },
            width:150
          },{
            data:"remarks",
            width:150
          }]
        },
      }
    },
    mounted () {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings)
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.data =new Array();
            let list=this.table.getData();
            for(var item in list){
              if(!this.table.isEmptyRow(item)){
                this.inputForm.data.push(list[item]);
              }
            }
            this.inputForm.data = JSON.stringify(this.inputForm.data);
            axios.post('/api/basic/hr/employeePhoneDeposit/batchSave',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
        axios.get('/api/ws/future/basic/employeePhoneDeposit/getBatchForm').then((response)=> {
          this.settings.columns[3].source=response.data.extra.departments;
          this.table = new Handsontable(this.$refs["handsontable"], this.settings)
        })
    }
  }
</script>
<style>
  @import "../../../../node_modules/handsontable/dist/handsontable.full.css";
</style>
