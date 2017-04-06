<template>
  <div>
    <head-tab :active="$t('accountChangeForm.accountChangeForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('accountChangeForm.account')" prop="accountId">
              <el-select v-model="inputForm.accountId" filterable remote clearable :placeholder="$t('accountChangeForm.inputWord')"  :remote-method="remoteAccount" :loading="remoteLoading" @change="getAccount(inputForm.accountId)">
                <el-option v-for="item in accounts" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('accountChangeForm.type')"  prop="type">
              <el-select v-model="inputForm.type" filterable clearable :placeholder="$t('accountChangeForm.selectGroup')"  @change="getOldValue">
                <el-option v-for="item in formProperty.types" :key="item":label="item" :value="item"></el-option>
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
                <el-option v-for="(value,key) in formProperty.bools"  :key="key" :label="value | bool2str" :value="key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='部门'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable remote clearable :placeholder="$t('accountChangeForm.selectKeyShow20time')"  :remote-method="remoteOffice" :loading="remoteLoading">
                <el-option v-for="item in offices"  :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='岗位'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable :placeholder="$t('accountChangeForm.inputWord')" >
                <el-option v-for="item in formProperty.positions"  :key="item.id" :label="item.name" :value="item.id"></el-option>
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
        formProperty:{},
        employee:{},
        account:{},
        accounts:[],
        offices:[],
        inputForm:{
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
            axios.post('/api/hr/accountChange/save', qs.stringify(this.inputForm)).then((response)=> {
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
          axios.get('/api/hr/account/search',{params:{key:query}}).then((response)=>{
            this.accounts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.accounts = [];
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.offices = [];
        }
      },getAccount(id){
        if(id){
          axios.get('/api/hr/account/findOne',{params: {id:id}}).then((response)=>{
            this.employee = response.data.employee;
            this.account=response.data;
            this.accounts = new Array(response.data);
            this.inputForm.accountId=response.data.id;
              this.getOldValue();
          })
        }
      },getOldValue(){
          this.inputForm.newValue = "";
            if(this.inputForm.type == "手机"){
              this.inputForm.oldValue = this.employee.mobilePhone;
            }else if(this.inputForm.type == "身份证"){
              this.inputForm.oldValue = this.employee.idcard;
            }else if(this.inputForm.type == "银行卡号"){
              this.inputForm.oldValue = this.employee.bankNumber;
            }else if(this.inputForm.type == "底薪"){
              this.inputForm.oldValue = this.employee.salary;
            }else if(this.inputForm.type == "部门"){
              this.inputForm.oldValue = this.account.office.name;
            }else if(this.inputForm.type == "岗位"){
              this.inputForm.oldValue = this.account.position.name;
            }else if(this.inputForm.type == "上级"){
              this.inputForm.oldValue = this.account.leader.fullName;
            } else if(this.inputForm.type == "转正"){
              this.inputForm.oldValue = this.employee.regularDate ;
            }else if(this.inputForm.type == "入职"){
              this.inputForm.oldValue = this.employee.entryDate;
            }else if(this.inputForm.type == "离职"){
              this.inputForm.oldValue = this.employee.leaveDate;
            }
      },findOne(){
        axios.get('/api/hr/accountChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          if(response.data.type){
            this.inputForm.type=response.data.type;
            this.getAccount(response.data.accountId);
          }
          this.accounts = new Array(response.data.account);
          this.inputForm.accountId=response.data.account.id;
        })
      },getFormProperty(){
        axios.get('/api/hr/accountChange/getFormProperty').then((response)=>{
          this.formProperty = response.data;
        });
      }
    },created(){
        this.getFormProperty();
      if(!this.type){
        this.inputForm.type=this.$route.query.type;
        this.getAccount(this.$route.query.accountId);
      }
      if(!this.isCreate){

          this.findOne();
      }
    }
  }
</script>
