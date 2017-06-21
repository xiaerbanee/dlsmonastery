<template>
  <div>
    <head-tab active="adGoodsOrderDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.orderCode')">
              {{adGoodsOrder.id}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.billDate')">
              {{adGoodsOrder.billDate}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.storeName')">
              {{adGoodsOrder.storeName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.outShopName')">
              {{adGoodsOrder.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.shopName')">
              {{adGoodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.threeMonthQty')">
              {{recentSaleDescription}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.investInCause')">
              {{adGoodsOrder.investInCause}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.imageDeposit')">
              {{imageDeposit}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.employeeFullName')">
              {{employee.officeName}}_{{employee.positionName}}_{{employee.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.employeeMobilePhone')">
              {{employee.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.processStatus')">
              {{adGoodsOrder.processStatus}}
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.expressCodes')">
              {{adGoodsOrder.expressOrderExpressCodes}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.createdBy')">
              {{adGoodsOrder.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.remarks')">
              {{adGoodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.contact')">
              {{adGoodsOrder.expressOrderContator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.address')">
              {{adGoodsOrder.expressOrderAddress}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.billAddress')">
              {{adGoodsOrder.billAddress}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.mobilePhone')">
              {{adGoodsOrder.expressOrderMobilePhone}}
            </el-form-item>
            <div v-if="audit">
              <el-form-item :label="$t('adGoodsOrderDetail.pass')" prop="pass">
                <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderDetail.comment')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">
                  {{$t('adGoodsOrderDetail.audit')}}
                </el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-table :data="adGoodsOrderDetailList" style="margin-top:5px;" stripe border>
          <el-table-column prop="productCode" :label="$t('adGoodsOrderDetail.code')"></el-table-column>
          <el-table-column prop="productName" :label="$t('adGoodsOrderDetail.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('adGoodsOrderDetail.qty')"></el-table-column>
          <el-table-column prop="confirmQty" :label="$t('adGoodsOrderDetail.confirmQty')"></el-table-column>
          <el-table-column prop="billQty" :label="$t('adGoodsOrderDetail.billQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('adGoodsOrderDetail.shippedQty')"></el-table-column>
          <el-table-column prop="productPrice2" :label="$t('adGoodsOrderDetail.price')"></el-table-column>
        </el-table>

        <process-details v-model="adGoodsOrder.processInstanceId"></process-details>
      </el-form>
    </div>
  </div>
</template>
<script>
  import processDetails from 'components/general/process-details';
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components: {processDetails, boolRadioGroup},
    data(){
      return {
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        audit: this.$route.query.action === 'audit',
        adGoodsOrder: {},
        imageDeposit: 0,
        employee: {},
        adGoodsOrderDetailList: [],
        inputForm: {
          id: this.$route.query.id,
          pass: false,
          remarks: "",
        },
        rules: {
          pass: {required: true, message: this.$t('adGoodsOrderDetail.prerequisiteMessage')},
        },
        recentSaleDescription: '',
      }
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/adGoodsOrder/audit', qs.stringify(this.inputForm, {allowDots: true})).then((response) => {
              this.$message(response.data.message);

              this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})

            }).catch(() => {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, refreshRecentMonthSaleAmount(){
        if (util.isBlank(this.adGoodsOrder.shopId)) {
          this.recentSaleDescription = '';
          return;
        }

        axios.get('/api/ws/future/basic/depot/getRecentMonthSaleAmount', {params: {depotId: this.adGoodsOrder.shopId, monthQty: 3}}).then((response) => {
          if (response.data) {
            let tmp = '';
            for (let key in response.data) {
              tmp = tmp + key + "销量：" + response.data[key] + "台；";
            }
            this.recentSaleDescription = tmp;
          } else {
            this.recentSaleDescription = '';
          }
        });
      }
    }, created(){
      axios.get('/api/ws/future/layout/adGoodsOrder/findDto', {params: {id: this.$route.query.id}}).then((response) => {
        this.adGoodsOrder = response.data;
        this.refreshRecentMonthSaleAmount();

        axios.get('/api/basic/hr/employee/findOne', {params: {id: this.adGoodsOrder.employeeId}}).then((response) => {
          this.employee = response.data;
        });
        axios.get('/api/ws/future/crm/shopDeposit/findLeftAmount', {params: {type: '形象保证金', depotId: this.adGoodsOrder.shopId}}).then((response) => {
          this.imageDeposit = response.data;
        });
      });
      axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListByAdGoodsOrderId', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
        this.adGoodsOrderDetailList = response.data;
      });
    }
  }
</script>
