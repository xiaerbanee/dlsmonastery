<template>
  <div>
    <head-tab active="depotChangeDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('depotChangeDetail.type')" prop="type">
              {{detailForm.type}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.processStatus')" prop="processStatus">
              {{detailForm.status}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.depotName')" prop="depotName">
              {{detailForm.depotName}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.expiryDate')" prop="expiryDate">
              {{detailForm.expiryDate}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.oldValue')" prop="oldValue">
              {{detailForm.oldValue}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.newValue')" prop="newValue">
              {{detailForm.newValue}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.remarks')" prop="remarks">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.pass')" prop="pass" v-if="isAudit">
              <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.auditRemarks')" prop="auditRemarks" v-if="isAudit">
              <el-input v-model="inputForm.auditRemarks"></el-input>
            </el-form-item>
            <el-form-item v-if="isAudit">
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('depotChangeDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

    </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group'
  export default{
    components:{boolRadioGroup},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        isAudit:this.$route.query.action=="audit",
        submitDisabled:false,
        inputForm:{},
        detailForm:{},
        rules: {
          pass: [{ required: true, message: this.$t('depotChangeDetail.prerequisiteMessage')}],
        },
      }
    },
    methods:{
      handlePreview(file) {
        window.open(file.url);
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/depotChange/audit',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'depotChangeList',query:util.getQuery("depotChangeList"),params:{_closeFrom:true}})
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/crm/depotChange/getAuditForm').then((response)=>{
        this.inputForm=response.data;
        axios.get('/api/ws/future/crm/depotChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.detailForm=response.data;
          util.copyValue(response.data,this.inputForm);
        })
      })


    }
  }
</script>
