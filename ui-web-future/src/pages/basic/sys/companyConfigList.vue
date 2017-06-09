<template>
  <div>
    <head-tab active="companyConfigList"></head-tab>
    <div>
      <el-row>
         <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:companyConfig:edit'">{{$t('companyConfigList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('companyConfigList.filter')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('companyConfigList.filter')"  v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
               <el-form-item :label="$t('companyConfigList.name')" :label-width="formLabelWidth">
                 <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('companyConfigList.likeSearch')"></el-input>
               </el-form-item>
              <el-form-item :label="$t('companyConfigList.code')" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('companyConfigList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('companyConfigList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('companyConfigList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('companyConfigList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('companyConfigList.name')" sortable></el-table-column>
        <el-table-column prop="code" :label="$t('companyConfigList.code')" sortable></el-table-column>
        <el-table-column prop="value" :label="$t('companyConfigList.value')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('companyConfigList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('companyConfigList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('companyConfigList.operation')" width="140">
          <template scope="scope">
              <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:companyConfig:edit'">修改</el-button>
             <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:companyConfig:delete'">删除</el-button>
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
        formLabelWidth: '120px',
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
        util.setQuery("companyConfigList",submitData);
        axios.get('/api/basic/sys/companyConfig?'+qs.stringify(submitData)).then((response) => {
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
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'companyConfigForm', query: { id: id }})
        }else if(action=="delete"){
           util.confirmBeforeDelRecord(this).then(()=> {
               axios.get('/api/basic/sys/companyConfig/delete',{params: {id: id}}).then((response) => {
                   this.$message(response.data.message);
                   this.pageRequest();
               });
           });
            }
      },itemAdd(){
         this.$router.push({ name: 'companyConfigForm'})
       }
    },created () {
        var that=this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/sys/companyConfig/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

