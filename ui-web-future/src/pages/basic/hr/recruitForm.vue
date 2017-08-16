<template>
  <div>
    <head-tab active="recruitForm"></head-tab>
    <el-form>
      <el-row :gutter="24">
        <el-col :span="6">
          <el-form-item :label="$t('recruitForm.name')" v-if="inputForm.active>1&&inputForm.name">{{inputForm.name}}</el-form-item>
          <el-form-item :label="$t('recruitForm.sex')" v-if="inputForm.active>1&&inputForm.sex">{{inputForm.sex}}</el-form-item>
          <el-form-item :label="$t('recruitForm.mobilePhone')" v-if="inputForm.active>1&&inputForm.mobilePhone">{{inputForm.mobilePhone}}</el-form-item>
          <el-form-item :label="$t('recruitForm.applyPositionId')" v-if="inputForm.active>1&&inputForm.applyPositionId">{{inputForm.applyPositionId}}</el-form-item>
          <el-form-item :label="$t('recruitForm.applyFrom')" v-if="inputForm.active>1&&inputForm.applyForm">{{inputForm.applyFrom}}</el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('recruitForm.workArea')" v-if="inputForm.active>2&&inputForm.workArea">{{inputForm.workArea}}</el-form-item>
          <el-form-item :label="$t('recruitForm.workCategory')" v-if="inputForm.active>2&&inputForm.workCategory">{{inputForm.workCategory}}</el-form-item>
          <el-form-item :label="$t('recruitForm.marriageStatus')" v-if="inputForm.active>2&&inputForm.marriageStatus">{{inputForm.marriageStatus}}</el-form-item>
          <el-form-item :label="$t('recruitForm.major')" v-if="inputForm.active>2&&inputForm.major">{{inputForm.major}}</el-form-item>
          <el-form-item :label="$t('recruitForm.education')" v-if="inputForm.active>2&&inputForm.education">{{inputForm.education}}</el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('recruitForm.secondComment')" v-if="inputForm.active>3&&inputForm.secondComment">{{inputForm.secondComment}}</el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-row :gutter="20">
      <el-steps :space="100" :active="inputForm.active" finish-status="success">
        <el-step :title="$t('recruitForm.reserve')"></el-step>
        <el-step :title="$t('recruitForm.initialTest')"></el-step>
        <el-step :title="$t('recruitForm.retest')"></el-step>
        <el-step :title="$t('recruitForm.auditStatus')"></el-step>
        <el-step :title="$t('recruitForm.entryJob')"></el-step>
        <el-step :title="$t('recruitForm.anaphaseTracking')"></el-step>
        <el-button  @click="next">{{$t('recruitForm.next')}}</el-button>
        <el-button  @click="privious">{{$t('recruitForm.privious')}}</el-button>
      </el-steps>
    </el-row>
    <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
    <el-row :gutter="20">
      <!--预约开始-->
      <div class="form input-form" style="margin-top: 20px" v-if="inputForm.active==0">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px">
          <el-form-item  :label="$t('recruitForm.inviteDate')" prop="contactDate">
            <date-picker v-model="inputForm.inviteDate"></date-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.name')" prop="name">
            <el-input v-model="inputForm.name"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.sex')" prop="sex">
            <el-radio-group v-model="inputForm.sex">
              <el-radio :label="$t('recruitForm.man')" value="男">{{$t('recruitForm.man')}}</el-radio>
              <el-radio :label="$t('recruitForm.women')" value="女">{{$t('recruitForm.women')}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.mobilePhone')" prop="mobilePhone">
            <el-input v-model="inputForm.mobilePhone"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.applyPositionId')" prop="applyPositionName">
            <el-select v-model="inputForm.applyPositionId">
              <el-option v-for="item in inputForm.extra.applyPositionList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.applyFrom')" prop="applyFrom">
            <el-select v-model="inputForm.applyFrom">
              <el-option v-for="item in inputForm.extra.applyFromList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.contactById')" prop="registerBy">
            <el-select v-model="inputForm.firstAppointBy"  clearable >
               <el-option v-for="item in inputForm.extra.firstAppointByList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.firstAppointDate')" prop="firstAppointDate">
            <date-time-picker v-model="inputForm.firstAppointDate"></date-time-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')" prop="registerRemarks">
            <el-input v-model="inputForm.registerRemarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--预约结束-->
      <!--初始开始-->
      <div style="margin-top: 20px"  v-if="inputForm.active==1">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-row :gutter="20">
            <el-col :span="10">
              <el-form-item  :label="$t('recruitForm.firstAppoint')" prop="firstAppoint">
                <bool-select v-model="inputForm.firstAppoint"></bool-select>
              </el-form-item>
          <el-form-item :label="$t('recruitForm.firstRealDate')" prop="firstRealDate">
            <date-time-picker v-model="inputForm.firstRealDate"></date-time-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.firstAppointBy')"  prop="firstAppointBy">
            <el-select v-model="inputForm.firstAppointBy"  clearable >
              <el-option v-for="item in inputForm.extra.firstAppointByList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.workArea')" prop="workArea">
            <el-input v-model="inputForm.workArea"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.workCategory')" prop="workCategory">
            <el-select v-model="inputForm.workCategory"  clearable >
              <el-option v-for="item in inputForm.extra.workCategoryList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>          </el-form-item>
          <el-form-item :label="$t('recruitForm.marriageStatus')" prop="marriageStatus" >
            <el-select v-model="inputForm.marriageStatus"  clearable >
            <el-option v-for="item in inputForm.extra.marriageStatusList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.birthday')" prop="birthday">
            <date-picker v-model="inputForm.birthday" ></date-picker>
          </el-form-item>
            </el-col>
            <el-col :span="10">
          <el-form-item :label="$t('recruitForm.originId')" prop="originId">
            <district-select v-model="inputForm.originId"></district-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.school')" prop="school">
            <el-input v-model="inputForm.school"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.major')" prop="major">
            <el-input v-model="inputForm.major"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.education')" prop="education">
            <el-select v-model="inputForm.education" >
              <el-option v-for="item in inputForm.extra.educationsList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.firstComment')" prop="firstComment">
            <el-input v-model="inputForm.firstComment"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.mainCompany')" prop="mainCompany">
            <el-input v-model="inputForm.mainCompany"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')" prop="firstAppointRemarks">
            <el-input v-model="inputForm.firstAppointRemarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <!--初试结束-->
      <!--复试开始-->
      <div  style="margin-top: 20px"  v-if="inputForm.active==2">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item  :label="$t('recruitForm.secondAppoint')" prop="secondAppoint">
            <bool-select v-model="inputForm.secondAppoint"></bool-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.secondRealDate')" prop="secondRealDate">
            <date-time-picker v-model="inputForm.secondRealDate"></date-time-picker>
          </el-form-item>
          <el-form-item  :label="$t('recruitForm.secondAppointBy')" prop="secondAppointBy">
            <el-select v-model="inputForm.secondAppointBy"  clearable >
              <el-option v-for="item in inputForm.extra.secondAppointByList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
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
          <el-form-item :label="$t('recruitForm.remarks')" prop="storageRemarks">
            <el-input v-model="inputForm.storageRemarks"></el-input>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.physicalAppointDate')" prop="physicalAppointDate">
            <date-time-picker v-model="inputForm.physicalAppointDate"></date-time-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--复试结束-->
      <!--资审开始-->
      <div  style="margin-top: 20px"  v-if="inputForm.active==3">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.auditRealDate')" prop="auditAppointDate">
            <date-picker v-model="inputForm.auditRealDate"></date-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.isExamination ')" prop="isExamination">
            <bool-select v-model="inputForm.isExamination"></bool-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')" prop="auditAppointRemarks">
            <el-input v-model="inputForm.auditAppointRemarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--资审结束-->
      <!--资审开始-->
      <div style="margin-top: 20px"  v-if="inputForm.active==4">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.entryRealDate')" prop="entryRealDate">
            <date-time-picker v-model="inputForm.entryRealDate"></date-time-picker>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.isPosition')"  prop="isPosition">
            <bool-select v-model="inputForm.isPosition"></bool-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')" prop="entryAppointRemarks">
            <el-input v-model="inputForm.entryAppointRemarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--资审结束-->
      <!--后期跟踪开始-->
      <div style="margin-top: 20px" v-if="inputForm.active==5">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item :label="$t('recruitForm.onJob')"  prop="twoMonth" >
            <bool-select v-model="inputForm.onJob"></bool-select>
          </el-form-item>
          <el-form-item :label="$t('recruitForm.remarks')"  prop="leaveJobRemarks" >
            <el-input v-model="inputForm.leaveJobRemarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitForm.save')}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--后期跟踪结束-->
    </el-row>
  </div>
</template>
<script>
  import dateTimePicker from "components/common/date-time-picker.vue"
  import accountSelect from 'components/basic/account-select'
  import districtSelect from 'components/general/district-select'
  import positionSelect from 'components/basic/position-select'
  import boolSelect from 'components/common/bool-select'
  export default{
      components:{
          dateTimePicker,
          accountSelect,
          districtSelect,
          positionSelect,
          boolSelect
      },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          var checkPhone=(rule,value,callback)=>{
            if(value===''){
              callback(new Error(this.$t('officeForm.prerequisiteMessage')))
            }else if(!(/^1[34578]\d{9}$/.test(value))){
              callback(new Error("手机号码有误，请重填"))
            }else{
              callback();
            }
          }
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            remoteLoading:false,
            message:'',
            inputForm:{
              extra:{}
            },
            rules: {
              mobilePhone:{request:true,validator:checkPhone,trigger:'blur'}
            },
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var submitData = util.deleteExtra(this.inputForm);
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/basic/hr/recruit/save', qs.stringify(submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(!this.isCreate){
                  this.$router.push({name:'recruitList',query:util.getQuery("recruitList")})
                }
              }).catch( ()=> {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },next() {
          this.inputForm.active = this.inputForm.active+1;
          if(this.inputForm.active ==7){
            this.inputForm.active = 6;
          }
        },privious(){
          this.inputForm.active = this.inputForm.active-1;
          console.log(this.inputForm.active)
          if(this.inputForm.active ==-1){
            this.inputForm.active = 0;
          }
        },
        initPage(){
          axios.get('/api/basic/hr/recruit/getForm').then((response)=>{
            console.log(response.data)
            this.inputForm=response.data;
            if(!this.isCreate){
              axios.get('/api/basic/hr/recruit/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
                util.copyValue(response.data,this.inputForm);
              })
            }
          });
        }
      },created () {
          this.initPage();
      }
    }
</script>
