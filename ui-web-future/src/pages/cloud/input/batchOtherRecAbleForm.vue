<template>
  <div>
    <head-tab active="batchOtherRecAbleForm"></head-tab>
    <div>
      <el-form :model="formData" method="post">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="formLabel.billDate.label" :label-width="formLabelWidth">
              <date-picker v-model="formData.billDate"></date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          </el-col>
        </el-row>
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
  export default {
    data() {
      return {
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          colHeaders: ["客户", "费用承担部门", "对方科目", "金额", "备注", '员工', "其他类", "费用类", '对方关联客户'],
          columns:[
            {type: "autocomplete", allowEmpty: false, strict: true, customerName:[], source: this.customerName},
            {type: "autocomplete", allowEmpty: false, strict: true, departmentName:[], source: this.departmentName},
            {type: "autocomplete", allowEmpty: false, strict: true, accountName:[], source: this.accountName },
            {type: 'numeric',format:"0,0.00", allowEmpty: false, strict: true},
            {type: "text", allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: false, strict: true, empInfoName:[], source: this.empInfoName,},
            {type: "autocomplete", allowEmpty: false, strict: true,  otherType:[], source: this.otherType,},
            {type: "autocomplete", allowEmpty: false, strict: true, expenseType:[], source: this.expenseType,},
            {type: "autocomplete", allowEmpty: true, strict: true,  customerName:[], source: this.customerName},
          ],
          afterChange: function (changes, source) {

          }
        },
        formData:{
          billDate:new Date(),
          data:[],
        },formLabel:{
          billDate:{label:"日期"},
        },
        submitDisabled:false,
        formLabelWidth: '120px',
      };
    },
    mounted() {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings);
    },
    methods: {
      formSubmit(){
        this.formData.data = new Array();
        let list = this.table.getData();
        for(let item in list){
          if(!this.table.isEmptyRow(item)){
            this.formData.data.push(list[item]);
          }
        }
        this.formData.data = JSON.stringify(this.formData.data);
        axios.post('/api/global/cloud/input/batchOtherRecAble/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
        }).catch(function () {
          this.submitDisabled = false;
        });
      },
    }
  }
</script>
