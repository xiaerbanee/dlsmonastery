<template>
  <div>
    <head-tab active="adGoodsOrderForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId">
              <depot-select v-model="inputForm.outShopId" category="SHOP"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId" v-if="isAdShop">
              <el-select v-model="inputForm.adShop" clearable filterable remote :placeholder="$t('adGoodsOrderForm.selectKeyShow20time')" :remote-method="remoteAdDepot"  :loading="remoteLoading">
                <el-option v-for="shop in adShops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.employeeName')" prop="employeeId">
              <employee-select v-model="inputForm.employeeId" ></employee-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.expressCompany')" prop="expressCompanyId">
              <express-company-select v-model="expressOrder.expressCompanyId"></express-company-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.address')" prop="address">
              <el-input v-model="expressOrder.address"></el-input>
            </el-form-item>
            </el-col>
            <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.contact')" prop="contator">
              <el-input v-model="expressOrder.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="expressOrder.mobilePhone"></el-input>
            </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.remarks')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.summery')" prop="summery">
                <div style="color:red" v-show="this.totalQty">{{$t('adGoodsOrderForm.totalQty')}}：{{this.totalQty}},{{$t('adGoodsOrderForm.totalAmount')}}：{{this.totalPrice}}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderForm.save')}}</el-button>
              </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adGoodsOrderForm.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="adGoodsOrderDetails" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderForm.loading')" stripe border >
        <el-table-column  prop="productCode" :label="$t('adGoodsOrderForm.code')" ></el-table-column>
        <el-table-column prop="qty" :label="$t('adGoodsOrderForm.qty')">
          <template scope="scope">
            <el-input  v-model="scope.row.qty" @blur="getSummery()"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="productId" :label="$t('adGoodsOrderForm.productName')">
          <template scope="scope">
            <product-select v-model ="scope.row.productId" @input="productSelected(scope.row)"></product-select>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('adGoodsOrderForm.price')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adGoodsOrderForm.remarks')"></el-table-column>
        <el-table-column :render-header="renderAction"  >
          <template scope="scope">
            <el-button size="small" type="danger" @click.prevent="removeDomain(scope.row)">{{$t('storeAllotForm.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import employeeSelect from 'components/basic/employee-select';
  import depotSelect from 'components/future/depot-select';
  import expressCompanySelect from 'components/future/express-company-select';
  import productSelect from 'components/future/product-select'
  export default{
    components:{
      employeeSelect,
      depotSelect,
      expressCompanySelect,
      productSelect
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        alertError:false,
        error:"",
        productName:"",
        adGoodsOrderDetails:[],
        remoteLoading:false,
        isAdShop:false,
        pageLoading:'',
        inputForm:{},
        expressOrder:{
          expressCompanyId:'',
          address:'',
          contator:'',
          mobilePhone:'',
        },
        submitData:{
          id:this.$route.query.id,
          outShopId:'',
          employeeId:'',
          expressOrder:{
            id:"",
            expressCompanyId:'',
            address:'',
            contator:'',
            mobilePhone:'',
          },
          remarks:'',
          adGoodsOrderDetailList:[],
        },
        rules: {
          outShopId:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          employeeId:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          address:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          contator:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          mobilePhone:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}]
        },
        rules:{},
        totalQty:'',
        totalPrice:''
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.expressOrder = this.expressOrder;
            this.inputForm.adGoodsOrderDetails=this.adGoodsOrderDetails
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/crm/adGoodsOrder/save',qs.stringify(this.submitData,{allowDots:true})).then((response)=> {
              if(!response.data.errors){
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
                }
              }else{
                 this.alertError=true;
                 this.error=response.data.errors.id.message
                 this.submitDisabled = false;
              }

            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },searchDetail(){
          var val=this.productName;
         var tempList=new Array();
          for(var index in this.adGoodsOrderDetails){
            var detail=this.inputForm.adGoodsOrderDetails[index];
            if(util.isNotBlank(detail.qty)){
              tempList.push(detail)
             }
          }
         for(var index in this.adGoodsOrderDetails){
           var detail=this.adGoodsOrderDetails[index];
           if((val.length>=1 && util.contains(detail.productName,val)) && util.isBlank(detail.qty)){
             tempList.push(detail)
           }
         }
         this.adGoodsOrderDetails = tempList;
       },
      productSelected(row){
        axios.get('/api/ws/future/basic/product/findForm',{params:{id:row.productId}}).then((res)=>{
          row.productCode=res.data.code;
          row.price=res.data.price2;
          row.remarks=res.data.remarks;
        });
      },
      removeDomain(item) {
      var index = this.adGoodsOrderDetails.indexOf(item)
        if (index !== -1) {
          this.adGoodsOrderDetails.splice(index, 1)
        }
      },
      renderAction(createElement) {
        return createElement(
          'a',{
            attrs: {
              class: 'el-button el-button--primary el-button--small'
            }, domProps: {
              innerHTML: '增加'
            },on: {
              click: this.addDomain
            }
          }
        );
      },addDomain(){
        this.adGoodsOrderDetails.push({productCode:"",qty:"",productId:"",price:"",remarks:""});
        return false;
      },getSummery(){
      let list=this.adGoodsOrderDetails;
      let totalQty=0;
      let totalPrice=0;
      for(let item in list){
        if(list[item].qty){
          totalQty=totalQty+parseInt(list[item].qty);
          totalPrice=totalPrice+parseInt(list[item].qty)*parseInt(list[item].price);
        }
      }
      this.totalQty=totalQty;
      this.totalPrice=totalPrice;
    }},created(){
      axios.get('/api/ws/future/layout/adGoodsOrder/findForm',{params:{id:this.$route.query.id}}).then((response)=> {
        this.inputForm =response.data;
        if(response.data.expressOrderForm!=null){
          this.expressOrder=response.data.expressOrderForm;
        }
        if(response.data.adGoodsOrderDetails!=null){
          this.adGoodsOrderDetails=response.data.adGoodsOrderDetails;
        }else{
            this.adGoodsOrderDetails.push({productCode:"",qty:"",productId:"",price:"",remarks:""});
        }
      });
    }
  }
</script>
