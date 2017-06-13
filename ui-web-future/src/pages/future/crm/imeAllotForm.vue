<template>
  <div>
    <head-tab active="imeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item >
              <su-alert  type="danger" :text="errMsg"> </su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item  :label="$t('imeAllotForm.imeStr')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('imeAllotForm.inputIme')" ></el-input>
            </el-form-item>
            <el-form-item >
              <el-button  type="primary" @click.native="onImeStrChange">{{$t('imeAllotForm.search')}}</el-button>
              <el-button  type="primary" @click.native="reset">{{$t('imeAllotForm.reset')}}</el-button>

            </el-form-item>
            <div v-if="searched" >
              <el-form-item :label="$t('imeAllotForm.toDepotId')" prop="toDepotId" >
                <depot-select v-model="inputForm.toDepotId"  category="shop" ></depot-select>
              </el-form-item>
              <el-form-item :label="$t('imeAllotForm.remarks')" prop="remarks" >
                <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('imeAllotForm.save')}}</el-button>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="16" v-if="searched">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('imeAllotForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('imeAllotForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('imeAllotForm.imeStr')"></el-table-column>
                <el-table-column prop="depotName" :label="$t('imeAllotForm.depotName')"></el-table-column>
                <el-table-column prop="productName" :label="$t('imeAllotForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('imeAllotForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('imeAllotForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('imeAllotForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('imeAllotForm.saleShopName')"></el-table-column>
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
  import suAlert from 'components/common/su-alert'

  export default{
    components:{
      depotSelect,
      suAlert,
    },
      data(){
        return this.getData()
      },
    methods:{
      getData() {
          return{
            searched:false,
            submitDisabled:false,
            inputForm:{
                extra:{},
            },
            productImeList:[],
            productQtyList:[],
            rules: {
              imeStr: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}],
              toDepotId: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}]
            },
            errMsg:''
          }
      },
        formSubmit(){

          this.submitDisabled = true;
          if (this.errMsg) {
            this.$alert( this.$t('imeAllotForm.formInvalid'), this.$t('imeAllotForm.notify'));
            return;
          }

          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/crm/imeAllot/save',qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                this.initPage();
              }).catch(()=>{
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          });
        },onImeStrChange(){
            this.searched = true;
          axios.get('/api/ws/future/crm/imeAllot/checkForImeAllot',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
            this.errMsg=response.data;
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
        },reset(){
        this.searched = false;
        this.errMsg='';
        this.productImeList=[];
        this.productQtyList = [];
        this.$refs["inputForm"].resetFields();
      },initPage(){
        axios.get('/api/ws/future/crm/imeAllot/getForm').then((response)=>{
            this.inputForm = response.data;
        });
      }
    },created () {
          this.initPage();
      }
  }
</script>
