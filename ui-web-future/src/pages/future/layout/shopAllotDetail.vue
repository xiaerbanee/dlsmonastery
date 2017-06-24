<template>
  <div>
    <head-tab active="shopAllotDetail"></head-tab>
    <div>
      <el-form :model="shopAllot" ref="inputForm"  label-width="120px" class="form input-form">
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

            <div v-if="action == 'audit'">
              <el-form-item :label="$t('shopAllotDetail.syn')">
                <bool-radio-group v-model="syn"></bool-radio-group>
              </el-form-item>

              <el-form-item :label="$t('shopAllotDetail.pass')">
                <bool-radio-group v-model="audit.pass"></bool-radio-group>
              </el-form-item>
              <el-form-item :label="$t('shopAllotDetail.auditRemarks')">
                <el-input type="textarea" v-model="audit.auditRemarks"></el-input>
              </el-form-item>
              <el-form-item >
                <el-button type="primary" @click.native="auditSubmit">{{$t('shopAllotDetail.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-table :data="shopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="productName" :label="$t('shopAllotDetail.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotDetail.qty')"></el-table-column>
          <el-table-column prop="returnPrice" :label="$t('shopAllotDetail.returnPrice')">
            <template   scope="scope">
              <div v-if="isView">{{scope.row.returnPrice}}</div>
              <div v-else><el-input v-model.number="scope.row.returnPrice"></el-input></div>
            </template>
          </el-table-column>
          <el-table-column prop="salePrice" :label="$t('shopAllotDetail.salePrice')">
            <template  scope="scope">
              <div v-if="isView">{{scope.row.salePrice}}</div>
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
        syn:true,
        audit:{
          pass:false,
          auditRemarks:'',
        },
        shopAllotDetailList:[],
        submitData:{
          id:'',
          syn:'',
          pass:'',
          auditRemarks:'',
          shopAllotDetailList:[],
        },
        isView:(this.$route.query.action === 'view'),
        shopAllotDetailLoading:false,
        submitDisabled:false,
        action:this.$route.query.action,
      }
    }, methods:{
      auditSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            this.initSubmitDataBeforeSubmit();
            axios.post('/api/ws/future/crm/shopAllot/audit', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
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
      }, initSubmitDataBeforeSubmit(){
        this.submitData.id = this.$route.query.id;
        this.submitData.syn = this.syn;
        this.submitData.pass = this.audit.pass;
        this.submitData.auditRemarks = this.audit.auditRemarks;
        this.submitData.shopAllotDetailList = this.shopAllotDetailList;

      }
    },created(){

      axios.get('/api/ws/future/crm/shopAllot/findDtoForViewOrAudit', {params: {id: this.$route.query.id}}).then((response) => {
        this.shopAllot = response.data;
      });

      this.shopAllotDetailLoading=true;
      axios.get('/api/ws/future/crm/shopAllot/findDetailListForViewOrAudit',{params:{id:this.$route.query.id}} ).then((response)=>{
        this.shopAllotDetailList=response.data;
        this.shopAllotDetailLoading=false;
      });

    }
  }
</script>
