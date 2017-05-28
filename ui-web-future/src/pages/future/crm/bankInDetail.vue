<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="bankIn" ref="inputForm" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.shopName')" >
              {{bankIn.shopName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.bankName')">
              {{bankIn.bankName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.amount')" >
              {{bankIn.amount}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.serialNumber')">
              {{bankIn.serialNumber}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.synInfo')">
              金蝶同步
             </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.outCode')">
              {{bankIn.outCode}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.processStatus')">
              {{bankIn.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.inputDate')">
              {{bankIn.inputDate}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.createdByName')">
              {{bankIn.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.remarks')">
              {{bankIn.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="action=='audit'" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('bankInDetail.synToCloud')" prop="syn" >
                <bool-radio-group v-model="syn"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.billDate')" prop="billDate"  >
                <date-picker v-model="bankIn.billDate"></date-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.pass')" prop="pass" >
                <bool-radio-group v-model="audit.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('bankInDetail.auditRemarks')" prop="auditRemarks"  >
                <el-input type="textarea" v-model="audit.auditRemarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()"  >{{$t('bankInDetail.save')}}</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-row v-if="bankIn.processInstanceId"   :gutter="4">
          <el-col :span="24">
            <process-details v-model="bankIn.processInstanceId"></process-details>
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
            bankIn:{},
            audit:{
                pass:false,
                auditRemarks:'',
            },
            syn:true,
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
                this.initSubmitDataBeforeSubmit();
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
          }, initSubmitDataBeforeSubmit(){

        this.submitData.id = this.$route.query.id;
        this.submitData.syn = this.syn;
        this.submitData.billDate = this.bankIn.billDate;
        this.submitData.pass = this.audit.pass;
        this.submitData.auditRemarks = this.audit.auditRemarks;

      }
    },created(){
          axios.get('/api/ws/future/crm/bankIn/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
              this.bankIn = response.data;

              if(!this.bankIn.billDate){
//TODO 默认设置为今天
              }
          })
      }
    }
</script>
