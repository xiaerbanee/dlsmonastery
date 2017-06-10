<template>
  <div>
    <head-tab active="clientList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >{{$t('clientList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('clientList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('clientList.filter')" v-model="formVisible"  size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-form-item :label="$t('clientList.name')" :label-width="formLabelWidth">
            <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('clientList.likeSearch')"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('clientList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('clientList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('clientList.name')" sortable width="120"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('clientList.mobilePhone')"></el-table-column>
        <el-table-column prop="locked" :label="$t('clientList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('clientList.enabled')" >
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('clientList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('clientList.operation')" width="140">
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
        searchText:"",
        page:{},
        formData:{
            extra:{}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
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
        util.setQuery("clientList",submitData);
        axios.get('/api/ws/future/basic/client?'+qs.stringify(submitData)).then((response) =>  {
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
      },itemAdd(){
        this.$router.push({ name: 'clientForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'clientForm', query: { id: id }})
        }else if(action==="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/client/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
        }).catch(()=>{});
        }
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/client/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

