<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<template>
  <div>
    <head-tab active="batchMaterialQuery"></head-tab>
    <div>
      <el-row class="button">
        <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
      </el-row>
      <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  export default {
    data() {
      return {
        submitDisabled:false,
        table:null,
        settings: {
          height: 650,
          rowHeaders: true,
          colHeaders: ["编码", "名称", "一级价","广告让利(返利1.5%输入1.5)","物料分组", "存货类别"],
          minSpareRows: 1,
          columns: [
            {type: 'text',allowEmpty: false,strict: true},
            {type: "text", allowEmpty: false, strict: true },
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false,format:"0,0.00"},
            {type: "autocomplete", allowEmpty: false, strict: true, materialGroupList:[],source: this.materialGroupList},
            {type: "autocomplete", allowEmpty: false, strict: true, materialCategoryList:[],source:this.materialCategoryList},
          ],
          stretchH: 'all',
          contextMenu: ['row_above', 'row_below', 'remove_row'],
        },
        formData:{
          data:[],
        },formLabel:{
        },
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    mounted () {
      axios.get("/api/global/cloud/input/batchMaterial/form").then((response)=>{
        this.settings.columns[4].source = response.data.materialGroupList;
        this.settings.columns[5].source = response.data.materialCategoryList;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods: {
      formSubmit(){
        this.formData.data =new Array();
        let list = this.table.getData();
        for(let item in list){
          if(!this.table.isEmptyRow(item)){
            this.formData.data.push(list[item]);
          }
        }
        this.formData.data = JSON.stringify(this.formData.data);
        axios.post('/api/global/cloud/input/batchMaterial/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
        });
      }
    },created () {

    }
  };
</script>
