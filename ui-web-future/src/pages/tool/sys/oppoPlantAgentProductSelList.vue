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
      <search-dialog :title="$t('过滤')" v-model="formVisible"  size="small" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="inputForm"  ref="inputForm" >
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="$t('物料描述')" :label-width="formLabelWidth">
                <el-input v-model="inputForm.name" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('物料编号')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="inputForm.code" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('型号')" :label-width="formLabelWidth">
                <el-input v-model="inputForm.code" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('确定')}}</el-button>
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
  import ElCol from "element-ui/packages/col/src/col";
  var table = null;
  export default {
    components: {ElCol},
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
              data:'id',
              strict: true,
              readOnly: true,
              width: 200
            }, {
              data:'',
              strict: true,
              readOnly: true,
              width: 200
            }, {
              data:'',
              strict: true,
              readOnly: true,
              width: 200
            }, {
              data:'',
              strict: true,
              readOnly: true,
              width: 200
            }, {
              data:'',
              strict: true,
              readOnly: true,
              width: 100
            }, {
              strict: true,
              readOnly: true,
              width: 100
            }, {
              data:'',
              type: "autocomplete",
              allowEmpty: true,
              strict: true,
              width: 100
            }, {
              data:'productId',
              type: "autocomplete",
              allowEmpty: true,
              strict: true,
              source:function () {

              },
              width: 100
            }, {
              data:'lxProductId',
              readOnly: true,
              strict: true,
              source:function () {

              },
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
                createForm.id = row[0];
                createForm.productId = row[6];
                createForm.lxProductId = row[7];
                tableData.push(createForm);
              }
            }
            this.inputForm.dataList = tableData;

            axios.post('', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
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
        util.setQuery("oppoPlantAgentProductSelList",submitData);
        axios.get('',{params:submitData}).then((response) => {
          this.settings.data  = null;
          table.loadData(this.settings.data);
        });
      },synData(){

      },initPage(){
        axios.get('').then((response)=>{
          this.inputForm = response.data;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    mounted() {
      this.initPage();
    },
  }
</script>
