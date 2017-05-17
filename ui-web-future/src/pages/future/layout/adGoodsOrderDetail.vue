<template>
  <div>
    <head-tab active="adGoodsOrderDetail"></head-tab>
    <div>
      <el-form :model="detailForm" ref="detailForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.orderCode')">
              {{detailForm.businessId}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.billDate')" >
              {{detailForm.billDate}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.outShopName')">
              {{detailForm.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.shopName')" >
              {{detailForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.threeMonthQty')">
              {{detailForm.threeMonthQty}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.employeeId')" >
              {{detailForm.employeeName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.mobilePhone')">
              {{detailForm.employeePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.processStatus')">
              {{detailForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.expressCodes')">
              {{expressOrderForm.expressCodes}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.createdBy')">
              {{detailForm.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.remarks')">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.contact')" >
              {{expressOrderForm.contator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.address')" >
              {{expressOrderForm.address}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.mobilePhone')">
              {{expressOrderForm.mobilePhone}}
            </el-form-item>
            <div  v-if="audit">
            <el-form-item :label="$t('adGoodsOrderDetail.pass')" >
              <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.comment')" prop="passRemarks">
              <el-input v-model="inputForm.passRemarks"></el-input>
            </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderDetail.audit')}}</el-button>
              </el-form-item>
              </div>
          </el-col>
        </el-row>
        <process-details v-model="detailForm.processInstanceId"></process-details>
      </el-form>
    </div>
  </div>
</template>
<script>
  import processDetails from 'components/general/process-details';
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components:{processDetails,boolRadioGroup},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        audit:this.$route.query.action=='audit',
        detailForm:{},
        expressOrderForm:{},
        inputForm:{
         id:this.$route.query.id,
         pass:"",
         passRemarks:"",
        },
        rules: {
          comment:{ required: true, message: this.$t('adGoodsOrderDetail.prerequisiteMessage')},
        },
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["detailForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/adGoodsOrder/audit',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.fileList = [];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/layout/adGoodsOrder/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        this.detailForm = response.data;
        if(response.data.expressOrderDto!=null){
          this.expressOrderForm = response.data.expressOrderDto;
        }

      })
    }
  }
</script>
