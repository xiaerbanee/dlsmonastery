<template>
  <div>
    <head-tab :active="$t('shopBuildDetail.shopBuildDetail') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="140px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildDetail.shopBuildCode')" prop="id">
             {{inputForm.id}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.office')" prop="officeId">
              {{inputForm.office}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.shopName')" prop="shopId">
              {{inputForm.shop ? inputForm.shop.name : '' }}
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
              {{inputForm.applyAccount ? inputForm.applyAccount.loginName : ''}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.mobilePhone')" prop="mobilePhone">
              {{inputForm.applyAccount ? inputForm.applyAccount.employee.mobilePhone : ''}}
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
              <el-upload action="/api/sys/folderFile/upload?uploadPath=/活动拉销":on-change="handleChange1" :on-remove="handleRemove1"  :on-preview="handlePreview1" :file-list="fileList1" list-type="picture">
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.confirmPhoto')" prop="confirmPhoto">
              <el-upload action="/api/sys/folderFile/upload?uploadPath=/活动拉销" :on-change="handleChange2" :on-remove="handleRemove2"  :on-preview="handlePreview2" :file-list="fileList2" list-type="picture">
              </el-upload>
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
        isCreate:this.$route.query.id==null,
        inputForm:{
            shop:{name:""}
        },
        fileList1:[],
        fileList2:[],
      }
    },
    methods:{
      findOne(){
        axios.get('/api/crm/shopBuild/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
        if(this.inputForm.scenePhoto !=null) {
          axios.get('/api/sys/folderFile/findByIds',{params: {ids:this.inputForm.scenePhoto}}).then((response)=>{
            this.fileList1= response.data;
        });
        }
        if(this.inputForm.confirmPhoto !=null) {
          axios.get('/api/sys/folderFile/findByIds',{params: {ids:this.inputForm.confirmPhoto}}).then((response)=>{
            this.fileList2= response.data;
        });
        }
        })
      },handlePreview1(file) {
      window.open(file.url);
    },handleChange1(file, fileList) {
      this.fileList1 = fileList;
    },handleRemove1(file, fileList) {
      this.fileList1 = fileList;
    },handlePreview2(file) {
      window.open(file.url);
    },handleChange2(file, fileList) {
      this.fileList2 = fileList;
    },handleRemove2(file, fileList) {
      this.fileList2 = fileList;
    },
    },created(){
      if(!this.isCreate){
        this.findOne();
      }
    }
  }
</script>
