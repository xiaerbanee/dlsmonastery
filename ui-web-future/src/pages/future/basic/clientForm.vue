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
        <el-input v-model="inputForm.remarks"></el-input>
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
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{},
        inputForm:{},
        submitData:{
          id:'',
          name:'',
          mobilePhone:'',
          remarks:'',
        },
        rules: {
          name: [{ required: true, message: this.$t('clientForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('clientForm.prerequisiteMessage')}]
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData)
            axios.post('/api/ws/future/basic/client/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'clientList',query:util.getQuery("clientList")})
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
      if(!this.isCreate){
        axios.get('/api/ws/future/basic/client/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data
        })
      }
    }
  }
</script>

