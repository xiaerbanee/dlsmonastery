<template>
  <div>
    <head-tab active="shopBuildList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopBuild:view'">{{$t('shopBuildList.filterOrExport')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchBlack')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopBuildList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('shopBuildList.inputKey')">
                  <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.auditType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.auditType" filterable clearable :placeholder="$t('shopBuildList.inputKey')">
                  <el-option v-for="(value,key) in auditTypes" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopBuildList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.processFlow.label" :label-width="formLabelWidth">
                <el-select v-model="formData.processFlow" filterable clearable :placeholder="$t('shopBuildList.inputKey')">
                  <el-option v-for="processFlow in formProperty.processFlows" :key="processFlow.id" :label="processFlow.name" :value="processFlow.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.fixtureType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.fixtureType" filterable clearable :placeholder="$t('shopBuildList.inputKey')">
                  <el-option v-for="fixtureType in formProperty.fixtureTypes" :key="fixtureType" :label="fixtureType" :value="fixtureType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('shopBuildList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('shopBuildList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData" icon="upload">{{$t('shopBuildList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('shopBuildList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopBuildList.loading')" @sort-change="sortChange" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column fixed prop="id" :label="$t('shopBuildList.code')" sortable></el-table-column>
        <el-table-column prop="shop.extendMap.officeName" :label="$t('shopBuildList.officeName')" ></el-table-column>
        <el-table-column prop="shop.name" :label="$t('shopBuildList.shopName')" ></el-table-column>
        <el-table-column prop="fixtureType" :label="$t('shopBuildList.fixtureType')" ></el-table-column>
        <el-table-column prop="content" :label="$t('shopBuildList.content')" width="140"></el-table-column>
        <el-table-column prop="oldContents"  :label="$t('shopBuildList.oldContents')"   ></el-table-column>
        <el-table-column prop="newContents" :label="$t('shopBuildList.newContents')" ></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopBuildList.processFlow')" width="150">
          <template scope="scope">
            <el-tag :type="scope.row.processStatus === '未通过' ? 'primary' : 'success'"  close-transition>{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created.loginName" :label="$t('shopBuildList.createdBy')"  ></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopBuildList.createdDate')" sortable ></el-table-column>
        <el-table-column prop="lastModified.loginName" :label="$t('shopBuildList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopBuildList.lastModifiedDate')" sortable width="150" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopBuildList.remarks')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('shopBuildList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
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
        pageLoading: false,
        page:{},
        formData:{
          page:0,
          size:25,
          officeId:'',
          auditType:"1",
          shopName:'',
          processFlow:'',
          fixtureType:'',
          createdBy:'',
          createdDate:'',
          createdDateBTW:'',
        },formLabel:{
          officeId:{label: this.$t('shopBuildList.officeName'),value:''},
          auditType:{label: this.$t('shopBuildList.auditType'),value:''},
          shopName:{label:this.$t('shopBuildList.shopName')},
          processFlow:{label:this.$t('shopBuildList.processFlow'),value:''},
          fixtureType:{label:this.$t('shopBuildList.fixtureType'),value:''},
          createdBy:{label: this.$t('shopBuildList.createdBy')},
          createdDateBTW:{label: this.$t('shopBuildList.createdDate')},
        },auditTypes:{
          0:this.$t('shopBuildList.all'),
          1:this.$t('shopBuildList.waitAudit')
        },
        multipleSelection:[],
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.officeId.value = util.getLabel(this.formProperty.areas, this.formData.officeId);
        this.formLabel.processFlow.value = util.getLabel(this.formProperty.processFlows, this.formData.processFlow);
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        this.formLabel.auditType.value = this.auditTypes[this.formData.auditType];
        util.setQuery("shopBuildList",this.formData);
        axios.get('/api/crm/shopBuild',{params:this.formData}).then((response) => {
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
      },exportData(){
        window.location.href= "/api/crm/shopBuild/export?"+qs.stringify(this.formData);
      },itemAdd(){
        this.$router.push({ name: 'shopBuildForm'});
      },itemAction:function(id,action){
        if(action=="详细") {
          this.$router.push({ name: 'shopBuildDetail', query:{id: id}});
        }else if(action=="删除") {
          axios.get('/api/crm/shopBuild/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }else if(action == "修改"){
          this.$router.push({name: 'shopBuildForm', query:{id: id}});
        }else if(action == "审核"){
          this.$router.push({name: 'shopBuildDetail', query:{id: id}});
        }
     },handleSelectionChange(val) {
         var arr = []
         for(var key in val){
           arr.push(val[key].id);
        }
        this.multipleSelection = arr;
    },batchPass(){
      axios.get('/api/crm/shopBuild/batchAudit',{params:{pass:true, ids:this.multipleSelection}}).then((response) =>{
        this.$message(response.data.message);
        this.pageRequest();
      })
    },batchBack(){
      axios.get('/api/crm/shopBuild/batchAudit',{params:{pass:false, ids:this.multipleSelection}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
    },getQuery(){
        axios.get('/api/crm/shopBuild/getQuery').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },
    created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
      this.getQuery();
    }
  };
</script>
