<template>
  <div>
    <head-tab active="shopAdDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopAdDetail.shopAdTypeId')" prop="shopAdTypeId">
              {{inputForm.shopAdTypeName}}
            </el-form-item>
            <el-form-item  :label="$t('shopAdDetail.shopId')"  prop="shopId">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.length')" prop="length">
              {{inputForm.length}}
            </el-form-item>
            <el-form-item  :label="$t('shopAdDetail.width')" prop="width">
              {{inputForm.width}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.qty')" prop="qty">
              {{inputForm.qty}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.content')" prop="content">
              {{inputForm.content}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.specialArea')" prop="specialArea">
              {{inputForm.specialArea | bool2str}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.remarks')" prop="remarks">
              {{inputForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.attachment')" prop="attachment">
              <el-upload  action="/api/basic/sys/folderFile/upload?uploadPath=/广告申请" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="2">
            <el-form-item :label="$t('shopAdDetail.pass')"  v-if="isAudit">
              <bool-radio-group v-model="formData.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopAdDetail.passRemarks')"  v-if="isAudit">
              <el-input v-model="formData.passRemarks" :placeholder="$t('shopAdDetail.inputRemarks')" type="textarea"></el-input>
            </el-form-item>
            <el-form-item v-if="isAudit">
              <el-button type="primary" :disabled="submitDisabled"  @click="passSubmit()">{{$t('shopAdDetail.save')}}</el-button>
            </el-form-item>
            <process-details v-model="inputForm.processInstanceId"></process-details>
          </el-col>
        </el-row>
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
            isCreate:this.$route.query.id==null,
            isAudit:this.$route.query.action=='audit',
            inputForm:{},
            formData:{
              extra:{}
            },
            fileList:[],
            submitDisabled:false,
          }
        },
      passSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            axios.post('/api/ws/future/layout/shopAd/audit', qs.stringify(util.deleteExtra(this.formData))).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
              if(response.data.success){
                Object.assign(this.$data,this.getData());
                this.initPage();
                this.submitDisabled = true;
                this.$router.push({name:'shopAdList',query:util.getQuery("shopAdList"),params:{_closeFrom:true}})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      handlePreview(file) {
        window.open(file.url);
      },initPage(){
        axios.get('/api/ws/future/layout/shopAd/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          if(this.inputForm.attachment !=null) {
            axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
              this.fileList= response.data;
            });
          }
        })
        axios.get('/api/ws/future/layout/shopAd/getAuditForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.formData = response.data;
        })
      }
    },created(){
      this.initPage();
    }
  }
</script>
