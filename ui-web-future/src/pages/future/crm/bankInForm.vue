<template>
  <div>
    <head-tab active="bankInForm"></head-tab>
    <div >
      <el-form :model="bankIn" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('bankInForm.shopName')" prop="shopId">
          <depot-select category="directShop" v-model="bankIn.shopId" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.type')" prop="type">
          <el-select v-model="bankIn.type"  clearable :placeholder="$t('bankInForm.selectType')">
            <el-option v-for="item in inputProperty.typeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.bankName')" prop="bankId">
          <el-select v-model="bankIn.bankId" filterable clearable :placeholder="$t('bankInForm.selectBank')">
            <el-option key="0" :label="$t('bankInForm.cashIn')" value="0"></el-option>
            <el-option v-for="item in inputProperty.bankDtoList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.cash')" prop="inputDate">
          <date-picker  v-model="bankIn.inputDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.amount')" prop="amount">
          <el-input v-model="bankIn.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.serialNumber')" prop="serialNumber">
          <el-input v-model="bankIn.serialNumber"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="bankIn.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('bankInForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'

  export default{
    components:{
      depotSelect,

    },
    data(){
          return{
            submitDisabled:false,
            inputProperty:{},
            bankIn:{},
            submitData:{
              id:'',
              shopId:'',
              type:'',
              bankId:'',
              inputDate:'',
              amount:'',
              serialNumber:'',
              remarks:''
            },
            rules: {
              shopId: [{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
              type:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
              amount:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
              serialNumber:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){

          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.initSubmitDataBeforeSubmit();
              axios.post('/api/ws/future/crm/bankIn/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.create){
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
        }, initSubmitDataBeforeSubmit(){
          this.submitData.id = this.$route.query.id;
          this.submitData.shopId = this.bankIn.shopId;
          this.submitData.type = this.bankIn.type;
          this.submitData.bankId = this.bankIn.bankId;
          this.submitData.inputDate = this.bankIn.inputDate;
          this.submitData.amount = this.bankIn.amount;
          this.submitData.serialNumber = this.bankIn.serialNumber;
          this.submitData.remarks = this.bankIn.remarks;
        }
      },created(){

        axios.get('/api/ws/future/crm/bankIn/getForm').then((response)=>{
          this.inputProperty = response.data;
        })

      axios.get('/api/ws/future/crm/bankIn/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
        this.bankIn = response.data;
      });

    }
    }
</script>
