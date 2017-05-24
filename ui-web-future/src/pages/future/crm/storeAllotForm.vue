<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="storeAllot" ref="inputForm"  label-width="120px"  class="form input-form">
        <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
          <el-select v-if="inputProperty.showAllotType" v-model="allotType"  clearable @change="allotTypeChanged" >
            <el-option v-for="item in inputProperty.allotTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStoreId">
          <depot-select :disabled="allotType=='快速调拨'" category="store" v-model="storeAllot.fromStoreId" @input="fromStoreChanged"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStoreId">
          <depot-select :disabled="allotType=='快速调拨'" category="store" v-model="storeAllot.toStoreId"  ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.shipType')" prop="shipType">
          <el-select v-model="storeAllot.shipType"  clearable >
            <el-option v-for="item in inputProperty.shipTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.expressCompany')" prop="expressCompany">
          <express-company-select v-model="storeAllot.expressCompanyId"></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.syn')" prop="syn">
          <bool-radio-group v-model="syn"></bool-radio-group>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="storeAllot.remarks"></el-input>
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
              <input type="text" v-model="scope.row.billQty" class="el-input__inner"/>
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
      return{
        isCreate:this.$route.query.id===null,
        submitDisabled:false,
        inputForm:{},
        inputProperty:{},
        storeAllot:{},
        allotType:null,
        syn:true,
        submitData:{
          allotType:'',
          fromStoreId:'',
          toStoreId:'',
          shipType:'',
          expressCompanyId:'',
          syn:'',
          remarks:'',
          storeAllotDetailList:[],
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
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
              this.initSubmitDataBeforeSubmit();
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
      }, initSubmitDataBeforeSubmit(){

          this.submitData.allotType = this.allotType;
          this.submitData.fromStoreId = this.storeAllot.fromStoreId;
          this.submitData.toStoreId = this.storeAllot.toStoreId;
          this.submitData.shipType = this.storeAllot.shipType;
          this.submitData.expressCompanyId = this.storeAllot.expressCompanyId;
          this.submitData.syn = this.syn;
          this.submitData.remarks = this.storeAllot.remarks;

          if(this.storeAllotDetailList){
            let tempList=[];
            for(let storeAllotDetail of this.storeAllotDetailList){
              if(util.isNotBlank(storeAllotDetail.billQty)){
                tempList.push(storeAllotDetail);
              }
            }
            this.submitData.storeAllotDetailList = tempList;
          }else{
            this.submitData.storeAllotDetailList = [];
          }

      },allotTypeChanged(newVal){
          if('快速调拨' === newVal){
            this.syn = false;
            axios.get('/api/ws/future/crm/storeAllot/findDetailListForFastAllot').then((response) => {
              this.setStoreAllotDetailList(response.data);
            });
            axios.get('/api/ws/future/crm/storeAllot/findStoreAllotForFastAllot').then((response) => {
              this.storeAllot.fromStoreId  = response.data.fromStoreId;
              this.storeAllot.toStoreId  = response.data.toStoreId;
              this.storeAllot.expressCompanyId  = response.data.expressCompanyId;
              this.storeAllot.shipType  = response.data.shipType;
            });

          }else{
            this.storeAllot.fromStoreId  = null;
            this.storeAllot.toStoreId  = null;
            this.storeAllot.expressCompanyId  = null;
            this.storeAllot.shipType  = null;
            this.syn = true;
            axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot').then((response) => {
              this.setStoreAllotDetailList(response.data);
            });
          }

      }, fromStoreChanged() {
          //快速调拨时，fromStore应该不可变更
          if('快速调拨' === this.allotType){
              return;
          }

        axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot', {
          params: {
            fromStoreId: this.storeAllot.fromStoreId
          }
        }).then((response) => {
          this.setStoreAllotDetailList(response.data);
        });
      },setStoreAllotDetailList(list){
        this.storeAllotDetailList = list;
        this.filterProducts();

      },filterProducts(){

        if(!this.storeAllotDetailList){
          this.filterStoreAllotDetailList = [];
          return;
        }
        if(!this.productName){
          this.filterStoreAllotDetailList = this.storeAllotDetailList;
          return;
        }
        let val=this.productName;
        let tempList=[];

        for(let storeAllotDetail of this.storeAllotDetailList){

          if(util.isNotBlank(storeAllotDetail.billQty)){
            tempList.push(storeAllotDetail);
          }
        }
        for(let storeAllotDetail of this.storeAllotDetailList){
          if(util.contains(storeAllotDetail.productName, val) && util.isBlank(storeAllotDetail.billQty)){
            tempList.push(storeAllotDetail);
          }
        }
        this.filterStoreAllotDetailList = tempList;

      }
    },created(){

      axios.get('/api/ws/future/crm/storeAllot/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputProperty = response.data;
      });

      //大库调拨只能新增和删除，不能修改，所以不用传id
      axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot').then((response)=>{
        this.setStoreAllotDetailList(response.data);
      });
      //大库调拨只能新增和删除，不能修改，所以不用传id
      axios.get('/api/ws/future/crm/storeAllot/findDto').then((response)=>{
        this.storeAllot = response.data;
      });

    }
  }
</script>
