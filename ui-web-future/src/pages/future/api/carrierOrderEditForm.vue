<template>
  <div>
    <head-tab active="carrierOrderEditForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="10">
          <el-col :span="12">
        <el-form-item label="货品订货单号" prop="category">
          {{inputForm.formatId}}
        </el-form-item>
        <el-form-item label="办事处" prop="sort">
          {{inputForm.areaName}}
        </el-form-item>
        <el-form-item label="门店" prop="value">
          {{inputForm.depotName}}
        </el-form-item>
        <el-form-item label="商城门店" prop="remarks">
          {{inputForm.carrierShopName}}
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货时间" prop="category">
              {{inputForm.shipDate}}
            </el-form-item>
            <el-form-item label="商城单号" prop="sort">
              {{inputForm.code}}
            </el-form-item>
            <el-form-item label="状态" prop="value">
              <el-select v-model="formData.carrierOrderStatus" filterable clearable placeholder="请选择">
                <el-option v-for="status in formData.extra.carrierOrderStatusList" :key="status" :label="status" :value="status"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="订单备注" prop="remarks">
              <el-input v-model="formData.remarks"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('dictEnumForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
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
          inputForm:{},
          formData:{extra:{}},
          rules: {}
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        let submitData = util.deleteExtra(this.formData);
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/api/carrierOrder/save', qs.stringify(util.deleteExtra(submitData))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'carrierOrderList',query:util.getQuery("carrierOrderList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/api/carrierOrder/getForm').then((response)=>{
          this.formData=response.data;
          axios.get('/api/ws/future/api/carrierOrder/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
            console.log(response.data)
            this.inputForm = response.data;
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
