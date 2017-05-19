<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
          <el-select v-model="inputForm.allotType"  clearable @change="refreshForm" >
            <el-option v-for="item in inputForm.allotTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStore">
          <depot-select :disabled="inputForm.allotType=='快速调拨'" category="store" v-model="inputForm.fromStoreId" @input="refreshForm"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStore">
          <depot-select :disabled="inputForm.allotType=='快速调拨'" category="store" v-model="inputForm.toStoreId"  ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType"  clearable >
            <el-option v-for="item in inputForm.shipTypeList":key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.expressCompany')" prop="expressCompany">
          <express-company-select v-model="inputForm.expressCompanyId"></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.syn')" prop="syn">
          <el-radio-group v-model="inputForm.syn">
            <el-radio :label="true">{{$t('storeAllotForm.yes')}}</el-radio>
            <el-radio :label="false">{{$t('storeAllotForm.no')}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>


        <template>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('storeAllotForm.save')}}</el-button>
          </el-form-item>
          <el-table :data="inputForm.storeAllotDetailFormList" border stripe>
            <el-table-column  :label="$t('storeAllotForm.productName')" prop="productName"></el-table-column>
            <el-table-column  :label="$t('storeAllotForm.cloudQty')" prop="cloudQty"></el-table-column>
            <el-table-column :label="$t('storeAllotForm.billQty')">
              <template scope="scope">
                <input type="text" v-model="scope.row.billQty" class="el-input__inner"/>
              </template>
            </el-table-column>

          </el-table>
        </template>
      </el-form>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select'
  import expressCompanySelect from 'components/future/express-company-select'
  import productSelect from 'components/future/product-select'
  export default{
    components:{
      depotSelect,
      expressCompanySelect,
      productSelect
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:'',
          allotType:'',
          fromStoreId:'',
          toStoreId:'',
          shipType:'',
          expressCompanyId:'',
          syn:'',
          remarks:'',
          storeAllotDetailFormList:[],
        },
        rules: {
          allotType: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          fromStoreId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          toStoreId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          shipType: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          expressCompanyId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          syn: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
        },
        productName:'',
        filterStoreAllotDetailFormList:[],
      }
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/ws/future/crm/storeAllot/save', qs.stringify(this.submitData, {allowDots: true})).then((response) => {
              if (response.data.message) {
                this.$message(response.data.message);
              }
              if (this.isCreate) {
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name: 'storeAllotList', query: util.getQuery("storeAllotList")});
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, refreshForm() {

        axios.get('/api/ws/future/crm/storeAllot/getForm', {
          params: {
            id: this.$route.query.id,
            allotType: this.inputForm.allotType,
            fromStoreId: this.inputForm.fromStoreId
          }
        }).then((response) => {
          this.inputForm = response.data;
        });
      }, refreshForm() {
        axios.get('/api/ws/future/crm/storeAllot/getForm', {
          params: {
            id: this.$route.query.id,
            allotType: this.inputForm.allotType,
            fromStoreId: this.inputForm.fromStoreId
          }
        }).then((response) => {
          this.inputForm = response.data;
        });
      }
    },created(){
      axios.get('/api/ws/future/crm/storeAllot/getForm', {params: {id: this.$route.query.id}}).then((response) => {
        this.inputForm = response.data;

      });
    }
  }
</script>
