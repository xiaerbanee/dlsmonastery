<template>
  <div>
    <head-tab active="auditFileDetail"></head-tab>
    <div>
      <el-form :model="submitData" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('auditFileDetail.createdBy')" prop="createdBy">
              {{inputForm.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.createdDate')" prop="createdDate">
              {{inputForm.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.id')" prop="id">
              {{inputForm.id}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.title')" prop="title">
              {{inputForm.title}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.processStatus')" prop="processStatus">
              {{inputForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.remarks')" prop="remarks">
              {{inputForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.attachment')" prop="attachment">
              <el-upload  action="/api/general/sys/folderFile/upload?uploadPath=/文件审批" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="2">
            <span v-html="inputForm.content"></span>
            <el-table :data="inputForm.activitiDetailList">
              <el-table-column prop="processStatus":label="$t('auditFileDetail.stageName')"></el-table-column>
              <el-table-column prop="auditByName" :label="$t('auditFileDetail.auditMan')" ></el-table-column>
              <el-table-column prop="auditDate" :label="$t('auditFileDetail.auditDate')"></el-table-column>
              <el-table-column prop="comment" :label="$t('auditFileDetail.auditRemarks')"></el-table-column>
            </el-table>
            <el-form-item :label="$t('auditFileDetail.isPass')" prop="pass" v-if="isAudit">
              <bool-radio-group v-model="submitData.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('auditFileDetail.comment')" prop="comment" v-if="isAudit">
              <el-input v-model="submitData.comment" type="textarea" :rows="5"></el-input>
            </el-form-item>
            <el-form-item  style="margin-top:20px">
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="isAudit">{{$t('auditFileDetail.save')}}</el-button>
              <el-button type="primary" @click="print()" >打印</el-button>
              <el-button type="primary" @click="collectAuditFile(true)" v-show="!inputForm.collect">收藏</el-button>
              <el-button type="primary" @click="collectAuditFile(false)" v-show="inputForm.collect">取消收藏</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

  </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components:{boolRadioGroup},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        isAudit:this.$route.query.action=="audit",
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:this.$route.query.id,
          content:"",
          pass:"",
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
            axios.post('/api/basic/hr/auditFile/audit', qs.stringify(this.submitData)).then((response) => {
              this.$message(response.data.message);
                util.closeAndBackToPage(this.$router,"auditFileList")
            }).catch(function () {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },
      print(){
        window.open('/#/basic/hr/auditFilePrint?id=' + this.$route.query.id);
      },collectAuditFile(collectStatus){
        var that=this;
        axios.get('/api/basic/hr/auditFileCollect/collect?auditFileId='+this.inputForm.id+'&collect='+collectStatus).then((response) => {
          this.$message(response.data.message);
          this.inputForm.collect=collectStatus
        }).catch(function () {
          that.submitDisabled = false;
        });
      },
    },
    created(){
        axios.get('/api/basic/hr/auditFile/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          if(this.inputForm.attachment != null) {
             axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
               this.fileList= response.data;
             });
          }
        })
    }
  }
</script>
