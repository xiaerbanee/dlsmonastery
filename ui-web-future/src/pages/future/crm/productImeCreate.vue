<template>
  <div>
    <head-tab active="productImeCreate"></head-tab>
    <div>
      <el-form  :model="inputForm" class="form input-form" ref="inputForm"  :rules="rules"  label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
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
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table = null;
  export default {
    data(){
      return this.getData()
    },

    methods: {
      getData() {
        return {

          inputForm: {
              extra:{},
          },
          settings: {
            rowHeaders: true,
            autoColumnSize: true,
            stretchH: 'all',
            height: 650,
            minSpareRows: 500,
            startRows: 500,
            maxRows: 5000,
            startCols: 5,
            colHeaders: ['货品', '仓库', '串码', '串码2', '箱号', 'meid码', '订单号', '工厂录入时间', '备注', '物料编码'],
            columns: [{
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width: 200
            }, {
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width: 200
            }, {
              strict: true,
              width: 200
            }, {
              strict: true,
              width: 200
            }, {
              strict: true,
              width: 100
            }, {
              strict: true,
              width: 100
            }, {
              strict: true,
              width: 100
            }, {
              type: "date",
              dateFormat: "YYYY-MM-DD",
              strict: false,
              width: 100
            }, {
              strict: true,
              width: 100
            }, {
              strict: true,
              width: 100
            }],
            contextMenu: ['row_above', 'row_below', 'remove_row'],
          }, rules: {},
          submitDisabled: false,
        };
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let tableData = [];
            let list = table.getData();
            for (let item in list) {
              if (!table.isEmptyRow(item)) {
                let row = list[item];
                let createForm = {};
                createForm.productName = row[0];
                createForm.storeName = row[1];
                createForm.ime = row[2];
                createForm.ime2 = row[3];
                createForm.boxIme = row[4];
                createForm.meid = row[5];
                createForm.billId = row[6];
                createForm.createdTimeStr = row[7];
                createForm.remarks = row[8];
                createForm.itemNumber = row[9];
                tableData.push(createForm);
              }
            }
            this.inputForm.productImeCreateFormList = tableData;

            axios.post('/api/ws/future/crm/productIme/batchCreate', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/crm/productIme/getBatchCreateForm').then((response)=>{
          this.inputForm = response.data;
          this.settings.columns[0].source = response.data.extra.productNameList;
          this.settings.columns[1].source = response.data.extra.storeNameList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    created() {
      this.initPage();
    },
  }
</script>
