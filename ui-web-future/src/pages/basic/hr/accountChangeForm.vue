<template>
  <div>
    <head-tab active="accountChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('accountChangeForm.account')" prop="accountId">
              <el-select v-model="inputForm.accountId" filterable remote clearable :placeholder="$t('accountChangeForm.inputWord')"  :remote-method="remoteAccount" :loading="remoteLoading" @change="getAccount(inputForm.accountId)">
                <el-option v-for="item in accounts" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountChangeForm.type')"  prop="type">
              <el-select v-model="inputForm.type" filterable clearable :placeholder="$t('accountChangeForm.selectGroup')"  @change="getOldValue">
                <el-option v-for="item in inputForm.typeList" :key="item" :label="$t('AccountChangeTypeEnum.'+item)" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountChangeForm.oldValue')" prop="oldValue">
              {{inputForm.oldValue}}
            </el-form-item>
            <el-form-item v-show="inputForm.type=='MOBILE_PHONE' ||inputForm.type=='ID_CARD' || inputForm.type=='BANK_CARD' ||inputForm.type=='BASE_SALARY'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <el-input v-model="inputForm.newValue" ></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='OFFICE'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <office-select v-model="inputForm.officeId"></office-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='POSITION'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable :placeholder="$t('accountChangeForm.inputWord')" >
                <el-option v-for="item in inputForm.positionList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item  v-show="inputForm.type=='LEADER'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <account-select v-model="inputForm.newValue"></account-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='REGULAR_WORKER'||inputForm.type=='ENTRY_WORKER'||inputForm.type=='LEAVE_WORKER'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <date-picker v-model="inputForm.newValue"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('accountChangeForm.remarks')"  prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('accountChangeForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import accountSelect from 'components/basic/account-select'
  import officeSelect from 'components/basic/office-select'
  export default{
      components:{
          accountSelect,
          officeSelect
      },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        type:this.$route.query.type==null,
        submitDisabled:false,
        employee:{},
        account:{},
        accounts:[],
        offices:[],
        inputForm:{},
        submitData:{
          id:'',
          accountId:"",
          type:'',
          oldValue:'',
          newValue:'',
          remarks:''
        },
        rules: {
          accountId: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
          remarks: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
        },
        remoteLoading:false,

      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        this.inputForm.expiryDate=util.formatLocalDate( this.inputForm.expiryDate)
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/basic/hr/accountChange/save', qs.stringify(this.submitData)).then((response)=> {
              if(response.data.message){
              this.$message(response.data.message);
            }
            if(this.isCreate){
              form.resetFields();
              this.submitDisabled = false;
            } else {
              this.$router.push({name:'accountChangeList',query:util.getQuery("accountChangeList")})
            }
          }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
      }
      })
      },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/account/searchFilter',{params:{loginName:query}}).then((response)=>{
            this.accounts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.accounts = [];
        }
      },getAccount(id){
        if(id){
            var type=this.inputForm.type;
          axios.get('/api/basic/hr/accountChange/findData',{params: {accountId:id}}).then((response)=>{
            this.inputForm=response.data;
            this.inputForm.type=type;
            if(response.data.accountId!=null){
              this.accounts=new Array({id:response.data.accountId,loginName:response.data.accountName})
            }
            this.getOldValue();
        })
        }
      },getOldValue(){
        this.inputForm.newValue = "";
        if(this.inputForm.type == "MOBILE_PHONE"){
          this.inputForm.oldValue = this.inputForm.employee.mobilePhone;
        }else if(this.inputForm.type == "ID_CARD"){
          this.inputForm.oldValue = this.inputForm.employee.idcard;
        }else if(this.inputForm.type == "BANK_CARD"){
          this.inputForm.oldValue = this.inputForm.employee.bankNumber;
        }else if(this.inputForm.type == "BASE_SALARY"){
          this.inputForm.oldValue = this.inputForm.employee.salary;
        }else if(this.inputForm.type == "OFFICE"){
          this.inputForm.oldValue = this.inputForm.officeName;
        }else if(this.inputForm.type == "POSITION"){
          this.inputForm.oldValue = this.inputForm.positionName;
        }else if(this.inputForm.type == "LEADER"){
          this.inputForm.oldValue = this.inputForm.leaderName;
        } else if(this.inputForm.type == "REGULAR_WORKER"){
          this.inputForm.oldValue = this.inputForm.employee.regularDate ;
        }else if(this.inputForm.type == "ENTRY_WORKER"){
          this.inputForm.oldValue = this.inputForm.employee.entryDate;
        }else if(this.inputForm.type == "LEAVE_WORKER"){
          this.inputForm.oldValue = this.inputForm.employee.leaveDate;
        }else {
          this.inputForm.oldValue ="";
        }
      }
    },created(){
      if(!this.type){
        this.inputForm.type=this.$route.query.type;
        this.getAccount(this.$route.query.accountId);
      }
      axios.get('/api/basic/hr/accountChange/findData',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
    })
    }
  }
</script>
