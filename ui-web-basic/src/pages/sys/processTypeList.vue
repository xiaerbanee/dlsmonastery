<template>
  <div>
    <head-tab active="processTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:processType:edit'">{{$t('processTypeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'sys:processType:view'">{{$t('processTypeList.filter')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('processTypeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('processTypeList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('processTypeList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('processTypeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('processTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('processTypeList.id')" sortable ></el-table-column>
        <el-table-column prop="name" :label="$t('processTypeList.name')"sortable ></el-table-column>
        <el-table-column prop="locked" :label="$t('processTypeList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('processTypeList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('processTypeList.operation')" width="200">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'detail')" v-permit="'sys:processType:view'">{{$t('processTypeList.detail')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:processType:view'">{{$t('processTypeList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:processType:delete'">{{$t('processTypeList.delete')}}</el-button>
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
        detailFormData:{},
        formLabelWidth: '25%',
        pageLoading: false,
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
        axios.get('/api/general/sys/processType?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'processTypeForm'})
      },itemAction:function(id,action){
        if(action=="detail") {
          this.$router.push({ name: 'processTypeForm', query: { id: id }})
        }else if(action === 'edit'){
          this.$router.push({ name: 'processTypeForm', query: { id: id,editable:true }})
        }
        else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/general/sys/processType/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            })
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
       this.pageHeight = 0.74*window.innerHeight;
      this.initPromise = axios.get('/api/general/sys/processType/getQuery').then((response) => {
        this.formData = response.data;
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

