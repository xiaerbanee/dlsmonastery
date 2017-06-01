<template>
  <div>
    <head-tab active="imeAllotForm"></head-tab>
    <div>
      <el-form :model="imeAllot" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item  :label="$t('imeAllotForm.imeStr')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr" :placeholder="$t('imeAllotForm.inputIme')" @change="imeStrChanged"></el-input>
            </el-form-item>
            <el-form-item :label="$t('imeAllotForm.toDepotId')" prop="toDepotId" v-if="imeStr !==''">
              <depot-select v-model="imeAllot.toDepotId"  category="shop" ></depot-select>
            </el-form-item>
            <el-form-item :label="$t('imeAllotForm.remarks')" prop="remarks" v-if="imeStr !==''">
              <el-input type="textarea" :rows="2" v-model="imeAllot.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="imeStr !==''">{{$t('imeAllotForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="16" v-if="imeStr !==''">
            <template>
              <el-alert :title="errMsg" type="error"  v-if="errMsg !==''"> </el-alert>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('imeAllotForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('imeAllotForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('imeAllotForm.imeStr')"></el-table-column>
                <el-table-column prop="depotName":label="$t('imeAllotForm.depotName')"></el-table-column>
                <el-table-column prop="productName" :label="$t('imeAllotForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('imeAllotForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName":label="$t('imeAllotForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('imeAllotForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('imeAllotForm.uploadShopName')"></el-table-column>
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


  export default{
    components:{
      depotSelect,


    },
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            imeAllot:{},
            imeStr:'',
            productImeList:[],
            productQtyList:[],
            submitData:{
              imeStr:'',
              toDepotId:"",
              remarks:''
            },
            rules: {
              imeStr: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}],
              toDepotId: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}]
            },
            errMsg:''
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];

          form.validate((valid) => {
            if (valid) {
                this.initSubmitDataBeforeSubmit();
              axios.post('/api/ws/future/crm/imeAllot/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  if (this.isCreate) {
                    form.resetFields();
                  } else {
                    this.$router.push({name: 'imeAllotList', query: util.getQuery("imeAllotList")})
                  }
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }
          })
        }, initSubmitDataBeforeSubmit(){
          this.submitData.imeStr = this.imeStr;
          this.submitData.toDepotId = this.imeAllot.toDepotId;
          this.submitData.remarks = this.imeAllot.remarks;
        },imeStrChanged(){
          axios.get('/api/ws/future/crm/imeAllot/checkForImeAllot',{params:{imeStr:this.imeStr}}).then((response)=>{
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
      },created(){
      axios.get('/api/ws/future/crm/imeAllot/findDto').then((response)=>{
        this.imeAllot=response.data;
      });
    }
  }
</script>
