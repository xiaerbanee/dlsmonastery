<template>
  <div>
    <head-tab active="salOutStock"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="仓库"   prop="stockNumber">
          <el-select v-model="formData.stockNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteStock" :loading="remoteLoading">
            <el-option v-for="item in stockList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
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
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 1,
          colHeaders: ["货品编码","门店","货品","价格","数量","类型","备注"],
          columns: [
            {type:"text", allowEmpty: false, readOnly: true, strict: true},
            {type: "autocomplete", allowEmpty: false, strict: true, customerNames:[],source:this.customerNames},
            {type: "autocomplete", allowEmpty: true, strict: true,productNames:[],source:this.productNames},
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true,billType:[], source: this.billType},
            {type: "text", allowEmpty: true, strict: true }
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if(column === 2){
                  let name = changes[i][3];
                  if(util.isNotBlank(name)) {
                    axios.get('/api/global/cloud/kingdee/bdMaterial/findByName?name='+ name).then((response) =>{
                      let material = response.data;
                      table.setDataAtCell(row,0,material.fnumber);
                    });
                  } else {
                    table.setDataAtCell(row,0,null);
                  }
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          json:[],
        },rules: {
          stockNumber: [{ required: true, message: '必填项'}],
          billDate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/salOutStock/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[1].source = extra.bdCustomerNameList;
        this.settings.columns[2].source = extra.bdMaterialNameList;
        this.settings.columns[5].source = extra.outStockBillTypeEnums;
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
      },
    }
  }
</script>
