<template>
  <div>
    <head-tab active="purMrb"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item label="供应商"   prop="supplierNumber">
          <el-select v-model="formData.supplierNumber" filterable placeholder="请输入关键词">
            <el-option v-for="item in supplierList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仓库"   prop="stockNumber">
          <el-select v-model="formData.stockNumber" filterable placeholder="请输入关键词">
            <el-option v-for="item in stockList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门"   prop="departmentNumber">
          <el-select v-model="formData.departmentNumber" filterable placeholder="请输入关键词">
            <el-option v-for="item in departmentList" :key="item.fnumber" :label="item.ffullName" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
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
    data:function () {
      return this.getData();
    },
    methods: {
      getData() {
        return {
          supplierList:{},
          stockList:{},
          departmentList:{},
          settings: {
            rowHeaders:true,
            autoColumnSize:true,
            stretchH: 'all',
            minSpareRows: 1,
            height: 650,
            colHeaders: ["货品", "单价","数量", "备注"],
            columns: [
              {type: "autocomplete", strict: true, allowEmpty: false, productName:[],source: this.productName},
              {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
              {type: 'numeric', format:"0,0", allowEmpty: false, strict: true},
              {allowEmpty: false, strict:true, type: 'text'}
            ],
            contextMenu: true,
          },
          formData:{
          },
          rules: {
            billDate: [{ required: true, message: '必填项'}],
            supplierNumber: [{ required: true, message: '必填项'}],
            stockNumber: [{ required: true, message: '必填项'}],
            departmentNumber: [{ required: true, message: '必填项'}],
          },
          submitDisabled:false,
        };
      },
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
            var submitData = util.deleteExtra(this.formData);
            axios.post('/api/global/cloud/input/purMrb/save', qs.stringify(submitData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
              }else{
                this.$alert(response.data.message);
              }
              this.submitDisabled = false;
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage() {
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      },
    },
    created() {
      axios.get('/api/global/cloud/input/purMrb/form').then((response)=>{
        this.formData = response.data;
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.materialNameList;
        this.initPage();
        this.supplierList = extra.supplierList;
        this.stockList = extra.stockList;
        this.departmentList = extra.departmentList;
      });
    },
  }
</script>
