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
            <el-option v-for="item in inputForm.extra.typeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('bankInForm.bankName')" prop="bankId">
          <bank-select v-model="inputForm.bankId"></bank-select>
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
  import bankSelect from 'components/future/bank-select'
  export default{
    components:{
      depotSelect,
      bankSelect
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            shopId: [{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
            type:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
            amount:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}],
            serialNumber:[{ required: true, message: this.$t('bankInForm.prerequisiteMessage')}]
          }
        }
      },
      formSubmit(){

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/bankIn/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);

              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data, this.getData());
                  this.initPage();

                }else {
                  this.submitDisabled = false;
                  this.$router.push({name: 'bankInList', query: util.getQuery("bankInList"), params:{_closeFrom:true}});
                }
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/crm/bankIn/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/crm/bankIn/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {

      this.initPage();
    }
  }
</script>


