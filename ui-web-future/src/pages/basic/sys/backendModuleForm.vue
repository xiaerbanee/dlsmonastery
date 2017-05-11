<template>
  <div>
    <head-tab active="backendModuleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('backendModuleForm.code')" prop="code">
          <el-input v-model.number="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('backendModuleForm.name')" prop="name">
          <el-input v-model.number="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item label="所属项目" prop="backendId">
          <el-select v-model="inputForm.backendId" filterable placeholder="所属项目">
            <el-option v-for="item in inputForm.backendList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('backendModuleForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('backendModuleForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            submitDisabled:false,
            inputForm:{},
            submitData:{
              id:'',
              code:'',
              backendId:'',
              name:'',
              remarks:''
            },
            rules: {
              backendId: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}],
              code: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}]
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
              axios.post('/api/basic/sys/backendModule/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.create){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'backendModuleList',query:util.getQuery("backendModuleList")})
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
          axios.get('/api/basic/sys/backendModule/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
      }
    }
</script>
