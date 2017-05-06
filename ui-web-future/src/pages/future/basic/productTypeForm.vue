<template>
  <div>
    <head-tab active="productTypeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('productTypeForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.reportName')" prop="reportName" >
          <el-input v-model="inputForm.reportName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.product')" prop="productIdList">
          <el-select v-model="inputForm.productIdList" multiple filterable remote :placeholder="$t('productTypeForm.inputWord')" :remote-method="remoteProduct" :loading="remoteLoading" :clearable=true>
            <el-option v-for="product in products" :key="product.id" :label="product.name" :value="product.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.code')" prop="code">
          <el-input v-model="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.baokaPrice')" prop="baokaPrice">
          <el-input v-model.number="inputForm.baokaPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.scoreType')" prop="scoreType" >
          <el-radio-group v-model="inputForm.scoreType">
            <el-radio :label=1>{{$t('productTypeForm.true')}}</el-radio>
            <el-radio :label=0>{{$t('productTypeForm.false')}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price1')" prop="price1">
          <el-input v-model.number="inputForm.price1"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price2')" prop="price2">
          <el-input v-model.number="inputForm.price2"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price3')" prop="price3">
          <el-input v-model.number="inputForm.price3"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productTypeForm.save')}}</el-button>
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
            inputForm:{
              id:this.$route.query.id,
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
            remoteLoading:false,
            products:[],
            rules: {
              name: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
              reportName: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
              scoreType: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage')}],
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/basic/productType/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'productTypeList',query:util.getQuery("productTypeList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteProduct(query) {
          if (query !== '') {
            this.remoteLoading = true;
             axios.get('/api/ws/future/basic/product/search',{params:{name:query}}).then((response)=>{
              this.products = response.data;
              this.remoteLoading = false;
            });
          }
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/ws/future/basic/productType/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            this.inputForm.scoreType = response.data.scoreType?1:0;
            if(response.data.productList!=null && response.data.productList.length>0){
              this.products = response.data.productList;
              this.inputForm.productIdList = util.getIdList(this.products);
            }
          })
        }
      }
    }
</script>
