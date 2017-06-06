<template>
  <div>
    <head-tab active="goodsOrderShip"></head-tab>
    <div>
      <audio ref="mediaNotify"><source :src="mediaNotify"  type="audio/ogg"></audio>
      <audio ref="mediaSuccess"><source :src="mediaSuccess"  type="audio/ogg"></audio>
      <su-alert  :text="shipResult.warnMsg"  type="warning"></su-alert>
      <su-alert :text="shipResult.errorMsg" type="danger"></su-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form" style="margin-top: 10px;">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderShip.businessId')" prop="businessId">
              <el-input v-model="inputForm.formatId"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.storeName')" prop="storeId">
              {{inputForm.storeName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.boxImeStr')" prop="boxImeStr">
              <textarea  v-model="inputForm.boxImeStr" :rows="5" class="el-textarea__inner"
                         @paste="showSummary(false,100)"
                         @keyup.enter="showSummary(false)"
                         @keyup.delete="showSummary(false)"
                         @keyup.backspace="showSummary(false)"
                         @keyup.control="showSummary(false)">
              </textarea>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.expressCodes')" prop="expressCodes">
              <el-input type="textarea" v-model="inputForm.expressCodes" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderShip.shopName')" prop="shopId">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.remarks')" prop="remarks">
              {{inputForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.redirectView')" prop="redictView">
              <bool-radio-group v-model="inputForm.redirectView"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.imeStr')" prop="imeStr">
              <textarea v-model="inputForm.imeStr"  :rows="5" class="el-textarea__inner"
                        @keyup.enter="showSummary(false,100)"
                        @keyup.delete="showSummary(false)"
                        @keyup.backspace="showSummary(false)"
                        @keyup.control="showSummary(false)">
              </textarea>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.shipRemarks')" prop="shipRemarks">
              <el-input type="textarea" v-model="inputForm.shipRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="showSummary(true)">{{$t('goodsOrderShip.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="inputForm.goodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderShip.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('goodsOrderShip.productName')" sortable width="200"></el-table-column>
          <el-table-column prop="hasIme" :label="$t('goodsOrderShip.hasIme')" >
            <template scope="scope">
              <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="billQty"  :label="$t('goodsOrderShip.billQty')"></el-table-column>
          <el-table-column prop="returnQty" :label="$t('goodsOrderShip.returnQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('goodsOrderShip.shippedQty')"></el-table-column>
          <el-table-column prop="shipQty" :label="$t('goodsOrderShip.shipQty')" ></el-table-column>
          <el-table-column prop="shipQty" :label="$t('goodsOrderShip.shipQty')"></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import mediaNotify from "assets/media/notify.mp3"
  import mediaSuccess from "assets/media/success.mp3"
  import boolRadioGroup from 'components/common/bool-radio-group'
  export default{
    components:{
      boolRadioGroup
    },
    data(){
      return{
        mediaNotify: mediaNotify,
        mediaSuccess: mediaSuccess,
        submitDisabled:false,
        inputForm:{},
        goodsOrder:{},
        shipResult:{},
        submitData:{
          id:'',
          businessId:'',
          boxImeStr:'',
          expressCodes:'',
          redirectView:'',
          imeStr:'',
          shipRemarks:''
        },
        rules: {},
        pageLoading:false,
      }
    },
    methods:{
      summary(isSubmit){
        if(isSubmit) {
          this.submitDisabled = true;
        }
        var boxImeStr=this.inputForm.boxImeStr;
        var imeStr=this.inputForm.imeStr;
        this.pageLoading = true;
        axios.get('/api/ws/future/crm/goodsOrderShip/shipCheck',{params:{id:this.inputForm.id,boxImeStr:boxImeStr,imeStr:imeStr}}).then((response) => {
          this.shipResult = response.data;
          this.pageLoading = false;
          //错误信息
          var errorMsg = "";
          for(var index in this.shipResult.restResponse.errors) {
            errorMsg = errorMsg + this.shipResult.restResponse.errors[index].message + "<br/>";
          }
          this.shipResult.errorMsg = errorMsg;
          //如果提交表单
          if(isSubmit) {
            if(this.shipResult.restResponse.success) {


            } else {
              alert("请先处理错误信息");
            }
          }
          this.submitDisabled = false;
        });
      },showSummary(isSubmit,timeout) {
        if(timeout != null) {
          setTimeout(this.summary(isSubmit), timeout);
        } else {
          this.summary(isSubmit);
        }
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrderShip/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      });
    }
  }
</script>

