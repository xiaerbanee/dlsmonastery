<template>
  <div>
    <head-tab active="afterSaleAreaEdit"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit" icon="check" v-permit="'crm:afterSale:areaEdit:edit'">{{$t('afterSaleAreaEdit.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:afterSale:view'">{{$t('afterSaleAreaEdit.filter')}}</el-button>
      <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
    </el-row>
    <el-dialog :title="$t('afterSaleAreaEdit.filter')" v-model="formVisible"  size="tiny" class="search-form">
      <el-form :model="formData">
        <el-form-item :label="formLabel.productTypeId.label" :label-width="formLabelWidth">
          <el-select v-model="formData.productTypeId"  clearable filterable remote :placeholder="$t('afterSaleAreaEdit.inputWord')" :remote-method="remoteProductType" :loading="remoteLoading">
            <el-option v-for="productType in productTypes" :key="productType.id"  :label="productType.name" :value="productType.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="formLabel.badProductIme.label" :label-width="formLabelWidth">
          <el-input v-model="formData.badProductIme" auto-complete="off" :placeholder="$t('afterSaleAreaEdit.likeSearch')"></el-input>
        </el-form-item>
        <el-form-item :label="formLabel.toStoreDateBTW.label" :label-width="formLabelWidth">
          <el-date-picker v-model="formData.toStoreDate" type="daterange" align="right" :placeholder="$t('afterSaleAreaEdit.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
        </el-form-item>
        <el-form-item :label="formLabel.toCompanyDateBTW.label" :label-width="formLabelWidth">
          <el-date-picker v-model="formData.toCompanyDate" type="daterange" align="right" :placeholder="$t('afterSaleAreaEdit.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('afterSaleAreaEdit.sure')}}</el-button>
      </div>
    </el-dialog>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
            <el-form-item>
            </el-form-item>
            <el-form-item :label="$t('afterSaleAreaEdit.fromCompanyDate')">
              <el-date-picker v-model="inputForm.fromCompanyDate" type="date" :placeholder="$t('afterSaleAreaEdit.selectDate')"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div ref="handsontable" style="width:800px;height:600px;overflow:hidden;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>

</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'

  export default{
    data(){
      return{
        formData:{
          productTypeId:'',
          badProductIme:'',
          toStoreDate:'',
          toStoreDateBTW:'',
          toCompanyDate:'',
          toCompanyDateBTW:''
        },formLabel:{
          productTypeId:{label:this.$t('afterSaleAreaEdit.badProductName'),value:''},
          badProductIme:{label:this.$t('afterSaleAreaEdit.badProductIme')},
          toStoreDateBTW:{label:this.$t('afterSaleAreaEdit.toStoreDate')},
          toCompanyDateBTW:{label:this.$t('afterSaleAreaEdit.toCompanyDate')}
        },
        inputForm:{
          fromCompanyDate:util.currentDate()
        },
        rules:{},
        formProperty:{},
        productTypes:[],
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false,
        pickerDateOption:util.pickerDateOption,

        table:null,
        settings: {
          colHeaders: [this.$t('afterSaleAreaEdit.badProductName'),this.$t('afterSaleAreaEdit.badProductIme'),this.$t('afterSaleAreaEdit.toAreaProductType'),this.$t('afterSaleAreaEdit.toAreaProductIme'),this.$t('afterSaleAreaEdit.badProductShop'),this.$t('afterSaleAreaEdit.badProductType'),this.$t('afterSaleAreaEdit.adPricesystem'),this.$t('afterSaleAreaEdit.areaDepot'),this.$t('afterSaleAreaEdit.packageStatus'),this.$t('afterSaleAreaEdit.toStoreType'),this.$t('afterSaleAreaEdit.memory'),this.$t('afterSaleAreaEdit.remarks')],
          rowHeaders:true,
          minSpareRows:100,
          startRows:300,
          maxRows:1000,
          columns: [{
            strict:true,
            width:100
          },{
            width:100
          },{
            width:100
          },{
            width:100
          },{
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
              },
              width:200
          },{
            width:100
          },{
            width:100
          },{
            width:100
          },{
            width:100
          },{
            width:100
          },{
            width:100
          },{
            width:100
          }]
        },
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
            axios.post('/api/crm/afterSale/save',qs.stringify(this.inputForm)).then((response)=> {
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
      },search() {
        this.formVisible = false;
        this.formData.toStoreDateBTW=util.formatDateRange(this.formData.toStoreDate);
        this.formData.toCompanyDateBTW=util.formatDateRange(this.formData.toCompanyDate);
        this.formLabel.productTypeId.value=util.getLabel(this.productTypes,this.formData.productTypeId);
      }, remoteProductType(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/productType/search',{params:{name:query}}).then((response)=>{
            this.productTypes = response.data;
            this.remoteLoading = false;
          })
        }
      }
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
