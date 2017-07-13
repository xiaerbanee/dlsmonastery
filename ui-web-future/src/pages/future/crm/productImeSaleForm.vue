<template>
  <div>
    <head-tab active="productImeSaleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm"  :rules="rules" label-width="120px" class="form input-form">
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
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('productImeSaleForm.inputIme')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-button  type="primary" @click.native="searchImeStr">{{$t('productImeSaleForm.search')}}</el-button>
              <el-button  type="primary" @click.native="reset">{{$t('productImeSaleForm.reset')}}</el-button>

            </el-form-item>
            <div v-if="searched">
              <el-form-item :label="$t('productImeSaleForm.buyer')" prop="buyer" >
                <el-input  v-model="inputForm.buyer"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleForm.buyerAge')" prop="buyerAge" >
                <el-input  v-model.number="inputForm.buyerAge"></el-input>
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
              <el-table :data="inputForm.productImeSaleDetailList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeSaleForm.ime')"></el-table-column>
                <el-table-column prop="depotName" :label="$t('productImeSaleForm.depotName')"></el-table-column>
                <el-table-column prop="productName"  :label="$t('productImeSaleForm.productType')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeSaleForm.baokaDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeSaleForm.productImeUploadCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('productImeSaleForm.alreadySaleShopName')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('productImeSaleForm.productImeSaleEmployeeName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeSaleForm.productImeSaleCreatedDate')"></el-table-column>
                <el-table-column :label="$t('productImeSaleForm.saleShopName')">
                  <template scope="scope" >
                    <div v-if="scope.row.editable">
                      <div v-if="scope.row.fromChain">
                        <el-select v-model="scope.row.saleShopId"  filterable>
                          <el-option v-for="item in scope.row.accessChainDepotList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                      </div>
                      <div v-else>{{scope.row.depotName}}</div>
                    </div>

                  </template>
                </el-table-column>
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
          submitDisabled:false,
          searched:false,
          inputForm:{
            extra:{},
          },
          errMsg:'',
          productQtyList:[],
          rules: {
            imeStr: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
            buyer: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
            buyerAge: [{ required: true, type:"number", message: this.$t('productImeSaleForm.prerequisiteAndNumberMessage')}],
            buyerSex: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
            buyerPhone: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
            remarks: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
          },
        }
      },
      formSubmit(){
        if (this.errMsg) {
          this.$alert( this.$t('productImeSaleForm.formInvalid'), this.$t('productImeSaleForm.notify'));
          return;
        }
        for(let productImeSaleDetail of this.inputForm.productImeSaleDetailList){
          if(productImeSaleDetail.editable && productImeSaleDetail.fromChain && util.isBlank(productImeSaleDetail.saleShopId)){
            this.$alert( this.$t('productImeSaleForm.pleaseSelectSaleShop'), this.$t('productImeSaleForm.notify'));
            return;
          }
        }
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/productImeSale/sale',qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false
            if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
          }).catch( () => {
              this.submitDisabled = false;
          });
          }else{
            this.submitDisabled = false;
          }
        });
      },searchImeStr(){
        this.searched = true;
        axios.get('/api/ws/future/crm/productImeSale/checkForSale',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
          this.errMsg=response.data;
      });
        axios.get('/api/ws/future/crm/productImeSale/findProductImeForSaleDto',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
            let tmp = [];
            if(response.data){
              for(let each of response.data){
                  tmp.push({
                    productImeId : each.id,
                    ime : each.ime,
                    depotName : each.depotName,
                    productName : each.productName,
                    retailDate : each.retailDate,
                    productImeUploadCreatedDate : each.productImeUploadCreatedDate,
                    productImeSaleShopName : each.productImeSaleShopName,
                    productImeSaleEmployeeName : each.productImeSaleEmployeeName,
                    productImeSaleCreatedDate : each.productImeSaleCreatedDate,
                    editable : each.editable,
                    fromChain : each.fromChain,
                    saleShopId : null,
                  });
              }
            }
          this.inputForm.productImeSaleDetailList=tmp;
          this.refreshProductQtyList(response.data ? response.data : []);
      });
      },
      refreshProductQtyList(productImeList){
        let tmpMap = new Map();

          for (let productIme of productImeList) {
            if (!tmpMap.has(productIme.productId)) {
              tmpMap.set(productIme.productId, {productName: productIme.productName, qty: 0});
            }
            tmpMap.get(productIme.productId).qty += 1;
          }

        let tmpList = [];
        for(let key of tmpMap.keys()){
          tmpList.push(tmpMap.get(key));
        }
        this.productQtyList = tmpList;
      },
      reset(){
        this.searched = false;
        this.errMsg='';
        this.productImeList=[];
        this.productQtyList = [];
        this.$refs["inputForm"].resetFields();
      },initPage(){
        axios.get('/api/ws/future/crm/productImeSale/getSaleForm').then((response)=>{
          this.inputForm=response.data;
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
