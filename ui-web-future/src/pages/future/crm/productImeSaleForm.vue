<template>
  <div>
    <head-tab active="productImeSaleForm"></head-tab>
    <div>
      <el-form :model="productImeSale" ref="inputForm"   label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item>
              <el-alert  v-show="errMsg"  :closable=false  title=""  :description="errMsg" type="error"> </el-alert>
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
                <depot-select v-model="productImeSale.shopId"  category="shop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyer')" prop="buyer" >
                <el-input  v-model="productImeSale.buyer"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerAge')" prop="buyerAge" >
                <el-input  v-model="productImeSale.buyerAge"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerSex')" prop="buyerSex"  >
                <el-radio-group v-model="productImeSale.buyerSex">
                  <el-radio :label="$t('productImeSaleForm.man')"></el-radio>
                  <el-radio :label="$t('productImeSaleForm.women')"></el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item  :label="$t('productImeSaleForm.buyerPhone')" prop="buyerPhone" >
                <el-input  v-model="productImeSale.buyerPhone"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerSchool')" prop="buyerSchool" v-if=" companyName=='JXIMOO'">
                <el-input  v-model="productImeSale.buyerSchool"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerGrade')" prop="buyerGrade" v-if=" companyName=='JXIMOO'">
                <el-input  v-model="productImeSale.buyerGrade"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.remarks')" prop="remarks" >
                <el-input type="textarea" :rows="2" v-model="productImeSale.remarks"></el-input>
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
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,

            searched:false,
            imeStr:'',
            productImeSale:{},
            companyName:'',

            errMsg:'',
            productImeList:[],
            productQtyList:[],
            submitData:{
              imeStr:'',
              shopId:'',
              buyer:"",
              buyerAge:"",
              buyerSex:"",
              buyerPhone:"",
              buyerGrade:'',
              buyerSchool:'',
              remarks:''
            },
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

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              this.initSubmitDataBeforeSubmit();
              axios.post('/api/ws/future/crm/productImeSale/sale',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(response.data.success){
                  if(!this.isCreate){
                    this.$router.push({name:'productImeSaleList',query:util.getQuery("productImeSaleList")})
                  }
                }
              }).catch( () => {
                this.submitDisabled = false;
              });
            }
          })
        }, initSubmitDataBeforeSubmit(){

          this.submitData.imeStr = this.imeStr;
          this.submitData.shopId = this.productImeSale.shopId;
          this.submitData.buyer = this.productImeSale.buyer;
          this.submitData.buyerAge = this.productImeSale.buyerAge;
          this.submitData.buyerSex = this.productImeSale.buyerSex;
          this.submitData.buyerPhone = this.productImeSale.buyerPhone;
          this.submitData.buyerGrade = this.productImeSale.buyerGrade;
          this.submitData.buyerSchool = this.productImeSale.buyerSchool;
          this.submitData.remarks = this.productImeSale.remarks;

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
        }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
          axios.get('/api/ws/future/crm/productImeSale/findDto').then((response)=>{
            this.productImeSale=response.data;
          });
        }
      this.isInit = true;
      }
    }
</script>
