<template>
  <div>
    <head-tab :active="$t('employeeForm.employeeForm') "></head-tab>
    <div>
      <el-form :model="employeeForm" ref="employeeForm" :rules="employeeRules" label-width="120px"  class="form input-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.employeeName')" prop="name">
              <el-input v-model="employeeForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.education')" prop="education">
              <el-select v-model="employeeForm.education" >
                <el-option v-for="item in formProperty.educationsList":key="item" :label="item" :value="item"></el-option>
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
              <el-date-picker  v-model="employeeForm.entryDate" type="date" align="left" :placeholder="$t('employeeForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.regularDate')" prop="regularDate">
              <el-date-picker  v-model="employeeForm.regularDate" type="date" align="left" :placeholder="$t('employeeForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leaveDate')" prop="leaveDate">
              <el-date-picker  v-model="employeeForm.leaveDate" type="date" align="left" :placeholder="$t('employeeForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
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
              <el-date-picker  v-model="employeeForm.birthday" type="date" align="left" :placeholder="$t('employeeForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.sex')" prop="sexLabel">
              <el-radio-group v-model="employeeForm.sexLabel">
                <el-radio :label="1">{{$t('employeeForm.man')}}</el-radio>
                <el-radio :label="0">{{$t('employeeForm.women')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.origin')" prop="origin">
              <el-select v-model="employeeForm.origin"  clearable filterable remote :placeholder="$t('employeeForm.inputWord')" :remote-method="remoteDistrict" :loading="remoteLoading">
                <el-option v-for="item in origins" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
              </el-select>
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
              <el-select v-model="accountForm.officeId" filterable remote :placeholder="$t('employeeForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.depotList')" prop="depotIdList">
              <el-select v-model="accountForm.depotIdList" multiple filterable remote :placeholder="$t('employeeForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true >
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeForm.leader')" prop="leaderId">
              <el-select v-model="accountForm.leaderId" filterable remote :placeholder="$t('employeeForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" :clearable=true>
                <el-option v-for="leader in leaders" :key="leader.id" :label="leader.loginName" :value="leader.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('employeeForm.position')" prop="positionId">
              <el-select v-model="accountForm.positionId"  filterable :placeholder="$t('employeeForm.selectGroup')" :clearable=true>
                <el-option v-for="position in formProperty.positions" :key="position.id" :label="position.name" :value="position.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('employeeForm.dataScopeOffices')" prop="officeIdList">
              <el-select v-model="accountForm.officeIdList" multiple filterable remote :placeholder="$t('employeeForm.inputWord')" :remote-method="remoteDataScopeOffice" :loading="remoteLoading" :clearable=true>
                <el-option v-for="office in dataScopeOffices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
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
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            employeeForm:{
              id:'',
              name:'',
              education:'',
              code:'',
              salary:'',
              bankNumber:'',
              bankName:'',
              entryDate:'',
              regularDate:'',
              leaveDate:'',
              school:'',
              mobilePhone:'',
              idcard:'',
              birthday:'',
              sex:"",
              sexLabel:"",
              origin:'',
              salerName:'',
            },
           accountForm:{
              id:"",
              loginName:'',
              employeeId:"",
              officeId:"",
              depotIdList:"",
              leaderId:'',
              positionId:"",
              type:"主账号",
              officeIdList:'',
            },
            leaders:[],
            offices:[],
            depots:[],
            origins:[],
            dataScopeOffices:[],
            remoteLoading:false,
            employeeRules: {
              name: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              mobilePhone: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              salary: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              idcard: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              entryDate: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
            },
            accountRules: {
              loginName: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              officeId: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
              positionId: [{ required: true, message:this.$t('employeeForm.prerequisiteMessage')}],
            },
            loading:false
          }
      },
      methods:{
        formSubmit(){
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
                  this.employeeForm.sex=this.employeeForm.sexLabel==1?"男":"女"
                    axios.post('/api/hr/employee/save', qs.stringify(this.employeeForm)).then((response)=> {
                        this.$message("员工"+response.data.message);
                        this.accountForm.employeeId=response.data.extendMap.employeeId;
                        axios.post('/api/hr/account/save', qs.stringify(this.accountForm)).then((response)=> {
                        this.$message("账户"+response.data.message);
                    });
                    if(this.isCreate){
                      employeeForm.resetFields();
                      this.submitDisabled = false;
                    } else {
                      this.$router.push({name:'employeeList',query:util.getQuery("employeeList")})
                    }
                    });
                }
              })
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/account/search',{params:{key:query}}).then((response)=>{
            this.leaders=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteDataScopeOffice(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.dataScopeOffices=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteEmployee(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/employee/search',{params:{key:query}}).then((response)=>{
            this.employees=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
            this.depots=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteDistrict(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/district/search',{params:{key:query}}).then((response)=>{
            this.origins=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.origins = [];
        }
      }
      },created(){
        axios.get('/api/hr/employee/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/hr/employee/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
         if(response.data.account.office!=null){
            this.offices=new Array(response.data.account.office)
          }
         if(response.data.account.officeList!=null&&response.data.account.officeList.length>0){
            this.dataScopeOffices=response.data.account.officeList;
            this.accountForm.officeIdList=util.getIdList(this.dataScopeOffices);
          }
          if(response.data.account.depotList!=null&&response.data.account.depotList.length>0){
            this.depots=response.data.account.depotList;
            this.accountForm.depotIdList=util.getIdList(this.depots);
          }
          if(response.data.account.leader!=null){
            this.leaders=new Array(response.data.account.leader)
          }
         this.employeeForm.sexLabel=response.data.sex=="男"?1:0;
         util.copyValue(response.data,this.employeeForm);
         util.copyValue(response.data.account,this.accountForm);
          })
        }
      }
    }
</script>
