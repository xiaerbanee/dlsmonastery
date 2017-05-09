<template>
  <div>
    <head-tab active="batchDeliveryForm"></head-tab>
    <div>
      <el-form :model="formData" method="get">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="formLabel.departmentNumber.label"  :label-width="formLabelWidth">
              <el-select v-model="formData.departmentNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteDepartment" :loading="remoteLoading">
                <el-option v-for="item in departmentList" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="formLabel.billDate.label" :label-width="formLabelWidth">
              <el-date-picker v-model="formData.billDate" type="date"></el-date-picker>
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
        departmentList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          product:'',
          colHeaders: ["编码", "货品", "仓库", "数量", "类型", "备注"],
          columns: [
            {type: "autocomplete", allowEmpty: false, strict: true, tempMaterialCodes:[],
              source:function (query, process) {
                var that = this;
                if(that.tempMaterialCodes.indexOf(query)>=0) {
                  process(that.tempMaterialCodes);
                } else {
                  var customerNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/input/bdMaterial/getNumberByNumberLike?number='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          var shopName = response.data[index];
                          customerNames.push(shopName);
                          if(that.tempMaterialCodes.indexOf(shopName)<0) {
                            that.tempMaterialCodes.push(shopName);
                          }
                        }
                      }
                      process(customerNames);
                    });
                  } else {
                    process(customerNames);
                  }
                }
              }
            },
            {type:"text", allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: true, strict: true,tempProductNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempProductNames.indexOf(query)>=0) {
                  process(that.tempProductNames);
                } else {
                  var productNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/input/bdStock/getNameByNameLike?name='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          var productName = response.data[index];
                          productNames.push(productName);
                          if(that.tempProductNames.indexOf(index)<0) {
                            that.tempProductNames.push(productName);
                          }
                        }
                      }
                      process(productNames);
                    });
                  } else {
                    process(productNames);
                  }
                }
              }
            },
            {type: 'numeric',allowEmpty: false,format:"0,0"},
            {type: "autocomplete", allowEmpty: false, strict: true,types:[], source: this.types},
            {type: "text", allowEmpty: true, strict: true }
          ],
          afterChange: function (changes, source) {

          }
        },
        formData:{
          billDate:'',
          departmentNumber:'',
          data:[],
        },formLabel:{
          billDate:{label:"日期"},
          departmentNumber:{label:"部门"},
        },
        pickerDateOption:'',
        submitDisabled:false,
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/batchDelivery/form').then((response)=>{
        this.settings.columns[4].source = response.data.types;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
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
        axios.post('/api/global/cloud/input/batchDelivery/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
        });
      },
      remoteDepartment(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/input/bdDepartment/getNameAndNumber',{params:{name:query}}).then((response)=>{
            this.departmentList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.departmentList = {};
        }
      }
    }
  }
</script>
