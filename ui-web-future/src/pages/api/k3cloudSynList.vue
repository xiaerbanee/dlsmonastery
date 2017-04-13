<template>
  <div>
    <head-tab active="金蝶核对"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bank:view'">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.extendType.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendType" auto-complete="off" placeholder="模糊搜索"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.extendFormatId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendFormatId" auto-complete="off" placeholder="模糊搜索"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确 定</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="extendType" label="名称" sortable width="150"></el-table-column>
        <el-table-column prop="formId" label="单据类型" sortable></el-table-column>
        <el-table-column prop="extendFormatId" label="单据编码"></el-table-column>
        <el-table-column prop="extendId" label="单据ID"></el-table-column>
        <el-table-column prop="billNo" label="外部编码"></el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.billNo==='' ? 'danger' : 'primary'">{{scope.row.billNo==''?'未同步':'已同步'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="locked" label="锁定" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="140">
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          page:0,
          size:25,
          extendType:'',
          extendFormatId:''
        },formLabel:{
          extendType:{label:"名称"},
          extendFormatId:{label:"单据编码"}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("k3cloudSynList",this.formData);
        axios.get('/api/api/K3cloudSyn',{params:this.formData}).then((response) => {
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
        if(action=="详细") {
          this.$router.push({ name: 'k3cloudSynForm', query: { id: id }})
        }else if(action=="删除") {
          axios.get('/api/api/K3cloudSyn/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }else if(action=="重新同步") {
          axios.get('/api/api/K3cloudSyn/syn',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -325;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

