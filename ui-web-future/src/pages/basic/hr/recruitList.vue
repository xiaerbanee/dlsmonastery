<template>
  <div>
    <head-tab active="recruitList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:recruit:edit'">{{$t('recruitList.add')}}</el-button>
        <el-button type="primary" @click="batchEdit" icon="edit" v-permit="'hr:recruit:edit'">{{$t('recruitList.batchEdit')}}</el-button>
        <el-button type="primary" @click="addCategory" icon="edit" v-permit="'hr:recruit:edit'">{{$t('recruitList.addCategory')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('recruitList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible = false" :title="$t('recruitList.filter')" v-model="formVisible" size="medium " class="search-form">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="姓名">
                <el-input v-model="formData.name"></el-input>
              </el-form-item>
              <el-form-item label="初试预约时间">
                   <date-time-picker v-model="formData.firstRealDate"></date-time-picker>
              </el-form-item>
              <el-form-item label="复试预约时间">
                <date-time-picker v-model="formData.secondRealDate"></date-time-picker>
              </el-form-item>
              <el-form-item label="资审时间">
                <date-time-picker v-model="formData.physicalRealDate"></date-time-picker>
              </el-form-item>
              <el-form-item label="预约入职时间">
                <date-time-picker v-model="formData.entryRealDate"></date-time-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号码">
                <el-input v-model="formData.mobilePhone"></el-input>
              </el-form-item>
              <el-form-item label="初试邀约人">
                <el-input v-model="formData.mobilePhone"></el-input>
              </el-form-item>
              <el-form-item label="初试人">
                <el-input v-model="formData.mobilePhone"></el-input>
              </el-form-item>
              <el-form-item label="复试人">
                <el-input v-model="formData.mobilePhone"></el-input>
              </el-form-item>
              <el-form-item label="2个月是否在岗">
                <bool-select v-model="formData.status"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('recruitList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"   :element-loading-text="$t('recruitList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" ></el-table-column>
        <el-table-column  prop="name"  :label="$t('recruitList.name')"></el-table-column>
        <el-table-column prop="sex" :label="$t('recruitList.sex')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('recruitList.mobilePhone')"></el-table-column>
        <el-table-column prop="applyPositionId" :label="$t('recruitList.positionName')"></el-table-column>
        <el-table-column prop="firstAppointDate" :label="$t('recruitList.firstAppointDate')"></el-table-column>
        <el-table-column prop="secondAppointDate" :label="$t('recruitList.secondAppointDate')"></el-table-column>
        <el-table-column prop="auditAppointDate" :label="$t('recruitList.auditAppointDate')"></el-table-column>
        <el-table-column prop="entryRealDate" :label="$t('recruitList.entryRealDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('recruitList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressOrderList.operation')" width="140">
          <template scope="scope">
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'修改')"  v-permit="'hr:recruit:edit'">修改</el-button> </div>
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'删除')"  v-permit="'hr:recruit:delete'">删除</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import dateTimePicker from "components/common/date-time-picker"
import boolSelect from "components/common/bool-select"
  export default {
  components:{
    boolSelect,
    dateTimePicker
  },
    data() {
      return {
        searchText:"",
        pageLoading: false,
        page:{},
        formData:{
          extra:{}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        selects:new Array(),
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
        util.setQuery("recruitList",this.submitData);
        axios.get('/api/basic/hr/recruit?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'recruitForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'recruitForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/hr/recruit/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchEdit(){
        if(this.selects.length>1){
          this.$router.push({ name: 'recruitBatchForm', query: { ids: this.selects }})
        }
      },addCategory(){
        this.$router.push({ name: 'recruitEnumList'})
      }
    },created () {
      var that = this;
      that.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/recruit/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>


