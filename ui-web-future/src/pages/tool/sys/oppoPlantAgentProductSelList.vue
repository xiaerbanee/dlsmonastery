<template>
  <div>
    <head-tab active="oppoPlantAgentProductSelList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
        <el-button type="primary" @click="synData" icon="plus">同步</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('oppoPlantAgentProductSelList.filter')" v-model="formVisible"  size="small" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="inputForm"  ref="inputForm" >
          <el-row :gutter="8">
            <el-col :span="24">
              <el-form-item :label="$t('productAdEdit.code')" :label-width="formLabelWidth">
                <el-input v-model="inputForm.name" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.code')" :label-width="formLabelWidth">
                <el-input v-model="inputForm.code" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productAdEdit.allowBill')" :label-width="formLabelWidth">
                <el-input v-model="inputForm.code" auto-complete="off" :placeholder="$t('productAdEdit.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productAdEdit.sure')}}</el-button>
        </div>
      </search-dialog>
      <div ref="handsontable" style="height:1200px;overflow:hidden;margin-top:20px"></div>
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
    },mounted() {
      //Handsontable初始化操作
      this.search();
      table = new Handsontable(this.$refs["handsontable"], this.settings);

    },methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },getData() {
        return {
          inputForm: {
            extra:{},
          },
          searchText:'',
          settings: {
            rowHeaders: true,
            autoColumnSize: true,
            stretchH: 'all',
            height: 650,
            minSpareRows: 500,
            startRows: 500,
            maxRows: 5000,
            startCols: 5,
            colHeaders: ['ID', '颜色Id', '颜色', '类型', '物料描述', '物料编码', 'TD对应货品', 'LX对应货品', '统计型号'],
            columns: [{
              strict: true,
              width: 200
            }, {
              strict: true,
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
            }],
            contextMenu: ['row_above', 'row_below', 'remove_row'],
          }, rules: {},
          submitDisabled: false,
          formLabelWidth: '120px',
          formVisible: false
        };
      },formSubmit(){
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
            if (tableData.length == 0) {
              this.$message.error("请录入需要添加的串码信息");
              this.submitDisabled = false;
              return;
            }

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
      },search() {
        this.formVisible = false;
        this.setSearchText();
        let submitData = util.deleteExtra(this.inputForm);
        util.setQuery("productList",submitData);
        axios.get('',{params:submitData}).then((response) => {
          this.settings.data  = null;
          table.loadData(this.settings.data);
        });
      },synData(){

      },initPage(){
        axios.get('/api/ws/future/crm/productIme/getBatchCreateForm').then((response)=>{
          this.inputForm = response.data;
          this.settings.columns[0].source = response.data.extra.productNameList;
          this.settings.columns[1].source = response.data.extra.storeNameList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    mounted() {
      this.initPage();
    },
  }
</script>
