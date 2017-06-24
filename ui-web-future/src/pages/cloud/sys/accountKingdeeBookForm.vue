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
        return this.getData()
      },
      methods:{
        getData(){
          return{
            isCreate:this.$route.query.id===null,
            submitDisabled:false,
            inputForm:{},
            submitData:{},
            rules: {
              username: [{ required: true, message: "必填信息"}],
              password: [{ required: true, message: "必填信息"}],
            }
          }
        },
        formSubmit(){
          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/global/cloud/sys/accountKingdeeBook/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                } else {
                  this.submitDisabled = false;
                  this.$router.push({name:'accountKingdeeBookList',query:util.getQuery("accountKingdeeBookList"),params:{_closeFrom:true}});
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/global/cloud/sys/accountKingdeeBook/form',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          });
        }
      },created(){
        this.initPage();
      }
    }
</script>
