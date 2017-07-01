<template>
  <div>
    <head-tab  active="carrierOrderForm"></head-tab>
    <su-alert  :text="warnMsg"  type="warning"></su-alert>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="form input-form">
     <el-row>
       <el-col :span="5">
         <el-form-item :label="$t('carrierOrderForm.orderCode')" prop="orderCode">
           <el-input v-model="form.orderCode"></el-input>
         </el-form-item>
         <el-form-item :label="$t('carrierOrderForm.shopDetail')" >
           <el-input type="textarea" v-model="form.shopDetail" :autosize="{ minRows: 4, maxRows: 10}"></el-input>
         </el-form-item>
         <el-form-item>
           <el-button type="primary">{{$t('carrierOrderForm.save')}}</el-button>
         </el-form-item>
       </el-col>
     </el-row>
    </el-form>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  export default {
    components: {
      depotSelect
    },
    data(){
      return {
        warnMsg:'',
        form:{},
        rules: {
          orderCode: [{required: true,validator: this.checkOrderCode}]
        }
      }
    },
    methods:{
      checkOrderCode(rule, value, callback){
        if (!value) {
          return callback(new Error('必填信息'));
        }else {
          axios.get('/api/basic/hr/account/checkLoginName?loginName='+value+"&id="+this.$route.query.id).then((response)=>{
            if(response.data.success){
              return callback();
            }else {
              return callback(new Error(response.data.message));
            }
          })
        }
      }
    }
  }
</script>

