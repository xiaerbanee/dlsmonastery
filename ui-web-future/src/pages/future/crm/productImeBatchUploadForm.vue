<template>
  <div>
    <head-tab active="productImeBatchUploadForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item  >
          <div style="color:#ff0000">
            用于办事处批量保卡上报，上报门店默认为核销门店，上报人员默认为核销人员，请谨慎使用该功能
          </div>
        </el-form-item>
        <el-form-item :label="$t('productImeBatchUploadForm.areaId')" prop="officeId">
          <el-select v-model="inputForm.officeId" filterable clearable :placeholder="$t('productImeBatchUploadForm.inputWord')">
            <el-option v-for="office  in inputForm.extra.officeList" :key="office.id" :label="office.name" :value="office.id"></el-option>
          </el-select>

        </el-form-item>
        <el-form-item :label="$t('productImeBatchUploadForm.month')" prop="month">
          <month-picker  v-model="inputForm.month"></month-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productImeBatchUploadForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import monthPicker from 'components/common/month-picker'

  export default{
    components:{
      monthPicker,

    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{

          submitDisabled:false,
          inputForm:{
            extra:{},
          },
          rules: {
            officeId: [{ required: true, message: this.$t('productImeBatchUploadForm.prerequisiteMessage')}],
            month:[{ required: true, message: this.$t('productImeBatchUploadForm.prerequisiteMessage')}],
          }
        }
      },
      formSubmit(){

        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.confirmBeforeAction(this, this.$t('productImeBatchUploadForm.confirmBatchUpload')).then(() => {

              this.submitDisabled = true;
              axios.post('/api/ws/future/crm/productImeUpload/batchUpload', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.submitDisabled = false;
                this.$message(response.data.message);
                this.$router.push({name: 'productImeUploadList', query: util.getQuery("productImeUploadList")});
              }).catch( () => {
                this.submitDisabled = false;
              });

            }).catch(()=>{});
          }
        });
      }
    },created () {
        axios.get('/api/ws/future/crm/productImeUpload/getBatchUploadForm').then((response)=>{
          this.inputForm = response.data;
        });
    }
  }
</script>
