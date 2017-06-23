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
              <el-input v-model="employeeForm.salary"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.bankNumber')" prop="bankNumber">
              <el-input v-model="employeeForm.bankNumber"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.bankName')" prop="bankName">
              <el-input v-model="employeeForm.bankName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.entryDate')" prop="entryDate">
              <date-picker v-model="employeeForm.entryDate"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.regularDate')" prop="regularDate">
              <date-picker v-model="employeeForm.regularDate"></date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leaveDate')" prop="leaveDate">
              <date-picker v-model="employeeForm.leaveDate"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.school')" prop="school">
              <el-input v-model="employeeForm.school"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.idCard')" prop="idcard">
              <el-input v-model="employeeForm.idcard"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="employeeForm.mobilePhone"></el-input>
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
              <el-input v-model.number="employeeForm.salerName"></el-input>
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
              <el-input v-model="accountForm.loginName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.office')" prop="officeId">
              <office-select v-model="accountForm.officeId"></office-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leader')" prop="leaderId">
              <account-select v-model="accountForm.leaderId"></account-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.position')" prop="positionId">
              <el-select v-model="accountForm.positionId"  filterable :placeholder="$t('employeeForm.selectGroup')" :clearable=true>
                <el-option v-for="position in accountForm.extra.positionDtoList" :key="position.id" :label="position.name" :value="position.id"></el-option>
              </el-select>
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
  import districtSelect from 'components/general/district-select'
  import accountSelect from 'components/basic/account-select'
  import officeSelect from 'components/basic/office-select'
  export default{
    components:{
        districtSelect,
        accountSelect,
        officeSelect
    },
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return {
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          employeeForm: {
            extra: {}
          },
          accountForm: {
            extra: {}
          },
          remoteLoading: false,
          employeeRules: {
            name: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            mobilePhone: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            salary: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            idcard: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            entryDate: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
          },
          accountRules: {
            loginName: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            officeId: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
            positionId: [{required: true, message: this.$t('employeeForm.prerequisiteMessage')}],
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
            this.employeeForm.birthday=util.formatLocalDate(this.employeeForm.birthday)
            this.employeeForm.entryDate=util.formatLocalDate(this.employeeForm.entryDate)
            this.employeeForm.regularDate=util.formatLocalDate(this.employeeForm.regularDate)
            this.employeeForm.leaveDate=util.formatLocalDate(this.employeeForm.leaveDate)
            this.employeeForm.sex=this.employeeForm.sex==1?"男":"女"
            accountForm.validate((accountValid) => {
              if (accountValid) {
                axios.post('/api/basic/hr/employee/save', qs.stringify(util.deleteExtra(this.employeeForm))).then((response)=> {
                  this.$message("员工"+response.data.message);
                  this.accountForm.employeeId=response.data.extra.employeeId;
                  axios.post('/api/basic/hr/account/save', qs.stringify(util.deleteExtra(this.accountForm))).then((response)=> {
                    this.$message("账户"+response.data.message);
                  });
                  if(!this.isCreate){
                    this.submitDisabled = false;
                    this.$router.push({name:'employeeList',query:util.getQuery("employeeList")})
                  } else {
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }
                }).catch( ()=> {
                  that.submitDisabled = false;
                });
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
