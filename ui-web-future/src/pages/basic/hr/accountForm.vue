<template>
  <div>
    <head-tab active="accountForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item :label="$t('accountForm.mainAccount')" prop="employeeId">
              <el-select v-model="inputForm.employeeId" filterable remote :placeholder="$t('accountForm.inputWord')" :remote-method="remoteEmployee" :loading="remoteLoading" :clearable=true>
                <el-option v-for="employee in employees" :key="employee.id" :label="employee.name" :value="employee.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.loginName')" prop="loginName">
              <el-input v-model="inputForm.loginName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('accountForm.officeName')" prop="officeId">
              <el-select v-model="inputForm.officeId" filterable remote :placeholder="$t('accountForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.leader')" prop="leaderId">
              <el-select v-model="inputForm.leaderId" filterable remote :placeholder="$t('accountForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" :clearable=true>
                <el-option v-for="item in leaders" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.position')" prop="positionId">
              <el-select v-model="inputForm.positionId"  filterable :placeholder="$t('accountForm.selectGroup')" :clearable=true>
                <el-option v-for="position in formProperty.positionDtoList" :key="position.id" :label="position.name" :value="position.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('accountForm.dataScopeOffice')" prop="officeIdList">
              <el-select v-model="inputForm.officeIdList" multiple filterable remote :placeholder="$t('accountForm.inputWord')" :remote-method="remoteDataScopeOffice" :loading="remoteLoading" :clearable=true>
                <el-option v-for="office in dataScopeOffices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.viewReport')" prop="viewReport">
              <el-radio-group v-model="inputForm.viewReport">
                <el-radio :label=1>{{$t('accountForm.true')}}</el-radio>
                <el-radio :label=0>{{$t('accountForm.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
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
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{positionDtoList:[]},
        employees:[],
        leaders:[],
        offices:[],
        depots:[],
        dataScopeOffices:[],
        inputForm:{},
        submitData:{
          id:'',
          employeeId:'',
          loginName:'',
          officeId:"",
          depotIdList:"",
          leaderId:'',
          positionId:"",
          officeIdList:'',
          viewReport:'',
          outId:'',
          outPassword:'',
          remarks:''
        },
        rules: {
          employeeId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
          loginName: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
          officeId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
          positionId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage')}],
        },
        radio:'1',
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/basic/hr/account/save',qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'accountList',query:util.getQuery("accountList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/account/search',{params:{key:query}}).then((response)=>{
            this.leaders=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteDataScopeOffice(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/office/search',{params:{name:query}}).then((response)=>{
            this.dataScopeOffices=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteEmployee(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/employee/search',{params:{key:query}}).then((response)=>{
            this.employees=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices = response.data;
            this.remoteLoading = false;
          })
        }
      }
    },created(){
      axios.get('/api/basic/hr/account/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        this.formProperty.positionDtoList = response.data.positionDtoList;
        this.inputForm.viewReport = response.data.viewReport?1:0;
        if(response.data.officeId!=null){
          this.offices=new Array({id:response.data.officeId,name:response.data.officeName})
        }
        if(response.data.officeIdList!=null&&response.data.officeIdList.length>0){
          let officeList=new Array();
          for(var i=response.data.officeIdList.length-1;i>=0;i--){
            officeList.push({id:response.data.officeIdList[i],name:response.data.officeListName[i]})
          }
          this.dataScopeOffices=officeList;
          this.inputForm.officeIdList=response.data.officeIdList;
        }
        if(response.data.employeeId!=null){
          this.employees=new Array({id:response.data.employeeId,name:response.data.employeeName})
        }
        if(response.data.leaderId!=null){
          this.leaders=new Array({id:response.data.leaderId,loginName:response.data.leaderName})
        }
      })
    }
  }
</script>
