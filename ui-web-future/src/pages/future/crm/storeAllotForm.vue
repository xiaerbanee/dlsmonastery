<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm"  label-width="120px"  class="form input-form">
        <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
          <el-select v-if="inputForm.showAllotType" v-model="allotType"  clearable @change="allotTypeChanged" >
            <el-option v-for="item in inputForm.extra.allotTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStoreId">
          <depot-select :disabled="allotType=='快速调拨'" category="store" v-model="inputForm.fromStoreId" @input="fromStoreChanged"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStoreId">
          <depot-select :disabled="allotType=='快速调拨'" category="store" v-model="inputForm.toStoreId"  ></depot-select>
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
          <bool-radio-group v-model="syn"></bool-radio-group>
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
          storeAllot:{},
          allotType:null,
          syn:true,
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
        var that = this;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            this.initSubmitDataBeforeSubmit();
            axios.post('/api/ws/future/crm/storeAllot/save', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
              this.$message(response.data.message);
            if(response.data.success) {
              if (!that.isCreate) {
                this.submitDisabled = false;
                this.$router.push({name: 'storeAllotList', query: util.getQuery("storeAllotList")});
              }else{
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
              }
          }).catch( () => {
              that.submitDisabled = false;
          });
          }
        })
      }, initSubmitDataBeforeSubmit(){

        this.inputForm.allotType = this.allotType;
        this.inputForm.fromStoreId = this.inputForm.fromStoreId;
        this.inputForm.toStoreId = this.inputForm.toStoreId;
        this.inputForm.shipType = this.inputForm.shipType;
        this.inputForm.expressCompanyId = this.inputForm.expressCompanyId;
        this.inputForm.syn = this.syn;
        this.inputForm.remarks = this.inputForm.remarks;

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
            fromStoreId: this.inputForm.fromStoreId
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
      },initPage(){
        axios.get('/api/ws/future/crm/storeAllot/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm= response.data;
        //大库调拨只能新增和删除，不能修改，所以不用传id
        axios.get('/api/ws/future/crm/storeAllot/findDetailListForCommonAllot').then((response)=>{
          this.setStoreAllotDetailList(response.data);
      });
        //大库调拨只能新增和删除，不能修改，所以不用传id
        axios.get('/api/ws/future/crm/storeAllot/findDto').then((response)=>{
        util.copyValue(response.data,this.inputForm);
      });
      });
      }
    },created () {
       this.initPage();
     }
  }
</script>
