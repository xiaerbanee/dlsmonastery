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
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible = false" :title="$t('recruitList.filter')" v-model="formVisible" size="medium " class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('recruitList.name')">
                <el-input v-model="formData.name"></el-input>
              </el-form-item>
              <el-form-item :label="$t('recruitList.firstAppointDate')">
                   <date-range-picker v-model="formData.firstAppointDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('recruitList.secondAppointDate')">
                <date-range-picker v-model="formData.secondAppointDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('recruitList.auditAppointDate')">
                <date-range-picker v-model="formData.auditAppointDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('recruitList.entryAppointDate')">
                <date-range-picker v-model="formData.entryAppointDate"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('recruitList.mobilePhone')">
                <el-input v-model="formData.mobilePhone"></el-input>
              </el-form-item>
              <el-form-item :label="$t('recruitList.registerBy')">
                <el-input v-model="formData.registerBy"></el-input>
              </el-form-item>
              <el-form-item :label="$t('recruitList.firstAppointBy')">
                <el-select v-model="formData.firstAppointBy"  clearable >
                  <el-option v-for="item in formData.extra.firstAppointByList"  :key="item.id" :label="item.loginName" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('recruitList.secondAppointBy')">
                <el-select v-model="formData.secondAppointBy"  clearable >
                  <el-option v-for="item in formData.extra.secondAppointByList"  :key="item.id" :label="item.loginName" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('recruitList.onJob')">
                <bool-select v-model="formData.onJob"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('recruitList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"   :element-loading-text="$t('recruitList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="name"  :label="$t('recruitList.name')"></el-table-column>
        <el-table-column prop="sex" :label="$t('recruitList.sex')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('recruitList.mobilePhone')"></el-table-column>
        <el-table-column prop="applyPositionName" :label="$t('recruitList.positionName')"></el-table-column>
        <el-table-column prop="firstAppointDate" :label="$t('recruitList.firstAppointDate')"></el-table-column>
        <el-table-column prop="secondAppointDate" :label="$t('recruitList.secondAppointDate')"></el-table-column>
        <el-table-column prop="auditAppointDate" :label="$t('recruitList.auditAppointDate')"></el-table-column>
        <el-table-column prop="entryAppointDate" :label="$t('recruitList.entryAppointDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('recruitList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('recruitList.operation')" width="140">
          <template scope="scope">
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'edit')"  v-permit="'hr:recruit:edit'">修改</el-button> </div>
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'delete')"  v-permit="'hr:recruit:delete'">删除</el-button></div>
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
        util.setQuery("recruitList",submitData);
        axios.get('/api/basic/hr/recruit?'+qs.stringify(submitData)).then((response) => {
          console.log(response.data)
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
        if(action=="edit") {
          this.$router.push({ name: 'recruitForm', query: { id: id }})
        } else if(action=="delete") {
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
          this.$router.push({ name: 'recruitBatchForm'})
      },addCategory(){
        this.$router.push({ name: 'recruitEnumList'})
      }
    },created () {
      var that = this;
      that.pageHeight = 0.74*window.innerHeight;
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


