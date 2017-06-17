<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
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
        <div v-if="action==='audit'" >
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
            inputForm:{
                extra:{},
            },
            action:this.$route.query.action,
            submitDisabled:false,
          }
      },methods:{
          formSubmit(){

            let form = this.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                this.submitDisabled = true;

                axios.post('/api/ws/future/crm/bankIn/audit', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
                  this.$message(response.data.message);
                  this.submitDisabled = false;
                  if(response.data.success) {
                    this.$router.push({name:'bankInList',query:util.getQuery("bankInList")});
                  }

                }).catch( () => {
                  this.submitDisabled = false;
                });
              }
            })
          }, initSubmitDataBeforeSubmit(){

        this.submitData.id = this.$route.query.id;
        this.submitData.syn = this.syn;
        this.submitData.billDate = this.bankIn.billDate;
        this.submitData.pass = this.audit.pass;
        this.submitData.auditRemarks = this.audit.auditRemarks;

      },initPage(){
              if(this.action !== 'audit'){
                  return;
              }
          axios.get('/api/ws/future/crm/bankIn/getAuditForm').then((response)=>{
            this.inputForm = response.data;


            axios.get('/api/ws/future/crm/bankIn/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
              this.bankIn = response.data;

              this.inputForm.id = this.bankIn.id;

              this.inputForm.billDate = this.bankIn.billDate;
              if(!this.inputForm.billDate){
                this.inputForm.billDate = this.inputForm.extra.defaultBillDate;
              }
            });
          });
      }
    },created(){
          this.initPage();
      }
    }
</script>
