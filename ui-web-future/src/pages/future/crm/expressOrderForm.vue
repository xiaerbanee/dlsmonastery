<template>
  <div>
    <head-tab active="expressOrderForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('expressOrderForm.fromDepotId')" prop="fromDepotId">
          <depot-select v-model="inputForm.fromDepotId"  ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.toDepotId')" prop="toDepotId">
          <depot-select v-model="inputForm.toDepotId"  ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.expressCompanyId')" prop="expressCompanyId">
          <express-company-select v-model="inputForm.expressCompanyId"  ></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.contact')" prop="contator">
          <el-input v-model="inputForm.contator"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.address')" prop="address">
          <el-input v-model="inputForm.address"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressOrderForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import expressCompanySelect from 'components/future/express-company-select'
  export default{
    components: {
      depotSelect,
      expressCompanySelect,
    },
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{},
            submitData:{
              id:'',
              fromDepotId:'',
              toDepotId:'',
              expressCompanyId:'',
              contator:'',
              address:'',
              mobilePhone:''
            },
            rules: {
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
              axios.post('/api/ws/future/crm/expressOrder/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.create){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressOrderList',query:util.getQuery("expressOrderList")})
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
        axios.get('/api/ws/future/crm/expressOrder/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
      }
    }
</script>
