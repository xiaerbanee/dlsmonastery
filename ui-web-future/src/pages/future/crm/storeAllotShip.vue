<template>
  <div>
    <head-tab active="storeAllotShip"></head-tab>
    <div>
      <audio id="mediaError"><source src="../../assets/media/notify.mp3"  type="audio/ogg"></audio>
      <audio id="mediaSuccess"><source src="/src/assets/media/success.mp3"  type="audio/ogg"></audio>

      <el-form :model="storeAllot" ref="inputForm"   label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item >
              <el-alert  v-show="warnMsg"  :closable=false title=""  :description="warnMsg"    type="warning">  </el-alert>
            </el-form-item>
            <el-form-item >
              <el-alert  v-show="errMsg"  :closable=false  title=""  :description="errMsg" type="error"> </el-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="12">

            <el-form-item :label="$t('storeAllotShip.businessId')" prop="storeId">
              {{storeAllot.formatId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.fromStore')" prop="storeId">
              {{storeAllot.fromStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.toStore')" prop="storeId">
            {{storeAllot.toStoreName}}
          </el-form-item>

            <el-form-item :label="$t('storeAllotShip.boxImeStr')" prop="boxImeStr">
              <el-input type="textarea" :autosize="autosize" v-model="boxImeStr" @change="shipBoxAndIme()"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotShip.imeStr')" prop="imeStr">
              <el-input type="textarea" :autosize="autosize"v-model="imeStr" @change="shipBoxAndIme()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.expressCodes')" prop="expressCodes">
              <el-input type="textarea" :autosize="autosize"v-model="storeAllot.expressOrderExpressCodes" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.remarks')" prop="remarks">
              <el-input  type="textarea" v-model="shipRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('storeAllotShip.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="storeAllotDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('storeAllotShip.loading')" stripe border >
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
        </el-table>
      </el-form>

    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id===null,
        submitDisabled:false,
        errMsg:'',
        warnMsg:'',

        storeAllot:{},
        boxImeStr:'',
        imeStr:'',
        shipRemarks:'',
        storeAllotDetailList:[],

        submitData:{
          id:'',
          expressOrderExpressCodes:'',
          imeStr:'',
          boxImeStr:'',
          shipRemarks:"",
          storeAllotDetailList:[]
        },
        rules: {},
        autosize: { minRows: 5},
        pageLoading:false,
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/storeAllot/ship',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message({message:response.data.message,type:response.data.type});
              if(response.data.success){
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'storeAllotList',query:util.getQuery("storeAllotList")});
                }
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },shipBoxAndIme(){

        if(util.isBlank(this.boxImeStr) && util.isBlank(this.imeStr)){
          if(this.storeAllotDetailList ){
            for(let storeAllotDetail of this.storeAllotDetailList){
              storeAllotDetail.shipQty = 0;
              storeAllotDetail.leftQty = storeAllotDetail.billQty - storeAllotDetail.shippedQty ;
            }
          }
          this.errMsg = '';
          this.warnMsg = '';
        }else{
          this.pageLoading = true;
          axios.get('/api/ws/future/crm/storeAllot/shipBoxAndIme',{params:{id:this.storeAllot.id,boxImeStr:this.boxImeStr,imeStr:this.imeStr}}).then((response) => {
            if(this.storeAllotDetailList && response.data.shipQtyMap){
              for(let storeAllotDetail of this.storeAllotDetailList){
                  if(response.data.shipQtyMap[storeAllotDetail.productId]){
                    storeAllotDetail.shipQty = response.data.shipQtyMap[storeAllotDetail.productId];
                  }else{
                    storeAllotDetail.shipQty = 0;
                  }

                storeAllotDetail.leftQty = storeAllotDetail.billQty - storeAllotDetail.shippedQty - storeAllotDetail.shipQty;
              }
            }
            this.errMsg = response.data.errMsg;
            this.warnMsg = response.data.warnMsg;

            this.pageLoading = false;
          });
        }

      }
    },created(){

      axios.get('/api/ws/future/crm/storeAllot/findDetailList' ,{params:{storeAllotId:this.$route.query.id}}).then((response)=>{
        this.storeAllotDetailList = response.data;
      });

      axios.get('/api/ws/future/crm/storeAllot/findDto' ,{params: {id:this.$route.query.id}}).then((response)=>{
        this.storeAllot = response.data;
      });

    }
  }
</script>

