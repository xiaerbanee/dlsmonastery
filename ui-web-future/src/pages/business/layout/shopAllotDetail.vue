<template>
  <div>
    <head-tab :active="$t('shopAllotDetail.shopAllotDetail') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.billCode')">
              {{inputForm.id}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.status')">
              {{inputForm.status}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outSaleCode')">
              {{inputForm.outSaleCode}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.outReturnCode')" v-if="inputForm.outSaleCode !=''">
              {{inputForm.outSaleCode}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.saleTotalPrice')">
              {{inputForm.saleTotalPrice}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.remarks')" >
              {{inputForm.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopAllotDetail.fromShop')"  >
              {{inputForm.fromShop.name}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.toShop')">
              {{inputForm.toShop.name}}
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.syn')" prop="syn" v-if="action=='审核'">
              <el-radio-group v-model="inputForm.syn">
                <el-radio  label="1" value="true">{{$t('shopAllotDetail.true')}}</el-radio>
                <el-radio  label="0" value = 'false'>{{$t('shopAllotDetail.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopAllotDetail.pass')" prop="pass" v-if="action=='审核'">
              <el-radio-group v-model="inputForm.pass">
                <el-radio  label="1" value="true">{{$t('shopAllotDetail.true')}}</el-radio>
                <el-radio  label="0" value = 'false'>{{$t('shopAllotDetail.false')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item  :label="$t('shopAllotDetail.auditRemarks')" prop="auditRemarks"  v-if="action=='审核'">
              <el-input v-model="inputForm.auditRemarks"></el-input>
            </el-form-item>
            <el-form-item v-if="action=='审核'">
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAllotDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="inputForm.shopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="product.name" :label="$t('shopAllotDetail.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotDetail.qty')">
          </el-table-column>
          <el-table-column prop="returnPrice" :label="$t('shopAllotDetail.returnPrice')">
            <template scope="scope">
              <el-input  v-model="scope.row.returnPrice"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="salePrice" :label="$t('shopAllotDetail.salePrice')">
            <template scope="scope">
              <el-input  v-model="scope.row.salePrice"></el-input>
            </template>
          </el-table-column>
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
            action:this.$route.query.action,
            submitDisabled:false,
            formProperty:{},
            accounts:[],
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              pass:'',
              syn:'',
              status:'',
              outSaleCode:'',
              outReturnCode:'',
              saleTotalPrice:'',
              remarks:'',
              fromShop:{
                name:''
              },
              toShop:{
                name:''
              },
              auditRemarks:'',
              shopAllotDetailList:[]
            },
            rules: {
              pass: [{ required: true, message: this.$t('shopAllotDetail.prerequisiteMessage')}],
              syn: [{ required: true, message: this.$t('shopAllotDetail.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/shopAllot/audit',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                   this.$router.push({name:'shopAllotList',query:util.getQuery("shopAllotList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/crm/shopAllot/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            this.inputForm.shopAllotDetailList=response.data.shopAllotDetailList;
          })
        }
      }
    }
</script>
