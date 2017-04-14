<template>
  <div>
    <head-tab :active="$t('dutyTaskForm.dutyTaskForm')"></head-tab>
    <el-row :gutter="20" style="margin-bottom:10px">
      <el-col :span="4"><div class="grid-content bg-purple">{{$t('dutyTaskForm.dutyTaskType')}}<span>&nbsp;&nbsp;</span><span style="color:red">{{dutyType}}</span></div></el-col>
    </el-row>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20" />
        <el-row :gutter="20" v-if="dutyType === '请假'">
          <el-form-item :label="$t('dutyTaskForm.applyAccount')" prop="fullName">
            <el-input v-model="showForm.created.fullName" disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.leaveType')" prop="leaveType">
            <el-input v-model="showForm.leaveType"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dutyLeaveDate')" prop="dutyDate">
            <el-input v-model="showForm.dutyDate"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dateLeaveType')" prop="dateType">
            <el-input v-model="showForm.dateType"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
            <el-input v-model="showForm.remarks"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="dutyType === '签到'">
          <el-form-item :label="$t('dutyTaskForm.applyAccount')" prop="fullName">
            <el-input v-model="showForm.created.fullName"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dutySignDate')" prop="dutyDate">
            <el-input v-model="showForm.dutyDate"disabled ></el-input>
          </el-form-item>
          <el-form-item  :label="$t('dutyTaskForm.dutySignTime')" prop="dutyTime">
            <el-input v-model="showForm.dutyTime"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.signAddress')" prop="address">
            <el-input v-model="showForm.address"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.attachment')" prop="attachment">
            <el-input v-model="showForm.attachment"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
            <el-input v-model="showForm.remarks"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="dutyType === '补卡'">
          <el-form-item  :label="$t('dutyTaskForm.applyAccount')" prop="fullName">
            <el-input v-model="showForm.created.fullName"disabled ></el-input>
          </el-form-item>
          <el-form-item  :label="$t('dutyTaskForm.dutyRepairDate')" prop="dutyDate">
            <el-input v-model="showForm.dutyDate"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dutyRepairTime')" prop="dutyTime">
            <el-input v-model="showForm.dutyTime"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="dutyType === '加班'">
          <el-form-item :label="$t('dutyTaskForm.applyAccount')"  prop="fullName">
            <el-input v-model="showForm.created.fullName"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dutyOverTimeDate')"  prop="dutyDate">
            <el-input v-model="showForm.dutyDate"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.OverTimeStart')" prop="timeStart">
            <el-input v-model="showForm.timeStart"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.OverTimeEnd')" prop="timeEnd">
            <el-input v-model="showForm.timeEnd"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.OverTimeHour')" prop="hour">
            <el-input v-model="showForm.hour"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
            <el-input v-model="showForm.remarks"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="dutyType === '调休'">
          <el-form-item :label="$t('dutyTaskForm.applyAccount')" prop="fullName">
            <el-input v-model="showForm.created.fullName"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.restType')" prop="type">
            <el-input v-model="showForm.type"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dutyRestDate')"prop="dutyDate">
            <el-input v-model="showForm.dutyDate"disabled ></el-input>
          </el-form-item>
          <div v-if="showForm.type === '年假调休'">
            <el-form-item :label="$t('dutyTaskForm.dateAnnualType')" prop="dateType">
              <el-input v-model="showForm.dateType"disabled ></el-input>
            </el-form-item>
          </div>
          <div v-if="showForm.type === '加班调休'">
            <el-form-item :label="$t('dutyTaskForm.restTimeStart')" prop="timeStart">
              <el-input v-model="showForm.timeStart"disabled ></el-input>
            </el-form-item>
            <el-form-item  :label="$t('dutyTaskForm.restTimeEnd')" prop="timeEnd">
              <el-input v-model="showForm.timeEnd"disabled ></el-input>
            </el-form-item>
            <el-form-item :label="$t('dutyTaskForm.restHour')"  prop="hour">
              <el-input v-model="showForm.hour"disabled ></el-input>
            </el-form-item>
          </div>
          <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
            <el-input v-model="showForm.remarks"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="dutyType === '出差'">
        <el-form-item :label="$t('dutyTaskForm.applyAccount')" prop="fullName">
          <el-input v-model="showForm.created.fullName"disabled ></el-input>
        </el-form-item>
        <el-form-item :label="$t('dutyTaskForm.restTimeStart')" prop="dateStart">
          <el-input v-model="showForm.dateStart"disabled ></el-input>
        </el-form-item>
        <el-form-item :label="$t('dutyTaskForm.restTimeEnd')" prop="dateEnd">
          <el-input v-model="showForm.dateEnd"disabled ></el-input>
        </el-form-item>
        <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
          <el-input v-model="showForm.remarks"disabled ></el-input>
        </el-form-item>
      </el-row>
        <el-row :gutter="20" v-if="dutyType === '免打卡'">
          <el-form-item :label="$t('dutyTaskForm.applyAccount')"  prop="fullName">
            <el-input v-model="showForm.created.fullName"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.freeDate')"  prop="freeDate">
            <el-input v-model="showForm.freeDate"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.dateFreeType')"  prop="dateType">
            <el-input v-model="showForm.dateType"disabled ></el-input>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.remarks')" prop="remarks">
            <el-input v-model="showForm.remarks"disabled ></el-input>
          </el-form-item>
        </el-row>
        <el-row :gutter="20" v-if="showForm.status === '申请中'">
          <el-form-item :label="$t('dutyTaskForm.isPass')" prop="pass">
            <el-radio-group v-model="inputForm.pass">
              <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="$t('dutyTaskForm.auditRemarks')" prop="auditRemarks">
            <el-input  type="textarea" v-model="inputForm.auditRemarks" ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('dutyTaskForm.sure')}}</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
 </div>
</template>
<script>
  export default {
    data() {
      return {
        submitDisabled:false,
        dutyType:'',
        formProperty:{bools:''},
        inputForm:{
          id: this.$route.query.id,
          dutyType: this.$route.query.dutyType,
          pass: '',
          auditRemarks:''
        },
        showForm:{},
        rules:{
          pass: [{ required: true, message: this.$t('dutyTaskForm.isPass')}],
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/duty/audit',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'dutyTaskList',query:util.getQuery("dutyTaskList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },
    created () {
      axios.get('/api/basic/hr/duty/detail',{params:{id:this.$route.query.id,dutyType:this.$route.query.dutyType}}).then((response)=>{
        this.dutyType = response.data.dutyType;
        this.showForm = response.data.item;
        this.formProperty.bools = response.data.bools;
      });
    }
  };
</script>
