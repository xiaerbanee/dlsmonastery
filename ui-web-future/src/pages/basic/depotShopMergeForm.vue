<template>
  <div>
    <head-tab active="depotShopMergeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="合并前门店" prop="fromDepotId">
              <depot-select v-model="inputForm.fromDepotId" category="shop"></depot-select>
            </el-form-item>
            <el-form-item label="合并后门店" prop="toDepotId" >
              <depot-select v-model="inputForm.toDepotId" category="shop"></depot-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select';
  export default{
    components:{depotSelect},
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return{
          submitDisabled:false,
          remoteLoading:false,
          inputForm:{
            extra:{}
          },
          rules: {
            fromDepotId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            toDepotId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          },
        }
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/depotShop/merge', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success) {
                util.closeAndBackToPage(this.$router,'depotShopList')
              }
            }).catch(() => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/basic/depotShop/getMergeForm').then((response)=>{
          this.inputForm = response.data;
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>
