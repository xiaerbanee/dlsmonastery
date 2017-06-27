<template>
  <div>
    <head-tab active="accountForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item :label="$t('accountForm.mainAccount')" prop="employeeId">
              <employee-select v-model="inputForm.employeeId"></employee-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.loginName')" prop="loginName">
              <el-input v-model="inputForm.loginName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('accountForm.officeName')" prop="officeId">
              <office-select v-model="inputForm.officeId"></office-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.leader')" prop="leaderId">
              <account-select v-model="inputForm.leaderId"></account-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.position')" prop="positionId">
              <el-select v-model="inputForm.positionId"  filterable :placeholder="$t('accountForm.selectGroup')" :clearable=true>
                <el-option v-for="position in inputForm.extra.positionDtoList" :key="position.id" :label="position.name" :value="position.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('accountForm.outId')" prop="outId">
              <el-input v-model="inputForm.outId"></el-input>
            </el-form-item>
            <el-form-item :label="$t('accountForm.outPassword')" prop="outPassword">
              <el-input v-model="inputForm.outPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item :label="$t('accountForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('accountForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import employeeSelect from 'components/basic/employee-select'
  import accountSelect from 'components/basic/account-select'
  import officeSelect from 'components/basic/office-select'
  export default{
      components:{
          employeeSelect,
          accountSelect,
          officeSelect
      },
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        var checkLoginName=(rule, value, callback)=>{
          if (!value) {
            return callback(new Error('必填信息'));
          }else {
            axios.get('/api/basic/hr/account/checkLoginName?loginName='+value+"&id="+this.$route.query.id).then((response)=>{
              if(response.data.success){
                return callback();
              }else {
                return callback(new Error(response.data.message));
              }
            })
          }
        };
        return{
          isCreate:this.$route.query.id==null,
          multiple:true,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          radio:'1',
          remoteLoading:false,
          rules: {
            employeeId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
            loginName: [{ required: true,validator: checkLoginName}],
            officeId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
            positionId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        console.log("form"+form);
        form.validate((valid) => {
          if (valid) {
            var submitData = util.deleteExtra(this.inputForm);
            axios.post('/api/basic/hr/account/save',qs.stringify(submitData)).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(!this.isCreate){
                this.$router.push({name:'accountList',query:util.getQuery("accountList"), params:{_closeFrom:true}})
              }else {
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage(){
        axios.get('/api/basic/hr/account/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          axios.get('/api/basic/hr/account/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
