<template>
  <div>
    <head-tab active="priceChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('priceChangeForm.name')" prop="name">
          <el-input v-model="inputForm.name" :disabled="!isCreate"></el-input>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.productTypeId')" prop="productTypeIdList">
          <product-type-select  v-model="inputForm.productTypeIdList" :multiple=true :disabled="!isCreate"></product-type-select>
        </el-form-item>
        <el-form-item  :label="$t('priceChangeForm.priceChangeDate')" prop="priceChangeDate">
          <date-picker  v-model="inputForm.priceChangeDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.uploadEndDate')" prop="uploadEndDate">
          <date-picker  v-model="inputForm.uploadEndDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productTypeSelect from 'components/future/product-type-select'
  export default{
    components:{productTypeSelect},
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        url:'',
        rules: {
          name: [{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
          productTypeIdList:[{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
          priceChangeDate:[{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
          uploadEndDate:[{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
        }
      }
    },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/priceChange/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(!that.isCreate){
                this.$router.push({name:'priceChangeList',query:util.getQuery("priceChangeList"), params:{_closeFrom:true}})
              }else{
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage(){
          axios.get('/api/ws/future/crm/priceChange/getForm').then((response)=>{
            this.inputForm = response.data;
            axios.get('/api/ws/future/crm/priceChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
            });
          });
      }
    },created () {
      this.initPage();
    }
  }
</script>
