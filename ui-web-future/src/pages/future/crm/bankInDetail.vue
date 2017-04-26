<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
        <el-form-item :label="$t('bankInDetail.shopName')" >
          {{inputForm.shop.name}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.type')" >
          {{inputForm.type}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.bankName')">
          {{inputForm.bank.name}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.cash')">
          {{inputForm.inputDate}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.amount')" >
          {{inputForm.amount}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.serialNumber')">
          {{inputForm.serialNumber}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.outCode')">
          {{inputForm.outCode}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.processStatus')">
          {{inputForm.processStatus}}
        </el-form-item>
        <el-form-item :label="$t('bankInDetail.remarks')">
          {{inputForm.remarks}}
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.synToCloud')" prop="syn" v-if="action=='审核'">
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
            formProperty:{},
            depots:[],
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              shop:{name:''},
              shopId:'',
              type:'',
              bank:{name:''},
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
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/crm/bankIn/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
