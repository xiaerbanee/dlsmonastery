<template>
  <div>
    <head-tab :active="$t('depotChangeDetail.depotChangeDetail') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('depotChangeDetail.type')" prop="type">
              {{detailForm.type}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.processStatus')" prop="processStatus">
              {{detailForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.depotName')" prop="depotName">
              {{detailForm.depot.name}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.expiryDate')" prop="expiryDate">
              {{detailForm.expiryDate}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.oldLabel')" prop="oldLabel">
              {{detailForm.oldLabel}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.newValue')" prop="newValue">
              {{detailForm.newValue}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.remarks')" prop="remarks">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.pass')" prop="pass" v-if="isAudit">
              <el-radio-group v-model="inputForm.pass">
                <el-radio v-for="(value,key) in inputForm.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('depotChangeDetail.comment')" prop="comment" v-if="isAudit">
              <el-input v-model="inputForm.comment"></el-input>
            </el-form-item>
            <el-form-item v-if="isAudit">
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('depotChangeDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="2">
            <span v-html="inputForm.content"></span>
            <el-table :data="activitiEntity.historicTaskInstances" stripe border  >
              <el-table-column prop="name" :label="$t('depotChangeDetail.nodeName')"></el-table-column>
              <el-table-column :label="$t('depotChangeDetail.auditMan')" >
                <template scope="scope">{{activitiEntity.accountMap?activitiEntity.accountMap[scope.row.id]:''}}</template>
              </el-table-column>
              <el-table-column :label="$t('depotChangeDetail.auditDate')">
                <template scope="scope">
                  {{scope.row.endTime | formatLocalDateTime}}
                </template>
              </el-table-column>
              <el-table-column :label="$t('depotChangeDetail.auditRemarks')">
                <template scope="scope">
                  {{activitiEntity.commentMap?activitiEntity.commentMap[scope.row.id]:''}}
                </template>
              </el-table-column>
            </el-table>
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
        isCreate:this.$route.query.id==null,
        isAudit:this.$route.query.action=="audit",
        submitDisabled:false,
        inputForm:{
          id:this.$route.query.id,
          comment:"",
          bools:[],
          pass:"",
        },
        detailForm:{
          depot:{name:''}
        },
        rules: {
          pass: [{ required: true, message: this.$t('depotChangeDetail.prerequisiteMessage')}],
        },
        activitiEntity:{}
      }
    },
    methods:{
      handlePreview(file) {
        window.open(file.url);
      },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/depotChange/audit',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'depotChangeList',query:util.getQuery("depotChangeList")})
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/crm/depotChange/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        this.detailForm=response.data.depotChange
        this.inputForm.bools=response.data.bools;
        this.activitiEntity = response.data.activitiEntity;
      })
    }
  }
</script>
