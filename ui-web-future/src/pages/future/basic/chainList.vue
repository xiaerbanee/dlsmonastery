<template>
  <div>
    <head-tab active="chainList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:chain:edit'">{{$t('chainList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:chain:view'">{{$t('chainList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('chainList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('chainList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('chainList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('chainList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('chainList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('chainList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('chainList.name')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('chainList.remarks')"></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('chainList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('chainList.createdDate')" sortable></el-table-column>
        <el-table-column prop="locked" :label="$t('chainList.locked')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('chainList.operation')" width="160">
          <template scope="scope">
            <div class="action" v-permit="'crm:chain:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('chainList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:chain:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('chainList.delete')}}</el-button></div>
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
        searchText:"",
        pageLoading: false,
        page:{},
        formData:{
          extra:{}
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
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
        util.setQuery("chainList",submitData);
        axios.get('/api/ws/future/basic/chain',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'chainForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'chainForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/chain/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/chain/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });

    }
  };
</script>

