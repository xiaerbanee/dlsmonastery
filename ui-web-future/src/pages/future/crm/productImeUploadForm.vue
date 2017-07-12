<template>
  <div>
    <head-tab active="productImeUploadForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="21">
            <el-form-item>
              <su-alert :text="errMsg" type="danger"></su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr"
                        :placeholder="$t('productImeUploadForm.inputIme')"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click.native="onImeStrChange">{{$t('productImeUploadForm.search')}}</el-button>
              <el-button type="primary" @click.native="reset">{{$t('productImeUploadForm.reset')}}</el-button>

            </el-form-item>
            <div v-if="searched">
              <el-form-item :label="$t('productImeUploadForm.month')" prop="month">
                <month-picker v-model="inputForm.month"></month-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.shopId')" prop="shopId">
                <depot-select v-model="inputForm.shopId" category="shop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.saleEmployee')" prop="employeeId">
                <employee-select v-model="inputForm.employeeId"></employee-select>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.remarks')" prop="remarks">
                <el-input type="textarea" v-model="inputForm.remarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">
                  {{$t('productImeUploadForm.save')}}
                </el-button>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="18" v-if="searched">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border show-summary>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeUploadForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeUploadForm.ime')"></el-table-column>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeUploadForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName"
                                 :label="$t('productImeUploadForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate"
                                 :label="$t('productImeUploadForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName"
                                 :label="$t('productImeUploadForm.saleShopName')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedByName"
                                 :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate"
                                 :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadStatus"
                                 :label="$t('productImeUploadForm.productImeUploadStatus')"></el-table-column>
                <el-table-column prop="productImeUploadShopName"
                                 :label="$t('productImeUploadForm.uploadShopName')"></el-table-column>
                <el-table-column prop="productImeUploadEmployeeName"
                                 :label="$t('productImeUploadForm.saleEmployee')"></el-table-column>
              </el-table>
            </template>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  .el-table {
    margin-bottom: 50px;
  }
</style>
<script>

  import depotSelect from 'components/future/depot-select'
  import employeeSelect from 'components/basic/employee-select'
  import monthPicker from 'components/common/month-picker'

  export default{
    components: {
      depotSelect,
      employeeSelect,
      monthPicker,
    },
    data(){
      return this.getData()
    },
    methods: {
      getData() {
        return {
          searched: false,
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          productImeList: [],
          inputForm: {
            extra: {}
          },
          errMsg: '',
          productQtyList: [],
          rules: {
            imeStr: [{required: true, message: this.$t('productImeUploadForm.prerequisiteMessage'),trigger:"blur"}],
            shopId: [{required: true, message: this.$t('productImeUploadForm.prerequisiteMessage'),trigger:"change"}],
            month: [{required: true, message: this.$t('productImeUploadForm.prerequisiteMessage'),trigger:"change"}],
            employeeId: [{required: true, message: this.$t('productImeUploadForm.prerequisiteMessage'),trigger:"change"}],
          },
        }
      },
      formSubmit(){

        if (this.errMsg) {
          this.$alert(this.$t('productImeUploadForm.formInvalid'), this.$t('productImeUploadForm.notify'));
          return;
        }

        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            axios.post('/api/ws/future/crm/productImeUpload/upload', qs.stringify(submitData)).then((response) => {
              this.$message(response.data.message);
            if (response.data.success) {
              if (!this.isCreate) {
                this.$router.push({name: 'productImeUploadList', query: util.getQuery("productImeUploadList"), params:{_closeFrom:true}})
              } else {
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }
          }).
            catch(() => {
              this.submitDisabled = false;
          });
          }
        }
      );
      }, onImeStrChange(){
        this.searched = true;
        axios.post('/api/ws/future/crm/productImeUpload/checkForUpload', qs.stringify({imeStr: this.inputForm.imeStr})).then((response) =>
        {
          this.errMsg = response.data;
        }
      );
        axios.post('/api/ws/future/crm/productIme/findDtoListByImes', qs.stringify({imeStr: this.inputForm.imeStr})).then((response) =>
        {
          this.productImeList = response.data;

          let tmpMap = new Map();
          if (this.productImeList) {
            for (let productIme of this.productImeList) {
              if (!tmpMap.has(productIme.productId)) {
                tmpMap.set(productIme.productId, {productName: productIme.productName, qty: 0});
              }
              tmpMap.get(productIme.productId).qty += 1;
            }
          }
          let tmpList = [];
          for (let key of tmpMap.keys()) {
            tmpList.push(tmpMap.get(key));
          }
          this.productQtyList = tmpList;
        }
      );
      }, reset(){
        this.searched = false;
        this.errMsg = '';
        this.productImeList = [];
        this.productQtyList = [];
        this.$refs["inputForm"].resetFields();
      }, initPage(){
        axios.get('/api/ws/future/crm/productImeUpload/getForm').then((response) => {
          this.inputForm = response.data;
      })
      }
    }, created () {
      this.initPage();
    }
  }
</script>
