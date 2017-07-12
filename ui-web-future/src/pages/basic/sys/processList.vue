<template>
  <div>
    <head-tab active="processList"></head-tab>
    <div>
      <el-table :data="processList" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('processList.loading')" stripe border>
        <el-table-column  prop="id" :label="$t('processList.id')"  ></el-table-column>
        <el-table-column prop="deploymentId" :label="$t('processList.deploymentId')"  ></el-table-column>
        <el-table-column prop="name" :label="$t('processList.name')" ></el-table-column>
        <el-table-column  prop="key" :label="$t('processList.key')"  ></el-table-column>
        <el-table-column prop="version" :label="$t('processList.version')"  ></el-table-column>
        <el-table-column  prop="resourceName" :label="$t('processList.resourceName')"  ></el-table-column>
        <el-table-column prop="deploymentTime" :label="$t('processList.deploymentTime')":formatter="formatterDate"></el-table-column>
        <el-table-column prop="suspended" :label="$t('processList.suspended')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        formData:{},
        submitData:{},
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
        processList:[]
      };
    },
     methods: {
     formatterDate(row, column){
        return util.formatLocalDateTime(row.deploymentTime);;
      }
     },
    created () {
       this.pageHeight = 0.75*window.innerHeight;
      util.copyValue(this.$route.query,this.submitData);
      util.setQuery("processList",this.submitData);
      util.copyValue(this.formData,this.submitData);
      this.initPromise = axios.get('/api/general/sys/activiti/processList?'+qs.stringify(this.submitData)).then((response) => {
        this.processList = response.data;
        this.pageLoading = false;
      })
    },activated(){
      this.initPromise.then(()=>{

      })
    }
  };
</script>

