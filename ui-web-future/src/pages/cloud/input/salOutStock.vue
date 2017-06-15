<template>
  <div>
    <head-tab active="salOutStock"></head-tab>
    <div>
      <el-form :model="formData"  ref="inputForm" :rules="rules" class="form input-form" :inline="true">
        <el-form-item label="仓库"   prop="storeNumber">
          <el-select v-model="formData.storeNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteStock" :loading="remoteLoading">
            <el-option v-for="item in stockList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="formSubmit" icon="check" style="margin-left:30px;">保存</el-button>
        </el-form-item>
      </el-form>
      <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table = null;
  export default {
    data() {
      return {
        table:null,
        stockList:{},
        productMap:[],
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 5,
          colHeaders: ["货品编码","门店","货品","价格","数量","类型","备注"],
          columns: [
            {type:"text", allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: false, strict: true, customerNames:[],source:this.customerNames},
            {type: "autocomplete", allowEmpty: true, strict: true,productNames:[],source:this.productNames},
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true,billType:[], source: this.billType},
            {type: "text", allowEmpty: true, strict: true }
          ],
          afterChange: function (changes) {
            if(changes) {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1]==2;
                if(column){
                  let name = changes[i][3];
                  axios.get('/api/global/cloud/kingdee/bdMaterial/findByName?name='+ name).then((response) =>{
                    let material = response.data;
                    table.setDataAtCell(row,0,material.fnumber);
                  });
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          storeNumber:'',
          json:{},
        },rules: {
          storeNumber: [{ required: true, message: '请选择仓库'}],
          billDate: [{ required: true, message: '请选择时间'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/salOutStock/form').then((response)=>{
        this.settings.columns[1].source = response.data.bdCustomerNameList;
        this.settings.columns[2].source = response.data.bdMaterialNameList;
        this.settings.columns[5].source = response.data.outStockBillTypeEnums;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.formData.json =new Array();
            let list = table.getData();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                this.formData.json.push(list[item]);
              }
            }
            this.formData.json = JSON.stringify(this.formData.json);
            this.formData.billDate = util.formatLocalDate(this.formData.billDate);
            axios.post('/api/global/cloud/input/salOutStock/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      remoteStock(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/kingdee/bdStock/findByNameLike',{params:{name:query}}).then((response)=>{
            this.stockList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.stockList = {};
        }
      }
    }
  }
</script>
