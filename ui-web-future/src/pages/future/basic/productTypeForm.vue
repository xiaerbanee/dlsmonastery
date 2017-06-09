<template>
  <div>
    <head-tab active="productTypeForm"></head-tab>
    <div>
      <el-form :model="productType" ref="productType" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('productTypeForm.name')" prop="name">
          <el-input v-model="productType.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.reportName')" prop="reportName" >
          <el-input v-model="productType.reportName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.product')" prop="productIdList">
          <product-select v-model="productType.productIdList" multiple ></product-select>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.code')" prop="code">
          <el-input v-model="productType.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.baokaPrice')" prop="baokaPrice">
          <el-input v-model.number="productType.baokaPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.scoreType')" prop="scoreType" >
          <bool-select v-model="productType.scoreType"></bool-select>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price1')" prop="price1">
          <el-input v-model.number="productType.price1"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price2')" prop="price2">
          <el-input v-model.number="productType.price2"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price3')" prop="price3">
          <el-input v-model.number="productType.price3"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.remarks')" prop="remarks">
          <el-input  type="textarea" v-model="productType.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productTypeForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select'
  import boolSelect from 'components/common/bool-select'

  export default{
    components:{
      productSelect,
      boolSelect,
    },
      data(){
        return this.getData()
      },
    methods:{
      getData() {
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            productType:{},
            submitData:{
              id:'',
              name:'',
              reportName:'',
              scoreType:"",
              code:"",
              baokaPrice:0,
              productIdList:'',
              price1:0,
              price2:0,
              price3:0,
              remarks:''
            },

            rules: {
              name: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
              reportName: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
              scoreType: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
            }
          }
      },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["productType"];
          form.validate((valid) => {
            if (valid) {
                util.copyValue(this.productType, this.submitData);
              axios.post('/api/ws/future/basic/productType/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(response.data.success) {
                    if (!this.isCreate) {
                      this.$router.push({name: 'productTypeList', query: util.getQuery("productTypeList")})
                    }
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
          axios.get('/api/ws/future/basic/productType/findDto', {params: {id:this.$route.query.id}}).then((response)=>{
            this.productType=response.data;
        });
        }
      this.isInit = true;
      }
  }
</script>
