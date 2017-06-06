<template>
  <div>
    <head-tab active="accountChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('accountChangeForm.account')" prop="accountId">
              <el-select :disabled="isDetail" v-model="inputForm.accountId" filterable remote clearable :placeholder="$t('accountChangeForm.inputWord')"  :remote-method="remoteAccount" :loading="remoteLoading" @change="getAccount(inputForm.accountId)">
                <el-option v-for="item in accounts" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <div v-show="inputForm.id!=null||inputForm.accountId!=null">
              <el-form-item :label="$t('accountChangeForm.type')"  prop="type">
                <el-select :disabled="isDetail" v-model="inputForm.type" filterable clearable :placeholder="$t('accountChangeForm.selectGroup')"  @change="getOldValue">
                  <el-option v-for="item in inputForm.typeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('accountChangeForm.oldValue')" prop="oldValue">
                {{inputForm.oldValue}}
              </el-form-item>
              <el-form-item v-show="inputForm.type=='手机' ||inputForm.type=='身份证' || inputForm.type=='银行卡号' ||inputForm.type=='底薪'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <el-input :disabled="isDetail" v-model="inputForm.newValue" ></el-input>
              </el-form-item>
              <el-form-item v-show="inputForm.type=='部门'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
                <office-select :disabled="isDetail" v-model="inputForm.newValue"></office-select>
              </el-form-item>
              <el-form-item v-show="inputForm.type=='岗位'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
                <el-select :disabled="isDetail" v-model="inputForm.newValue" filterable :placeholder="$t('accountChangeForm.inputWord')" >
                  <el-option v-for="item in inputForm.positionList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item  v-show="inputForm.type=='上级'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <account-select  :disabled="isDetail" v-model="inputForm.newValue"></account-select>
              </el-form-item>
              <el-form-item v-show="inputForm.type=='转正'||inputForm.type=='入职'||inputForm.type=='离职'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <date-picker  :disabled="isDetail" v-model="inputForm.newValue"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('accountChangeForm.remarks')"  prop="remarks">
                <el-input  :disabled="isDetail" v-model="inputForm.remarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button v-if="!isDetail" type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('accountChangeForm.save')}}</el-button>
              </el-form-item>
            </div>
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
        isDetail:this.$route.query.action=="detail",
        isCreate:this.$route.query.id==null,
        type:this.$route.query.type==null,
        submitDisabled:false,
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
         var that=this;
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
            form.resetFields();
            this.submitDisabled = false;
            if(!this.isCreate){
              this.$router.push({name:'accountChangeList',query:util.getQuery("accountChangeList")})
            }
          }).catch(function () {
              that.submitDisabled = false;
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
      },getAccount(accountId){
        axios.get('/api/basic/hr/accountChange/findData',{params: {accountId:accountId,id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          if(response.data.accountId!=null){
            this.accounts=new Array({id:response.data.accountId,loginName:response.data.accountName})
          }
          this.getOldValue();
        })
      },getOldValue(){
        if (!this.isDetail) {
          this.inputForm.newValue = "";
        }
        if(this.inputForm.type == "手机"){
          this.inputForm.oldValue = this.inputForm.mobilePhone;
        }else if(this.inputForm.type == "身份证"){
          this.inputForm.oldValue = this.inputForm.idcard;
        }else if(this.inputForm.type == "银行卡号"){
          this.inputForm.oldValue = this.inputForm.bankNumber;
        }else if(this.inputForm.type == "底薪"){
          this.inputForm.oldValue = this.inputForm.salary;
        }else if(this.inputForm.type == "部门"){
          this.inputForm.oldValue = this.inputForm.officeName;
        }else if(this.inputForm.type == "岗位"){
          this.inputForm.oldValue = this.inputForm.positionName;
        }else if(this.inputForm.type == "上级"){
          this.inputForm.oldValue = this.inputForm.leaderName;
        } else if(this.inputForm.type == "转正"){
          this.inputForm.oldValue = this.inputForm.regularDate ;
        }else if(this.inputForm.type == "入职"){
          this.inputForm.oldValue = this.inputForm.entryDate;
        }else if(this.inputForm.type == "离职"){
          this.inputForm.oldValue = this.inputForm.leaveDate;
        }else {
          this.inputForm.oldValue ="";
        }
      },initPage() {
        if(!this.type){
          this.inputForm.type=this.$route.query.type;
        }else {
          this.inputForm.id=this.$route.query.id;
        }
        this.getAccount(this.$route.query.accountId);
      }
    },created () {
      this.initPage();
    }
  }
</script>
