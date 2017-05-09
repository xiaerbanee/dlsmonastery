<template>
  <div>
    <head-tab active="shopAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <template>

          <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
        </template>
        <el-form-item :label="$t('shopAllotForm.fromShop')" prop="fromShopId">
            <su-depot type="shop" v-model="inputForm.fromShopId"  @input="refreshProductListIfNeeded" ></su-depot>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.toShop')" prop="toShopId">
          <su-depot type="shop" v-model="inputForm.toShopId"   @input="refreshProductListIfNeeded"></su-depot>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>

        <div v-if="inputForm.shopAllotDetailFormList!=null && inputForm.shopAllotDetailFormList.length >0 ">
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAllotForm.save')}}</el-button>
        </el-form-item>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table  :data="filterShopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="productName" :label="$t('shopAllotForm.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotForm.qty')">
            <template scope="scope">
              <input type="text" v-model="scope.row.qty" class="el-input__inner"/>
            </template>
          </el-table-column>

        </el-table>
        </div >
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
            productName:'',
            products:[],
            filterShopAllotDetailList:[],
            message:'',
            inputForm:{},
            submitData:{
              id:'',
              fromShopId:'',
              toShopId:'',
              remarks:'',
              shopAllotDetailFormList:[]
            },
            rules: {
              fromShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}],
              toShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.shopAllotDetailFormList=this.filterShopAllotDetailList;
              axios.post('/api/ws/future/crm/shopAllot/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                   this.$router.push({name:'shopAllotList',query:util.getQuery("shopAllotList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },refreshProductListIfNeeded(){
            console.log('refreshProductListIfNeeded');
          if(this.inputForm.fromShopId!=='' && this.inputForm.toShopId!=='' && this.isCreate){
            this.message='';
            axios.get('/api/ws/future/crm/shopAllot/getShopAllotDetailFormList',{params:{fromShopId:this.inputForm.fromShopId,toShopId:this.inputForm.toShopId}}).then((response)=>{
              if(!response.data.success){
                this.message=response.data.message;

              }else{
                this.inputForm.shopAllotDetailFormList=response.data.shopAllotDetailFormList;
                this.searchDetail();
              }
            })
          }
          return true;
        },searchDetail(){
          var val=this.productName;
          var tempList=new Array();
          for(var index in this.inputForm.shopAllotDetailFormList){

            let shopAllotDetailForm = this.inputForm.shopAllotDetailFormList[index];
            if(util.isNotBlank(shopAllotDetailForm.qty)){
              tempList.push(shopAllotDetailForm);
            }
          }
          for(var index in this.inputForm.shopAllotDetailFormList){
            let shopAllotDetailForm = this.inputForm.shopAllotDetailFormList[index];
            if(util.contains(shopAllotDetailForm.productName, val) && util.isBlank(shopAllotDetailForm.qty)){
              tempList.push(shopAllotDetailForm);
            }
          }
          this.filterShopAllotDetailList = tempList;

        }
      },created(){
          axios.get('/api/ws/future/crm/shopAllot/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm=response.data;
          })
      }
    }
</script>
