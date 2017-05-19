<template>
  <div>
    <head-tab active="adGoodsOrderShip"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderShip.shipCode')" prop="id">
              {{shipForm.id}}
          </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.remarks')" prop="remarks">
              {{shipForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.smallQty')" prop="smallQty" >
              <el-input v-model="shipForm.smallQty" @blur="materialChange()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mediumQty')" prop="mediumQty" >
              <el-input v-model="shipForm.mediumQty" @blur="materialChange()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.largeQty')" prop="largeQty">
              <el-input v-model="shipForm.largeQty" @blur="materialChange()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.expressCodes')" prop="expressCodes">
              <el-input type="textarea" v-model="inputForm.expressCodes"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressOrder.expressCompanyId" clearable  >
                <el-option v-for="item in shipFormProperty.expressCompanys" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderShip.ship')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderShip.shipShopName')" prop="outShopId">
              {{shipForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.remarks')" prop="shopId">
              {{shipForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleContact')" prop="contator">
              {{shipForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleMobile')" prop="mobilePhone">
              {{shipForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.billRemarks')" prop="billRemarks">
              {{shipForm.billRemarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.contact')" prop="contator">
              {{shipForm.expressOrder.contator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.address')" prop="address">
              {{shipForm.expressOrder.address}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mobilePhone')" prop="mobilePhone">
              {{shipForm.expressOrder.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldPay')" prop="shouldPay">
              {{inputForm.expressOrder.shouldPay}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldGet')" prop="shouldGet">
              <el-input v-model="inputForm.expressOrder.shouldGet"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.realPay')" prop="realPay">
              <el-input v-model="inputForm.expressOrder.realPay"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table :data="inputForm.adGoodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderShip.loading')" stripe border >
        <el-table-column  prop="product.code" :label="$t('adGoodsOrderShip.code')" sortable width="200"></el-table-column>
        <el-table-column prop="product.name" :label="$t('adGoodsOrderShip.productName')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderShip.billQty')"></el-table-column>
        <el-table-column prop="shippedQty" :label="$t('adGoodsOrderShip.shippedQty')"></el-table-column>
        <el-table-column prop="extendMap.waitShipQty" :label="$t('adGoodsOrderShip.waitShipQty')" ></el-table-column>
        <el-table-column prop="shipQty" :label="$t('adGoodsOrderShip.shipQty')" >
          <template scope="scope">
            <el-input v-model="scope.row.shipQty"  @blur="materialChange()"> </el-input>
          </template>
        </el-table-column>
      </el-table>
      <el-table :data="activitiEntity.historicTaskInstances" v-if="activitiEntity">
        <el-table-column prop="name" :label="$t('adGoodsOrderShip.nodeName')"></el-table-column>
        <el-table-column :label="$t('adGoodsOrderShip.auditMan')" >
          <template scope="scope">{{activitiEntity.accountMap?activitiEntity.accountMap[scope.row.id]:''}}</template>
        </el-table-column>
        <el-table-column :label="$t('adGoodsOrderShip.auditDate')">
          <template scope="scope">
            {{scope.row.endTime | formatLocalDateTime}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('adGoodsOrderShip.auditRemarks')">
          <template scope="scope">
            {{activitiEntity.commentMap?activitiEntity.commentMap[scope.row.id]:''}}
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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        shipFormProperty:{},
        activitiEntity:{},
        shipForm:{
          id:"",
          remarks:"",
          shop:{name:''},
          outShop:{name:''},
          billRemarks:'',
          employee:{
            name:'',
            mobilePhone:''
          },
          expressOrder:{
            concator:'',
            address:'',
            mobilePhone:'',
            shouldPay:''
          },
          largeQty:'',
          mediumQty:'',
          smallQty:'',
          shouldGet:'',
          shouldPay:'',
        },
        inputForm:{
          id:this.$route.query.id,
          expressCompanyId:'',
          expressCodes:'',
          expressOrder:{
            expressCompanyId:'',
            contator:'',
            address:'',
            realPay:'',
            shouldGet:'',
            shouldPay:'',
            mobilePhone:''
          },
          adGoodsOrderDetailList:[],
        },
        rules: {

        },
        pageLoading:'',
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/adGoodsOrder/ship',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },materialChange(val){
      var shouldGet= this.shipForm.shouldGet
      var shouldPay= this.shipForm.shouldPay
      var ysyfAmount=this.shipFormProperty.ysyfAmount;
      var yfyfAmount=this.shipFormProperty.yfyfAmount;
      var smallPrice=this.shipFormProperty.smallPrice;
      var mediumPrice=this.shipFormProperty.mediumPrice;
      var bigPrice=this.shipFormProperty.bigPrice;
      var shipPrice=this.shipForm.smallQty*smallPrice+ this.shipForm.mediumQty*mediumPrice+ this.shipForm.largeQty*bigPrice;
			var pay=0; //实付运费(实际面单金额)
       let list=this.inputForm.adGoodsOrderDetailList;
        for(let item in list){
              console.log(parseInt(list[item].shipQty==null?0:list[item].shipQty)+parseInt(list[item].shippedQty==NaN?0:list[item].shippedQty))
             console.log()
          if(list[item].shipQty|| list[item].shippedQty){
            pay=parseInt(pay)+(parseInt(list[item].shipQty==null?0:list[item].shipQty)+parseInt(list[item].shippedQty==null?0:list[item].shippedQty))*parseInt(list[item].product.price==null?0:list[item].price);
          }
        }
			this.inputForm.expressOrder.realPay=parseInt(pay)+parseInt(shipPrice);
			this.inputForm.expressOrder.shouldGet=parseInt(shouldGet)+parseInt(shipPrice);
			this.inputForm.expressOrder.shouldPay=parseInt(shouldPay)+parseInt(shipPrice);
      } ,findOne(){
        axios.get('/api/crm/adGoodsOrder/ship',{params: {id:this.$route.query.id}}).then((response)=>{
          this.shipForm=response.data;
          util.copyValue(response.data,this.inputForm);

          this.inputForm.expressOrder.shouldGet=response.data.expressOrder.shouldGet==null?0:expressOrder.shouldGet;
          this.inputForm.expressOrder.shouldPay=response.data.expressOrder.shouldPay==null?0:expressOrder.shouldPay;
          this.inputForm.adGoodsOrderDetailList=response.data.adGoodsOrderDetailList;
         for(var index in this.inputForm.adGoodsOrderDetailList){
           this.inputForm.adGoodsOrderDetailList[index].shipQty=this.inputForm.adGoodsOrderDetailList[index].extendMap.waitShipQty
         }
          this.shipForm.shouldGet=this.inputForm.expressOrder.shouldGet
          this.shipForm.shouldPay=this.inputForm.expressOrder.shouldPay

        })
      },getForm(){
        axios.get('/api/crm/adGoodsOrder/getShipFormProperty',{params:{id:this.$route.query.id,}}).then((response)=>{
          this.shipFormProperty=response.data;
          this.activitiEntity = response.data.activitiEntity;
        });
      }
    },created(){
      this.findOne();
      this.getForm();
    }
  }
</script>

