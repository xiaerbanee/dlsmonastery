<template>
  <div>
    <head-tab :active="$t('shopDepositForm.shopDepositForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('shopDepositForm.shopName')" prop="shopId">
          <el-select v-model="inputForm.shopId"  filterable remote :placeholder="$t('shopDepositForm.inputWord')" :disabled="disabled" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true  @change="changeDepartment">
            <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.department')" prop="departMent" v-if="isCreate" >
          <el-select v-model="inputForm.departMent"  filterable :clearable=true >
            <el-option v-for="departMent in formProperty.departMents" :key="departMent" :label="departMent" :value="departMent"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.type')" prop="type" v-if="!isCreate" >
          <el-input v-model="inputForm.type" :disabled="!isCreate" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.amount')" prop="amount" v-if="!isCreate">
          <el-input v-model="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.outBillType')" prop="outBillType"  v-if="isCreate">
          <el-select v-model="inputForm.outBillType" filterable :placeholder="$t('shopDepositForm.outBillType')">
            <el-option v-for="item in formProperty.outBillTypes" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.bank')" prop="bankId" v-if="inputForm.outBillType==='手工日记账'" >
          <el-select v-model="inputForm.bankId" filterable clearable :placeholder="$t('shopDepositForm.inputBank')">
            <el-option v-for="item in formProperty.banks" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.marketAmount')" prop="marketAmount" v-if="isCreate">
          <el-input v-model.number="inputForm.marketAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.imageAmount')" prop="imageAmount" v-if="isCreate">
          <el-input v-model.number="inputForm.imageAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.demoPhoneAmount')" prop="demoPhoneAmount" v-if="isCreate">
          <el-input v-model.number="inputForm.demoPhoneAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopDepositForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            depots:[],
            remoteLoading:false,
            bankShow:false,
            disabled:false,
            inputForm:{
              id:this.$route.query.id,
              shopId:"",
              type:'',
              amount:'',
              outBillType:'',
              bankId:'',
              marketAmount:'',
              imageAmount:'',
              demoPhoneAmount:'',
              remarks:'',
              departMent:"",
            },
            rules: {
              shopId: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              departMent: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              outBillType: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              marketAmount: [{ type: 'number', message: this.$t('shopDepositForm.inputLegalValue')}],
              imageAmount: [{ type: 'number', message: this.$t('shopDepositForm.inputLegalValue')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/shopDeposit/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'shopDepositList',query:util.getQuery("shopDepositList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query,category:"SHOP"}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
          }
        },changeDepartment(){
           var shopId=this.inputForm.shopId;
            axios.get('/api/crm/shopDeposit/getDepartmentByShopId',{params:{shopId:shopId}}).then((response)=>{
              this.inputForm.departMent=response.data.fname;
            })
        }
      },created(){
        axios.get('/api/crm/shopDeposit/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          this.disabled = true;
          axios.get('/api/crm/shopDeposit/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.shop!=null){
              this.depots=new Array(response.data.shop)
            }

          })
        }
      }
    }
</script>
