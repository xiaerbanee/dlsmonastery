<template>
  <div>
    <head-tab active="expressForm"></head-tab>
    <div>
      <el-form :model="express" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('expressForm.expressOrderExtendType')" prop="expressOrderExtendType" v-if="!isCreate">
          {{express.expressOrderExtendType}}
        </el-form-item>
        <el-form-item :label="$t('expressForm.expressOrderId')" prop="expressOrderId"  v-if="!isCreate">
          {{express.expressOrderId}}
        </el-form-item>
        <el-form-item :label="$t('expressForm.expressOrderToDepotId')" prop="expressOrderToDepotId">
          <depot-select :disabled="!isCreate" v-model="express.expressOrderToDepotId" category="shop" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressForm.code')" prop="code">
          <el-input v-model="express.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.weight')" prop="weight">
          <el-input v-model="express.weight"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.qty')" prop="qty">
          <el-input v-model="express.qty"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.tracking')" prop="tracking">
          <el-input v-model="express.tracking"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.remarks')" prop="remarks">
          <el-input type="textarea"  v-model="express.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  export default{
    components:{
      depotSelect,
    },
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,

            express:{},
            submitData:{
              id:'',
              code:'',
              weight:'',
              qty:'',
              tracking:'',
              remarks:'',
              expressOrderId:'',
              expressOrderToDepotId:'',
              expressOrderIdExtendType:''
            },
            rules: {
              code: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}],
              expressOrderToDepotId: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}],
            }
          }
      },
      methods:{
        formSubmit(){

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.express,this.submitData);
              axios.post('/api/ws/future/crm/express/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  if (this.isCreate) {
                    form.resetFields();
                  } else {
                    this.$router.push({name: 'expressList', query: util.getQuery("expressList")});
                  }
                }
              }).catch( () => {
                this.submitDisabled = false;
              });
            }
          })
        },initPage(){
          axios.get('/api/ws/future/crm/express/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
            this.express = response.data;
        });
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
