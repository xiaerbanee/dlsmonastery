<template>
  <div>
    <head-tab active="productImeBatchQuery"></head-tab>
    <div>
      <el-form ref="inputForm"   label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item>
              <su-alert :text="errMsg" type="danger"> </su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeBatchQuery.ime')" prop="allImeStr">
              <el-input type="textarea" :rows="6" v-model="allImeStr" :placeholder="$t('productImeBatchQuery.inputAllIme')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-button  type="primary" @click.native="onAllImeStrChange">{{$t('productImeBatchQuery.search')}}</el-button>
              <el-button  type="primary" @click.native="exportData">{{$t('productImeBatchQuery.export')}}</el-button>
              <el-button  type="primary" @click.native="reset">{{$t('productImeBatchQuery.reset')}}</el-button>

            </el-form-item>

          </el-col>
          <el-col :span="18" v-if="searched">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('productImeBatchQuery.name')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeBatchQuery.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="companyName" :label="$t('productImeBatchQuery.companyName')"></el-table-column>
                <el-table-column prop="ime" :label="$t('productImeBatchQuery.ime')"></el-table-column>
                <el-table-column prop="ime2" :label="$t('productImeBatchQuery.ime2')"></el-table-column>
                <el-table-column prop="boxIme" :label="$t('productImeBatchQuery.boxIme')"></el-table-column>
                <el-table-column prop="meid" :label="$t('productImeBatchQuery.meid')"></el-table-column>
                <el-table-column prop="depotName" :label="$t('productImeBatchQuery.depotName')"></el-table-column>
                <el-table-column prop="productName"  :label="$t('productImeBatchQuery.productType')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeBatchQuery.baokaDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('productImeBatchQuery.productImeSaleEmployeeName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeBatchQuery.productImeSaleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeUploadMonth" :label="$t('productImeBatchQuery.productImeUploadMonth')"></el-table-column>
                <el-table-column prop="productImeUploadEmployeeName" :label="$t('productImeBatchQuery.productImeUploadEmployeeName')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeBatchQuery.productImeUploadCreatedDate')"></el-table-column>

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


  export default{

    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          searched:false,
          allImeStr:'',
          errMsg:'',
          productImeList:[],
          productQtyList:[],

          rules: {
            allImeStr: [{ required: true, message: this.$t('productImeBatchQuery.prerequisiteMessage')}],
          },
        }
      }, onAllImeStrChange(){
        this.searched = true;
        axios.post('/api/ws/future/crm/productIme/batchQuery',qs.stringify({allImeStr: this.allImeStr})).then((response)=>{
          this.errMsg=response.data.errMsg;
          this.productImeList=response.data.productImeList;

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
      },
      exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.post('/api/ws/future/crm/productIme/batchExport',qs.stringify({allImeStr: this.allImeStr})).then((response)=>{
            window.location.href="/api/ws/future/crm/productIme/download?tempFileName="+response.data;
          });
        }).catch(()=>{});
      },
      reset(){
        this.searched = false;
        this.allImeStr = '';
        this.errMsg='';
        this.productImeList=[];
        this.productQtyList = [];
      }
    }
  }
</script>
