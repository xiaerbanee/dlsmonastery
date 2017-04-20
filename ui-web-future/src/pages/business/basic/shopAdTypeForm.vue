<template>
  <div>
    <head-tab active="shopAdTypeForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('shopAdTypeForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopAdTypeForm.totalPriceType')" prop="totalPriceType" >
          <el-radio v-model='inputForm.totalPriceType' class="radio"  v-for="item in formProperty.totalPriceTypes" :key="item" :label="item">{{item}}</el-radio>
        </el-form-item>
        <el-form-item :label="$t('shopAdTypeForm.price')" prop="price">
          <el-input v-model.number="inputForm.price"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopAdTypeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAdTypeForm.save')}}</el-button>
        </el-form-item>
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
            inputForm:{
              id:'',
              name:'',
              totalPriceType:'按数量',
              price:'',
              remarks:''
            },
            rules: {
              name: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')}],
              price: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')},{ type: 'number', message: this.$t('shopAdTypeForm.inputLegalValue')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.get('/api/future/business/basic/shopAdType/save', {params: this.inputForm}).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'shopAdTypeList',query:util.getQuery("shopAdTypeList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        axios.get('/api/future/business/basic/shopAdType/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/future/business/basic/shopAdType/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
