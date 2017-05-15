<template>
  <div>
    <head-tab active="goodsOrderForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
        <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
          <depot-select v-model="inputForm.shopId" type="SHOP" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.parentShop')" prop="parentId">
          {{inputForm.parentName}}
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
          <el-select v-model="inputForm.netType"  filterable  clearable :placeholder="$t('goodsOrderForm.inputWord')" >
            <el-option v-for="item in inputForm.netTypeList" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.isUseTicket')" prop="isUseTicket">
          <bool-radio-group v-model="inputForm.isUseTicket"></bool-radio-group>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType" filterable clearable :placeholder="$t('goodsOrderForm.inputKey')" >
            <el-option v-for="item in inputForm.shipTypeList" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderForm.carrierShop')" prop="carrierShopId">
              <!--<el-select v-model="inputForm.carrierShopId" clearable filterable remote ::placeholder="$t('goodsOrderForm.selectWord')" :remote-method="remoteCarrierShop" :loading="remoteLoading">-->
                <!--<el-option v-for="item in carrierShops" :key="item.id" :label="item.name" :value="item.id"></el-option>-->
              <!--</el-select>-->
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.carrierCodes')" prop="carrierCodes">
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.carrierDetails')"  prop="carrierDetails">
              <el-input type="textarea" v-model="inputForm.carrierDetails" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.shopType')" prop="type">
              {{inputForm.shopType}}
          </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
              {{inputForm.priceSystemName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary">
              {{inputForm.summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-table :data="inputForm.goodsOrderDetailFormList" border stripe>
        <el-table-column  :label="$t('goodsOrderForm.productName')" >
          <template scope="scope">
            <product-select v-model ="scope.row.productId" @input="productSelected(scope.row)" ></product-select>
          </template>
        </el-table-column>
        <el-table-column prop="hasIme" :label="$t('goodsOrderForm.hasIme')" width="70">
          <template scope="scope">
            <el-tag  v-if="scope.row.productHasIme!=undefined && scope.row.productHasIme!=null" :type="scope.row.productHasIme ? 'primary' : 'danger'">{{scope.row.productHasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderForm.price')" width="100"></el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
          <template scope="scope">
            <input   type="text" v-model="scope.row.qty" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="productAllowOrderAndBill" :label="$t('goodsOrderForm.allowOrderAndBill')">
          <template scope="scope">
            <el-tag v-if="scope.row.productAllowOrderAndBill!=undefined && scope.row.productAllowOrderAndBill!=null" :type="scope.row.productAllowOrderAndBill? 'primary' : 'danger'">{{scope.row.productAllowOrderAndBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="areaQty" :label="$t('goodsOrderForm.areaQty')" ></el-table-column>
        <el-table-column :render-header="renderAction"  width="100">
          <template scope="scope">
            <el-button size="small" type="danger" @click.prevent="removeDomain(scope.row)">{{$t('storeAllotForm.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import productSelect from 'components/future/product-select'
  import boolRadioGroup from 'components/common/bool-radio-group'

  export default{
    components:{
      depotSelect,
      productSelect,
      boolRadioGroup,

    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        pageLoading:false,
        alertError:false,
        carrierShops:[],
        inputForm:{},
        submitData:{
          id:'',
          shopId:'',
          netType:'',
          shipType:'',
          remarks:'',
          carrierShopId:'',
          carrierCodes:'',
          carrierDetails:'',
          goodsOrderDetailFormList:[],
        },
        rules: {
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.create){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },removeDomain(item) {
        var index = this.inputForm.goodsOrderDetailFormList.indexOf(item);
        if (index !== -1) {
          this.inputForm.goodsOrderDetailFormList.splice(index, 1)
        }
      },renderAction(createElement) {
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
        this.inputForm.goodsOrderDetailFormList.push({});
        return false;
      },productSelected(row){
          console.log("productSelected");
        axios.get('/api/ws/future/crm/goodsOrderDetail/getFormWithTodaysAreaQty?productId='+row.productId+'&depotId='+ this.inputForm.shopId).then((res)=>{
            if(res.data){
              util.copyValue(res.data, row);
            }

//          row.cloudQty=res.data
        });
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;

        this.inputForm.goodsOrderDetailFormList.push({});
      })
    }
  }
</script>
