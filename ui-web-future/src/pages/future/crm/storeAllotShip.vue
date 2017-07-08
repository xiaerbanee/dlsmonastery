<template>
  <div>
    <head-tab active="storeAllotShip"></head-tab>
    <div>
      <audio ref="mediaNotify"><source :src="mediaNotify"  type="audio/ogg"></audio>
      <audio ref="mediaSuccess"><source :src="mediaSuccess"  type="audio/ogg"></audio>
      <su-alert :text="shipResult.warnMsg"  type="warning"></su-alert>
      <su-alert :text="shipResult.errorMsg" type="danger"></su-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotShip.businessId')">
              {{storeAllot.formatId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.fromStore')">
              {{storeAllot.fromStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.toStore')">
              {{storeAllot.toStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.boxImeStr')" prop="boxImeStr">
              <textarea v-model="inputForm.boxImeStr" :rows="5" class="el-textarea__inner" @input="checkAndSummary()"></textarea>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotShip.imeStr')" prop="imeStr">
              <textarea ref="imeStrTextArea" v-model="inputForm.imeStr" :rows="5" class="el-textarea__inner" @input="checkAndSummary()"></textarea>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.expressCodes')" prop="expressOrderExpressCodes">
              <el-input type="textarea" :autosize="autosize" v-model="inputForm.expressOrderExpressCodes" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.remarks')" prop="shipRemarks">
              <el-input  type="textarea" v-model="inputForm.shipRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('storeAllotShip.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="storeAllotDetailList" style="margin-top:5px;"  v-loading="pageLoading" :element-loading-text="$t('storeAllotShip.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('storeAllotShip.productName')" sortable width="200"></el-table-column>
          <el-table-column prop="productHasIme" :label="$t('storeAllotShip.hasIme')" >
            <template scope="scope">
              <el-tag :type="scope.row.productHasIme ? 'primary' : 'danger'">{{scope.row.productHasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotShip.billQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotShip.shippedQty')"></el-table-column>
          <el-table-column prop="leftQty" :label="$t('storeAllotShip.leftQty')" ></el-table-column>
          <el-table-column prop="shipQty" :label="$t('storeAllotShip.shipQty')"></el-table-column>
          <el-table-column prop="finish" :label="$t('storeAllotShip.finish')" >
            <template scope="scope">
              <el-tag :type="scope.row.leftQty==0 ? 'primary' : 'danger'">{{scope.row.leftQty==0 | bool2str}}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import mediaNotify from "assets/media/notify.mp3"
  import mediaSuccess from "assets/media/success.mp3"
  export default{
    data(){
      return{
        mediaNotify: mediaNotify,
        mediaSuccess: mediaSuccess,
        submitDisabled:false,
        rules: {},
        autosize: { minRows: 5},
        pageLoading:false,

        storeAllot:{},
        shipResult:{},
        storeAllotDetailList:[],
        inputForm:{
          id:null,
          boxImeStr:null,
          imeStr:null,
          expressOrderExpressCodes:null,
          shipRemarks:null,
        },
      }
    }, methods:{
      formSubmit(){

        if(util.isBlank(this.inputForm.imeStr) && util.isBlank(this.inputForm.boxImeStr)) {
          this.$alert("请填入发货串码或箱号");
          return;
        }
        this.submitDisabled = true;
        let checkAndSummaryPromise = this.checkAndSummary();
        checkAndSummaryPromise.then((checkResult)=>{
          if(checkResult === "error" ){
            this.$alert("请先处理错误信息");
            this.submitDisabled = false;
            return;
          }

          if(checkResult === "warning" && !confirm("还有货品未发送完，确认保存？")){
            this.submitDisabled = false;
            return;
          }

          axios.post('/api/ws/future/crm/storeAllot/ship', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
            this.$message(response.data.message);
            this.$router.push({name:'storeAllotList',query:util.getQuery("storeAllotList"), params:{_closeFrom:true}});
          }).catch(()=> {
            this.submitDisabled = false;
          });
        });

      },checkAndSummary(){
      return axios.get('/api/ws/future/crm/storeAllot/shipCheck',{params:{id:this.inputForm.id,boxImeStr:this.inputForm.boxImeStr,imeStr:this.inputForm.imeStr}}).then((response) => {
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
      for(let item of this.storeAllotDetailList) {
        if(item.productHasIme) {
          item.shipQty = (shipQtyMap[item.productId] ? shipQtyMap[item.productId] : 0);
          item.leftQty = item.realBillQty - item.shippedQty - item.shipQty;
        } else {
          item.shipQty = item.realBillQty-item.shippedQty;
          item.leftQty = 0;
        }
      }
    },initPage(){
        axios.get('/api/ws/future/crm/storeAllot/findDetailList' ,{params:{storeAllotId:this.$route.query.id}}).then((response)=>{
          this.storeAllotDetailList = response.data;
        });
        axios.get('/api/ws/future/crm/storeAllot/findDto' ,{params: {id:this.$route.query.id}}).then((response)=>{
          this.storeAllot = response.data;
          this.inputForm.id = this.storeAllot.id;
          this.inputForm.expressOrderExpressCodes = this.storeAllot.expressOrderExpressCodes;
        });
        this.$nextTick(()=>{
          this.$refs.imeStrTextArea.focus();
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>

