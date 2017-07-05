<template>
  <div>
    <head-tab active="carrierProductForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('carrierProductForm.name')" prop="category">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('carrierProductForm.mallProductTypeName')" prop="sort">
          <el-input v-model.number="inputForm.mallProductTypeName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('carrierProductForm.productName')" prop="value">
          <product-select v-model="inputForm.productName"></product-select>
        </el-form-item>
        <el-form-item :label="$t('carrierProductForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('carrierProductForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import ProductSelect from "components/future/product-select.vue"
  export default{
    components:{
      ProductSelect
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
            name: [{ required: true, message: this.$t('carrierProductForm.prerequisiteMessage'),trigger:"blur"}],
            mallProductTypeName: [{ type: 'number',required:true, message: this.$t('carrierProductForm.inputLegalValue'),trigger:"blur"}],
            productName: [{ required: true, message: this.$t('carrierProductForm.prerequisiteMessage'),trigger:"blur"}]
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/api/carrierProduct/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'carrierProductList',query:util.getQuery("carrierProductList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/api/carrierProduct/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/api/carrierProduct/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
