<template>
  <div>
    <head-tab active="expressOrderForm"></head-tab>
    <div>
      <el-form :model="expressOrder" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('expressOrderForm.fromDepotId')" prop="fromDepotId">
          <depot-select v-model="expressOrder.fromDepotId" category="store"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.toDepotId')" prop="toDepotId">
          <depot-select v-model="expressOrder.toDepotId"  category="shop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.expressCompanyId')" prop="expressCompanyId">
          <express-company-select v-model="expressOrder.expressCompanyId"  ></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.contact')" prop="contator">
          <el-input v-model="expressOrder.contator"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.address')" prop="address">
          <el-input type="textarea" v-model="expressOrder.address"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="expressOrder.mobilePhone"></el-input>
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
            expressOrder:{},
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

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.expressOrder,this.submitData);
              axios.post('/api/ws/future/crm/expressOrder/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressOrderList',query:util.getQuery("expressOrderList")})
                }
              }).catch( () => {
                this.submitDisabled = false;
              });
            }
          })
        }
      },created(){
        axios.get('/api/ws/future/crm/expressOrder/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
          this.expressOrder = response.data;
        })
      }
    }
</script>
