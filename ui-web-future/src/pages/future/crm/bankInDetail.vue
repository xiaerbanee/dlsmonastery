<template>
  <div>
    <head-tab active="bankInDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.shopName')" >
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.bankName')">
              {{inputForm.bankName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.amount')" >
              {{inputForm.amount}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.serialNumber')">
              {{inputForm.serialNumber}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.synInfo')">
              金蝶同步
             </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('bankInDetail.outCode')">
              {{inputForm.outCode}}
            </el-form-item>

            <el-form-item :label="$t('bankInDetail.processStatus')">
              {{inputForm.processStatus}}
            </el-form-item>

            <el-form-item :label="$t('bankInDetail.inputDate')">
              {{inputForm.inputDate}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.createdByName')">
              {{inputForm.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('bankInDetail.remarks')">
              {{inputForm.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            inputForm:{},
          }
      },created(){
          axios.get('/api/ws/future/crm/bankIn/findDetail',{params: {id:this.$route.query.id}}).then((response)=>{
              this.inputForm = response.data;
          })
      }
    }
</script>
