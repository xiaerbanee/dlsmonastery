<template>
    <div>
      <head-tab active="afterSaleFromCompany"></head-tab>
      <el-row>
        <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleFromCompany.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleFromCompany.filter')}}</el-button>
      </el-row>
      <search-dialog :title="$t('afterSaleFromCompany.filter')" v-model="formVisible"  size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-form-item label="型号" :label-width="formLabelWidth">
            <product-type-select v-model="formData.productTypeId"></product-type-select>
          </el-form-item>
          <el-form-item label="串码" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="formData.imeStr" auto-complete="off" ></el-input>
          </el-form-item>
          <el-form-item label="返厂时间" :label-width="formLabelWidth">
          <date-range-picker v-model="formData.inputDateRanger" />
        </el-form-item>
          <el-form-item label="类型" :label-width="formLabelWidth">
            <el-select v-model="formData.type" placeholder="请选择">
              <el-option v-for="item in inputTypeList" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleFromCompany.sure')}}</el-button>
        </div>
      </search-dialog>
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
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  import productTypeSelect from 'components/future/product-type-select';
  import dateRangePicker from 'components/common/date-range-picker';
  import SearchDialog from "../../../components/common/search-dialog.vue";
  var table = null;
  export default{
    components:{
      SearchDialog,
      productTypeSelect,dateRangePicker},
    data(){
      return{
        formData:{
          type:"售后机",
        },
        inputForm:{
          fromCompanyDate:util.currentDate(),
          data:""
        },
        rules:{},
        inputTypeList:['售后机','窜货机'],
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false,
        settings: {
          colHeaders: [this.$t('afterSaleFromCompany.badProductName'),this.$t('afterSaleFromCompany.toAreaProductType'),"调换机串码","返还价格",this.$t('afterSaleFromCompany.toCompanyDate'),this.$t('afterSaleFromCompany.badProductIme'),this.$t('afterSaleFromCompany.areaDepot'),this.$t('afterSaleFromCompany.packageStatus'),this.$t('afterSaleFromCompany.toStoreType'),this.$t('afterSaleFromCompany.memory'),this.$t('afterSaleFromCompany.remarks')],
          rowHeaders:true,
          maxRows:1000,
          columns: [{
            data:"badProductName",
            strict:true,
            width:150
          },{
            data:"replaceProductName",
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
                  axios.get('/api/ws/future/basic/product/search?name='+query).then((response)=>{
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
            data:"replaceProductIme",
            strict:true,
            width:150
          },{
            data:"replaceAmount",
            width:100
          },{
            data:"inputDate",
            type: 'date',
            dateFormat: 'YYYY-DD-MM',
            width:150
          },{
            data:"badProductIme",
            width:150
          },{
            data:"fromDepotName",
            width:150
          },{
            data:"packageStatus",
            width:150
          },{
            data:"badType",
            width:150
          },{
            data:"memory",
            width:150
          },{
            data:"remarks",
            width:150
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
          this.inputForm.data =new Array();
          let list=this.table.getData();
          for(var item in list){
            if(!this.table.isEmptyRow(item)){
              this.inputForm.data.push(list[item]);
            }
          }
          this.inputForm.fromCompanyDate=util.formatLocalDate(this.inputForm.fromCompanyDate)
          this.inputForm.data = JSON.stringify(this.inputForm.data);
          axios.post('/api/ws/future/crm/afterSale/fromCompany',qs.stringify(this.inputForm)).then((response)=> {
            this.$message(response.data.message);
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
      var submitData = util.deleteExtra(this.formData);
      axios.get('/api/ws/future/crm/afterSale/getFromCompanyData?'+qs.stringify(submitData)).then((response)=>{
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
