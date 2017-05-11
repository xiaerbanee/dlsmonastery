<template>
  <div>
    <head-tab active="adApplyGoods"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyGoods.productName')" prop="productId">
          <el-select v-model="inputForm.productId" filterable remote :placeholder="$t('adApplyGoods.inputWord')" :remote-method="remoteProduct" :loading="remoteLoading" :clearable=true>
            <el-option v-for="product in products" :key="product.id" :label="product.name" :value="product.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyGoods.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyGoods.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="inputForm.adApplyList"  stripe border>
        <el-table-column prop="shop.name" :label="$t('adApplyGoods.shopName')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyGoods.applyQty')">
          <template scope="scope">
            <el-input v-model="scope.row.applyQty"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        submitDisabled:false,
        products:{},
        inputForm:{
          productId:'',
          remarks:'',
          adApplyList:[],
        },
        rules: {
          productId: [{ required: true, message: this.$t('adApplyGoods.prerequisiteMessage')}]
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

            var tempList = new Array();
            for(var index in this.inputForm.adApplyList){
              var detail = this.inputForm.adApplyList[index];
              if(util.isNotBlank(detail.applyQty)){
                tempList.push(detail)
               }
            }
           this.inputForm.adApplyList = tempList;

            axios.post('/api/crm/adApply/goodsSave',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adApplyList',query:util.getQuery("adApplyList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteProduct(query) {
        if (query !== '') {
          this.remoteLoading = true;
            axios.get('/api/crm/product/searchAll',{params:{name:query}}).then((response)=>{
            this.products = response.data;
            this.remoteLoading = false;
           })
        }
     },
    },created () {
        this.pageHeight = window.outerHeight -320;
        axios.get('api/crm/adApply/getAdApplyGoodsList').then((response) =>{
          this.inputForm.adApplyList = response.data;
          console.log(this.inputForm.adApplyList);
       });
    }
  }
</script>
