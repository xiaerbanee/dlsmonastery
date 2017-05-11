<template>
  <div>
    <head-tab active="shopAdDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopAdDetail.shopAdTypeId')" prop="shopAdTypeId">
              {{inputForm.shopAdTypeId}}
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
            <span v-html="inputForm.content"></span>
            <su-process-details v-model="inputForm.processInstanceId"></su-process-details>
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
          shopAdType:{name:''},
          shop:{name:''},
          attachment:""
        },
        activitiEntity:{historicTaskInstances:[]},
        fileList:[]
      }
    },
    methods:{
      findOne(){
        axios.get('/api/crm/shopAd/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.inputForm=response.data.shopAd;
        console.log(this.inputForm.attachment);
        if(this.inputForm.attachment !=null) {
          axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
            this.fileList= response.data;
        }).catch(function () {
            this.submitDisabled = false;
          });
        }
          if(response.data.activitiEntity.historicTaskInstances){
            this.activitiEntity.historicTaskInstances = response.data.activitiEntity.historicTaskInstances;
          }
        })
      }, handlePreview(file) {
        window.open(file.url);
      }
    },created(){
      axios.get('/api/ws/future/layout/shopAd/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        console.log(this.inputForm);
        if(this.inputForm.attachment !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
            this.fileList= response.data;
          });
        }
        if(response.data.activitiEntity.historicTaskInstances){
          this.activitiEntity.historicTaskInstances = response.data.activitiEntity.historicTaskInstances;
        }
      })
    }
  }
</script>
