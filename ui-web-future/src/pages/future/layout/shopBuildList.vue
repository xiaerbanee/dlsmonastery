<template>
  <div>
    <head-tab active="shopBuildList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopBuild:view'">{{$t('shopBuildList.filterOrExport')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopBuild:edit'">{{$t('shopBuildList.batchBlack')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopBuildList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
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
                  <el-option v-for="processFlow in formData.processFlows" :key="processFlow.id" :label="processFlow.name" :value="processFlow.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.fixtureType.label" :label-width="formLabelWidth">
                <dict-enum-select v-model="formData.fixtureType" category="装修类别"></dict-enum-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('shopBuildList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
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
        <el-table-column prop="officeName" :label="$t('shopBuildList.officeName')" ></el-table-column>
        <el-table-column prop="shopName" :label="$t('shopBuildList.shopName')" ></el-table-column>
        <el-table-column prop="fixtureType" :label="$t('shopBuildList.fixtureType')" ></el-table-column>
        <el-table-column prop="content" :label="$t('shopBuildList.content')" width="150"></el-table-column>
        <el-table-column prop="oldContents"  :label="$t('shopBuildList.oldContents')"   ></el-table-column>
        <el-table-column prop="newContents" :label="$t('shopBuildList.newContents')" ></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopBuildList.processFlow')" width="150">
          <template scope="scope">
            <el-tag :type="scope.row.processStatus === '已通过' ? 'primary' : 'danger'"  close-transition>{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdByName" :label="$t('shopBuildList.createdBy')"  ></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopBuildList.createdDate')" sortable ></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('shopBuildList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopBuildList.lastModifiedDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopBuildList.remarks')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('shopBuildList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:shopBuild:view'" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopPrintList.detail')}}</el-button>
            <el-button size="small" v-if="scope.row.isAuditable" v-permit="'crm:shopBuild:edit'" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopBuildList.audit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopBuild:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopBuildList.edit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopBuild:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopBuildList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import dictEnumSelect from 'components/basic/dict-enum-select'
  export default {
    components:{officeSelect,dictEnumSelect},
    data() {
      return {
        pageLoading: false,
        page:{},
        formData:{
            auditType:'1',
        },
        submitData:{
          page:0,
          size:25,
          officeId:'',
          auditType:'',
          shopName:'',
          processFlow:'',
          fixtureType:'',
          createdBy:'',
          createdDate:'',
        },
        formLabel:{
          officeId:{label: this.$t('shopBuildList.officeName'),value:''},
          auditType:{label: this.$t('shopBuildList.auditType'),value:''},
          shopName:{label:this.$t('shopBuildList.shopName')},
          processFlow:{label:this.$t('shopBuildList.processFlow'),value:''},
          fixtureType:{label:this.$t('shopBuildList.fixtureType'),value:''},
          createdBy:{label: this.$t('shopBuildList.createdBy')},
          createdDate:{label: this.$t('shopBuildList.createdDate')},
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
        this.formLabel.auditType.value = this.auditTypes[this.formData.auditType];
        util.copyValue(this.formData,this.submitData);
        util.setQuery("shopBuildList",this.submitData);
        axios.get('/api/ws/future/layout/shopBuild',{params:this.submitData}).then((response) => {
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
        if(action=="detail") {
          this.$router.push({ name: 'shopBuildDetail', query:{id: id}});
        }else if(action=="delete") {
          axios.get('/api/crm/shopBuild/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }else if(action == "edit"){
          this.$router.push({name: 'shopBuildForm', query:{id: id}});
        }else if(action == "audit"){
          this.$router.push({name: 'shopBuildDetail', query:{id: id,action:action}});
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
    }
    },
    created () {
        var that = this;
        that.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/shopBuild/getQuery',{params:this.formData}).then((response) =>{
           that.formData = response.data;
           util.copyValue(this.$route.query,that.formData);
           that.pageRequest();
        });
    }
  };
</script>
