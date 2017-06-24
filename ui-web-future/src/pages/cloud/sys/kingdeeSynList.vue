<template>
  <div>
    <head-tab active="kingdeeSynList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog title="过滤" v-model="formVisible" size="small" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
                <el-form-item label="创建时间" :label-width="formLabelWidth">
                  <date-picker placeholder="选择创建时间" v-model="formData.createdDate"></date-picker>
                </el-form-item>
                <el-form-item label="单据编码" :label-width="formLabelWidth">
                  <el-input v-model="formData.billNo"  placeholder="模糊匹配搜索"></el-input>
                </el-form-item>
                <el-form-item label="OA单据ID" :label-width="formLabelWidth">
                  <el-input v-model="formData.extendId" placeholder="模糊匹配搜索"></el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单据分类" :label-width="formLabelWidth">
                <el-select v-model="formData.extendType" filterable clearable placeholder="请选择">
                  <el-option v-for="name in formData.extra.extendTypeList" :key="name" :label="name" :value="name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否同步成功" :label-width="formLabelWidth">
                <bool-select v-model="formData.success" clearable="false"></bool-select>
              </el-form-item>
              <el-form-item label="是否锁定" :label-width="formLabelWidth">
                <bool-select v-model="formData.locked" clearable="false"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="extendId" label="OA单据ID" sortable ></el-table-column>
        <el-table-column prop="extendType" label="单据分类" ></el-table-column>
        <el-table-column prop="formId" label="单据类型"></el-table-column>
        <el-table-column prop="billNo" label="单据编码"></el-table-column>
        <el-table-column prop="createdDate" label="创建时间"></el-table-column>
        <el-table-column prop="success" label="同步成功">
          <template scope="scope">
            <el-tag :type="scope.row.success === true ? 'success' : 'danger'" >{{scope.row.success | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="locked" label="锁定">
          <template scope="scope">
            <el-tag :type="scope.row.locked === true ? 'danger' : 'success'" >{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="240">
          <template scope="scope">
            <el-button size="small" v-if="!scope.row.success" @click.native="itemAction(scope.row.id,'syn')">重新同步</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'detail')">详细</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import boolSelect from 'components/common/bool-select'
  export default{
    components:{
      boolSelect,
    },
    data() {
      return {
        page:{},
        formData:{
          extra:{}
        },
        searchText:"",
        initPromise:{},
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("kingdeeSynList",submitData);
        axios.get('/api/global/cloud/sys/kingdeeSyn?'+qs.stringify(submitData)).then((response) => {
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
      },itemAction:function(id,action) {
        if (action === "detail") {
          this.$router.push({name: 'kingdeeSynDetail', query: {id: id}})
        }else if (action === "delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/global/cloud/sys/kingdeeSyn/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(() => {
          });
        }else if (action === "syn") {
          axios.get('/api/global/cloud/sys/kingdeeSyn/syn', {params: {id: id}}).then((response) => {
            this.$message(response.data.message);
            this.pageRequest();
          });
        }
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise = axios.get('/api/global/cloud/sys/kingdeeSyn/getQuery').then((response) =>{
        that.formData = response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
        this.initPromise.then(()=>{
          this.pageRequest();
        });
    }
  };
</script>

