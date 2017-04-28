<template>
  <div>
    <head-tab active="officeRuleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  >{{$t('officeRuleList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('officeRuleList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('officeRuleList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('officeRuleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.parentName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.parentName" auto-complete="off" :placeholder="$t('officeRuleList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('officeRuleList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('officeRuleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('officeRuleList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('officeRuleList.name')" ></el-table-column>
        <el-table-column prop="code" label="Code" ></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template scope="scope">
            {{$t('officeRuleList.'+ scope.row.type)}}
          </template>
        </el-table-column>
        <el-table-column prop="parentName" label="上级" ></el-table-column>
        <el-table-column prop="hasPoint" label="是否有点位" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.hasPoint ? 'primary' : 'danger'">{{scope.row.hasPoint | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('officeRuleList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('officeRuleList.operation')" width="140">
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          parentName:'',
          name:"",
        },formLabel:{
          parentName:{label:"上级名称"},
          name:{label:"名称"},
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.getQuery("officeRuleList");
        util.setQuery("officeRuleList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/officeRule',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'officeRuleForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'officeRuleForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/sys/officeRule/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      this.pageHeight = window.outerHeight -320;
      util.copyValue(that.$route.query,that.formData);
      that.pageRequest();
    }
  };
</script>

