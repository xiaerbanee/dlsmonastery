<template>
  <div>
    <head-tab active="shopBuildDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="140px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildDetail.shopBuildCode')" prop="id">
             {{inputForm.formatId}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.office')" prop="depotDto">
              {{inputForm.officeName}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.shopName')" prop="shopId">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.monthSaleQty')" prop="recentSaleDescription">
              {{recentSaleDescription}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.investInCause')" prop="investInCause">
              {{inputForm.investInCause}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.imageDeposit')">
              {{imageDeposit}}
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
            <el-form-item :label="$t('shopBuildDetail.shopAgreement')" prop="shopAgreement">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :on-preview="handlePreview3" :file-list="fileList3" list-type="picture">
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.scenePhoto')" prop="scenePhoto">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :on-preview="handlePreview1" :file-list="fileList1" list-type="picture">
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopBuildDetail.confirmPhoto')" prop="confirmPhoto">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :on-preview="handlePreview2" :file-list="fileList2" list-type="picture">
              </el-upload>
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
            imageDeposit:'',
            recentSaleDescription:'',
            fileList1:[],
            fileList2:[],
            fileList3:[],
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
      },refreshRecentMonthSaleAmount(){
        if(util.isBlank(this.inputForm.shopId)){
          this.recentSaleDescription='';
          return;
        }

        axios.get('/api/ws/future/basic/depot/getRecentMonthSaleAmount' , {params: {depotId: this.inputForm.shopId, monthQty:3}}).then((response) => {
          if(response.data){
            let tmp = '';
            for(let key in response.data){
              tmp = tmp + key +"销量："+  response.data[key] +"台；";
            }
            this.recentSaleDescription=tmp;
          }else{
            this.recentSaleDescription='';
          }
        });
      }, handlePreview1(file) {
        window.open(file.url);
      },handlePreview2(file) {
        window.open(file.url);
      },handlePreview3(file) {
        window.open(file.url);
      },initPage(){
        axios.get('/api/ws/future/layout/shopBuild/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
          this.refreshRecentMonthSaleAmount();
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
          if(this.inputForm.shopAgreement !=null) {
            axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.shopAgreement}}).then((response)=>{
              this.fileList3= response.data;
            });
          }
          axios.get('/api/ws/future/layout/shopDeposit/findLeftAmount', {params: {type: '形象保证金', depotId: this.inputForm.shopId}}).then((response) => {
            this.imageDeposit = response.data;
          });
        });
        axios.get('/api/ws/future/layout/shopBuild/getAuditForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.formData = response.data;
        })
      }
    },created(){
      this.initPage();
    }
  }
</script>
