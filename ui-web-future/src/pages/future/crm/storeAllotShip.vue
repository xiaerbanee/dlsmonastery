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
              <el-input type="textarea" :autosize="autosize" v-model="inputForm.boxImeStr"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotShip.imeStr')" prop="imeStr">
              <el-input type="textarea" :autosize="autosize" v-model="inputForm.imeStr"></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.expressCodes')" prop="expressOrderExpressCodes">
              <el-input type="textarea" :autosize="autosize" v-model="inputForm.expressOrderExpressCodes" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.remarks')" prop="shipRemarks">
              <el-input  type="textarea" v-model="inputForm.shipRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="summary(true)">{{$t('storeAllotShip.save')}}</el-button>
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
        storeAllot:{},
        shipResult:{},
        storeAllotDetailList:[],
        inputForm:{
          extra:{},
        },
        rules: {},
        autosize: { minRows: 5},
        pageLoading:false,
      }
    }, watch: {
    "inputForm.imeStr": function (oldVal,newVal) {
      if(_.trim(oldVal) != _.trim(newVal)) {
        this.summary(false);
      }
    },"inputForm.boxImeStr":function (oldVal,newVal) {
      if(_.trim(oldVal) != _.trim(newVal)) {
        this.summary(false);
      }
    }
  }, methods:{
      formSubmit(){
        axios.post('/api/ws/future/crm/storeAllot/ship', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
          this.$message(response.data.message);
          this.$router.push({name:'storeAllotList',query:util.getQuery("storeAllotList"), params:{_closeFrom:true}});
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
          return;
        }
        axios.get('/api/ws/future/crm/storeAllot/shipCheck',{params:{id:this.inputForm.id,boxImeStr:boxImeStr,imeStr:imeStr}}).then((response) => {
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
          for(var index in this.storeAllotDetailList) {
            var item = this.storeAllotDetailList[index];
            if(item.productHasIme) {
              item.shipQty = shipQtyMap[item.productId];
              item.leftQty = item.billQty - item.shippedQty - item.shipQty;
            } else {
              item.shipQty = item.billQty-item.shippedQty;
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
      },initPage(){
        axios.get('/api/ws/future/crm/storeAllot/getShipForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/crm/storeAllot/findDetailList' ,{params:{storeAllotId:this.$route.query.id}}).then((response)=>{
            this.storeAllotDetailList = response.data;
          });
          axios.get('/api/ws/future/crm/storeAllot/findDto' ,{params: {id:this.$route.query.id}}).then((response)=>{
            this.storeAllot = response.data;
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>

