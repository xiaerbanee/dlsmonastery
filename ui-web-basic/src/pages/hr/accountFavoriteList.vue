<template>
  <div>
    <head-tab active="accountFavoriteList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:dictEnum:edit'">{{$t('accountFavoriteList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'sys:dictEnum:view'">{{$t('accountFavoriteList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('accountFavoriteList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-form-item :label="$t('accountFavoriteList.name')">
            <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('accountFavoriteList.likeSearch')"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountFavoriteList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountFavoriteList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="parentName" label="上级文件夹"></el-table-column>
        <el-table-column prop="name" :label="$t('accountFavoriteList.name')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('accountFavoriteList.remarks')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('accountFavoriteList.createdDate')"></el-table-column>
        <el-table-column prop="locked" :label="$t('accountFavoriteList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('accountFavoriteList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:dictEnum:edit'">{{$t('accountFavoriteList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:dictEnum:delete'">{{$t('accountFavoriteList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      accountSelect
    },
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
        util.setQuery("accountFavoriteList",submitData);
        axios.get('/api/basic/hr/accountFavorite?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'accountFavoriteForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'accountFavoriteForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/basic/hr/accountFavorite/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/accountFavorite/getQuery').then((response) =>{
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

