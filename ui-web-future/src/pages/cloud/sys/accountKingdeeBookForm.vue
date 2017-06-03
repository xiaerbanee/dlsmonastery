<template>
  <div>
    <head-tab active="accountKingdeeBookForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item label="金蝶账户名称" prop="username">
          <el-input v-model="inputForm.username"></el-input>
        </el-form-item>
        <el-form-item label="金蝶账户密码" prop="password">
          <el-input v-model="inputForm.password"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
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
              username:'',
              password:'',
              remarks:'',
            },
            rules: {
              username: [{ required: true, message: "必填信息"}],
              password: [{ required: true, message: "必填信息"}],
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
              axios.post('/api/global/cloud/sys/accountKingdeeBook/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(!this.inputForm.id){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'accountKingdeeBookList',query:util.getQuery("accountKingdeeBookList")})
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
        axios.get('/api/global/cloud/sys/accountKingdeeBook/form',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
      }
    }
</script>
