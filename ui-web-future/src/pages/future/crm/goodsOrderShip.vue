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
            <el-form-item :label="$t('goodsOrderShip.businessId')">
              <el-input v-model="formatId" @blur="formatIdChanged(formatId)"></el-input>
            </el-form-item>
            <div v-if="goodsOrder.id">
              <el-form-item :label="$t('goodsOrderShip.storeName')">
                {{goodsOrder.storeName}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.boxImeStr')" prop="boxImeStr">
              <textarea  v-model="inputForm.boxImeStr" :rows="5" class="el-textarea__inner" >
              </textarea>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.expressCodes')" prop="expressCodes">
                <el-input type="textarea" v-model="inputForm.expressCodes" ></el-input>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="12">
            <div v-if="goodsOrder.id">
              <el-form-item :label="$t('goodsOrderShip.shopName')">
                {{goodsOrder.shopName}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.remarks')">
                {{goodsOrder.remarks}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.continueShip')" prop="continueShip">
                <bool-radio-group v-model="continueShip"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.imeStr')" prop="imeStr">
                <textarea v-model="inputForm.imeStr" :rows="5" class="el-textarea__inner" >
                </textarea>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.shipRemarks')" prop="shipRemarks">
                <el-input type="textarea" v-model="inputForm.shipRemarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="summary(true)">{{$t('goodsOrderShip.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <div v-if="goodsOrder.id">
          <el-table :data="goodsOrder.goodsOrderDetailDtoList" style="margin-top:5px;" :element-loading-text="$t('goodsOrderShip.loading')" stripe border >
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
            <el-table-column prop="leftQty" :label="$t('goodsOrderShip.leftQty')"></el-table-column>
            <el-table-column prop="finish" :label="$t('goodsOrderShip.finish')" >
              <template scope="scope">
                <el-tag :type="scope.row.leftQty==0 ? 'primary' : 'danger'">{{scope.row.leftQty==0 | bool2str}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
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
      return this.getData();
    },watch: {
      "inputForm.imeStr": function (oldVal,newVal) {
          if(_.trim(oldVal) != _.trim(newVal)) {
            this.summary(false);
          }
      },"inputForm.boxImeStr":function (oldVal,newVal) {
        if(_.trim(oldVal) != _.trim(newVal)) {
          this.summary(false);
        }
      }
    },
    methods:{
      getData() {
        return{
          mediaNotify: mediaNotify,
          mediaSuccess: mediaSuccess,
          continueShip:true,
          submitDisabled:false,
          inputForm:{},
          formatId:'',
          goodsOrder:{
            goodsOrderDetailDtoList:[],
          },
          shipResult:{},
          rules: {}
        }
      },
      formSubmit() {
        axios.post('/api/ws/future/crm/goodsOrderShip/ship', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
          this.$message(response.data.message);
          if(this.continueShip){
            Object.assign(this.$data, this.getData());
            this.initPage("");
          } else {
            this.$router.push({name:'goodsOrderShipList',query:util.getQuery("goodsOrderShipList"), params:{_closeFrom:true}});
          }
        }).catch(()=> {
          this.submitDisabled = false;
        });
      },
      summary(isSubmit){
        if(isSubmit) {
          this.submitDisabled = true;
        }
        var boxImeStr=this.inputForm.boxImeStr;
        var imeStr=this.inputForm.imeStr;
        console.log("boxImeStr"+boxImeStr)
        console.log("imeStr"+imeStr)
        if(util.isBlank(imeStr)&&util.isBlank(boxImeStr)){
          if(isSubmit) {
            alert("请填入发货串码或箱号");
            this.submitDisabled = false;
          }
          this.shipResult.errorMsg ="";

          for(let item of this.goodsOrder.goodsOrderDetailDtoList) {
            if(item.hasIme) {
              item.shipQty = 0;
              item.leftQty = item.realBillQty - item.shippedQty;
            } else {
              item.shipQty = item.realBillQty-item.shippedQty;
              item.leftQty = 0;
            }
          }
          return;
        }
        axios.get('/api/ws/future/crm/goodsOrderShip/shipCheck',{params:{id:this.inputForm.id,boxImeStr:boxImeStr,imeStr:imeStr}}).then((response) => {
          this.shipResult = response.data;
          //错误信息
          var errorMsg = "";
          for(var index in this.shipResult.restResponse.errors) {
            errorMsg = errorMsg + this.shipResult.restResponse.errors[index].message + "<br/>";
          }
          this.shipResult.errorMsg = errorMsg;
          if(util.isNotBlank(errorMsg)) {
            this.$refs.mediaNotify.play();
          } else {
            if(util.isBlank(this.shipResult.warnMsg)) {
              this.$refs.mediaSuccess.play();
            }
          }
          //设置发货数和待发货数
          var shipQtyMap = this.shipResult.shipQtyMap;
          for(var index in this.goodsOrder.goodsOrderDetailDtoList) {
            var item = this.goodsOrder.goodsOrderDetailDtoList[index];
            if(item.hasIme) {
              item.shipQty = shipQtyMap[item.productId];
              item.leftQty = item.realBillQty - item.shippedQty - item.shipQty;
            } else {
              item.shipQty = item.realBillQty-item.shippedQty;
              item.leftQty = 0;
            }
          }
          //如果提交表单
          if(isSubmit) {
            if(util.isBlank(errorMsg)) {
                if(util.isBlank(this.shipResult.warnMsg)) {
                  this.formSubmit();
                } else {
                  if (confirm("还有货品未发送完，确认保存？")) {
                    this.formSubmit();
                  } else {
                    this.submitDisabled = false;
                  }
                }
            } else {
              alert("请先处理错误信息");
              this.submitDisabled = false;
            }
          }
        });
      },initPage(formatId){
        this.formatId = formatId;
        axios.get('/api/ws/future/crm/goodsOrderShip/getForm').then((response)=>{
          this.inputForm = response.data;
          if(util.isNotBlank(formatId)){
            axios.get('/api/ws/future/crm/goodsOrderShip/getShipByFormatId',{params: {formatId:formatId}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
              this.goodsOrder = response.data;
            });
          }
        });
      },formatIdChanged(formatId){
        Object.assign(this.$data, this.getData());
        this.initPage(formatId);
      }
    },created(){
      this.initPage(this.$route.query.formatId);
    }
  }
</script>

