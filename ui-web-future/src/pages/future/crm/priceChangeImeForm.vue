<template>
  <div>
    <head-tab active="priceChangeImeForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
        <el-form-item :label="$t('priceChangeImeForm.priceChangeId')" prop="priceChangeId">
          <el-select v-model="inputForm.priceChangeId" filterable   :placeholder="$t('priceChangeImeForm.selectPriceChangeId')" >
            <el-option v-for="item in formProperty.priceChangeDtos" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeImeForm.save')}}</el-button>
        </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <div ref="handsontable" style="width:800px;height:600px;overflow:hidden;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
    export default{
      data(){
          return{
            submitDisabled:false,
            table:null,
            settings: {
                colHeaders: [this.$t('priceChangeImeForm.shopName'),this.$t('priceChangeImeForm.ime'),this.$t('priceChangeImeForm.remarks')],
                rowHeaders:true,
                minSpareRows:500,
                startRows: 500,
                maxRows:1000,
                columns: [{
                  type: "autocomplete",
                  allowEmpty: false,
                  strict: true,
                  tempShopNames:[],
                  source:function (query, process) {
                      var that = this;
                      if(that.tempShopNames.indexOf(query)>=0) {
                          process(that.tempShopNames);
                      } else {
                        var shopNames = new Array();
                        if(query.length>=2) {
                          axios.get('/api/ws/future/basic/depot/shop?name='+query).then((response)=>{
                              if(response.data.length>0) {
                                for(var index in response.data) {
                                  var shopName = response.data[index].name;
                                  shopNames.push(shopName);
                                  if(that.tempShopNames.indexOf(shopName)<0) {
                                    that.tempShopNames.push(shopName);
                                  }
                                }
                              }
                              process(shopNames);
                          });
                        } else {
                          process(shopNames);
                        }
                      }
                  },
                  width:300
                },{
                  strict:true,
                  width:200
                },{
                  width:200
              }]
            },
            inputForm:{
              imeUploadList:[[]],
              priceChangeId:''
            },
            formProperty:{},
            remoteLoading:false,
            rules: {
              name: [{ required: true, message: '名称不能为空'}]
            }
          }
      },
      mounted () {
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
                this.inputForm.imeUploadList=new Array();
                let list=this.table.getData()
                console.log(list);
                for(var item in list){
                  if(!this.table.isEmptyRow(item)){
                    this.inputForm.imeUploadList.push(list[item]);
                  }
                }
                /*this.inputForm.imeUploadList = JSON.stringify(this.inputForm.imeUploadList);*/
                axios.post('/api/ws/future/crm/priceChangeIme/save',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
                  this.$message(response.data.message);
                  form.resetFields();
                  this.submitDisabled = false;
                }).catch(function () {
                  this.submitDisabled = false;
                });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/ws/future/crm/priceChangeIme/getForm').then((response)=>{
            this.formProperty=response.data;
          });
        }
      },created() {
        this.initPage();
      },activated () {
        if(!this.$route.query.headClick) {
          this.initPage();
        }
      }
    }
</script>
<style>
@import "~handsontable/dist/handsontable.full.css";
</style>
