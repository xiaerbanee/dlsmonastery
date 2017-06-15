<template>
  <div>
    <head-tab active="stkInStock"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-form-item label="供应商"   prop="supplierNumber">
          <el-select v-model="formData.supplierNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteSupplier" :loading="remoteLoading">
            <el-option v-for="item in supplierList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仓库"   prop="stockNumber">
          <el-select v-model="formData.stockNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteStock" :loading="remoteLoading">
            <el-option v-for="item in stockList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门"   prop="departmentNumber">
          <el-select v-model="formData.departmentNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteDepartment" :loading="remoteLoading">
            <el-option v-for="item in departmentList" :key="item.fnumber" :label="item.ffullName" :value="item.fnumber"></el-option>
          </el-select>
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
  var typeList = [];
  export default {
    data() {
      return {
        table:null,
        supplierList:{},
        stockList:{},
        departmentList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          minSpareRows: 1,
          height: 650,
          colHeaders: ["货品编码","货品","单价","数量","备注","售后服务费","服务费类型",'广告让利(返利1.5%输入1.5)','让利类型'],
          columns: [
            {type: 'text',allowEmpty: false, strict:true, readOnly: true},
            {type: "autocomplete", strict: true, allowEmpty: false, materialName:[],source: this.materialName},
            {type: 'numeric', format:"0,0.00000000000", allowEmpty: false, strict: true},
            {type: 'numeric', format:"0,0", allowEmpty: false, strict: true},
            {type: 'text',allowEmpty: false, strict:true},
            {type: 'numeric', format:"0,0.00", strict: true},
            {type: 'text',readOnly: true, strict: true},
            {type: 'numeric', format:"0,0.00", strict: true}
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if(column === 1) {
                  let materialName = changes[i][3];
                  if (util.isNotBlank(materialName)){
                    axios.get('/api/global/cloud/kingdee/bdMaterial/findByName?name=' + materialName).then((response) => {
                      let material = response.data;
                      table.setDataAtCell(row, 0, material.fnumber);
                    });
                  }else {
                    table.setDataAtCell(row, 0, null);
                  }
                }
                if(column === 0){
                  let materialNumber = changes[i][3];
                  if (util.isNotBlank(materialNumber)){
                    axios.get('/api/global/cloud/sys/product/findByCode?code=' + materialNumber).then((response) => {
                      let product = response.data;
                      if (product){
                        table.setDataAtCell(row, 2, product.price1);
                      }else {
                        table.setDataAtCell(row, 2, '');
                      }
                      table.setDataAtCell(row, 6, typeList[1]);
                      table.setDataAtCell(row, 8, typeList[0]);
                    });
                  }else {
                    table.setDataAtCell(row, 2, null);
                    table.setDataAtCell(row, 6, null);
                    table.setDataAtCell(row, 8, null);
                  }
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString()
        },
        rules: {
          billDate: [{ required: true, message: '必填项'}],
          supplierNumber: [{ required: true, message: '必填项'}],
          stockNumber: [{ required: true, message: '必填项'}],
          departmentNumber: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/stkInStock/form').then((response)=>{
        this.settings.columns[1].source = response.data.materialNameList;
        let kingdeeName = response.data.kingdeeName;
        if(kingdeeName === "JXDJ"){
          this.settings.columns.push({type: "autocomplete", allowEmpty: true, strict: true, typeList:[], source: this.typeList});
          this.settings.columns[8].source = response.data.typeList;
        }else{
          this.settings.columns.push({type: "text", readOnly: true, strict: true});
        }
        typeList = response.data.typeList;
        console.log(typeList);
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
            axios.post('/api/global/cloud/input/stkInStock/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      remoteSupplier(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/kingdee/bdSupplier/findByNameLike',{params:{name:query}}).then((response)=>{
            this.supplierList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.supplierList = {};
        }
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
