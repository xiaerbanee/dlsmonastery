<template>
  <div>
    <head-tab :active="$t('bankInForm.bankInForm') "></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('bankInForm.shopName')" prop="shopId">
          <el-select v-model="inputForm.shopId" filterable remote  :placeholder="$t('bankInForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" >
            <el-option v-for="item in depots" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.type')" prop="type">
          <el-radio-group v-model="inputForm.type">
            <el-radio  :label="$t('bankInForm.saleMoney')" value="销售收款">{{$t('bankInForm.saleMoney')}}</el-radio>
            <el-radio  :label="$t('bankInForm.deposit')" value='定金收款'>{{$t('bankInForm.deposit')}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.bankName')" prop="bankId">
          <el-select v-model="inputForm.bankId" filterable clearable :placeholder="$t('bankInForm.selectBank')">
            <el-option v-for="item in formProperty.banks" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.cash')" prop="inputDate">
          <el-date-picker  v-model="inputForm.inputDate" type="date" align="left" :placeholder="$t('bankInForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.amount')" prop="amount">
          <el-input v-model="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.serialNumber')" prop="serialNumber">
          <el-input v-model="inputForm.serialNumber"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('bankInForm.save')}}</el-button>
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
            inputForm:{
              id:this.$route.query.id,
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
              this.inputForm.inputDate=util.formatLocalDate(this.inputForm.inputDate)
              axios.post('/api/crm/bankIn/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
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
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search', {params: {name: query,category:'DIRECT_AND_SUB_SHOP'}}).then((response)=> {
              this.depots = response.data;
              this.remoteLoading = false;
            })
          } else {
            this.depots = [];
          }
        }
      },created(){
        axios.get('/api/crm/bankIn/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/crm/bankIn/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.shop!=null){
              this.depots = new Array(response.data.shop);
            }
            if(response.data.bank){
                this.formProperty.banks=new Array(response.data.bank);
                this.inputForm.bankId=response.data.bankId;
            }

          })
        }
      }
    }
</script>
