<template>
  <div>
    <head-tab active="stkMisDelivery"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="部门"   prop="departmentNumber">
          <el-select v-model="formData.departmentNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteDepartment" :loading="remoteLoading">
            <el-option v-for="item in departmentList" :key="item.fnumber" :label="item.ffullName" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-button type="primary" :disabled="submitDisabled" @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
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
        departmentList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 1,
          colHeaders: ["货品编码", "货品", "仓库", "数量", "类型", "备注"],
          columns: [
            {type: "autocomplete", strict: true, productNumber:[],source: this.productNumber},
            {type: "autocomplete", allowEmpty: false, strict: true, productName:[],source: this.productName},
            {type: "autocomplete", allowEmpty: false, strict: true, stockName:[],source: this.stockName},
            {type: 'numeric',allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: false, strict: true, types:[],source: this.types},
            {type: "text", allowEmpty: true, strict: true }
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if(column === 1) {
                  let productName = changes[i][3];
                  if(util.isNotBlank(productName)){
                    axios.get('/api/global/cloud/kingdee/bdMaterial/findByName?name=' + productName).then((response) => {
                      let material = response.data;
                      table.setDataAtCell(row, 0, material.fnumber);
                    });
                  }else {
                    table.setDataAtCell(row, 0, null);
                  }
                }else if(column === 0){
                  let productNumber = changes[i][3];
                  if(util.isNotBlank(productNumber)){
                    axios.get('/api/global/cloud/kingdee/bdMaterial/findByNumber?number=' + productNumber).then((response) => {
                      let material = response.data;
                      table.setDataAtCell(row, 1, material.fname);
                    });
                  }else {
                    table.setDataAtCell(row, 1, null);
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
          departmentNumber: [{ required: true, message: '必填项'}],
          billDate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/stkMisDelivery/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.materialNumberList;
        this.settings.columns[1].source = extra.materialNameList;
        this.settings.columns[2].source = extra.stockNameList;
        this.settings.columns[4].source = extra.stkMisDeliveryTypeEnums;
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
            axios.post('/api/global/cloud/input/stkMisDelivery/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      remoteDepartment(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/kingdee/bdDepartment/findByNameLike',{params:{name:query}}).then((response)=>{
            this.departmentList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.departmentList = {};
        }
      },
    }
  }
</script>
