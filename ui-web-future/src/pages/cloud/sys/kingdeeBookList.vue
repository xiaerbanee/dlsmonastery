<template>
  <div>
    <head-tab active="kingdeeBookList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" title="过滤" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-form-item label="公司名称" >
              <el-input v-model="formData.companyName" auto-complete="off" placeholder="请输入"></el-input>
            </el-form-item>
          </el-row>
          <el-row :gutter="4">
            <el-form-item label="账套" >
              <el-select v-model="formData.name" filterable clearable placeholder="请选择">
                <el-option v-for="name in formData.extra.nameList" :key="name" :label="name" :value="name"></el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-row :gutter="4">
            <el-form-item label="账套类型" >
              <el-select v-model="formData.type" filterable clearable placeholder="请选择">
                <el-option v-for="type in formData.extra.typeList" :key="type" :label="type" :value="type"></el-option>
              </el-select>
            </el-form-item>
            <el-col :span="24">
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" label="ID" sortable width="100"></el-table-column>
        <el-table-column prop="name" label="账套"></el-table-column>
        <el-table-column prop="type" label="账套类型"></el-table-column>
        <el-table-column prop="companyName" label="公司名称"></el-table-column>
        <el-table-column prop="lastModifiedBy" label="修改人"></el-table-column>
        <el-table-column prop="lastModifiedDate" label="修改时间"></el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>
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
          extra:{}
        },
        searchText:"",
        initPromise:{},
        formLabelWidth: '28%',
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("kingdeeBookList", submitData);
        axios.get('/api/global/cloud/sys/kingdeeBook?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'kingdeeBookForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'kingdeeBookForm', query: { id: id }})
        } else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/global/cloud/sys/kingdeeBook/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(() => {
          });
        }
      }
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise = axios.get('/api/global/cloud/sys/kingdeeBook/getQuery').then((response) =>{
        that.formData = response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    }
  };
</script>

