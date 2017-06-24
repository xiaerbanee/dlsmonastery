<template>
  <div>
    <head-tab active="shopBuildDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="140px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildDetail.shopBuildCode')" prop="id">
             {{inputForm.id}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.office')" prop="depotDto">
              {{inputForm.officeName}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.shopName')" prop="shopId">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.monthSaleQty')" prop="monthSaleQty">
              {{inputForm.monthSaleQty}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.shopType')" prop="shopType">
              {{inputForm.shopType}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.address')" prop="address">
              {{inputForm.address}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.applyAccount')" prop="loginName">
              {{inputForm.applyAccountName}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.mobilePhone')" prop="mobilePhone">
              {{inputForm.applyAccountPhone}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.remarks')"  prop="remarks">
              {{inputForm.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item  :label="$t('shopBuildDetail.fixtureType')" prop="fixtureType">
              {{inputForm.fixtureType}}
            </el-form-item>
            <el-form-item  :label="$t('shopBuildDetail.oldContents')" prop="oldContents">
              {{inputForm.oldContents}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.newContents')" prop="newContents">
              {{inputForm.newContents}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.buildType')" prop="buildType">
              {{inputForm.buildType}}
            </el-form-item>
            <el-form-item  :label="$t('shopBuildDetail.content')" prop="content">
              {{inputForm.content}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.processStatus')" prop="processStatus">
              {{inputForm.processStatus}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.scenePhoto')" prop="scenePhoto">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :on-preview="handlePreview1" :file-list="fileList1" list-type="picture">
              </el-upload>
              <el-dialog v-model="dialogVisible" size="tiny">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.confirmPhoto')" prop="confirmPhoto">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :on-preview="handlePreview2" :file-list="fileList2" list-type="picture">
              </el-upload>
              <el-dialog v-model="dialogVisible" size="tiny">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.pass')"  v-if="action=='audit'">
              <bool-radio-group v-model="formData.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.passRemarks')"  v-if="action=='audit'">
              <el-input v-model="formData.passRemarks" :placeholder="$t('shopBuildDetail.inputRemarks')" type="textarea"></el-input>
            </el-form-item>
            <el-form-item v-if="action=='audit'">
              <el-button type="primary" :disabled="submitDisabled"  @click="passSubmit()">{{$t('shopBuildDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <process-details v-model="inputForm.processInstanceId"></process-details>
      </el-form>
    </div>
  </div>
</template>

<script>
  import processDetails from 'components/general/process-details';
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components:{processDetails,boolRadioGroup},
    data(){
      return this.getData();
    },
    methods:{
        getData(){
          return{
            submitDisabled:false,
            isCreate:this.$route.query.id==null,
            action:this.$route.query.action,
            inputForm:{},
            formData:{
              extra:{}
            },
            fileList1:[],
            fileList2:[],
            dialogImageUrl:'',
            dialogVisible:false
          }
        },
      passSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/shopBuild/audit', qs.stringify(util.deleteExtra(this.formData))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                this.$router.push({name:'shopBuildList',query:util.getQuery("shopBuildList"),params:{_closeFrom:true}})
              }
            }).catch( ()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      handlePreview1(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },handlePreview2(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },initPage(){
        axios.get('/api/ws/future/layout/shopBuild/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          if(this.inputForm.scenePhoto !=null) {
            axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.scenePhoto}}).then((response)=>{
              this.fileList1= response.data;
            });
          }
          if(this.inputForm.confirmPhoto !=null) {
            axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.confirmPhoto}}).then((response)=>{
              this.fileList2= response.data;
            });
          }
        })
        axios.get('/api/ws/future/layout/shopBuild/getAuditForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.formData = response.data;
        })
      }
    },created(){
      this.initPage();
    }
  }
</script>
