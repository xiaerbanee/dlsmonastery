<template>
  <div>
    <head-tab active="bankList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bank:view'">{{$t('bankList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus" v-permit="'crm:bank:edit'">{{$t('bankList.syn')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('bankList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('bankList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('bankList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('bankList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('bankList.name')" sortable width="150"></el-table-column>
        <el-table-column prop="code" :label="$t('bankList.code')"  sortable></el-table-column>
        <el-table-column prop="accountNameStr" :label="$t('bankList.account')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('bankList.remarks')"></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('bankList.createdBy')" sortable></el-table-column>
        <el-table-column prop="enabled" :label="$t('bankList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('bankList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:bank:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('bankList.edit')}}</el-button></div>
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
          sort:"id,DESC",
          name:''
        },formLabel:{
          name:{label:this.$t('bankList.name')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("bankList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/ws/future/basic/bank',{params:this.submitData}).then((response) => {
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
      },synData(){
        axios.get('/api/ws/future/basic/bank/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'bankForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/bank/getQuery').then((response) => {
          this.formData = response.data;
          util.copyValue(this.$route.query, this.formData);
          this.pageRequest();
      })
    }
  };
</script>

