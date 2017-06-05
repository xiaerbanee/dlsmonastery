<template>
  <div>
    <head-tab active="permissionList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:permission:edit'">{{$t('permissionList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:permission:view'">{{$t('permissionList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('permissionList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('permissionList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.permission.label" :label-width="formLabelWidth">
                <el-input v-model="formData.permission" auto-complete="off" :placeholder="$t('permissionList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.menuName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.menuName" auto-complete="off" :placeholder="$t('permissionList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('permissionList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('permissionList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="name" :label="$t('permissionList.name')" sortable></el-table-column>
        <el-table-column prop="permission" :label="$t('permissionList.permission')" sortable></el-table-column>
        <el-table-column prop="menuName" :label="$t('permissionList.menuName')"></el-table-column>
        <el-table-column prop="locked" :label="$t('permissionList.locked')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('permissionList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('permissionList.operation')" width="140">
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:'',
          permission:'',
          menuName:''
        },formLabel:{
          name:{label:this.$t('permissionList.name')},
          permission:{label:this.$t('permissionList.permission')},
          menuName:{label: this.$t('permissionList.menuName')}
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("permissionList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/permission?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'permissionForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'permissionForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/permission/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

