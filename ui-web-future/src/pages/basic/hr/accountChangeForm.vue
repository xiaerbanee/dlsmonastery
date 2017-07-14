<template>
  <div>
    <head-tab active="accountChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('accountChangeForm.account')" prop="accountId">
              <account-select v-model="inputForm.accountId" :disabled="isDetail=='detail'||isAudit=='audit'"></account-select>
            </el-form-item>
            <div v-show="inputForm.id!=null||inputForm.accountId!=null">
              <el-form-item :label="$t('accountChangeForm.type')"  prop="type">
                <el-select :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.type" filterable clearable :placeholder="$t('accountChangeForm.selectGroup')"  @change="getOldValue">
                  <el-option v-for="item in inputForm.extra.typeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('accountChangeForm.oldValue')" prop="oldValue">
                {{inputForm.oldValue}}
              </el-form-item>
              <el-form-item v-if="inputForm.type=='手机' ||inputForm.type=='身份证' || inputForm.type=='银行卡号' ||inputForm.type=='底薪'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <el-input :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.newValue" ></el-input>
              </el-form-item>
              <el-form-item v-if="inputForm.type=='部门'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
                <office-select :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.newValue"></office-select>
              </el-form-item>
              <el-form-item v-if="inputForm.type=='岗位'" :label="$t('accountChangeForm.newValue')"  prop="newValue" >
                <el-select :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.newValue" filterable :placeholder="$t('accountChangeForm.inputWord')" >
                  <el-option v-for="item in inputForm.extra.positionList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item  v-if="inputForm.type=='上级'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <account-select  :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.newValue"></account-select>
              </el-form-item>
              <el-form-item v-if="inputForm.type=='转正'||inputForm.type=='入职'||inputForm.type=='离职'" :label="$t('accountChangeForm.newValue')"  prop="newValue">
                <date-picker  :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.newValue"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('accountChangeForm.remarks')"  prop="remarks">
                <el-input  :disabled="isDetail=='detail'||isAudit=='audit'" v-model="inputForm.remarks"></el-input>
              </el-form-item>
              <el-form-item v-if="isAudit=='audit'" :label="$t('accountChangeForm.isPass')"  prop="pass">
                <el-switch v-model="pass" on-color="#13ce66" off-color="#ff4949">
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-button v-if="isDetail!='detail'&&isAudit!='audit'" type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('accountChangeForm.save')}}</el-button>
              </el-form-item>
              <el-form-item>
                <el-button v-if="isAudit=='audit'" type="primary"  :disabled="submitDisabled" @click="formAudit()">{{$t('accountChangeForm.audit')}}</el-button>
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
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isInit:false,
          isDetail:this.$route.query.action,
          isAudit:this.$route.query.action,
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          accounts:[],
          offices:[],
          inputForm:{
              extra:[],
          },
          pass:true,
          rules: {
            accountId: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
            type: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
            remarks: [{ required: true, message: this.$t('accountChangeForm.prerequisiteMessage')}],
          },
          remoteLoading:false,

        }
      },
      formSubmit(){
         var that=this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/accountChange/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              this.$router.push({name:'accountChangeList',query:util.getQuery("accountChangeList")})
          }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
      }
      })
      },formAudit(){
          axios.get('api/basic/hr/accountChange/audit',{params:{id:this.$route.query.id,pass:this.pass}}).then((response)=>{
            console.log(response.data)
            this.$message(response.data.message);
            this.$router.push({name: 'accountChangeList', query: util.getQuery("accountChangeList"),params:{_closeFrom:true}})
        })
      },getAccount(accountId){
        var type=this.inputForm.type;
        axios.get('/api/basic/hr/accountChange/findData',{params: {type:type,accountId:accountId,id:this.$route.query.id}}).then((response)=>{
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
        console.log(this.inputForm.newValue)
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
          this.inputForm.oldValue = "";
        }
      }
    },created () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
        this.inputForm.type=this.$route.query.type;
        this.inputForm.id=this.$route.query.id;
        this.getAccount(this.$route.query.accountId);
      }
      this.isInit = true;
    }
  }
</script>
