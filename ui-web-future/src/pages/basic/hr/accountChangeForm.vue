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
                <el-option v-for="item in inputForm.typeList" :key="item":label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountChangeForm.oldValue')" prop="oldValue">
              {{inputForm.oldValue}}
            </el-form-item>
            <el-form-item v-show="inputForm.type=='手机' ||inputForm.type=='身份证' || inputForm.type=='银行卡号' ||inputForm.type=='底薪'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <el-input v-model="inputForm.newValue" ></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='有无导购' || inputForm.type=='是否让利'" :label="$t('accountChangeForm.account')"  prop="newValue">
              <el-select v-model="inputForm.newValue"  clearable :placeholder="$t('accountChangeForm.inputKey')" >
                <el-option v-for="(value,key) in inputForm.boolMap"  :key="key" :label="value" :value="key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='部门'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable remote clearable :placeholder="$t('accountChangeForm.selectKeyShow20time')"  :remote-method="remoteOffice" :loading="remoteLoading">
                <el-option v-for="item in offices"  :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='岗位'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable :placeholder="$t('accountChangeForm.inputWord')" >
                <el-option v-for="item in inputForm.positionList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item  v-show="inputForm.type=='上级'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <el-select v-model="inputForm.newValue" filterable remote clearable :placeholder="$t('accountChangeForm.inputWord')"  :remote-method="remoteAccount" :loading="remoteLoading">
                <el-option v-for="item in accounts" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='转正'||inputForm.type=='入职'||inputForm.type=='离职'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
              <el-date-picker v-model="inputForm.newValue" type="date" align="right" :placeholder="$t('accountChangeForm.selectDateRange')"  ></el-date-picker>
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
  export default{
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
          accountId:'',
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
          });
          }else{
            this.submitDisabled = false;
      }
      })
      },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/account/search',{params:{key:query}}).then((response)=>{
            this.accounts=response.data;
            this.remoteLoading = false;
        })
        } else {
          this.accounts = [];
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
          this.remoteLoading = false;
        })
        } else {
          this.offices = [];
        }
      },getAccount(id){
        if(id){
            var type=this.inputForm.type
          axios.get('/api/basic/hr/accountChange/findData',{params: {accountId:id}}).then((response)=>{
            this.inputForm=response.data;
            this.inputForm.type=type
            if(response.data.accountId!=null){
              this.accounts=new Array({id:response.data.accountId,loginName:response.data.accountName})
            }
            this.getOldValue();
        })
        }
      },getOldValue(){
        this.inputForm.newValue = "";
        if(this.inputForm.type == "手机"){
          this.inputForm.oldValue = this.inputForm.employee.mobilePhone;
        }else if(this.inputForm.type == "身份证"){
          this.inputForm.oldValue = this.inputForm.employee.idcard;
        }else if(this.inputForm.type == "银行卡号"){
          this.inputForm.oldValue = this.inputForm.employee.bankNumber;
        }else if(this.inputForm.type == "底薪"){
          this.inputForm.oldValue = this.inputForm.employee.salary;
        }else if(this.inputForm.type == "部门"){
          this.inputForm.oldValue = this.inputForm.officeName;
        }else if(this.inputForm.type == "岗位"){
          this.inputForm.oldValue = this.inputForm.positionName;
        }else if(this.inputForm.type == "上级"){
          this.inputForm.oldValue = this.inputForm.leaderName;
        } else if(this.inputForm.type == "转正"){
          this.inputForm.oldValue = this.inputForm.employee.regularDate ;
        }else if(this.inputForm.type == "入职"){
          this.inputForm.oldValue = this.inputForm.employee.entryDate;
        }else if(this.inputForm.type == "离职"){
          this.inputForm.oldValue = this.inputForm.employee.leaveDate;
        }
      }
    },created(){
      if(!this.type){
        this.inputForm.type=this.$route.query.type;
        this.getAccount(this.$route.query.accountId);
      }
      axios.get('/api/basic/hr/accountChange/findData',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
        if(response.data.accountId!=null){
          this.accounts=new Array({id:response.data.accountId,loginName:response.data.accountName})
        }
    })
    }
  }
</script>
