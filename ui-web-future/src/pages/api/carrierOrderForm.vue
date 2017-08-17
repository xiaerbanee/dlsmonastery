<template>
  <div>
    <head-tab  active="carrierOrderForm"></head-tab>
    <el-form ref="form" :model="submitData" :rules="rules" label-width="120px" class="form input-form">
     <el-row>
       <el-col :span="5">
         <el-form-item :label="$t('carrierOrderForm.orderCode')" prop="formatId">
           <el-input v-model="submitData.formatId"></el-input>
         </el-form-item>
         <el-form-item :label="$t('carrierOrderForm.shopDetail')" prop="detailJson" >
           <el-input type="textarea" v-model="submitData.detailJson" :autosize="{ minRows: 4, maxRows: 10}"></el-input>
         </el-form-item>
         <el-form-item>
           <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adPricesystemForm.save')}}</el-button>
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
        submitDisabled:false,
        submitData:{
          goodsOrderId:"",
          detailJson:"",
          formatId:"",
        },
        rules: {
          formatId: [{required: true,validator: this.checkOrderCode,trigger:'blur'}],
          detailJson: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
        }
      }
    },
    methods:{
      formSubmit(){
        var that=this;
        that.submitDisabled=true;
        var form = this.$refs["form"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/api/carrierOrder/save', qs.stringify(util.deleteExtra(this.submitData), {allowDots:true})).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                if (this.isCreate) {
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else {
                  this.$router.push({name: 'carrierOrderList', query: util.getQuery("carrierOrderList"),params:{_closeFrom:true}})
                }
              }else {
                that.submitDisabled = false;
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            }).catch(()=> {
              that.submitDisabled = false;
            });
          } else {
            that.submitDisabled = false;
          }
        })
      },
      checkOrderCode(rule, value, callback){
        if (!value) {
          return callback(new Error('必填信息'));
        }else {
          axios.get('/api/ws/future/api/carrierOrder/checkBusinessId?formatId='+value).then((response)=>{
            if(response.data.success){
              this.submitData.goodsOrderId=response.data.extra.goodsOrderId;
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

