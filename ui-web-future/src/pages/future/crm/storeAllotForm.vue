<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm"  label-width="120px"  class="form input-form">
        <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
          <el-select v-if="inputForm.extra.showAllotType" v-model="inputForm.allotType"  clearable @change="allotTypeChanged" >
            <el-option v-for="item in inputForm.extra.allotTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStoreId">
          <depot-select :disabled="inputForm.allotType=='快速调拨'" category="store" v-model="inputForm.fromStoreId" @input="fromStoreChanged"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStoreId">
          <depot-select :disabled="inputForm.allotType=='快速调拨'" category="store" v-model="inputForm.toStoreId"  ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType"  clearable>
            <el-option v-for="item in inputForm.extra.shipTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.expressCompany')" prop="expressCompany">
          <express-company-select v-model="inputForm.expressCompanyId"></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.syn')" prop="syn">
          <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('storeAllotForm.save')}}</el-button>
        </el-form-item>
      </el-form>
      <div>
        <el-input v-model="productName" @input="filterProducts" :placeholder="$t('storeAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table :data="filterStoreAllotDetailList" border stripe>
          <el-table-column  :label="$t('storeAllotForm.productName')" prop="productName"></el-table-column>
          <el-table-column  :label="$t('storeAllotForm.cloudQty')" prop="cloudQty"></el-table-column>
          <el-table-column :label="$t('storeAllotForm.billQty')">
            <template scope="scope">
              <el-input v-model.number="scope.row.billQty"></el-input>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select'
  import expressCompanySelect from 'components/future/express-company-select'
  import boolRadioGroup from 'components/common/bool-radio-group'


  export default{
    components:{
      depotSelect,
      expressCompanySelect,
      boolRadioGroup,
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
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
          filterStoreAllotDetailList:[],
        }
      },
      formSubmit(){
        let validateMsg = this.customValidate();
        if(util.isNotBlank(validateMsg)){
          this.$alert(validateMsg);
          return;
        }

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData =util.deleteExtra(this.inputForm);
            submitData.storeAllotDetailList = this.getDetailListForSubmit();
            axios.post('/api/ws/future/crm/storeAllot/save', qs.stringify(submitData, {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success) {
                  if (!this.isCreate) {
                    this.$router.push({name: 'storeAllotList', query: util.getQuery("storeAllotList")});
                  }else{
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }
                }
            }).catch( () => {
                this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        });
      }, customValidate(){
        let totalBillQty = 0;
        for(let storeAllotDetail of this.inputForm.storeAllotDetailList){
          if(util.isBlank(storeAllotDetail.billQty)){
            continue;
          }

          if(!Number.isInteger(storeAllotDetail.billQty) || storeAllotDetail.billQty < 0){
            return '产品：'+storeAllotDetail.productName+'的调拨数不是一个大于等于0的整数';
          }

          totalBillQty += storeAllotDetail.billQty;
        }
        if(totalBillQty<=0){
          return "总调拨数要大于0";
        }
        return null;
      }, getDetailListForSubmit(){

        if(this.inputForm.storeAllotDetailList){
          let tempList=[];
          for(let storeAllotDetail of this.inputForm.storeAllotDetailList){
            if(Number.isInteger(storeAllotDetail.billQty) && storeAllotDetail.billQty>0 ){
              tempList.push(storeAllotDetail);
            }
          }
          return tempList;
        }else{
          return [];
        }

      },allotTypeChanged(newVal){
        if('快速调拨' === newVal){
          this.inputForm.syn = false;
          axios.get('/api/ws/future/crm/storeAllot/findDetailListForFastAllot').then((response) => {
            this.setStoreAllotDetailList(response.data);
          });
          axios.get('/api/ws/future/crm/storeAllot/findStoreAllotForFastAllot').then((response) => {
            this.inputForm.fromStoreId  = response.data.fromStoreId;
            this.inputForm.toStoreId  = response.data.toStoreId;
            this.inputForm.expressCompanyId  = response.data.expressCompanyId;
            this.inputForm.shipType  = response.data.shipType;
          });

        }else{
          this.inputForm.fromStoreId  = null;
          this.inputForm.toStoreId  = null;
          this.inputForm.expressCompanyId  = null;
          this.inputForm.shipType  = null;
          this.inputForm.syn = true;
          axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot').then((response) => {
            this.setStoreAllotDetailList(response.data);
          });
        }

      }, fromStoreChanged() {
        //快速调拨时，fromStore应该不可变更
        if('快速调拨' === this.inputForm.allotType){
          return;
        }

        axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot', {params: {fromStoreId: this.inputForm.fromStoreId}}).then((response) => {
          this.setStoreAllotDetailList(response.data);
        });
      },setStoreAllotDetailList(list){
        this.inputForm.storeAllotDetailList = list;
        this.filterProducts();

      },filterProducts(){

        if(!this.inputForm.storeAllotDetailList){
          this.filterStoreAllotDetailList = [];
          return;
        }
        if(!this.productName){
          this.filterStoreAllotDetailList = this.inputForm.storeAllotDetailList;
          return;
        }
        let val=this.productName;
        let tempList=[];

        for(let storeAllotDetail of this.inputForm.storeAllotDetailList){

          if(util.isNotBlank(storeAllotDetail.billQty)){
            tempList.push(storeAllotDetail);
          }
        }
        for(let storeAllotDetail of this.inputForm.storeAllotDetailList){
          if(util.contains(storeAllotDetail.productName, val) && util.isBlank(storeAllotDetail.billQty)){
            tempList.push(storeAllotDetail);
          }
        }
        this.filterStoreAllotDetailList = tempList;
      },initPage(){
        axios.get('/api/ws/future/crm/storeAllot/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm= response.data;
          //大库调拨只能新增和删除，不能修改，所以不用传id
          axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot').then((response)=>{
            this.setStoreAllotDetailList(response.data);
          });
      });
      }
    },created () {
       this.initPage();
     }
  }
</script>
