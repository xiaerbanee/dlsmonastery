<template>
  <div>
    <head-tab active="employeeForm"></head-tab>
    <div>
      <el-form :model="employeeForm" ref="employeeForm" :rules="employeeRules" label-width="120px"  class="form input-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.employeeName')" prop="name">
              <el-input v-model="employeeForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.education')" prop="education">
              <el-select v-model="employeeForm.education" >
                <el-option v-for="item in employeeForm.extra.educationList":key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.code')" prop="code">
              <el-input v-model="employeeForm.code"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.salary')" prop="salary">
              <el-input v-model="employeeForm.salary"  :disabled="!isCreate&&!hasPermit"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.bankNumber')" prop="bankNumber">
              <el-input v-model="employeeForm.bankNumber"  :disabled="!isCreate&&!hasPermit"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.bankName')" prop="bankName">
              <el-input v-model="employeeForm.bankName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.entryDate')" prop="entryDate">
              <date-picker v-model="employeeForm.entryDate"  :disabled="!isCreate&&!hasPermit"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.regularDate')" prop="regularDate">
              <date-picker v-model="employeeForm.regularDate"  :disabled="!isCreate&&!hasPermit"></date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leaveDate')" prop="leaveDate">
              <date-picker v-model="employeeForm.leaveDate"  :disabled="!isCreate&&!hasPermit"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.school')" prop="school">
              <el-input v-model="employeeForm.school"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.idCard')" prop="idcard">
              <el-input v-model="employeeForm.idcard"  :disabled="!isCreate&&!hasPermit"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="employeeForm.mobilePhone"  :disabled="!isCreate&&!hasPermit"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.birthday')" prop="birthday">
              <date-picker v-model="employeeForm.birthday"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.sex')" prop="sex">
              <el-radio-group v-model="employeeForm.sex">
                <el-radio :label="1">{{$t('employeeForm.man')}}</el-radio>
                <el-radio :label="0">{{$t('employeeForm.women')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.origin')" prop="originId">
                  <district-select v-model="employeeForm.originId"></district-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.salerName')" prop="salerName">
              <el-input v-model="employeeForm.salerName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div>
      <el-form :model="accountForm"   ref="accountForm" :rules="accountRules" label-width="120px"  class="form input-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.loginName')" prop="loginName">
              <el-input v-model="accountForm.loginName" :readonly="!isCreate"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.office')" prop="officeId">
              <office-select v-model="accountForm.officeId"  :disabled="!isCreate&&!hasPermit"></office-select>
            </el-form-item>
            <el-form-item label="绑定门店" prop="depotIdList">
              <depot-select v-model="depotIdList" category="shop" :multiple="true"></depot-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leader')" prop="leaderId">
              <account-select v-model="accountForm.leaderId" :disabled="!isCreate&&!hasPermit"></account-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.position')" prop="positionId">
              <el-select v-model="accountForm.positionId"  filterable :placeholder="$t('employeeForm.selectGroup')" :clearable=true  :disabled="!isCreate&&!hasPermit">
                <el-option v-for="position in accountForm.extra.positionDtoList" :key="position.id" :label="position.name" :value="position.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="数据部门" prop="officeIdList">
              <office-select v-model="accountForm.officeIdList" :multiple="true"></office-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('employeeForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import districtSelect from 'components/general/district-select'
  import accountSelect from 'components/basic/account-select'
  import officeSelect from 'components/basic/office-select'
  export default{
    components:{
        depotSelect,
        districtSelect,
        accountSelect,
        officeSelect
    },
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        let checkLoginName = (rule, value, callback)=>{
          if (!value) {
            return callback(new Error('必填信息'));
          }else {
            axios.get('/api/basic/hr/account/checkLoginName?loginName='+value+"&id="+this.accountForm.id).then((response)=>{
              if(response.data.success){
                return callback();
              }else {
                return callback(new Error(response.data.message));
              }
            })
          }
        };
        return {
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          hasPermit:util.isPermit('hr:employee:enableUpdate'),
          employeeForm: {
            extra: {}
          },
          accountForm: {
            extra: {}
          },
          depotIdList:[],
          remoteLoading: false,
          employeeRules: {
            name: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            mobilePhone: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            salary: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            idcard: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            entryDate: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
          },
          accountRules: {
            loginName: [{ required: true,validator: checkLoginName}],
            officeId: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            positionId: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            officeIdList:[{required:true,message:this.$t('employeeForm.prerequisiteMessage')}],
            leaderId:[{required:true,message:this.$t('employeeForm.prerequisiteMessage')}]
          },
          depotRules:{

          },
          loading: false
        };
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var employeeForm = this.$refs["employeeForm"];
        var accountForm = this.$refs["accountForm"];
        employeeForm.validate((valid) => {
          if (valid) {
            accountForm.validate((accountValid) => {
              if (accountValid) {
                this.employeeForm.birthday=util.formatLocalDate(this.employeeForm.birthday)
                this.employeeForm.entryDate=util.formatLocalDate(this.employeeForm.entryDate)
                this.employeeForm.regularDate=util.formatLocalDate(this.employeeForm.regularDate)
                this.employeeForm.leaveDate=util.formatLocalDate(this.employeeForm.leaveDate)
                this.employeeForm.sex=this.employeeForm.sex==1?"男":"女"
                this.employeeForm.accountForm=util.deleteExtra(this.accountForm)
                axios.post('/api/basic/hr/employee/save', qs.stringify(this.employeeForm, {allowDots: true})).then((response)=> {
                  axios.post('/api/ws/future/basic/accountDepot/save', qs.stringify({accountId:response.data.extra.accountId,depotIdList:this.depotIdList}, {allowDots: true})).then((response)=> {
                    this.$message(response.data.message);
                    if(!this.isCreate){
                      this.submitDisabled = false;
                        util.closeAndBackToPage(this.$router,"employeeList")
                    } else {
                      Object.assign(this.$data, this.getData());
                      this.initPage();
                    }
                  })
                }).catch( ()=> {
                  that.submitDisabled = false;
                })
              }else {
                this.submitDisabled = false;
              }
            })
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage() {
        axios.get('/api/basic/hr/employee/getForm').then((response)=>{
          this.employeeForm = response.data;
          axios.get('/api/basic/hr/account/getForm').then((response)=>{
            this.accountForm = response.data;
            axios.get('/api/basic/hr/employee/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data, this.employeeForm);
              this.employeeForm.sex = response.data.sex == "男" ? 1 : 0;
              if(!this.isCreate){
                axios.get('/api/ws/future/basic/accountDepot/findByAccountId',{params: {accountId:this.employeeForm.accountId}}).then((response)=>{
                  this.depotIdList=response.data;
                })
              }
              axios.get('/api/basic/hr/account/findOne',{params: {id:this.employeeForm.accountId}}).then((response)=>{
                util.copyValue(response.data, this.accountForm);
                this.accountForm.type="主账号"
              })
            })
          })
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
