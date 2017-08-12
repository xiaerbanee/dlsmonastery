<template>
  <div>
    <head-tab active="auditFileList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:auditFile:edit'">{{$t('auditFileList.add')}}</el-button>
        <el-button type="primary" @click="itemFavorite" icon="plus" v-permit="'hr:auditFile:edit'">{{$t('auditFileList.favorite')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'hr:auditFile:view'">{{$t('auditFileList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('auditFileList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get"  :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('auditFileList.id')">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('auditFileList.notZero')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.auditType')">
                <el-select v-model="formData.auditType" filterable clearable :placeholder="$t('auditFileList.inputKey')">
                  <el-option v-for="item in auditTypes" :key="item.id" :label="item.value" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('auditFileForm.processTypeName')" prop="processTypeName">
                <el-select v-model="formData.processTypeId" filterable clearable :placeholder="$t('auditFileForm.inputWord')">
                  <el-option v-for="type in processTypeList" :key="type.id" :label="type.name" :value="type.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.officeName')">
                <el-input v-model="formData.officeName" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.officeId')">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.createdDate')">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('auditFileList.applyAccount')">
                <el-input v-model="formData.createdByName" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.stageName')">
                <el-input v-model="formData.processStatus" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.content')">
                <el-input v-model="formData.content" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('auditFileList.title')">
                <el-input v-model="formData.title" auto-complete="off" :placeholder="$t('auditFileList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="是否收藏">
                <bool-select v-model="formData.collect"></bool-select>
              </el-form-item>
              <el-form-item label="收藏日期">
                <date-range-picker v-model="formData.collectDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('auditFileList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-dialog  title="订单详细" v-model="updateVisible" size="tiny" class="search-form" z-index="1500">
        <el-form :model="inputForm" label-width="120px">
              <el-form-item :label="$t('auditFileList.memo')" prop="memo">
                <el-input v-model="inputForm.memo"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="formSubmit()">确定</el-button>
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
        <el-table-column prop="processTypeName" label="流程名称"></el-table-column>
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
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'verify')"  class="action" v-if="scope.row.auditable">审核</el-button></div>
            <div class="action"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')"  class="action"  v-permit="'hr:auditFile:view'">详细</el-button></div>
            <div class="action"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')"  class="action" v-permit="'hr:auditFile:delete'" v-if="scope.row.editable">删除</el-button></div>
            <div class="action"><el-button size="small" @click.native="itemAction(scope.row.id,'collect',scope.row)"  class="action"  v-if="scope.row.collect">取消收藏</el-button></div>
            <div class="action"><el-button size="small" @click.native="collect(scope.row)"  class="action"  v-if="!scope.row.collect">收藏</el-button></div>
            <div class="action"><el-button size="small" @click.native="updateMemo(scope.row)"  v-permit="'hr:auditFile:updateMemo'">批注修改</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  import boolSelect from 'components/common/bool-select'
  export default {
    components:{officeSelect,boolSelect},
    data() {
      return {
        page:{},
        formData:{
          extra:{}
        },
        inputForm:{},
        initPromise:{},
        searchText:'',
        auditTypes:[
          {id:"1",value:this.$t('auditFileList.all')},
          {id:"0",value:this.$t('auditFileList.waitAudit')}
        ],
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false,
        updateVisible:false,
        processTypeList:[],
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("auditFileList",submitData);
        axios.get('/api/basic/hr/auditFile?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'auditFileForm'})
      },itemFavorite(){
        this.$router.push({ name: 'accountFavoriteList'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'auditFileForm', query: { id: id }})
        } else if(action=="detail"){
          this.$router.push({ name: 'auditFileDetail', query: { id: id,action:"detail" }})
        }else if(action=="verify"){
          this.$router.push({ name: 'auditFileDetail', query: { id: id,action:"audit" }})
        }else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/hr/auditFile/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      },collect(row){
        axios.get("/api/basic/hr/accountFavorite/findTreeNodeList").then((response)=>{
          console.log(response.data)
        })

//        axios.get('/api/basic/hr/auditFileCollect/collect?auditFileId='+row.id+'&collect='+!row.collect).then((response) =>{
//          this.$message(response.data.message);
//          row.collect=!row.collect
//        });
      }, updateMemo(row){
        this.updateVisible=true;
        this.inputForm=JSON.parse(JSON.stringify(row));
      },formSubmit(){
        axios.post('/api/basic/hr/auditFile/updateMemo', qs.stringify(this.inputForm)).then((response)=> {
          this.$message(response.data.message);
          this.updateVisible=false;
          this.pageRequest();
        })
      }
    },created () {
      var that=this;
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/auditFile/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(that.$route.query,this.formData);
      });
      axios.get('/api/general/sys/processType/findByAuditFileTypeIsTrue').then((response)=>{
        this.processTypeList = response.data;
      })
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

