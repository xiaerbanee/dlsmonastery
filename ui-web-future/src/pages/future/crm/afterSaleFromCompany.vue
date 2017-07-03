<template>
    <div>
      <head-tab :active="$t('afterSaleFromCompany.afterSaleFromCompany') "></head-tab>
      <el-row>
        <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleFromCompany.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleFromCompany.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <el-dialog :title="$t('afterSaleFromCompany.filter')" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-form-item :label="$t('afterSaleFromCompany.badProductName')" >
            <product-type-select v-model="formData.productTypeId" ></product-type-select>
          </el-form-item>
          <el-form-item :label="$t('afterSaleFromCompany.badProductIme')" >
            <el-input v-model="formData.badProductIme" auto-complete="off" :placeholder="$t('afterSaleFromCompany.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('afterSaleFromCompany.toStoreDate')">
            <date-range-picker v-model="formData.toStoreDate"></date-range-picker>
          </el-form-item>
          <el-form-item :label="$t('afterSaleFromCompany.toCompanyDate')">
          <date-range-picker v-model="formData.toCompanyDate"></date-range-picker>
        </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleFromCompany.sure')}}</el-button>
        </div>
      </el-dialog>
      <div>
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-row>
            <el-col :span="12">
              <el-form-item>
              </el-form-item>
              <el-form-item :label="$t('afterSaleFromCompany.fromCompanyDate')">
                <el-date-picker v-model="inputForm.fromCompanyDate" type="date" :placeholder="$t('afterSaleFromCompany.selectDate')"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
</template>
<style>

</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  import ProductTypeSelect from "components/future/product-type-select"
  export default{
       components:{
         ProductTypeSelect
       },
        data(){
          return{
            searchText:"",
            formData:{
              extra:{}
            },
            inputForm:{
              fromCompanyDate:util.currentDate(),
              data:""
            },
            rules:{},
            formLabelWidth: '120px',
            formVisible: false,
            remoteLoading:false,

            table:null,
            settings: {
              colHeaders: [this.$t('afterSaleFromCompany.badProductName'),this.$t('afterSaleFromCompany.toAreaProductType'),this.$t('afterSaleFromCompany.toCompanyDate'),this.$t('afterSaleFromCompany.badProductIme'),this.$t('afterSaleFromCompany.areaDepot'),this.$t('afterSaleFromCompany.packageStatus'),this.$t('afterSaleFromCompany.toStoreType'),this.$t('afterSaleFromCompany.memory'),this.$t('afterSaleFromCompany.remarks')],
              rowHeaders:true,
              maxRows:1000,
              columns: [{
                data:"badProductIme.product.name",
                strict:true,
                width:150
              },{
                  data:"toAreaProductIme.product.name",
                type: "autocomplete",
                allowEmpty: false,
                strict: true,
                tempProductNames:[],
                source:function (query, process) {
                  var that = this;
                  if(that.tempProductNames.indexOf(query)>=0) {
                    process(that.tempProductNames);
                  } else {
                    var productNames = new Array();
                    if(query.length>=2) {
                      axios.get('/api/crm/product/search?name='+query).then((response)=>{
                        if(response.data.length>0) {
                          for(var index in response.data) {
                            var productName = response.data[index].name;
                            productNames.push(productName);
                            if(that.tempProductNames.indexOf(productName)<0) {
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
                },
                width:200
              },{
                data:"toCompanyDate",
                type: 'date',
                dateFormat: 'YYYY-DD-MM',
                width:150
              },{
                data:"badProductIme.ime",
                width:150
              },{
                data:"areaDepot.name",
                width:150
              },{
                data:"packageStatus",
                width:150
              },{
                data:"toStoreType",
                width:150
              },{
                width:150
              },{
                width:150
              }]
            },
          }

        },
      mounted () {
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      },
      methods:{
        setSearchText() {
          this.$nextTick(function () {
            this.searchText = util.getSearchText(this.$refs.searchDialog);
          })
        },
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
              this.inputForm.fromCompanyDate=util.formatLocalDate(this.inputForm.fromCompanyDate)
              this.inputForm.data = JSON.stringify(this.inputForm.data);
              axios.post('/api/crm/afterSale/fromCompany',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },search() {
          this.formVisible = false;
          var submitData = util.deleteExtra(this.formData);
          axios.get('/api/crm/afterSale/getFromCompanyData',{params:submitData}).then((response)=>{
              this.settings.data=response.data;
              this.table.loadData(this.settings.data);
          })
        }
      }
    }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
