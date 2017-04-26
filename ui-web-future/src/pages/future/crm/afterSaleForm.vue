<template>
  <div>
    <head-tab active="afterSaleForm"></head-tab>
    <div class="form input-form ">
      <el-row class="button">
        <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleForm.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleForm.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleForm.filter')" v-model="formVisible" size="tiny" class="search-form">
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
          <el-button type="primary" @click="getImeStr(formData.imeStr)">{{$t('afterSaleForm.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-alert :title="$t('afterSaleForm.alertSearchAfterSaleIme')" type="error"  :closable="false" v-if="formData.imeStr =='' "></el-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px">
        <el-form-item></el-form-item>
        <el-form-item :label="$t('afterSaleForm.toStoreDate')" prop="toStoreDate">
          <el-date-picker v-model="inputForm.toStoreDate" type="date" :placeholder="$t('afterSaleForm.selectDateRange')" ></el-date-picker>
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="24">
            <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
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
        formData:{
          imeStr:''
        },formLabel:{
          imeStr:{label: this.$t('afterSaleForm.ime')}
        },
        inputForm:{
          data:"",
          toStoreDate:util.currentDate()
        },
        formLabelWidth: '120px',
        formVisible: false,
        rules:{},
        autosize: { minRows: 5},
        table:null,
        settings: {
          data:{},
          colHeaders: [this.$t('afterSaleForm.badProductIme'),this.$t('afterSaleForm.badProductName'),this.$t('afterSaleForm.saleShopName'),this.$t('afterSaleForm.toAreaProductIme'),this.$t('afterSaleForm.areaDepot'),this.$t('afterSaleForm.packageStatus'),this.$t('afterSaleForm.toStoreType'),this.$t('afterSaleForm.memory'),this.$t('afterSaleForm.toStoreTypeRemarks')],
          rowHeaders:true,
          maxRows:1000,
          columns: [
            {data: "ime",strict:true, readOnly: true,width:120},
            {data: "product.name",strict:true, readOnly: true,width:120},
            {data: "retailShop.name",strict:true, readOnly: true,width:120},
            {type: "autocomplete",strict:true, allowEmpty:true,
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
            {type: "autocomplete",strict:true,
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
            {type: "autocomplete",strict:true,width:150},
            {type: "autocomplete",strict:true,width:150},
            {type: "autocomplete",strict:true,width:100},
            {data: "remarks",width:150}
          ]
        },
      }
    }, mounted () {
      axios.get("/api/crm/afterSale/getFormProperty").then((response)=>{
        this.settings.columns[5].source=response.data.packageStatus;
        this.settings.columns[6].source=response.data.toStoreType
        this.settings.columns[7].source=response.data.memory;
        this.settings.columns[3].goodStoreId=response.data.goodStoreId;
        this.inputForm.toStoreDate=response.data.toStoreDate;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.data =new Array();
            let list=this.table.getData();
            for(var item in list){
              if(!this.table.isEmptyRow(item)){
                this.inputForm.data.push(list[item]);
              }
            }
            this.inputForm.data = JSON.stringify(this.inputForm.data);
            this.inputForm.toStoreDate=util.formatLocalDate(this.inputForm.toStoreDate)
            axios.post('/api/crm/afterSale/save',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            if(this.isCreate){
              this.table.loadData(null);
              this.submitDisabled = false;
            } else {
               this.$router.push({name:'afterSaleList',query:util.getQuery("afterSaleList")})
            }
          });
          }else{
            this.submitDisabled = false;
      }
      })
      },getImeStr(imeStr){
        this.formVisible = false;
        axios.get("/api/crm/afterSale/formData",{params:{imeStr:imeStr}}).then((response)=>{
          this.settings.data=response.data.list;
          this.table.loadData(this.settings.data);
        })
      }
    },created(){
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
  .button{
    margin-bottom:20px;
  }
</style>
