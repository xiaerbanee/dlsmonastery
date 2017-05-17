<template>
  <div>
    <head-tab active="bankInForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('bankInForm.shopName')" prop="shopId">
          <depot-select category="directShop" v-model="inputForm.shopId" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.type')" prop="type">
          <el-select v-model="inputForm.type"  clearable :placeholder="$t('bankInForm.selectType')">
            <el-option v-for="item in inputForm.typeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.bankName')" prop="bankId">
          <el-select v-model="inputForm.bankId" filterable clearable :placeholder="$t('bankInForm.selectBank')">
            <el-option key="0" :label="$t('bankInForm.cashIn')" value="0"></el-option>
            <el-option v-for="item in inputForm.bankDtoList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.cash')" prop="inputDate">
          <date-picker  v-model="inputForm.inputDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.amount')" prop="amount">
          <el-input v-model="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.serialNumber')" prop="serialNumber">
          <el-input v-model="inputForm.serialNumber"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
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
            inputForm:{},
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
              util.copyValue(this.inputForm,this.submitData);
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
        }
      },created(){

        axios.get('/api/ws/future/crm/bankIn/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
      }
    }
</script>
