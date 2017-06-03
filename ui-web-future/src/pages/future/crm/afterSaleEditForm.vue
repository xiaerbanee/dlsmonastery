<template>
  <div>
    <head-tab active="afterSaleEditForm"></head-tab>
    <div>
      <el-row class="button">
        <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleEditForm.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleEditForm.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleEditForm.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.imeStr.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.imeStr" :autosize="autosize" auto-complete="off" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="getImeStr(formData.imeStr)">{{$t('afterSaleEditForm.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-alert :title="$t('afterSaleEditForm.alertSearchAfterSaleIme')" type="error"  :closable="false" v-if="formData.imeStr =='' "></el-alert>
      <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
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
        formData:{
          imeStr:''
        },formLabel:{
          imeStr:{label:this.$t('afterSaleEditForm.ime')}
        },
        inputForm:{
          data:"",
        },
        formLabelWidth: '120px',
        formVisible: false,
        rules:{},
        autosize: { minRows: 5},
        table:null,
        settings: {
          data:{},
          colHeaders: [this.$t('afterSaleEditForm.badProductIme'),this.$t('afterSaleEditForm.badProductName'),this.$t('afterSaleEditForm.saleShopName'),this.$t('afterSaleEditForm.toAreaProductIme'),this.$t('afterSaleEditForm.areaDepot'),this.$t('afterSaleEditForm.packageStatus'),this.$t('afterSaleEditForm.toStoreType'),this.$t('afterSaleEditForm.memory'),this.$t('afterSaleEditForm.fromCompanyProduct'),this.$t('afterSaleEditForm.toStoreTypeRemarks')],
          rowHeaders:true,
          maxRows:1000,
          columns: [
            {data: "badProductIme.ime",strict:true, readOnly: true,width:120},
            {data: "badProductIme.product.name",strict:true, readOnly: true,width:120},
            {data: "badProductIme.retailShop.name",strict:true, readOnly: true,width:120},
            {data: "toAreaProductIme.ime",type: "autocomplete",strict:true, allowEmpty:true,
              goodStoreId:[],
              imes:[],
              source:function (query, process) {
                var that = this;
                if(that.imes.indexOf(query)>=0) {
                  process(that.imes);
                } else {
                  var imeList = new Array();
                  if(query.length>=2) {
                    axios.get('/api/crm/productIme/searchByStore?depotId='+that.goodStoreId+'&ime='+query).then((response)=>{
                      if(response.data.length>0) {
                      for(var index in response.data) {
                        var ime = response.data[index].ime;
                        imeList.push(ime);
                        if(that.imes.indexOf(ime)<0) {
                          that.imes.push(ime);
                        }
                      }
                    }
                    process(imeList);
                  });
                  } else {
                    process(imeList);
                  }
                }
              } ,width:150},
            {data: "areaDepot.name",type: "autocomplete",strict:true,
              tempShopNames:[],
              source:function (query, process) {
                var that = this;
                if(that.tempShopNames.indexOf(query)>=0) {
                  process(that.tempShopNames);
                } else {
                  var shopNames = new Array();
                  if(query.length>=2) {
                    axios.get('/api/crm/depot/shop?name='+query).then((response)=>{
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
              },width:150
            },
            {data: "packageStatus",type: "autocomplete",strict:true,width:150},
            {data: "toStoreType",type: "autocomplete",strict:true,width:150},
            {data: "memory",type: "autocomplete",strict:true,width:100},
            {data: "fromCompanyProduct.name",type: "autocomplete",strict:true,
            productNames:[],
            source:function (query, process) {
              var that = this;
              if(that.productNames.indexOf(query)>=0) {
                process(that.productNames);
              } else {
                var names = new Array();
                if(query.length>=2) {
                  axios.get('/api/crm/product/search?name='+query).then((response)=>{
                    if(response.data.length>0) {
                    for(var index in response.data) {
                      var name = response.data[index].name;
                      names.push(name);
                      if(that.productNames.indexOf(name)<0) {
                        that.productNames.push(name);
                      }
                    }
                  }
                  process(names);
                });
                } else {
                  process(names);
                }
              }
            },width:150},
            {data: "remarks",strict:true,width:100},
          ]
        },
      }
    }, mounted () {
      axios.get("/api/crm/afterSale/getForm").then((response)=>{
        this.settings.columns[5].source=response.data.packageStatus;
        this.settings.columns[6].source=response.data.toStoreType;
        this.settings.columns[7].source=response.data.memory;
        this.settings.columns[3].goodStoreId=response.data.goodStoreId;
        this.inputForm.toStoreDate=response.data.toStoreDate;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods:{
      formSubmit(){
          this.inputForm.data =new Array();
          let list=this.table.getData();
          for(var item in list){
            if(!this.table.isEmptyRow(item)){
              this.inputForm.data.push(list[item]);
            }
          }
          this.inputForm.data = JSON.stringify(this.inputForm.data);
          axios.post('/api/crm/afterSale/update',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
            this.$message(response.data.message);
          if(this.isCreate){
            this.table.loadData(null);
            this.submitDisabled = false;
          } else {
             this.$router.push({name:'afterSaleList',query:util.getQuery("afterSaleList")})
          }
        }).catch(function () {
          this.submitDisabled = false;
        });
      },getImeStr(imeStr){
        this.formVisible = false;
        axios.get("/api/crm/afterSale/editFormData",{params:{imeStr:imeStr}}).then((response)=>{
        console.log(response.data);
          this.settings.data=response.data.list;
          this.table.loadData(this.settings.data);
        })
      },initPage(){

      }
    },created(){
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
  .button{
    margin-bottom:20px;
  }
</style>
