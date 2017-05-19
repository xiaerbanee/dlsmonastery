<template>
  <div>
    <head-tab active="priceChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('priceChangeForm.name')" prop="name">
          <el-input v-model="inputForm.name" :disabled="!isCreate"></el-input>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.productTypeId')" prop="productTypeIdList">
          <product-type  v-model="inputForm.productTypeIdList" :multiple=true :disabled="!isCreate"></product-type>
        </el-form-item>
        <el-form-item  :label="$t('priceChangeForm.priceChangeDate')" prop="priceChangeDate">
          <date-picker  v-model="inputForm.priceChangeDate" :disabled="action=='audit'"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.uploadEndDate')" prop="uploadEndDate">
          <date-picker  v-model="inputForm.uploadEndDate" :disabled="action=='audit'"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" :disabled="action=='audit'"></el-input>
        </el-form-item>
        <el-form-item v-if="action=='audit'"  :label="$t('priceChangeDetail.checkPercent')" prop="checkPercent">
          <el-input v-model.number="inputForm.checkPercent" ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productType from 'components/future/product-type-select'
  export default{
    components:{productType},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        action:this.$route.query.action,
        submitDisabled:false,
        inputForm:{},
        url:'',
        submitData:{
          id:this.$route.query.id,
          name:'',
          priceChangeDate:'',
          uploadEndDate:'',
          productTypeIdList:[],
          remarks:'',
          checkPercent:'',
        },
        rules: {
          name: [{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData)

            if(this.action==='audit'){
              this.url = '/api/ws/future/crm/priceChange/check';
            }else{
              this.url = '/api/ws/future/crm/priceChange/save';
            }

            axios.post(this.url,qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'priceChangeList',query:util.getQuery("priceChangeList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
    },created(){
      axios.get('/api/ws/future/crm/priceChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        console.log(this.inputForm);
      })
    }
  }
</script>
