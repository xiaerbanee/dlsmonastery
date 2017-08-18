<template>
  <div>
    <head-tab active="recruitEnumList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:recruitEnum:edit'">编辑</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'sys:recruitEnum:view'">{{$t('recruitEnumList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('recruitEnumList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-form-item :label="$t('recruitEnumList.category')">
            <el-select v-model="formData.category" filterable clearable :placeholder="$t('recruitEnumList.inputKey')">
              <el-option v-for="category in formData.extra.categoryList" :key="category" :label="category" :value="category"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('recruitEnumList.value')">
            <el-input v-model="formData.label" auto-complete="off" :placeholder="$t('recruitEnumList.likeSearch')"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('recruitEnumList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('recruitEnumList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="id" :label="$t('recruitEnumList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="category" :label="$t('recruitEnumList.category')"></el-table-column>
        <el-table-column prop="label" :label="$t('recruitEnumList.value')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('recruitEnumList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('recruitEnumList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('recruitEnumList.createdDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('dictEnumList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:dictEnum:delete'">{{$t('dictEnumList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default{
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false
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
        util.setQuery("recruitEnumList",submitData);
        axios.get('/api/basic/hr/recruitEnum?'+qs.stringify(submitData)).then((response) => {
          console.log(response.data)
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'recruitEnumForm'})
      },itemAction(id,action){
        if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/basic/hr/recruitEnum/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/recruitEnum/getQuery').then((response) =>{
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

