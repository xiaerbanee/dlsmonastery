<template>
  <div>
    <head-tab active="adGoodsOrderDetail"></head-tab>
    <div>
      <el-form :model="detailForm" ref="detailForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.orderCode')">
              {{detailForm.id}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.billDate')" >
              {{detailForm.billDate}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.outShopName')">
              {{detailForm.outShop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.shopName')" >
              {{detailForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.threeMonthQty')">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.employeeId')" >
              {{detailForm.employeeId ? detailForm.employee.name :''}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.mobilePhone')">
              {{detailForm.employeeId ? detailForm.employee.mobilePhone :''}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.processStatus')">
              {{detailForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.expressCodes')">
              {{detailForm.expressOrder.expressCodes}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderDetail.createdBy')">
              {{detailForm.created.loginName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.remarks')">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.contact')" >
              {{detailForm.expressOrder.contator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.address')" >
              {{detailForm.expressOrder.address}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.mobilePhone')">
              {{detailForm.expressOrder.mobilePhone}}
            </el-form-item>
            <div  v-if="audit">
            <el-form-item :label="$t('adGoodsOrderDetail.pass')" >
              <el-radio-group v-model="inputForm.pass">
                <el-radio v-for="(value,key) in detailProperty.bools" :key="key" :label="value">{{key | bool2str}} </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderDetail.comment')" prop="comment">
              <el-input v-model="inputForm.comment"></el-input>
            </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderDetail.audit')}}</el-button>
              </el-form-item>
              </div>
          </el-col>
        </el-row>
        <el-table :data="detailForm.adGoodsOrderDetailList" style="margin-top:5px;"  stripe border >
          <el-table-column  prop="product.code" :label="$t('adGoodsOrderDetail.code')" ></el-table-column>
          <el-table-column  prop="product.name" :label="$t('adGoodsOrderDetail.productName')" ></el-table-column>
          <el-table-column prop="qty" :label="$t('adGoodsOrderDetail.qty')"></el-table-column>
          <el-table-column prop="confirmQty" :label="$t('adGoodsOrderDetail.confirmQty')"></el-table-column>
          <el-table-column prop="billQty" :label="$t('adGoodsOrderDetail.billQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('adGoodsOrderDetail.shippedQty')"></el-table-column>
          <el-table-column prop="price" :label="$t('adGoodsOrderDetail.price')"></el-table-column>
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
        audit:this.$route.query.action=='审核',
        detailProperty:{},
        detailForm:{
          store:{name:''},
          outShop:{name:''},
          shop:{name:''},
          created:{loginName:''},
          employee:{
            name:'',
            moblePhone:''
          },
          expressOrder:{
            contator:'',
            address:'',
            mobilePhone:'',
            expressCodes:''
          },
          adGoodsOrderDetailList:[],
          processStatus:'',
          remarks:'',
        },
        inputForm:{
         id:this.$route.query.id,
         pass:"",
         comment:""
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
            axios.post('/api/crm/adGoodsOrder/audit',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.fileList = [];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }, findOne(){
        axios.get('/api/crm/adGoodsOrder/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          this.detailForm = response.data.adGoodsOrder;
          this.detailProperty.bools = response.data.bools;
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
