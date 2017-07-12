<template>
  <div>
    <head-tab active="shopAttributeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">添加</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('chainList.filter')" v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item label="门店" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('chainList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="门店" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('chainList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="门店" :label-width="formLabelWidth">
                 <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('chainList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column prop="name" label="门店名称" sortable></el-table-column>
        <el-table-column prop="createdByName" :label="$t('chainList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('chainList.createdDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('chainList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('chainList.locked')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="有效" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('chainList.operation')" width="160">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:chain:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('chainList.edit')}}</el-button>
            <el-button size="small" v-permit="'crm:chain:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('chainList.delete')}}</el-button>
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
          extra:{}
        },
        initPromise:{},
        searchText:"",
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading:false
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("shopAttributeList",submitData);
        axios.get('/api/ws/future/layout/shopAttribute',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'shopAttributeForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopAttributeForm', query: { shopId: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopAttribute/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/layout/shopAttribute/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.value,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

