<template>
  <div>
    <head-tab active="productImeUploadForm"></head-tab>
    <div>
      <el-form :model="productImeUpload" ref="inputForm" label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item>
              <su-alert  type="danger" :text="errMsg"> </su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr" :placeholder="$t('productImeUploadForm.inputIme')" @change="imeStrChanged"></el-input>
            </el-form-item>
            <div v-if="imeStr !==''">
              <el-form-item :label="$t('productImeUploadForm.month')" prop="month">
                <month-picker  v-model="productImeUpload.month"></month-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.shopId')" prop="shopId">
                <depot-select v-model="productImeUpload.shopId"  category="shop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.saleEmployee')" prop="employeeId">
                <employee-select v-model="productImeUpload.employeeId" ></employee-select>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadForm.remarks')" prop="remarks">
                <el-input type="textarea" v-model="productImeUpload.remarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productImeUploadForm.save')}}</el-button>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="18" v-if="imeStr !==''">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeUploadForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeUploadForm.ime')"></el-table-column>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeUploadForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('productImeUploadForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeUploadForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('productImeUploadForm.saleShopName')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedByName" :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadStatus" :label="$t('productImeUploadForm.productImeUploadStatus')"></el-table-column>
                <el-table-column prop="productImeUploadShopName" :label="$t('productImeUploadForm.uploadShopName')"></el-table-column>
                <el-table-column prop="productImeUploadEmployeeName" :label="$t('productImeUploadForm.saleEmployee')"></el-table-column>
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
  import employeeSelect from 'components/basic/employee-select'
  import monthPicker from 'components/common/month-picker'

  export default{
    components:{
      depotSelect,
      employeeSelect,
      monthPicker,
    },
      data(){
        return this.getData()
      },
    methods:{
      getData() {
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            productImeList:[],
            productImeUpload:{},
            imeStr:'',
            errMsg:'',
            productQtyList:[],
            submitData:{
              imeStr:'',
              shopId:'',
              month:"",
              employeeId:"",
              remarks:''
            },
            rules: {
              imeStr: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              shopId: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              month: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              employeeId: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
            },
          }
      },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.productImeUpload, this.submitData);
              this.submitData.imeStr = this.imeStr;
              axios.post('/api/ws/future/crm/productImeUpload/upload',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
                if(response.data.success){
                  if(!this.isCreate){
                    this.$router.push({name:'productImeUploadList',query:util.getQuery("productImeUploadList")})
                  }
                }
              }).catch( () => {
                that.submitDisabled = false;
              });
            }
          });
        },imeStrChanged(){
          axios.get('/api/ws/future/crm/productImeUpload/checkForUpload',{params:{imeStr:this.imeStr}}).then((response)=>{
            this.errMsg=response.data;
          });

          axios.get('/api/ws/future/crm/productIme/findDtoListByImes',{params:{imeStr:this.imeStr}}).then((response)=>{
            this.productImeList=response.data;

            let tmpMap = new Map();
            if(this.productImeList){
                for(let productIme of this.productImeList ){
                    if(!tmpMap.has(productIme.productId)){
                        tmpMap.set(productIme.productId, {productName:productIme.productName, qty:0});
                    }
                    tmpMap.get(productIme.productId).qty+=1;
                }
            }
            let tmpList = [];
            for(let key of tmpMap.keys()){
              tmpList.push(tmpMap.get(key));
            }
            this.productQtyList = tmpList;
          });
        }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
          axios.get('/api/ws/future/crm/productImeUpload/findDto').then((response)=>{
            this.productImeUpload=response.data;
        });
        }
      this.isInit = true;
      }
  }
</script>
