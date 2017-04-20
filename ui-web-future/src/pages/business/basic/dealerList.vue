<template>
  <div>
    <head-tab active="dealerList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:dealer:edit'">{{$t('dealerList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:dealer:view'">{{$t('dealerList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dealerList.filter')" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData">
          <el-form-item :label="formLabel.name" :label-width="formLabelWidth">
            <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('dealerList.likeSearch')"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dealerList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dealerList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('dealerList.name')" sortable width="120"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('dealerList.mobilePhone')"></el-table-column>
        <el-table-column prop="locked" :label="$t('dealerList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('dealerList.enabled')" >
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('dealerList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('dealerList.operation')" width="140">
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
          page:0,
          size:25,
          name:'',
          type:'',
          hasIme:'',
          allowBill:'',
          productType:'',
          allowOrder:'',
          outGroupName:'',
          netType:'',
        },formLabel:{
          name:this.$t('dealerList.name'),
        },
        offices:[],
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("productList",this.formData);
        axios.get('/api/future/business/basic/dealer',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'dealerForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'dealerForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

