<template>
  <div>
    <head-tab active="productMonthPriceForm"></head-tab>
    <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('productMonthPriceForm.month')" prop="month">
          <el-date-picker  v-model="inputForm.month" type="month" align="left" :placeholder="$t('productMonthPriceForm.selectDate')" format="yyyy-MM" @change="onChange"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('productMonthPriceForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productMonthPriceForm.save')}}</el-button>
        </el-form-item>
        <template>
          <el-table :data="inputForm.productMonthPriceDetailList" border >
            <el-table-column :label="$t('productMonthPriceForm.totalName')" prop="productType.name"></el-table-column>
            <el-table-column :label="$t('productMonthPriceForm.baokaPrice')">
              <template scope="scope">
                <el-input v-model="scope.row.baokaPrice"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('productMonthPriceForm.price3')">
              <template scope="scope">
                <el-input v-model="scope.row.price3"></el-input>
              </template>
            </el-table-column>
              <el-table-column :label="$t('productMonthPriceForm.price2')">
                <template scope="scope">
                  <el-input v-model="scope.row.price2"></el-input>
                </template>
            </el-table-column>
          </el-table>
        </template>
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
            formProperty:{},
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              month:'',
              remarks:'',
              productMonthPriceDetailList:[]
            },
            rules: {
              month: [{ required: true, message: this.$t('productMonthPriceForm.prerequisiteMessage')}]
            },
            message:''
          }
      },
      methods:{
        formSubmit(){

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              axios.post('/api/crm/productMonthPrice/save',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  if (this.isCreate) {
                    form.resetFields();
                  } else {
                    this.$router.push({name: 'productMonthPriceList', query: util.getQuery("productMonthPriceList")})
                  }
                }
              }).catch(() => {
                this.submitDisabled = false;
              });
            }
          })
        },onChange(){
          if(this.isCreate){
            this.message='';
            this.submitDisabled = false;
            this.inputForm.month=util.formatDate( this.inputForm.month,'yyyy-MM');
            axios.get('/api/crm/productMonthPrice/checkMonth',{params: {month:this.inputForm.month}}).then((response)=>{
              this.submitDisabled = true;
              if(!response.data.success){
                this.message=response.data.message;
              }

            })
          }
        },initPage(){
          if(!this.isCreate){
            axios.get('/api/crm/productMonthPrice/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
              this.inputForm.productMonthPriceDetailList = response.data.productMonthPriceDetailList;
            })
          }else{
            axios.get('/api/crm/productMonthPrice/getForm').then((response)=>{
              this.inputForm.productMonthPriceDetailList = response.data.productMonthPriceDetailList;
            })
          }
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
