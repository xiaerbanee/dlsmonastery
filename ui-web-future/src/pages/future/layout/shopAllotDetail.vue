<template>
  <div>
    <head-tab active="shopAllotDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm"  label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.billCode')">
              {{inputForm.shopAllotDto.formatId}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.status')">
              {{inputForm.shopAllotDto.status}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outSaleCode')" v-if="inputForm.shopAllotDto.outSaleCode !=''">
              {{inputForm.shopAllotDto.outSaleCode}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outReturnCode')" v-if="inputForm.shopAllotDto.outReturnCode !=''">
              {{inputForm.shopAllotDto.outReturnCode}}
            </el-form-item>

            <el-form-item :label="$t('shopAllotDetail.saleTotalPrice')">
              {{inputForm.shopAllotDto.saleTotalPrice}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.remarks')" >
              {{inputForm.shopAllotDto.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.fromShop')"  >
              {{inputForm.shopAllotDto.fromShopName}}
              <div v-if="inputForm.status== '申请中'">
                {{$t('shopAllotDetail.shouldGet')}} {{inputForm.fromShopShouldGet}}
              </div>
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.toShop')">

              {{inputForm.shopAllotDto.toShopName}}
              <div v-if="inputForm.status== '申请中'">
                {{$t('shopAllotDetail.shouldGet')}} {{inputForm.toShopShouldGet}}
              </div>

            </el-form-item>

            <div v-if="action == 'audit'">
              <el-form-item :label="$t('shopAllotDetail.syn')">
                <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
              </el-form-item>

              <el-form-item :label="$t('shopAllotDetail.pass')">
                <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('shopAllotDetail.auditRemarks')">
                <el-input type="textarea" v-model="inputForm.auditRemarks"></el-input>
              </el-form-item>
              <el-form-item >
                <el-button size="small"  @click.native="auditSubmit">{{$t('shopAllotDetail.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-table :data="inputForm.shopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="productName" :label="$t('shopAllotDetail.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotDetail.qty')"></el-table-column>
          <el-table-column prop="returnPrice" :label="$t('shopAllotDetail.returnPrice')">
            <template   scope="scope">
              <el-input  :readonly="isView" v-model="scope.row.returnPrice"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="salePrice" :label="$t('shopAllotDetail.salePrice')">
            <template  scope="scope">
              <el-input :readonly="isView"  v-model="scope.row.salePrice"></el-input>
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
        inputForm: {
          shopAllotDto:{},
        },
        submitData:{
          id:'',
          syn:'',
          pass:'',
          auditRemarks:'',
          shopAllotDetailList:[],
        },
        isView:(this.$route.query.action == 'view'),
        submitDisabled:false,
        action:this.$route.query.action,
      }
    }, methods:{
      auditSubmit(){
        console.log("auditSubmit");
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/ws/future/crm/shopAllot/audit', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              this.$router.push({name:'shopAllotList',query:util.getQuery("shopAllotList")});
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/crm/shopAllot/getViewOrAuditForm', {params: {id: this.$route.query.id, action:this.$route.query.action}}).then((response) => {
        this.inputForm = response.data;
      });
    }
  }
</script>
