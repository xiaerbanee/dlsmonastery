<template>
  <div>
    <head-tab :active="$t('dictEnumList.dictEnumList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:dictEnum:edit'">{{$t('dictEnumList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:dictEnum:view'">{{$t('dictEnumList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dictEnumList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('dictEnumList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.category.label" :label-width="formLabelWidth">
                <el-select v-model="formData.category" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="category in formProperty.category" :key="category" :label="category" :value="category"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.value.label" :label-width="formLabelWidth">
                <el-input v-model="formData.value" auto-complete="off" :placeholder="$t('dictEnumList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dictEnumList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dictEnumList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('dictEnumList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="category" :label="$t('dictEnumList.category')"></el-table-column>
        <el-table-column prop="value" :label="$t('dictEnumList.value')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dictEnumList.remarks')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('dictEnumList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('dictEnumList.createdDate')"></el-table-column>
        <el-table-column prop="locked" :label="$t('dictEnumList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('dictEnumList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
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
          pageNumber:0,
          pageSize:25,
          createdDate:'',
          createdDateBTW:'',
          category:'',
          value:''
        },formLabel:{
          createdDateBTW:{label: this.$t('dictEnumList.createdDate')},
          category:{label: this.$t('dictEnumList.category')},
          value:{label: this.$t('dictEnumList.value')}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.getQuery("dictEnumList");
        util.setQuery("dictEnumList",this.formData);
        axios.get('/api/sys/dictEnum',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'dictEnumForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'dictEnumForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/sys/dictEnum/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/sys/dictEnum/getListProperty').then((response) =>{
        this.formProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

