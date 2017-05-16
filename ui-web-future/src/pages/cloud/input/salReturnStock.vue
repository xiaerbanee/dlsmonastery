<template>
  <div>
    <head-tab active="salReturnStock"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="formLabel.storeNumber.label"  :label-width="formLabelWidth" prop="storeNumber">
              <el-select v-model="formData.storeNumber" filterable remote placeholder="请输入关键词" :remote-method="remoteStore" :loading="remoteLoading">
                <el-option v-for="item in storeList" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="formLabel.billDate.label" :label-width="formLabelWidth" prop="billDate">
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
  var table = null;
  export default {
    data() {
      return {
        table:null,
        storeList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          colHeaders: ["编码","门店","货品","价格","数量","类型","备注"],
          columns: [
            {type:"text", allowEmpty: false, strict: true},
            {type: "autocomplete", allowEmpty: false, strict: true, tempCustomerNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempCustomerNames.indexOf(query)>=0) {
                  process(that.tempCustomerNames);
                } else {
                  var customerNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/kingdee/bdCustomer/getNameByNameLike?name='+query).then((response)=>{
                      if(response.data.length>0) {
                        for(let index in response.data) {
                          var shopName = response.data[index];
                          customerNames.push(shopName);
                          if(that.tempCustomerNames.indexOf(shopName)<0) {
                            that.tempCustomerNames.push(shopName);
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
            {type: "autocomplete", allowEmpty: true, strict: true,tempProductNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempProductNames.indexOf(query)>=0) {
                  process(that.tempProductNames);
                } else {
                  var productNames = new Array();
                  if(query.length>0) {
                    axios.get('/api/global/cloud/kingdee/bdMaterial/getNameByNameLike?name='+query).then((response)=>{
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
            {type: 'numeric',allowEmpty: false,format:"0,0.00"},
            {type: "numeric", allowEmpty: false},
            {type: "autocomplete", allowEmpty: false, strict: true,billType:[], source: this.billType},
            {type: "text", allowEmpty: true, strict: true }
          ],
          afterChange: function (changes, source) {
            var that = this;
            if (source === 'edit') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1]==2;
                if(column){
                  let name = changes[i][3];
                  axios.get('/api/global/cloud/kingdee/bdMaterial/getByName?name='+ name).then((response) =>{
                    let  material = response.data;
                    table.setDataAtCell(row,0,material.fnumber);
                  });
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          storeNumber:'',
          json:[],
        },formLabel:{
          billDate:{label:"日期"},
          storeNumber:{label:"仓库"},
        },rules: {
          storeNumber: [{ required: true, message: '请选择仓库'}],
          billDate: [{ required: true, message: '请选择时间'}],
        },
        submitDisabled:false,
        formLabelWidth: '120px',
        remoteLoading:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/salReturnStock/form').then((response)=>{
        this.settings.columns[5].source = response.data.returnStockBillTypeEnums;
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
            axios.post('/api/global/cloud/input/salReturnStock/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      remoteStore(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/kingdee/bdStock/getByNameLike',{params:{name:query}}).then((response)=>{
            this.storeList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.storeList = {};
        }
      },
    }
  }
</script>
