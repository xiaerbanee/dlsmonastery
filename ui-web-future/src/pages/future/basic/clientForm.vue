<template>
  <div>
    <head-tab active="clientForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('clientForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('clientForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('clientForm.remarks')" prop="remarks">
        <el-input type="textarea" v-model="inputForm.remarks"></el-input>
      </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('clientForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return this.getData()
    },
    methods: {
      getData() {
        return {
          isInit: false,
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          inputForm: {
            extra:{}
          },
          rules: {
            name: [{required: true, message: this.$t('clientForm.prerequisiteMessage')}],
            mobilePhone: [{required: true, message: this.$t('clientForm.prerequisiteMessage')}]
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/client/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response) => {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              if (!this.isCreate) {
                this.$router.push({name: 'clientList', query: util.getQuery("clientList")})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }
      }, activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
        axios.get('/api/ws/future/basic/client/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/basic/client/findOne', {params: {id: this.$route.query.id}}).then((response) => {
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
      this.isInit = true;
      }
  }
</script>

