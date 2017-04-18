<template>
  <div>
    <head-tab :active="$t('companyConfigList.companyConfigList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('companyConfigList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('companyConfigList.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
               <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                 <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('companyConfigList.likeSearch')"></el-input>
               </el-form-item>
              <el-form-item :label="formLabel.code.label" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('companyConfigList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('companyConfigList.sure')}}</el-button>
        </div>
      </el-dialog>
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
              <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>
             <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
          code:''
        },formLabel:{
          name:{label:this.$t('companyConfigList.name')},
          code:{label:this.$t('companyConfigList.code')}
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
    };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("companyConfigList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/companyConfig',{params:this.submitData}).then((response) => {
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
        if(action=="修改") {
          this.$router.push({ name: 'companyConfigForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

