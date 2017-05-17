<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
        <el-form-item :label="$t('bankInDetail.shopName')" >
          {{inputForm.originShopName}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.bankName')">
          {{inputForm.originBankName}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.amount')" >
          {{inputForm.amount}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.serialNumber')">
          {{inputForm.serialNumber}}
        </el-form-item>
            <el-form-item :label="$t('bankInDetail.jingdie')">
              金蝶同步
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.outCode')">
              {{inputForm.outCode}}
            </el-form-item>

            <el-form-item :label="$t('bankInDetail.processStatus')">
              {{inputForm.processStatus}}
            </el-form-item>

            <el-form-item :label="$t('bankInDetail.inputDate')">
              {{inputForm.inputDate}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.createdByName')">
              {{inputForm.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.remarks')">
              {{inputForm.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="4">
          <el-col :span="24">
            <el-form-item :label="$t('bankInDetail.synToCloud')" prop="syn" v-if="action=='审核'">
              <bool-
              <el-radio-group v-model="inputForm.syn">
                <el-radio  :label="$t('bankInDetail.true')" value="true">{{$t('bankInDetail.true')}}</el-radio>
                <el-radio  :label="$t('bankInDetail.false')" value = 'false'>{{$t('bankInDetail.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.pass')" prop="pass" v-if="action=='审核'">
              <el-radio-group v-model="inputForm.pass">
                <el-radio  :label="$t('bankInDetail.true')" value="true">{{$t('bankInDetail.true')}}</el-radio>
                <el-radio  :label="$t('bankInDetail.false')" value = 'false'>{{$t('bankInDetail.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.auditRemarks')" prop="auditRemarks"  v-if="action=='审核'">
              <el-input v-model="inputForm.auditRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()"  v-if="action=='审核'">{{$t('expressOrderList.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="4">
          <el-col :span="24">

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
            action:this.$route.query.action,
            submitDisabled:false,
            depots:[],
            remoteLoading:false,
            inputForm:{},
            submitData:{
              id:'',
              shopId:'',
              type:'',
              bankId:'',
              inputDate:'',
              amount:'',
              serialNumber:'',
              remarks:'',
              processStatus:"",
              pass:'',
              outCode:"",
              syn:'',
              auditRemarks:''
            },
            rules: {
              syn:{required: true, message: this.$t('bankInDetail.prerequisiteMessage')},
              pass:{required: true, message: this.$t('bankInDetail.prerequisiteMessage')}
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.pass= this.inputForm.pass==='是'?true:false;
              this.inputForm.syn= this.inputForm.syn==='是'?true:false;
              axios.post('/api/crm/bankIn/audit',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                   this.$router.push({name:'bankInList',query:util.getQuery("bankInList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
          axios.get('/api/ws/future/crm/bankIn/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
              this.inputForm = response.data;

          })
      }
    }
</script>
