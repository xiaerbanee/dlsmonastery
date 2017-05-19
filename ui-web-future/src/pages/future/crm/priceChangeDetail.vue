<template>
  <div>
    <head-tab active="priceChangeDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('priceChangeDetail.name')" prop="name">
          {{inputForm.name}}
        </el-form-item>
        <el-form-item :label="$t('priceChangeDetail.productTypeNames')" prop="productTypeIdList">
            {{inputForm.productTypeNames}}
        </el-form-item>
        <el-form-item :label="$t('priceChangeDetail.priceChangeDate')" prop="priceChangeDate">
          {{inputForm.priceChangeDate}}
        </el-form-item>
        <el-form-item :label="$t('priceChangeDetail.uploadEndDate')" prop="uploadEndDate">
          {{inputForm.uploadEndDate}}
        </el-form-item>
        <el-form-item :label="$t('priceChangeDetail.remarks')" prop="remarks">
          {{inputForm.remarks}}
        </el-form-item>
        <el-form-item  :label="$t('priceChangeDetail.checkPercent')" prop="checkPercent">
          <el-input v-model.number="inputForm.checkPercent" ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeDetail.save')}}</el-button>
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
            inputForm:{},
            submitData:{
              id:"",
              checkPercent:"",
            },
            rules: {
              checkPercent: [{ type: 'number', message: this.$t('priceChangeDetail.checkPercentIdNumber')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
                util.copyValue(this.inputForm,this.submitData);

              axios.post('/api/ws/future/crm/priceChange/check',qs.stringify(this.submitData)).then((response)=> {
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
        }
      },created(){
          axios.get('/api/ws/future/crm/priceChange/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
        }

    }
</script>
