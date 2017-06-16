<template>
  <div>
    <head-tab active="productImeSaleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm"   label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item>
              <su-alert :text="errMsg" type="danger"> </su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeSaleForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr" :placeholder="$t('productImeSaleForm.inputIme')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-button  type="primary" @click.native="onImeStrChange">{{$t('productImeSaleForm.search')}}</el-button>
              <el-button  type="primary" @click.native="reset">{{$t('productImeSaleForm.reset')}}</el-button>

            </el-form-item>
            <div v-if="searched">
              <el-form-item :label="$t('productImeSaleForm.shopId')" prop="shopId" >
                <depot-select v-model="inputForm.shopId"  category="shop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyer')" prop="buyer" >
                <el-input  v-model="inputForm.buyer"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerAge')" prop="buyerAge" >
                <el-input  v-model="inputForm.buyerAge"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerSex')" prop="buyerSex"  >
                <el-radio-group v-model="inputForm.buyerSex">
                  <el-radio :label="$t('productImeSaleForm.man')"></el-radio>
                  <el-radio :label="$t('productImeSaleForm.women')"></el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item  :label="$t('productImeSaleForm.buyerPhone')" prop="buyerPhone" >
                <el-input  v-model="inputForm.buyerPhone"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.remarks')" prop="remarks" >
                <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('productImeSaleForm.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="18" v-if="searched">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('productImeSaleForm.name')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeSaleForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeSaleForm.ime')"></el-table-column>
                <el-table-column prop="depotName" :label="$t('productImeSaleForm.depotName')"></el-table-column>
                <el-table-column prop="productName"  :label="$t('productImeSaleForm.productType')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeSaleForm.baokaDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeSaleForm.productImeUploadCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('productImeSaleForm.shopName')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('productImeSaleForm.productImeSaleEmployeeName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeSaleForm.productImeSaleCreatedDate')"></el-table-column>
              </el-table>
            </template>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  .el-table { margin-bottom: 50px;}
</style>
<script>

  import depotSelect from 'components/future/depot-select'


  export default{
    components:{
      depotSelect,


    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          searched:false,
          imeStr:'',
          inputForm:{},
          errMsg:'',
          productImeList:[],
          productQtyList:[],
          rules: {
            imeStr: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
          },

        }
      },
      formSubmit(){

        if (this.errMsg) {
          this.$alert( this.$t('productImeSaleForm.formInvalid'), this.$t('productImeSaleForm.notify'));
          return;
        }
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.initSubmitDataBeforeSubmit();
            axios.post('/api/ws/future/crm/productImeSale/sale',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
            if(response.data.success){
              if(!this.inputForm.create){
                this.submitDisabled = false;
                this.$router.push({name:'productImeSaleList',query:util.getQuery("productImeSaleList")})
              }else{
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }
          }).catch( () => {
              this.submitDisabled = false;
          });
          }
        })
      }, initSubmitDataBeforeSubmit(){

        this.inputForm.imeStr = this.imeStr;
        this.inputForm.shopId = this.inputForm.shopId;
        this.inputForm.buyer = this.inputForm.buyer;
        this.inputForm.buyerAge = this.inputForm.buyerAge;
        this.inputForm.buyerSex = this.inputForm.buyerSex;
        this.inputForm.buyerPhone = this.inputForm.buyerPhone;
        this.inputForm.buyerGrade = this.inputForm.buyerGrade;
        this.inputForm.buyerSchool = this.inputForm.buyerSchool;
        this.inputForm.remarks = this.inputForm.remarks;

      },onImeStrChange(){
        this.searched = true;
        axios.get('/api/ws/future/crm/productImeSale/checkForSale',{params:{imeStr:this.imeStr}}).then((response)=>{
          this.errMsg=response.data;
      });
        axios.get('/api/ws/future/crm/productIme/findDtoListByImes',{params:{imeStr:this.imeStr}}).then((response)=>{
          this.productImeList=response.data;

        let tmpMap = new Map();
        if(this.productImeList){
          for(let productIme of this.productImeList ){
            if(!tmpMap.has(productIme.productId)){
              tmpMap.set(productIme.productId, {productName:productIme.productName, qty:0});
            }
            tmpMap.get(productIme.productId).qty+=1;
          }
        }
        let tmpList = [];
        for(let key of tmpMap.keys()){
          tmpList.push(tmpMap.get(key));
        }
        this.productQtyList = tmpList;
      });
      },reset(){
        this.searched = false;
        this.imeStr = '';
        this.errMsg='';
        this.productImeList=[];
        this.productQtyList = [];
        this.$refs["inputForm"].resetFields();
      },initPage(){
        axios.get('/api/ws/future/crm/productImeSale/getForm').then((response)=>{
          this.inputForm=response.data;
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
