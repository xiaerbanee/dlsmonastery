<template>
  <div>
    <head-tab active="adPricesystemChangeForm"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemChangeForm.filter')}}</el-button>
      <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
    </el-row>
    <el-dialog :title="$t('adPricesystemChangeForm.filter')"  v-model="formVisible"  size="tiny" class="search-form">
      <el-form :model="formData">
        <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="formData.ime" auto-complete="off" :autosize="autosize" placeholder="请输入串码，逗号或换行隔开"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('adPricesystemChangeForm.sure')}}</el-button>
      </div>
    </el-dialog>
    <div class="position:relative;margin-top:20px">
      <el-form :model="formData">
        <el-form-item label="类型" :label-width="formLabelWidth">
          <el-select v-model="type" placeholder="请选择" @change="onchange(type)">
            <el-option v-for="item in options" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top:20px"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'

  export default{
    data(){
      return{
        formData:{
          ime:'',
        },formLabel:{
          ime:{label:'串码'},
        },
        inputForm:{
          data:''
        },
        colHeaders1:['坏机串码','坏机型号','核销门店','退机类型','包装','内存','坏机来源','坏机所在库','替换机串码','替换机型号','返还金额'],
        colHeaders2:['坏机串码','坏机型号','核销门店','退机类型','包装','内存','坏机来源','坏机所在库','替换机串码','替换机型号','返还金额','窜货机串码','窜货机门店','联系人','手机号','地址','购买金额'],
        columns1: [{
          data:"badProductImeId",
          width:100
        },{
          data:"badProductId",
          type: "autocomplete",
          allowEmpty: false,
          strict: true,
          badProductNames:[],
          source:function (query, process) {
            var that = this;
            if(that.badProductNames.indexOf(query)>=0) {
              process(that.badProductNames);
            } else {
              var productNames = new Array();
              if(query.length>=2) {
                axios.get('/api/ws/future/basic/product/search?name='+query).then((response)=>{
                  if(response.data.length>0) {
                    for(var index in response.data) {
                      var productName = response.data[index].name;
                      productNames.push(productName);
                      if(that.badProductNames.indexOf(productName)<0) {
                        that.badProductNames.push(productName);
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
          width:100
        },{
          data:"badDepotId",
          type: "autocomplete",
          allowEmpty: false,
          strict: true,
          badDepotNames:[],
          source:function (query, process) {
            var that = this;
            if(that.badDepotNames.indexOf(query)>=0) {
              process(that.badDepotNames);
            } else {
              var productNames = new Array();
              if(query.length>=2) {
                axios.get('/api/ws/future/basic/depotShop/searchShop?category=SHOP&name='+query).then((response)=>{
                  console.log(response.data)

                  if(response.data.length>0) {
                    for(var index in response.data) {
                      var productName = response.data[index].name;
                      productNames.push(productName);
                      if(that.badDepotNames.indexOf(productName)<0) {
                        that.badDepotNames.push(productName);
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
          width:120
        },{
          data:"badType",
          width:100
        },{
          data:"packageStatus",
          width:100
        },{
          data:"memory",
          width:100,
        },{
           data:"fromDepotId",
           width:100
        },{
          data:"toDepotId",
          width:100
        },{
          data:"replaceProductImeId",
          width:100
        },{
          data:"replaceProductId",
          type: "autocomplete",
          allowEmpty: false,
          strict: true,
          replaceProductNames:[],
          source:function (query, process) {
            var that = this;
            if(that.replaceProductNames.indexOf(query)>=0) {
              process(that.replaceProductNames);
            } else {
              var productNames = new Array();
              if(query.length>=2) {
                axios.get('/api/ws/future/basic/product/search?name='+query).then((response)=>{
                  if(response.data.length>0) {
                    for(var index in response.data) {
                      var productName = response.data[index].name;
                      productNames.push(productName);
                      if(that.replaceProductNames.indexOf(productName)<0) {
                        that.replaceProductNames.push(productName);
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
          width:100
        },{
          data:"replaceAmount",
          width:100
        }],
        columns2: [{
          data:"badProductImeId",
          width:100
        },{
          data:"badProductId",
          width:100
        },{
          data:"badDepotId",
          readOnly: true,
          width:100
        },{
          data:"badType",
          width:100
        },{
          data:"packageStatus",
          width:100
        },{
          data:"memory",
          width:100,
        },{
          data:"fromDepotId",
          width:100
        },{
          data:"toDepotId",
          width:100
        },{
          data:"replaceProductImeId",
          width:100
        },{
          data:"replaceProductId",
          width:100
        },{
          data:"replaceAmount",
          width:100
        },{
          data:"ime",
          width:100
        },{
          data:"fleeShopName",
          width:100
        },{
          data:"contact",
          width:100
        },{
          data:"mobilePhone",
          width:100
        },{
          data:"address",
          width:100
        },{
          data:"buyAmount",
          width:100
        }],
        autosize:{ minRows: 4},
        rules:{},
        type:'售后机',
        options:['售后机','窜货机'],
        productTypes:[],
        formLabelWidth: '120px',
        formVisible: false,
        submitDisabled:false,
        table:null,
        settings: {
          colHeaders:null,
          rowHeaders:true,
          autoColumnSize:true,
          allowInsertRow:false,
          maxRows:1000,
          columns:null,
        },
      }

    },
    mounted () {
      this.onchange(this.type);
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for(var item in list){
          if(!this.table.isEmptyRow(item)){
            this.inputForm.data.push(list[item]);
          }
        }
        this.inputForm.data = JSON.stringify(this.inputForm.data);
        axios.post('/api/crm/adPricesystemChange/save',qs.stringify({data:this.inputForm.data},{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
          this.submitDisabled = false;
        }).catch(function () {
          this.submitDisabled = false;
        });
      },search() {
        this.formVisible = false;
        this.getData();
      },getData(){
        axios.get('/api/crm/adPricesystemChange/findFilter',{params:this.formData}).then((response)=>{
          this.settings.data = response.data;
          this.table.loadData(this.settings.data);
        })
      },onchange(type){
        this.settings.colHeaders=null;
        this.settings.columns=null;
        if(this.type=='售后机'){
          this.settings.colHeaders=this.colHeaders1;
          this.settings.columns=this.columns1;
        }else{
          this.settings.colHeaders=this.colHeaders2;
          this.settings.columns=this.columns2;
        }
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      }
    }, created(){
      this.getData();
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>


