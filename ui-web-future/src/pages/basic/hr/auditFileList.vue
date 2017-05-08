<template>
  <div>
    <head-tab active="auditFileList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:auditFile:edit'">{{$t('auditFileList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:auditFile:view'">{{$t('auditFileList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('auditFileList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('auditFileList.notZero')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.auditType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.auditType" filterable clearable :placeholder="$t('auditFileList.inputKey')">
                  <el-option v-for="(value,key) in auditTypes" :key="key" :label="value" :value="key | toInteger"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.officeName.label"  :label-width="formLabelWidth">
                <el-input v-model="formData.officeName" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label"  :label-width="formLabelWidth">
                <el-select v-model="formData.officeId"  clearable filterable remote :placeholder="$t('auditFileList.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading">
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item :label="formLabel.createdByName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdByName" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.stageName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.stageName" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.processTypeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.processTypeId" filterable clearable :placeholder="$t('auditFileList.inputKey')">
                  <el-option v-for="item in formData.processTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.content.label" :label-width="formLabelWidth">
                <el-input v-model="formData.content" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.title.label" :label-width="formLabelWidth">
                <el-input v-model="formData.title" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('auditFileList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('auditFileList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="id" :label="$t('auditFileList.id')" sortable></el-table-column>
        <el-table-column prop="createdByName":label="$t('auditFileList.applyAccount')"></el-table-column>
        <el-table-column prop="areaName":label="$t('auditFileList.areaName')"></el-table-column>
        <el-table-column prop="officeName" :label="$t('auditFileList.officeName')"></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('auditFileList.createdDate')"></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('auditFileList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('auditFileList.lastModifiedDate')"></el-table-column>
        <el-table-column prop="title" :label="$t('auditFileList.title')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('auditFileList.processStatus')" width="150">
          <template scope="scope">
            <el-tag type="primary">{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column >
        <el-table-column prop="remarks" :label="$t('auditFileList.remarks')"></el-table-column>
        <el-table-column prop="memo":label="$t('auditFileList.memo')"></el-table-column>
        <el-table-column :label="$t('auditFileList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'审核')"  class="action">审核</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'详细')"  class="action">详细</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'删除')"  class="action">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page:{},
        formData:{
          createdDate:'',
        },submitData:{
          page:0,
          size:25,
          id:'',
          auditType:1,
          officeName:'',
          officeId:'',
          createdDate:'',
          createdByName:'',
          stageName:'',
          processTypeId:'',
          content:'',
          title:''
        },formLabel:{
          id:{label:this.$t('auditFileList.id')},
          auditType:{label:this.$t('auditFileList.auditType'),value:''},
          officeName:{label:this.$t('auditFileList.officeName')},
          officeId:{label:this.$t('auditFileList.officeId'),value:''},
          createdDate:{label:this.$t('auditFileList.createdDate')},
          createdByName:{label:this.$t('auditFileList.applyAccount')},
          stageName:{label:this.$t('auditFileList.stageName')},
          processTypeId:{label:this.$t('auditFileList.processTypeName'),value:''},
          content:{label:this.$t('auditFileList.content')},
          title:{label:this.$t('auditFileList.title')}
        },auditTypes:{
          0:this.$t('auditFileList.all'),
          1:this.$t('auditFileList.waitAudit')
        },
        offices:[],
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.auditType.value=this.auditTypes[this.formData.auditType];
        this.formLabel.officeId.value=util.getLabel(this.offices,this.formData.officeId);
        this.formLabel.processTypeId.value=util.getLabel(this.formData.processTypes,this.formData.processTypeId);
        util.setQuery("auditFileList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/auditFile?'+qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'auditFileForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'auditFileForm', query: { id: id }})
        } else if(action=="详细"){
          this.$router.push({ name: 'auditFileDetail', query: { id: id,action:"detail" }})
        }else if(action=="审核"){
          this.$router.push({ name: 'auditFileDetail', query: { id: id,action:"audit" }})
        }else if(action=="删除") {
          axios.get('/api/basic/hr/auditFile/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        }
      }
    },created () {
        var that=this;
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/hr/auditFile/getQuery').then((response) =>{
      this.formData=response.data;
      util.copyValue(that.$route.query,this.formData);
      that.pageRequest();
    });
    }
  };
</script>

