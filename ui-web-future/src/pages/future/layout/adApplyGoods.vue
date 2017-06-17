<template>
  <div>
    <head-tab active="adApplyGoods"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyGoods.productName')" prop="productId">
          <product-select v-model="inputForm.productId"></product-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyGoods.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyGoods.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="inputForm.depotAdApplyForms"  stripe border>
        <el-table-column prop="name" :label="$t('adApplyGoods.shopName')"></el-table-column>
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
  import productSelect from 'components/future/product-select';
  export default{
    components:{
      productSelect
    },
    data(){
      return this.getData();
    },
    methods:{
        getData(){
          return{
            submitDisabled:false,
            products:{},
            inputForm:{},
            rules: {
              productId: [{ required: true, message: this.$t('adApplyGoods.prerequisiteMessage')}]
            },
            remoteLoading:false
          }
        },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var tempList = new Array();
            for(var index in this.inputForm.depotAdApplyForms){
              var detail = this.inputForm.depotAdApplyForms[index];
              if(util.isNotBlank(detail.applyQty)){
                tempList.push(detail)
               }
            }
           let submitData = util.deleteExtra(this.inputForm);
            submitData.depotAdApplyForms = tempList;
            axios.post('/api/ws/future/layout/adApply/goodsSave',qs.stringify(submitData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        this.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/adApply/getAdApplyGoodsList').then((response) =>{
          this.inputForm = response.data;
        });
      }
    },created () {
        this.initPage();
    }
  }
</script>
