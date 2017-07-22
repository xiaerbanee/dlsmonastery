<template>
  <div>
    <head-tab active="accountRoleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item label="用户名" prop="loginName">
              <el-input v-model="inputForm.loginName" :readonly="true"></el-input>
            </el-form-item>
            <el-form-item label="绑定角色" prop="roleIdList">
              <el-select v-model="inputForm.roleIdList"   :placeholder="$t('accountForm.inputWord')" multiple>
                <el-option v-for="item in inputForm.extra.roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('positionForm.save')}}</el-button>
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
        return {
          remoteLoading: false,
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          inputForm: {
            id: this.$route.query.id,
            extra: {}
          },
          rules: {
            roleIdList: [{required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          },
        };
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/account/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              this.$router.push({name:'accountList',query:util.getQuery("accountList"), params:{_closeFrom:true}})
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage() {
        axios.get('/api/basic/hr/account/getForm').then((response)=>{
          this.inputForm=response.data;
          axios.get('/api/basic/hr/account/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        })
      }
    },created () {
      this.initPage();
    }
  }
</script>
