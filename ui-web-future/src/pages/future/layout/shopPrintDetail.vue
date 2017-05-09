<template>
  <div>
    <head-tab active="shopPrintDetail"></head-tab>
    <div>
      <el-form :model="shopPrint" ref="shopPrint" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('shopPrintDetail.code')">
              {{shopPrint.id}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.officeName')">
              {{shopPrint.officeName}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.printType')">
              {{shopPrint.printType}}
            </el-form-item>
            <el-form-item  :label="$t('shopPrintDetail.content')">
              {{shopPrint.content}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.attachment')" prop="attachment">
              <el-upload  action="/api/basic/sys/folderFile/upload?uploadPath=/广告印刷" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopPrintDetail.address')">
              {{shopPrint.address}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.contact')">
              {{shopPrint.contator}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.mobilePhone')">
              {{shopPrint.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.createdBy')" >
              {{shopPrint.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.createdDate')"  >
              {{shopPrint.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.remarks')"  >
              {{shopPrint.remarks}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.pass')"  v-if="action=='audit'">
              <su-radio-group v-model="shopPrint.pass"></su-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopPrintDetail.passRemarks')"  v-if="action=='audit'">
              <el-input v-model="shopPrint.passRemarks" :placeholder="$t('shopPrintDetail.inputRemarks')" type="textarea"></el-input>
            </el-form-item>
            <el-form-item v-if="action=='audit'">
              <el-button type="primary" :disabled="submitDisabled"  @click="passSubmit()">{{$t('shopPrintDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <su-process-details v-model="shopPrint.processInstanceId"></su-process-details>
  </div>
</template>

<script>
  import suRadioGroup from 'components/common/su-radio-group';
  export default{
    components:{suRadioGroup},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        action:this.$route.query.action,
        shopPrint:{
            pass:'1',
        },
        submitData:{
            id:'',
            pass:'1',
            passRemarks:'',
        },
        submitDisabled:false,
        activitiEntity:{historicTaskInstances:[]},
        fileList:[]
      }
    },
    methods:{
      passSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["shopPrint"];
        form.validate((valid) => {
          if (valid) {
              util.copyValue(this.shopPrint,this.submitData);
              console.log(this.submitData);
            axios.post('/api/ws/future/layout/shopPrint/audit', qs.stringify(this.submitData)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      handlePreview(file) {
        window.open(file.url);
      }
    },created(){
      axios.get('/api/ws/future/layout/shopPrint/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        this.shopPrint = response.data;
        if(this.shopPrint.attachment != null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.shopPrint.attachment}}).then((response)=>{
            this.fileList = response.data;
          });
        }
        if(response.data.activitiEntity.historicTaskInstances){
          this.activitiEntity.historicTaskInstances = response.data.activitiEntity.historicTaskInstances;
        }
      })
    }
  }
</script>
