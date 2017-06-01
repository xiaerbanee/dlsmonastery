<template>
  <div>
    <head-tab active="productImeUploadForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('productImeUploadForm.inputIme')" @change="onchange"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.month')" prop="month" v-if="inputForm.imeStr !==''">
              <month-picker  v-model="inputForm.month" ></month-picker>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.shopId')" prop="shopId" v-if="inputForm.imeStr !==''">
              <depot-select v-model="inputForm.shopId"  category="shop"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.saleEmployee')" prop="employeeId" v-if="inputForm.imeStr !==''">
              <employee-select v-model="inputForm.employeeId" ></employee-select>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.remarks')" prop="remarks" v-if="inputForm.imeStr !==''">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.imeStr !==''">{{$t('productImeUploadForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="18" v-if="inputForm.imeStr !==''">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeUploadForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeUploadForm.ime')"></el-table-column>
                <el-table-column prop="depotName" :label="$t('productImeUploadForm.depotName')"></el-table-column>
                <el-table-column prop="productName" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeUploadForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('productImeUploadForm.saleShopName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedByName" :label="$t('productImeUploadForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeUploadForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeUploadShopName" :label="$t('productImeUploadForm.uploadShopName')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedByName" :label="$t('productImeUploadForm.saleEmployee')"></el-table-column>
              </el-table>
            </template>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
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
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            productImeList:[],
            productQtyList:[],
            inputForm:{
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
            message:'',
          }
      },
      methods:{
        formSubmit(){

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              axios.post('/api/crm/productImeUpload/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success){
                  if(this.isCreate){
                    form.resetFields();
                  } else {
                    this.$router.push({name:'productImeUploadList',query:util.getQuery("productImeUploadList")})
                  }
                }
              }).catch( () => {
                this.submitDisabled = false;
              });
            }
          });
        },onchange(){

            axios.get('/api/ws/future/crm/productIme/search',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
              this.message=response.data;
            });
          axios.get('/api/ws/future/crm/productIme/findDtoListByImes',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
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
      }
    }
</script>
