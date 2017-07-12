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
            <el-form-item :label="$t('goodsOrderShip.searchBusinessId')">
              <input class="el-input__inner" ref="businessIdInput" v-model="businessId" @input="initPage(businessId, false)"
                     @keyup.enter="initPage(businessId, true)" placeholder="请输入订单号末6位加回车或完整订单号"/>
            </el-form-item>
            <div v-show="inputForm.id">
              <el-form-item :label="$t('goodsOrderShip.storeName')">
                {{goodsOrder.storeName}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.formatId')">
                {{goodsOrder.formatId}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.boxImeStr')" prop="boxImeStr">
                <!--回车，删除，ctrl+v，ctrl+x时均触发-->
                <textarea v-model="inputForm.boxImeStr" :rows="5" class="el-textarea__inner"
                          @keyup.enter="checkAndSummary()"
                          @keyup.delete="checkAndSummary()"
                          @keyup.ctrl.86="checkAndSummary()"
                          @keyup.ctrl.88="checkAndSummary()"
                ></textarea>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.expressCodes')" prop="expressCodes">
                <el-input type="textarea" v-model="inputForm.expressCodes" ></el-input>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="12">
            <div v-show="inputForm.id">
              <el-form-item :label="$t('goodsOrderShip.shopName')">
                {{goodsOrder.shopName}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.remarks')">
                {{goodsOrder.remarks}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.continueShip')" prop="continueShip">
                <bool-radio-group v-model="continueShip"></bool-radio-group>
              </el-form-item>
              <el-form-item   :label="$t('goodsOrderShip.imeStr')" prop="imeStr">
                <!--回车，删除，ctrl+v，ctrl+x时均触发-->
                <textarea ref="imeStrTextArea" v-model="inputForm.imeStr" :rows="5" class="el-textarea__inner"
                          @keyup.enter="checkAndSummary()"
                          @keyup.delete="checkAndSummary()"
                          @keyup.ctrl.86="checkAndSummary()"
                          @keyup.ctrl.88="checkAndSummary()"
                ></textarea>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShip.shipRemarks')" prop="shipRemarks">
                <el-input type="textarea" v-model="inputForm.shipRemarks"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderShip.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <div v-show="inputForm.id">
          <el-table :data="goodsOrder.goodsOrderDetailDtoList" :row-class-name="tableRowClassName" style="margin-top:5px;"  :element-loading-text="$t('goodsOrderShip.loading')" stripe border >
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
    },
    methods:{
      getData() {
        return{
          mediaNotify: mediaNotify,
          mediaSuccess: mediaSuccess,
          continueShip:true,
          submitDisabled:false,
          rules: {},

          businessId:'',
          inputForm:{
            id:null,
            boxImeStr:null,
            expressCodes:null,
            imeStr:null,
            shipRemarks:null,
          },
          goodsOrder:{
            goodsOrderDetailDtoList:[],
          },
          shipResult:{},
        }
      },
      formSubmit() {
        this.submitDisabled = true;
        let checkAndSummaryPromise = this.checkAndSummary();
        checkAndSummaryPromise.then((checkResult)=>{
          if(checkResult === "error" ){
            this.$alert("请先处理错误信息");
            this.submitDisabled = false;
          }else if(checkResult === "warning"){
            this.$confirm("还有货品未发送完，确认保存？").then(()=>{
              this.doSubmit();
            }).catch(()=>{
              this.submitDisabled = false;
            });
          }else{
            this.doSubmit();
          }
        });
      }, doSubmit(){
        axios.post('/api/ws/future/crm/goodsOrderShip/ship', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
          this.$message(response.data.message);
          this.submitDisabled = false;
          if(this.continueShip){
            this.initPage(null, true);
          } else {
            this.$router.push({name:'goodsOrderShipList',query:util.getQuery("goodsOrderShipList"), params:{_closeFrom:true}});
          }
        }).catch(()=> {
          this.submitDisabled = false;
        });
      }, checkAndSummary(){
        return axios.get('/api/ws/future/crm/goodsOrderShip/shipCheck',{params:{id:this.inputForm.id,boxImeStr:this.inputForm.boxImeStr,imeStr:this.inputForm.imeStr}}).then((response) => {
          this.shipResult = response.data;
          this.refreshDetailShipInfo(this.shipResult.shipQtyMap);

          let errorMsg = "";
          for(let eachError of this.shipResult.restResponse.errors) {
            errorMsg = errorMsg + eachError.message + "<br/>";
          }
          this.shipResult.errorMsg = errorMsg;

          if(util.isNotBlank(errorMsg)) {
            this.$refs.mediaNotify.play();
            return "error";
          } else if(util.isNotBlank(this.shipResult.warnMsg)){
            return "warning";
          }else{
            this.$refs.mediaSuccess.play();
            return "success";
          }
        });
      },refreshDetailShipInfo(shipQtyMap){
        if(!this.goodsOrder.goodsOrderDetailDtoList){
          return ;
        }
        for(let item of this.goodsOrder.goodsOrderDetailDtoList) {
          if(item.hasIme) {
            item.shipQty = (shipQtyMap[item.productId] ? shipQtyMap[item.productId] : 0);
            item.leftQty = item.realBillQty - item.shippedQty - item.shipQty;
          } else {
            item.shipQty = item.realBillQty-item.shippedQty;
            item.leftQty = 0;
          }
        }
      },
      focusOnImeStrTextArea(){
        this.$nextTick(()=>{
          this.$refs.imeStrTextArea.focus();
        });
      },focusOnBusinessIdInput(){
        this.$nextTick(()=>{
          this.$refs.businessIdInput.focus();
        });
      },
      initPage(businessId, searchImmediately){
        this.businessId = businessId;
        this.shipResult={};

        if( util.isNotBlank(businessId) && (_.trim(businessId).length === 12 || _.trim(businessId).length===14 || searchImmediately)){
          axios.get('/api/ws/future/crm/goodsOrderShip/getShipByBusinessId',{params: {businessId:businessId}}).then((response)=>{
            this.goodsOrder = response.data;
            this.inputForm.id  = this.goodsOrder.id;
            this.inputForm.expressCodes  = this.goodsOrder.expressOrderExpressCodes;
            this.inputForm.boxImeStr  = null;
            this.inputForm.imeStr  = null;
            this.inputForm.shipRemarks  = null;
            if(util.isNotBlank(this.goodsOrder.id)){
              this.focusOnImeStrTextArea();
            }else{
              this.$message("该单号不存在或者不是待发货状态");
              this.focusOnBusinessIdInput();
            }
          });
        }else{
          this.goodsOrder ={goodsOrderDetailDtoList:[]};
          this.inputForm={
            id:null,
            boxImeStr:null,
            expressCodes:null,
            imeStr:null,
            shipRemarks:null,
          };
          this.focusOnBusinessIdInput();
        }
      },
      tableRowClassName(row, index){
        if (row.leftQty !== 0) {
          return "row-unfinished";
        }else{
          return "row-finished";
        }
      }
    },created(){
      this.initPage(this.$route.query.businessId, true);
    }
  }
</script>
<style>
  .el-table .row-unfinished,.el-table .row-unfinished>td{
    background: rgba(255,73,73,.1) !important;
  }
  .el-table .row-finished,.el-table .row-finished>td{
    background: rgba(18,206,102,.1) !important;
  }
</style>
