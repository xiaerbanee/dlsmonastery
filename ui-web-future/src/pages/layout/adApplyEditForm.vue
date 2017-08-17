<template>
  <div>
    <head-tab active="adApplyEditForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('adApplyEditForm.shopName')">{{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.productName')">{{inputForm.productName}}
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.applyQty')">{{inputForm.applyQty}}
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.confirmQty')" prop="confirmQty">
              <el-input v-model.number="inputForm.confirmQty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.billedQty')">{{inputForm.billedQty}}
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.leftQty')">{{inputForm.leftQty}}
            </el-form-item>
            <el-form-item :label="$t('adApplyEditForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" type="textarea"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('adApplyEditForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default{
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            confirmQty: [{ required: true, message: this.$t('adApplyEditForm.prerequisiteMessage')},{type:"number",message:this.$t('adApplyEditForm.inputLegalValue')}],
          },
        }
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/adApply/saveConfirmQty', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else{
                  this.$router.push({name: 'adApplyList', query: util.getQuery("adApplyList"), params:{_closeFrom:true}})
                }
              }
            }).catch(() => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/layout/adApply/getEditForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate) {
            axios.get('/api/ws/future/layout/adApply/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
            });
          }
        });

      }
    },created () {
      this.initPage();
    }
  }
</script>
