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
              <office-select v-model="inputForm.officeId" :multiple="multiple"></office-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.leader')" prop="leaderId">
              <account-select v-model="inputForm.leaderId"></account-select>
            </el-form-item>
            <el-form-item :label="$t('accountForm.position')" prop="positionId">
              <el-select v-model="inputForm.positionId"  filterable :placeholder="$t('accountForm.selectGroup')" :clearable=true>
                <el-option v-for="position in formProperty.positionDtoList" :key="position.id" :label="position.name" :value="position.id"></el-option>
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
      var checkLoginName=(rule, value, callback)=>{
        if (!value) {
          return callback(new Error('必填信息'));
        }else {
            axios.get('/api/basic/hr/account/checkLoginName?loginName='+value+"&id="+this.$route.query.id).then((response)=>{
          return callback(new Error(response.data.message));
        })
        }
      };
      return{
        isCreate:this.$route.query.id==null,
        multiple:true,
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
          loginName: [{ required: true,validator: checkLoginName,trigger: 'blur'}],
          officeId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage'),trigger: 'blur'}],
          positionId: [{ required: true, message: this.$t('accountForm.prerequisiteMessage'),trigger: 'blur'}],
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
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/basic/hr/account/getFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
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
