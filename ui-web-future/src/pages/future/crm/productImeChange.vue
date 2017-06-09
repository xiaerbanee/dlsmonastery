<template>
  <div>
    <head-tab active="productImeChange"></head-tab>
    <search-dialog :title="$t('productImeChange.filter')" v-model="searchFormVisible" size="small" class="search-form" zindex="1500">
      <el-form >
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('productImeChange.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr"  ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search" icon="search">{{$t('productImeChange.sure')}}</el-button>
      </div>
    </search-dialog>
    <div>
      <el-form  :model="batchChangeForm"  ref="inputForm"  :rules="rules">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
            <el-button type="primary" @click="searchFormVisible = true"  >过滤</el-button>
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
  import SearchDialog from "../../../components/common/search-dialog";

  export default {
    components: {SearchDialog},
    data(){
      return this.getData()
    },

    methods: {
      getData() {
        return {
          isInit: false,
          table: null,
          searchFormVisible:false,
          imeStr:'',
          batchChangeForm: {},
          settings: {
            rowHeaders: true,
            minSpareRows: 100,
            startRows: 100,
            startCols: 4,
            colHeaders: [ '串码', '仓库', '产品型号',  '调整后型号'],
            columns: [{
              data: "ime",
              strict: true,
              allowEmpty: true,
              width: 200
            }, {
              data: "depotName",
              readOnly: true,
              width: 300
            }, {
              data: "productName",
              readOnly: true,
              width: 300
            }, {
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width: 300
            }],
            contextMenu: ['row_above', 'row_below', 'remove_row'],

          }, rules: {},
          submitDisabled: false,
          formLabelWidth: '120px',
          remoteLoading: false,

        };
      },
      search() {
        this.searchFormVisible = false;

        console.log("123231")
        axios.get('/api/ws/future/crm/productIme/findDtoListByImes?'+qs.stringify(this.imeStr)).then((response) => {
          let ht = this.$refs.handsontable.handsontable('getInstance');
          ht.loadData(response.data);
        })
      },
      formSubmit(){

        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            let tableData = [];

            let list = this.table.getData();
            for (let item in list) {
              if (!this.table.isEmptyRow(item)) {
                let row = list[item];
                let changeForm = {};
                changeForm.ime = row[0];
                changeForm.productName = row[3];
                tableData.push(changeForm);
              }
            }
            this.batchChangeForm.productImeChangeFormList = tableData;

            axios.post('/api/ws/future/crm/productIme/batchChange', qs.stringify(this.batchChangeForm, {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());

            }).catch( () => {
              this.submitDisabled = false;
            });

          }
        })
      },search(){

      }
    },
    activated() {


      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());

        axios.get('/api/ws/future/crm/productIme/getBatchChangeForm').then((response)=>{
          this.batchChangeForm = response.data;
          this.settings.columns[3].source = response.data.extra.productNames;
          this.table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
      this.isInit = true;


    },
  }
</script>
