<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.shopName')" >
              {{bankIn.shopName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.bankName')">
              {{bankIn.bankName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.transferType')">
              {{bankIn.transferType}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.amount')" >
              {{bankIn.amount}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.serialNumber')">
              {{bankIn.serialNumber}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.synInfo')">
              <el-button type="text" @click="toKingdeeSynList"  >{{$t('bankInDetail.toKingdeeSynList')}}</el-button>
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
            rules: {
              syn: [{ required: true, message: this.$t('bankInDetail.prerequisiteMessage')}],
              billDate:[{ required: true, message: this.$t('bankInDetail.prerequisiteMessage')}],
              pass:[{ required: true, message: this.$t('bankInDetail.prerequisiteMessage')}],
            }
          }
      },methods:{
          formSubmit(){
            this.submitDisabled = true;
            let form = this.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                axios.post('/api/ws/future/crm/bankIn/audit', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
                  this.$message(response.data.message);
                  this.submitDisabled = false;
                  if(response.data.success) {
                    this.$router.push({name:'bankInList',query:util.getQuery("bankInList")});
                  }

                }).catch( () => {
                  this.submitDisabled = false;
                });
              }else{
                this.submitDisabled = false;
              }
            })
          },initPage(){

        axios.get('/api/ws/future/crm/bankIn/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
          this.bankIn = response.data;
          if(this.action === 'audit'){
            axios.get('/api/ws/future/crm/bankIn/getAuditForm').then((response)=>{
              this.inputForm = response.data;
              this.inputForm.id = this.bankIn.id;
              this.inputForm.syn = true;
              this.inputForm.pass = null;
              this.inputForm.billDate = this.bankIn.billDate;
              if(!this.inputForm.billDate){
                this.inputForm.billDate = this.inputForm.extra.defaultBillDate;
              }
            });
          }
        });
      },toKingdeeSynList(){
        this.$router.push({ name: 'kingdeeSynList', query: { extendId: this.bankIn.id, extendType:'销售收款' }});
      }
    },created(){
      this.initPage();
    }
  }
</script>
