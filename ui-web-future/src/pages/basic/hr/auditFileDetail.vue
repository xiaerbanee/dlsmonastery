<template>
  <div>
    <head-tab active="auditFileDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('auditFileDetail.createdBy')" prop="createdBy">
              {{detailForm.created.loginName}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.createdDate')" prop="createdDate">
              {{detailForm.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.id')" prop="id">
              {{detailForm.id}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.title')" prop="title">
              {{detailForm.title}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.processStatus')" prop="processStatus">
              {{detailForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.remarks')" prop="remarks">
              {{detailForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.attachment')" prop="attachment">
              <el-upload  action="/api/basic/sys/folderFile/upload?uploadPath=/文件审批" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.isPass')" prop="pass" v-if="isAudit">
              <el-radio-group v-model="inputForm.pass">
                <el-radio v-for="(value,key) in inputForm.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.comment')" prop="comment" v-if="isAudit">
              <el-input v-model="inputForm.comment"></el-input>
            </el-form-item>
            <el-form-item v-if="isAudit">
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('auditFileDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="2">
            <span v-html="inputForm.content"></span>
            <el-table :data="activityEntity.historicTaskInstances">
              <el-table-column prop="name":label="$t('auditFileDetail.stageName')"></el-table-column>
              <el-table-column :label="$t('auditFileDetail.auditMan')" >
                <template scope="scope">{{activityEntity.accountMap?activityEntity.accountMap[scope.row.id]:''}}</template>
              </el-table-column>
              <el-table-column :label="$t('auditFileDetail.auditDate')">
                <template scope="scope">
                    {{scope.row.endTime | formatLocalDateTime}}
                </template>
              </el-table-column>
              <el-table-column :label="$t('auditFileDetail.auditRemarks')">
                <template scope="scope">
                  {{activityEntity.commentMap?activityEntity.commentMap[scope.row.id]:''}}
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
          content:"",
          bools:[],
          pass:"",
        },
        detailForm:{
        created:{loginName:''},
        },
        rules: {
          pass: [{ required: true, message: this.$t('auditFileDetail.prerequisiteMessage')}],
        },
        activityEntity:{},
        fileList:[]
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
            axios.post('/api/basic/hr/auditFile/audit',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
               this.$router.push({name:'auditFileList',query:util.getQuery("auditFileList")})
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
        axios.get('/api/basic/hr/auditFile/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data.auditFile,this.inputForm);
          console.log(response.data.auditFile.content)
          console.log(this.inputForm.content)
          this.detailForm=response.data.auditFile
          this.inputForm.bools=response.data.bools;
          this.activityEntity = response.data.activityEntity;
          if(this.detailForm.attachment != null) {
             axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.detailForm.attachment}}).then((response)=>{
               this.fileList= response.data;
             });
          }
        })
    }
  }
</script>
