<template>
  <div>
    <head-tab active="productForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row>
          <el-col :span="8">
            <el-form-item :label="$t('productForm.name')">
              <el-input v-model="inputForm.name" disabled></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.code')" prop="code">
              <el-input v-model="inputForm.code"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.outGroupName')" prop="outGroupName">
              <el-input v-model="inputForm.outGroupName" disabled></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.productTypeName')" prop="productTypeName">
              <el-select v-model="inputForm.productTypeId"  clearable filterable remote :placeholder="$t('productForm.inputWord')" :remote-method="remoteProductType" :loading="remoteLoading">
                <el-option v-for="productType in productTypeList" :key="productType.id"  :label="productType.name" :value="productType.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item  :label="$t('productForm.netType')" prop="netType">
              <el-select v-model="inputForm.netType"   clearable  :placeholder="$t('productForm.select')">
                <el-option v-for="netType in inputForm.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('productForm.hasIme')" prop="hasIme">
              <el-radio-group v-model="inputForm.hasIme">
                <el-radio :label=1>{{$t('productForm.true')}}</el-radio>
                <el-radio :label=0>{{$t('productForm.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('productForm.allowOrder')" prop="allowOrder">
              <el-radio-group v-model="inputForm.allowOrder">
                <el-radio :label=1>{{$t('productForm.true')}}</el-radio>
                <el-radio :label=0 checked>{{$t('productForm.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('productForm.allowBill')" prop="allowBill">
              <el-radio-group v-model="inputForm.allowBill">
                <el-radio :label=1>{{$t('productForm.true')}}</el-radio>
                <el-radio :label=0>{{$t('productForm.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('productForm.price2')" prop="price2">
              <el-input v-model="inputForm.price2"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.retailPrice')" prop="retailPrice">
              <el-input v-model="inputForm.retailPrice"></el-input>
            </el-form-item>
            <el-form-item  :label="$t('productForm.depositPrice')" prop="depositPrice">
              <el-input v-model="inputForm.depositPrice"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.mappingName')" prop="mappingName">
              <el-input v-model="inputForm.mappingName"></el-input>
            </el-form-item>
            <el-form-item  :label="$t('productForm.image')" prop="image">
              <el-input v-model="inputForm.image"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="formSubmit()">{{$t('productForm.save')}}</el-button>
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
        submitDisabled:false,
        formProperty:{},
        productTypeList:[],
        inputForm:{},
        submitData:{
          id:'',
          name:'',
          code:'',
          outGroupName:'',
          productTypeId:'',
          netType:'',
          hasIme:'',
          allowOrder:'',
          allowBill:'',
          price2:'',
          retailPrice:'',
          depositPrice:'',
          mappingName:'',
          image:'',
          remarks:''
         },
        rules: {
        },
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/product/save', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'productList',query:util.getQuery("productList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteProductType(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/basic/productType/search',{params:{name:query}}).then((response)=>{
            this.productTypeList = response.data;
            this.remoteLoading = false;
          })
        }
      }
    },created(){
        axios.get('/api/ws/future/basic/product/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          this.inputForm.hasIme = response.data.hasIme?1:0;
          this.inputForm.allowOrder = response.data.allowOrder?1:0;
          this.inputForm.allowBill = response.data.allowBill?1:0;
          if(response.data.productType != null){
            this.productTypeList = new Array(response.data.productType)
          }
        })
    }
  }
</script>
