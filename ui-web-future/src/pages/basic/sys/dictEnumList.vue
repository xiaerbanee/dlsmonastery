<template>
  <div>
    <head-tab active="dictEnumList"></head-tab>
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
              <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.category.label" :label-width="formLabelWidth">
                <el-select v-model="formData.category" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="category in formData.categoryList" :key="category" :label="category" :value="category"></el-option>
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
        <el-table-column prop="createdByName" :label="$t('dictEnumList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('dictEnumList.createdDate')"></el-table-column>
        <el-table-column prop="locked" :label="$t('dictEnumList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('dictEnumList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('dictEnumList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('dictEnumList.delete')}}</el-button>
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
          createdDate:'',
        },
        submitData:{
          page:0,
          size:25,
          createdDate:'',
          category:'',
          value:''
        },
        formLabel:{
          createdDate:{label: this.$t('dictEnumList.createdDate')},
          category:{label: this.$t('dictEnumList.category')},
          value:{label: this.$t('dictEnumList.value')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("dictEnumList",this.submitData);
        axios.get('/api/basic/sys/dictEnum?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'dictEnumForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'dictEnumForm', query: { id: id }})
        } else if(action=="delete") {
          axios.get('/api/basic/sys/dictEnum/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/sys/dictEnum/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

