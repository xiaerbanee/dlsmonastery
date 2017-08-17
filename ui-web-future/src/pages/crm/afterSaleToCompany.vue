<template>
  <div>
    <head-tab active="afterSaleToCompany"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('afterSaleToCompany.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('afterSaleToCompany.inputIme')" @change="onchange"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="onchange(inputForm.imeStr)">{{$t('afterSaleToCompany.search')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="16" v-if="inputForm.imeStr !==''">
            <template>
              <el-alert :title="message" type="error" show-icon v-if="message !==''"> </el-alert>
              <el-table :data="product" style="width: 100%" border>
                <el-table-column prop="name" :label="$t('afterSaleToCompany.productType')"></el-table-column>
                <el-table-column prop="qty" :label="$t('afterSaleToCompany.qty')"></el-table-column>
              </el-table>
            </template>
            <div style="height:30px"></div>
            <template>
              <el-table :data="searchData" style="width: 100%" border>
                <el-table-column prop="badProductName" :label="$t('afterSaleToCompany.badProductName')"></el-table-column>
                <el-table-column prop="badProductIme" :label="$t('afterSaleToCompany.badProductIme')"></el-table-column>
                <el-table-column prop="toAreaProductName" :label="$t('afterSaleToCompany.toAreaProductName')"></el-table-column>
                <el-table-column prop="areaDepotName" :label="$t('afterSaleToCompany.areaDepot')"></el-table-column>
                <el-table-column prop="packageStatus" :label="$t('afterSaleToCompany.packageStatus')"></el-table-column>
                <el-table-column prop="toStoreType" :label="$t('afterSaleToCompany.toStoreType')"></el-table-column>
                <el-table-column prop="memory" :label="$t('afterSaleToCompany.memory')"></el-table-column>
                <el-table-column prop="toStoreRemarks" :label="$t('afterSaleToCompany.remarks')"></el-table-column>
              </el-table>
            </template>
            <el-form-item :label="$t('afterSaleToCompany.toCompanyDate')" prop="toCompanyDate">
              <el-date-picker  v-model="inputForm.toCompanyDate" type="date" align="left" :placeholder="$t('afterSaleToCompany.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('afterSaleToCompany.companyRemarks')">
              <el-input v-model="inputForm.toCompanyRemarks" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.imeStr !==''">{{$t('afterSaleToCompany.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        remoteLoading:false,
        inputForm:{
          imeStr:'',
          toCompanyDate:'',
          toCompanyRemarks:''
        },
        rules: {
          imeStr: [{ required: true, message: this.$t('afterSaleToCompany.prerequisiteMessage')}],
        },
        searchData:[],
        message:'',
        product:[],
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        if(this.message !==''){
          alert($t('afterSaleToCompany.alertDealErrorData'));
          form.resetFields();
          return
        }
        form.validate((valid) => {
          if (valid) {
            this.inputForm.toCompanyDate=util.formatLocalDate(this.inputForm.toCompanyDate)
            axios.post('/api/ws/future/crm/afterSale/toCompany',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'afterSaleList',query:util.getQuery("afterSaleList"),params:{_closeFrom:true}})
            });
          }
        })
      },onchange(imeStr){
        axios.get('/api/ws/future/crm/afterSale/toCompanyForm',{params:{imeStr:imeStr}}).then((response)=>{
          this.searchData=response.data.list;
          var product=new Array();
          for(let i in response.data.qtyMap){
            product.push({name:i,qty:response.data.qtyMap[i]})
          }
        this.product=product;
        this.message = response.data.message;
        this.inputForm.toCompanyDate=response.data.toCompanyDate
        })
      }
    },created(){
    }
  }
</script>
