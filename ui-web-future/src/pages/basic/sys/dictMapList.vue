<template>
  <div>
    <head-tab active="dictMapList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:dictMap:edit'">{{$t('dictMapList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search"  v-permit="'sys:dictMap:view'">{{$t('dictMapList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('dictMapList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('dictMapList.createdDate')" >
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('dictMapList.category')">
                <el-select v-model="formData.category" filterable clearable :placeholder="$t('dictMapList.inputKey')">
                  <el-option v-for="category in formData.extra.categoryList" :key="category" :label="category" :value="category"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('dictMapList.value')">
                <el-input v-model="formData.value" auto-complete="off" :placeholder="$t('dictMapList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dictMapList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dictMapList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('dictMapList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="category" :label="$t('dictMapList.category')" sortable></el-table-column>
        <el-table-column prop="value" :label="$t('dictMapList.value')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dictMapList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('dictMapList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('dictMapList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:dictMap:edit'">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:dictMap:delete'">删除</el-button>
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
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading:false
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
        util.setQuery("dictMapList",submitData);
        axios.get('/api/basic/sys/dictMap?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'dictMapForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'dictMapForm', query: { id: id }})
        } else if(action=="delete") {
            util.confirmBeforeDelRecord(this).then(()=> {
              axios.get('/api/basic/sys/dictMap/delete', {params: {id: id}}).then((response) => {
                this.$message(response.data.message);
                this.pageRequest();
              });
            }).catch(()=>{});
        }
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/sys/dictMap/getQuery').then((response) =>{
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

