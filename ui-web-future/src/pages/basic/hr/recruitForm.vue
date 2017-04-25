<template>
  <div>
    <head-tab active="recruitForm"></head-tab>
    <el-row :gutter="20">
      <el-steps :space="100" :active="active" finish-status="success">
        <el-step :title="$t('recruitForm.reserve')"></el-step>
        <el-step :title="$t('recruitForm.initialTest')"></el-step>
        <el-step :title="$t('recruitForm.retest')"></el-step>
        <el-step :title="$t('recruitForm.auditStatus')"></el-step>
        <el-step :title="$t('recruitForm.entryJob')"></el-step>
        <el-button  @click="next">{{$t('recruitForm.next')}}</el-button>
      </el-steps>
    </el-row>
    <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
    <el-row :gutter="20">
      <!--预约开始-->
      <div class="form input-form" style="margin-top: 20px" v-if="active==1">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px">
          <el-form-item  :label="$t('recruitForm.contactDate')" prop="contactDate">
            <el-date-picker  v-model="inputForm.contactDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.name')" prop="name">
            <el-input v-model="inputForm.name"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.name')" prop="sex">
            <el-radio-group v-model="inputForm.sex">
              <el-radio :label="$t('recruitForm.man')" value="男">{{$t('recruitForm.man')}}</el-radio>
              <el-radio :label="$t('recruitForm.women')" value="女">{{$t('recruitForm.women')}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.mobilePhone')" prop="mobilePhone">
            <el-input v-model="inputForm.mobilePhone" @change="onChange"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.applyPositionId')" prop="applyPositionName">
            <el-input v-model="inputForm.applyPositionName"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.applyFrom')" prop="applyFrom">
            <el-input v-model="inputForm.applyFrom"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.firstAppointDate')" prop="firstAppointDate">
            <el-date-picker  v-model="inputForm.firstAppointDate" type="datetime" align="left" :placeholder="$t('recruitForm.selectFirstAppointDate')"></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.contactById')" prop="contactById">
            <el-select v-model="inputForm.contactById" filterable remote :placeholder="$t('recruitForm.selectContactById')" :remote-method="remoteAccount" :loading="remoteLoading" :clearable=true>
              <el-option v-for="item in accounts" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')" prop="remarks">
            <el-input v-model="inputForm.remarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--预约结束-->
      <!--初始开始-->
      <div style="margin-top: 20px"  v-if="active==2">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.firstRealDate')" prop="firstRealDate">
            <el-date-picker  v-model="inputForm.firstRealDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.workArea')" prop="workArea">
            <el-input v-model="inputForm.workArea"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.workCategory')" prop="workCategory">
            <el-input v-model="inputForm.workCategory"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.marriageStatus')" prop="marriageStatus" >
            <el-select v-model="inputForm.marriageStatus"  clearable >
            <el-option v-for="item in marriageStatus"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.birthday')" prop="birthday">
            <el-date-picker  v-model="inputForm.birthday" type="date" align="left"  format="yyyy-MM-dd" ></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.originSelectId')" prop="originSelectId">
            <el-select v-model="inputForm.originSelectId"  clearable filterable remote :placeholder="$t('recruitForm.inputWord')" :remote-method="remoteDistrict" :loading="remoteLoading">
              <el-option v-for="item in origins"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.school')" prop="school">
            <el-input v-model="inputForm.school"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.major')" prop="major">
            <el-input v-model="inputForm.major"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.education')" prop="education">
            <el-select v-model="inputForm.education" >
              <el-option v-for="item in formProperty.educationsList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.firstBy')"  prop="firstBy">
            <el-select v-model="inputForm.firstBy" filterable remote clearable :placeholder="$t('recruitForm.selectFirstBy')" :remote-method="remoteAccount" :loading="remoteLoading">
              <el-option v-for="item in accounts" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.firstPoint')" prop="firstPoint">
            <el-input v-model="inputForm.firstPoint"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.firstComment')" prop="firstComment">
            <el-input v-model="inputForm.firstComment"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.secondAppointDate')" prop="secondAppointDate">
            <el-date-picker  v-model="inputForm.secondAppointDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--初试结束-->
      <!--复试开始-->
      <div  style="margin-top: 20px"  v-if="active==3">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item  :label="$t('recruitForm.secondRealDate')" prop="secondRealDate">
            <el-date-picker  v-model="inputForm.secondRealDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.secondBy')" prop="secondBy">
            <el-select v-model="inputForm.secondBy" filterable remote clearable :placeholder="$t('recruitForm.selectSecondBy')" :remote-method="remoteAccount" :loading="remoteLoading">
              <el-option v-for="item in accounts" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.secondComment')"  prop="secondComment">
            <el-input v-model="inputForm.secondComment"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.toStorage')" prop="toStorage">
            <el-radio-group v-model="inputForm.toStorage">
              <el-radio :label="1">{{$t('recruitForm.true')}}</el-radio>
              <el-radio :label="0">{{$t('recruitForm.false')}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.storageRemarks')" prop="storageRemarks">
            <el-input v-model="inputForm.storageRemarks"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.physicalAppointDate')" prop="physicalAppointDate">
            <el-date-picker  v-model="inputForm.physicalAppointDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--复试结束-->
      <!--资审开始-->
      <div  style="margin-top: 20px"  v-if="active==4">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.physicalRealDate')" prop="physicalRealDate">
            <el-date-picker  v-model="inputForm.physicalRealDate" type="datetime" align="left"  ></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.auditAppointDate')" prop="auditAppointDate">
            <el-date-picker  v-model="inputForm.auditAppointDate" type="datetime" align="left"  ></el-date-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.auditRealDate')" prop="auditRealDate">
            <el-date-picker  v-model="inputForm.auditRealDate" type="datetime" align="left"  ></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.entryAppointDate')" prop="entryAppointDate">
            <el-date-picker  v-model="inputForm.entryAppointDate" type="datetime" align="left"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--资审结束-->
      <!--资审开始-->
      <div style="margin-top: 20px"  v-if="active==5">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.entryRealDate')" prop="entryRealDate">
            <el-date-picker  v-model="inputForm.entryRealDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.idCard')"  prop="idcard">
            <el-input v-model="inputForm.idcard"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--资审结束-->
    </el-row>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            remoteLoading:false,
            active:1,
            accounts:[],
            selectedAccount:[],
            origins:[],
            marriageStatus:[this.$t('recruitForm.unmarriedAndChildless'),this.$t('recruitForm.marriedChildless'),this.$t('recruitForm.marriedAlready'),this.$t('recruitForm.other')],
            message:'',
            inputForm:{
              id:this.$route.query.id,
              contactDate:'',
              name:'',
              sex:'',
              mobilePhone:'',
              applyPositionName:'',
              applyFrom:'',
              firstAppointDate:'',
              contactById:'',
              remarks:'',

              firstRealDate:'',
              workArea:'',
              workCategory:'',
              marriageStatus:'',
              birthday:'',
              originSelectId:'',
              school:'',
              major:'',
              education:'',
              firstBy:'',
              firstPoint:'',
              firstComment:'',
              secondAppointDate:'',

              secondRealDate:'',
              secondBy:'',
              secondComment:'',
              toStorage:'',
              storageRemarks:'',
              physicalAppointDate:'',

              physicalRealDate:'',
              auditAppointDate:'',
              auditRealDate:'',
              entryAppointDate:'',

              entryRealDate:'',
              idcard:''

            },
            rules: {
            },
        };
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.birthday=util.formatLocalDate(this.inputForm.birthday);
              this.inputForm.contactDate=util.formatLocalDateTime(this.inputForm.contactDate);
              this.inputForm.firstAppointDate=util.formatLocalDateTime(this.inputForm.firstAppointDate);
              this.inputForm.firstRealDate=util.formatLocalDateTime(this.inputForm.firstRealDate);
              this.inputForm.secondAppointDate=util.formatLocalDateTime(this.inputForm.secondAppointDate);
              this.inputForm.secondRealDate=util.formatLocalDateTime(this.inputForm.secondRealDate);
              this.inputForm.physicalAppointDate=util.formatLocalDateTime(this.inputForm.physicalAppointDate);
              this.inputForm.physicalRealDate=util.formatLocalDateTime(this.inputForm.physicalRealDate);
              this.inputForm.auditAppointDate=util.formatLocalDateTime(this.inputForm.auditAppointDate);
              this.inputForm.auditRealDate=util.formatLocalDateTime(this.inputForm.auditRealDate);
              this.inputForm.entryAppointDate=util.formatLocalDateTime(this.inputForm.entryAppointDate);
              this.inputForm.entryRealDate=util.formatLocalDateTime(this.inputForm.entryRealDate);
              axios.post('/api/basic/hr/recruit/save', qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'recruitList',query:util.getQuery("recruitList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },next() {
          this.active = this.active+1;
          if(this.active ==6){
            this.active = 1;
          }
        },remoteAccount(query) {
          if (query !== '') {
            this.remoteLoading = true;
              axios.get('/api/basic/hr/account/search',{params:{key:query}}).then((response)=>{
                var dataMap = new Map();
                response.data.map((v,index)=>{
                  dataMap.set(response.data[index].id,response.data[index]);
                });
                this.accounts.map((v,index)=>{
                  dataMap.set(this.accounts[index].id,this.accounts[index]);
                });
                this.accounts = new Array();
                for(let value of dataMap.values()){
                  this.accounts.push(value);
                }
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
        },onChange(){
          this.message="";
          if(this.inputForm.mobilePhone.length>=11){
            axios.get('/api/basic/hr/recruit/checkMobilePhone',{params:{mobilePhone:this.inputForm.mobilePhone}}).then((response)=>{
              if(!response.data.success){
                this.message=response.data.message;
                this.submitDisabled = true;
              }
            })
          }
        }
      },created(){
        axios.get('/api/basic/hr/recruit/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/basic/hr/recruit/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
