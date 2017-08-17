<template>
  <div>
    <head-tab active="kingdeeSynForNoPushList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="flush()" icon="update">刷新</el-button>
      </el-row>
      <el-table :data="list" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....."  stripe border>
        <el-table-column fixed prop="extendId" label="OA单据ID" sortable ></el-table-column>
        <el-table-column prop="extendType" label="单据分类" ></el-table-column>
        <el-table-column prop="formId" label="单据类型"></el-table-column>
        <el-table-column prop="billNo" label="单据编码"></el-table-column>
        <el-table-column prop="nextBillNo" label="下推单据"></el-table-column>
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
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'detail')">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data() {
      return {
        list:[],
        pageLoading: false
      };
    },
    methods: {
      initPage() {
        this.pageLoading = true;
        axios.get('/api/global/cloud/sys/kingdeeSyn/noPush').then((response) => {
          this.list = response.data;
          this.pageLoading = false;
        })
      },itemAction:function(id,action) {
        if (action === "detail") {
          this.$router.push({name: 'kingdeeSynDetail', query: {id: id}})
        }
      },flush:function(){
        axios.get('/api/global/cloud/sys/kingdeeSyn/flush').then((response) => {
          if(response.data.success){
            this.$message(response.data.message);
            this.initPage();
          }else{
            this.$alert(response.data.message);
          }
        });
      }
    },created () {
      this.pageHeight = 0.75*window.innerHeight;
      this.initPage();
    }
  };
</script>

