<template>
  <div>
    <head-tab active="accountChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'hr:accountChange:edit'">{{$t('accountChangeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" >{{$t('accountChangeList.filter')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check"    v-permit="'hr:accountChange:audit'">批量通过</el-button>
        <el-button type="primary" @click="batchNoPass" icon="close"    v-permit="'hr:accountChange:audit'">批量打回</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('accountChangeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData"  :label-width="formLabelWidth">
          <el-form-item :label="$t('accountChangeList.createdDate')">
            <date-range-picker v-model="formData.createdDate"></date-range-picker>
          </el-form-item>
          <el-form-item :label="$t('accountChangeList.createdBy')">
            <el-input v-model="formData.createdByName" auto-complete="off" :placeholder="$t('accountChangeList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('accountChangeList.areaName')">
            <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('accountChangeList.inputKey')">
              <el-option v-for="area in formData.extra.areaList" :key="area.id" :label="area.name" :value="area.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('accountChangeList.type')">
            <el-select v-model="formData.type" clearable filterable :placeholder="$t('accountChangeList.selectGroup')">
              <el-option v-for="type in formData.extra.typeList" :key="type" :label="type" :value="type"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountChangeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('accountChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="id" :label="$t('accountChangeList.id')" sortable></el-table-column>
        <el-table-column  prop="areaName" :label="$t('accountChangeList.areaName')" sortable></el-table-column>
        <el-table-column  prop="createdByName" :label="$t('accountChangeList.applyAccount')" sortable></el-table-column>
        <el-table-column  prop="createdDate" :label="$t('accountChangeList.applyDate')" sortable></el-table-column>
        <el-table-column  prop="type" :label="$t('accountChangeList.type')" sortable></el-table-column>
        <el-table-column  prop="oldLabel" :label="$t('accountChangeList.oldValue')" sortable></el-table-column>
        <el-table-column  prop="newLabel" :label="$t('accountChangeList.newValue')" sortable></el-table-column>
        <el-table-column  prop="processStatus" :label="$t('accountChangeList.processStatus')" sortable ></el-table-column>
        <el-table-column  prop="remarks" :label="$t('accountChangeList.remarks')" sortable ></el-table-column>
        <el-table-column :label="$t('accountChangeList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'detail')" v-permit="'hr:accountChange:view'" >详细</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'hr:accountChange:delete'" v-if="scope.row.processStatus !== '已通过' &&scope.row.processStatus !== '未通过'">删除</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'audit')" v-permit="'hr:accountChange:audit'" v-if="scope.row.processStatus !== '已通过' &&scope.row.processStatus !== '未通过'">审核</el-button>
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
          extra:{},
        },
        initPromise:{},
        searchText:"",
        selects:[],
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false,
        show:false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("accountChangeList",submitData);
        axios.get('/api/basic/hr/accountChange?'+qs.stringify(submitData)).then((response) => {
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
        this.show = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'accountChangeForm'})
      },itemAction:function(id,action){
        if(action=="detail") {
          this.$router.push({ name: 'accountChangeForm', query: { id: id,action:action }})
        } else if(action=="audit"){
          this.$router.push({ name: 'accountChangeForm', query: { id: id,action:action }})
        }else if(action="delete") {
            util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/hr/accountChange/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }).catch(()=>{});
        }
      },selectionChange(selection){
        this.selects = [];
        for (let each of selection) {
          this.selects.push(each.id);
        }
      },batchPass(){
        if(!this.selects || this.selects.length < 1){
          this.$message("请选择审批记录");
          return ;
        }
        util.confirmBeforeBatchPass(this).then(() => {
          this.submitDisabled = true;
          this.pageLoading = true;
          axios.get('/api/basic/hr/accountChange/batchPass',{params:{ids:this.selects, pass:'1'}}).then((response) =>{
            this.$message(response.data.message);
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          });
        }).catch(()=>{});
      },batchNoPass(){
        if(!this.selects || this.selects.length < 1){
          this.$message("请选择审批记录");
          return ;
        }
        util.confirmBeforeBatchPass(this).then(() => {
          this.submitDisabled = true;
          this.pageLoading = true;
          axios.get('/api/basic/hr/accountChange/batchPass',{params:{ids:this.selects, pass:'0'}}).then((response) =>{
            this.$message(response.data.message);
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          });
        }).catch(()=>{});
      },checkSelectable(row) {
        return row.processStatus !== '已通过' && row.processStatus !== '未通过'
      }
    },created () {
      var that=this;
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/accountChange/getQuery').then((response) =>{
        that.formData=response.data;
        console.log(this.formData.extra.typeList);
        util.copyValue(that.$route.query,that.formData);
      });
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

