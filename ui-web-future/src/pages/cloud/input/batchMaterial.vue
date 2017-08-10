<template>
  <div>
    <head-tab active="batchMaterial"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
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
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  export default {
    data:function () {
      return this.getData();
    },
    methods: {
      getData() {
        return {
          departmentList:{},
          settings: {
            rowHeaders:true,
            autoColumnSize:true,
            stretchH: 'all',
            height: 650,
            minSpareRows: 1,
            colHeaders:["编码", "名称", "一级价","广告让利(返利1.5%输入1.5)","物料分组", "存货类别"],
            columns: [
              {type: 'text',allowEmpty: false,strict: true},
              {type: "text", allowEmpty: false, strict: true },
              {type: 'numeric',allowEmpty: false,format:"0,0.00"},
              {type: "numeric", allowEmpty: false,format:"0,0.00"},
              {type: "autocomplete", allowEmpty: false, strict: true, materialGroupNameList:[], source: this.materialGroupNameList},
              {type: "autocomplete", allowEmpty: false, strict: true, materialCategoryNameList:[], source: this.materialCategoryNameList}
            ],
            contextMenu: true,
          },
          formData:{
            json:[],
          },rules: {},
          submitDisabled:false,
          remoteLoading:false
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
            axios.post('/api/global/cloud/kingdee/bdMaterial/batchSave', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
                this.initPage();
                form.resetFields();
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
      axios.get('/api/global/cloud/kingdee/bdMaterial/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[4].source = extra.materialGroupNameList;
        this.settings.columns[5].source = extra.materialCategoryNameList;
        this.initPage();
      });
    },
  }
</script>
