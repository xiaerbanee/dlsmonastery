<template>
  <div>
    <head-tab active="shopAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <template>
          <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
        </template>
        <el-form-item :label="$t('shopAllotForm.fromShop')" prop="fromShopId">
            <depot-select :disabled="!isCreate" category="directShop" v-model="inputForm.fromShopId"  @input="refreshProductListIfNeeded" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.toShop')" prop="toShopId">
          <depot-select :disabled="!isCreate"  category="directShop" v-model="inputForm.toShopId"   @input="refreshProductListIfNeeded"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>

        <div v-if="inputForm.fromShopId && inputForm.toShopId ">
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
  import depotSelect from 'components/future/depot-select'

  export default{
    components:{
      depotSelect,

    },
      data(){
          return{
            isCreate:(this.$route.query.id==null || this.$route.query.id==''),
            submitDisabled:false,
            productName:'',
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
              this.initSubmitDataBeforeSubmit();

            axios.post('/api/ws/future/crm/shopAllot/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopAllotList',query:util.getQuery("shopAllotList")})
              }
            }).catch(function () {
                this.submitDisabled = false;
              });
          }else{
            this.submitDisabled = false;
          }
        })
      }, initSubmitDataBeforeSubmit(){
        util.copyValue(this.inputForm,this.submitData);

        let tempList=new Array();
        for(let each of this.inputForm.shopAllotDetailFormList){

          if(util.isNotBlank(each.qty)){
            tempList.push(each);
          }
        }
        this.submitData.shopAllotDetailFormList = tempList;
      }
      ,refreshProductListIfNeeded(){
          //只有新增的时候才会刷新产品列表，修改的时候不能修改fromShop和toShop，所以也就不需要再次加载产品列表
        if(this.inputForm.fromShopId && this.inputForm.toShopId && !this.inputForm.id){
          this.message='';
          axios.get('/api/ws/future/crm/shopAllot/getShopAllotDetailFormListForNew',{params:{fromShopId:this.inputForm.fromShopId,toShopId:this.inputForm.toShopId}}).then((response)=>{
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
        let val=this.productName;
        let tempList=new Array();
        for(let each of this.inputForm.shopAllotDetailFormList){
          if(util.isNotBlank(each.qty)){
            tempList.push(each);
          }
        }
        for(let each of this.inputForm.shopAllotDetailFormList){
          if(util.contains(each.productName, val) && util.isBlank(each.qty)){
            tempList.push(each);
          }
        }
        this.filterShopAllotDetailList = tempList;
      }
    },created(){
      axios.get('/api/ws/future/crm/shopAllot/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
        this.searchDetail();
      })
    }
  }
</script>
