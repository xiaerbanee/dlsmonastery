<template>
  <div>
    <head-tab active="shopAllotDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules"  label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.billCode')">
              {{shopAllot.formatId}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.status')">
              {{shopAllot.status}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outSaleCode')" v-if="shopAllot.outSaleCode !=''">
              {{shopAllot.outSaleCode}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outReturnCode')" v-if="shopAllot.outReturnCode !=''">
              {{shopAllot.outReturnCode}}
            </el-form-item>

            <el-form-item :label="$t('shopAllotDetail.saleTotalPrice')">
              {{shopAllot.saleTotalPrice}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.remarks')" >
              {{shopAllot.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.fromShop')"  >
              {{shopAllot.fromShopName}}
              <div v-if="shopAllot.status=== '申请中'">
                {{$t('shopAllotDetail.shouldGet')}} {{shopAllot.fromShopShouldGet}}
              </div>
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.toShop')">

              {{shopAllot.toShopName}}
              <div v-if="shopAllot.status=== '申请中'">
                {{$t('shopAllotDetail.shouldGet')}} {{shopAllot.toShopShouldGet}}
              </div>

            </el-form-item>

            <div v-if="action === 'audit'">
              <el-form-item :label="$t('shopAllotDetail.syn')" prop="syn">
                <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
              </el-form-item>

              <el-form-item :label="$t('shopAllotDetail.pass')" prop="pass">
                <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('shopAllotDetail.auditRemarks')">
                <el-input type="textarea" v-model="inputForm.auditRemarks"></el-input>
              </el-form-item>
              <el-form-item >
                <el-button type="primary" @click.native="formSubmit">{{$t('shopAllotDetail.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-table :data="inputForm.shopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="productName" :label="$t('shopAllotDetail.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotDetail.qty')"></el-table-column>
          <el-table-column prop="returnPrice" :label="$t('shopAllotDetail.returnPrice')">
            <template   scope="scope">
              <div v-if="action === 'view'">{{scope.row.returnPrice}}</div>
              <div v-else><el-input v-model.number="scope.row.returnPrice"></el-input></div>
            </template>
          </el-table-column>
          <el-table-column prop="salePrice" :label="$t('shopAllotDetail.salePrice')">
            <template  scope="scope">
              <div v-if="action === 'view'">{{scope.row.salePrice}}</div>
              <div v-else><el-input v-model.number="scope.row.salePrice"></el-input></div>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group'

  export default{
    components:{
      boolRadioGroup,

    },
    data(){
      return {
        shopAllot: {},
        inputForm:{
          id:null,
          syn:true,
          pass:null,
          auditRemarks:'',
          shopAllotDetailList:[],
        },
        shopAllotDetailLoading:false,
        submitDisabled:false,
        action:this.$route.query.action,
        rules: {
          syn: {required: true, message: this.$t('shopAllotDetail.prerequisiteMessage')},
          pass: {required: true, message: this.$t('shopAllotDetail.prerequisiteMessage')},
        },
      }
    }, methods:{
      formSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            axios.post('/api/ws/future/crm/shopAllot/audit', qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success) {
                this.$router.push({name: 'shopAllotList', query: util.getQuery("shopAllotList"), params:{_closeFrom:true}});
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }
        });
      }
    },created(){

      this.inputForm.id = this.$route.query.id;
      axios.get('/api/ws/future/crm/shopAllot/findDtoForViewOrAudit', {params: {id: this.$route.query.id}}).then((response) => {
        this.shopAllot = response.data;
      });

      this.shopAllotDetailLoading=true;
      axios.get('/api/ws/future/crm/shopAllot/findDetailListForViewOrAudit',{params:{id:this.$route.query.id}} ).then((response)=>{
        this.inputForm.shopAllotDetailList=response.data;
        this.shopAllotDetailLoading=false;
      });
    }
  }
</script>
