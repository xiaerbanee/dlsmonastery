<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.shopName')" >
              {{inputForm.bankInDto.shopName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.bankName')">
              {{inputForm.bankInDto.bankName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.amount')" >
              {{inputForm.bankInDto.amount}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.serialNumber')">
              {{inputForm.bankInDto.serialNumber}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.synInfo')">
              金蝶同步
             </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.outCode')">
              {{inputForm.bankInDto.outCode}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.processStatus')">
              {{inputForm.bankInDto.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.inputDate')">
              {{inputForm.bankInDto.inputDate}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.createdByName')">
              {{inputForm.bankInDto.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.remarks')">
              {{inputForm.bankInDto.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="action=='audit'" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('bankInDetail.synToCloud')" prop="syn" >
                <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.billDate')" prop="billDate"  >
                <date-picker v-model="inputForm.billDate"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.pass')" prop="pass" >
                <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.auditRemarks')" prop="auditRemarks"  >
                <el-input type="textarea" v-model="inputForm.auditRemarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()"  >{{$t('bankInDetail.save')}}</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-row v-if="inputForm.bankInDto.processInstanceId"   :gutter="4">
          <el-col :span="24">
            <process-details v-model="inputForm.bankInDto.processInstanceId"></process-details>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import processDetails from 'components/general/process-details'
  import boolRadioGroup from 'components/common/bool-radio-group'

  export default{
    components:{
      processDetails,
      boolRadioGroup,

    },
      data(){
          return{
            inputForm:{
                bankInDto:{},

            },
            submitData:{
              id:'',
              syn:'',
              billDate:'',
              pass:'',
              auditRemarks:'',
            },
            action:this.$route.query.action,
            submitDisabled:false,
          }
      },methods:{
          formSubmit(){
            this.submitDisabled = true;
            var form = this.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                util.copyValue(this.inputForm, this.submitData);
                axios.post('/api/ws/future/crm/bankIn/audit', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
                  this.$message(response.data.message);
                  this.$router.push({name:'bankInList',query:util.getQuery("bankInList")})
                }).catch(function () {
                  this.submitDisabled = false;
                });
              }else{
                this.submitDisabled = false;
              }
            })
          }
    },created(){
          axios.get('/api/ws/future/crm/bankIn/findDetail',{params: {id:this.$route.query.id, action:this.$route.query.action}}).then((response)=>{
              this.inputForm = response.data;

          })
      }
    }
</script>
