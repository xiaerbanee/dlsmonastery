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
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table :data="activitiEntity.historicTaskInstances">
      <el-table-column prop="name" :label="$t('shopPrintDetail.nodeName')"></el-table-column>
      <el-table-column :label="$t('shopPrintDetail.auditMan')" >
        <template scope="scope">{{activitiEntity.accountMap?activitiEntity.accountMap[scope.row.id]:''}}</template>
      </el-table-column>
      <el-table-column :label="$t('shopPrintDetail.auditDate')">
        <template scope="scope">
          {{scope.row.endTime | formatLocalDateTime}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('shopPrintDetail.auditRemarks')">
        <template scope="scope">
          {{activitiEntity.commentMap?activitiEntity.commentMap[scope.row.id]:''}}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        shopPrint:{id:"",attachment:""},
        activitiEntity:{historicTaskInstances:[]},
        fileList:[]
      }
    },
    methods:{
      findOne(){
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
      }, handlePreview(file) {
        window.open(file.url);
      }
    },created(){
      if(!this.isCreate){
        this.findOne();
      }
    }
  }
</script>
