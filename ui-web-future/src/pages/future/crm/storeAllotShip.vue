<template>
  <div>
    <head-tab active="storeAllotShip"></head-tab>
    <div>
      <el-form :model="shipForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">

            <el-form-item :label="$t('storeAllotShip.businessId')" prop="storeId">
              {{shipForm.businessId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.fromStore')" prop="storeId">
              {{shipForm.fromStore.name}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.toStore')" prop="storeId">
            {{shipForm.toStore.name}}
          </el-form-item>

            <el-form-item :label="$t('storeAllotShip.boxImeStr')" prop="boxImeStr">
              <el-input type="textarea" :autosize="autosize" v-model="inputForm.boxImeStr" @change="shipBoxAndIme(inputForm.boxImeStr,inputForm.imeStr)"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotShip.imeStr')" prop="imeStr">
              <el-input type="textarea" :autosize="autosize"v-model="inputForm.imeStr" @change="shipBoxAndIme(inputForm.boxImeStr,inputForm.imeStr)"></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.expressCodes')" prop="expressCodes">
              <el-input type="textarea" :autosize="autosize"v-model="inputForm.expressCodes" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('storeAllotShip.remarks')" prop="remarks">
              <el-input  v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('storeAllotShip.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="inputForm.storeAllotDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('storeAllotShip.loading')" stripe border >
          <el-table-column  prop="product.name" :label="$t('storeAllotShip.productName')" sortable width="200"></el-table-column>
          <el-table-column prop="product.hasIme" :label="$t('storeAllotShip.hasIme')" >
            <template scope="scope">
              <el-tag :type="scope.row.product.hasIme ? 'primary' : 'danger'">{{scope.row.product.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotShip.billQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotShip.shippedQty')"></el-table-column>
          <el-table-column prop="extendMap.waitShipQty" :label="$t('storeAllotShip.waitShipQty')" ></el-table-column>
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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{},
        shipForm:{
          fromStore:{name:''},
          toStore:{name:''},
        },
        inputForm:{
          id:this.$route.query.id,
          boxImeStr:'',
          expressOrder:{
             expressCodes:'',
          },
          imeStr:'',
          remarks:"",
          storeAllotDetailList:[]
        },
        rules: {},
        autosize: { minRows: 5},
        pageLoading:'',
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
                  this.$router.push({name:'storeAllotList',query:util.getQuery("storeAllotList")})
                }
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },shipBoxAndIme(boxImeStr,imeStr){
        this.pageLoading = true;
        axios.get('/api/crm/storeAllot/shipBoxAndIme',{params:{id:this.inputForm.id,boxImeStr:boxImeStr,imeStr:imeStr}}).then((response) => {
          this.inputForm.storeAllotDetailList=response.data.storeAllotDetailList;
          this.pageLoading = false;
        })
      }, findOne(){
        axios.get('/api/crm/storeAllot/ship',{params: {id:this.$route.query.id}}).then((response)=>{
            this.shipForm=response.data;
            this.inputForm.storeAllotDetailList=response.data.storeAllotDetailList;
            console.log(response.data);
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>

