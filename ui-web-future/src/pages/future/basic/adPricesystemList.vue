<template>
  <div>
    <head-tab active="adPricesystemList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adPricesystem:edit'">{{$t('adPricesystemList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adPricesystem:view'">{{$t('adPricesystemList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('adPricesystemList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('adPricesystemList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('adPricesystemList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adPricesystemList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adPricesystemList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('adPricesystemList.name')" sortable width="150"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adPricesystemList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('adPricesystemList.createdBy')" sortable></el-table-column>
        <el-table-column prop="enabled" :label="$t('adPricesystemList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="locked" :label="$t('adPricesystemList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('adPricesystemList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:adPricesystem:edit'"><el-button size="small"  @click.native="itemAction(scope.row.id,'edit')">{{$t('adPricesystemList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:adPricesystem:edit'"><el-button size="small"  @click.native="itemAction(scope.row.id,'delete')">{{$t('adPricesystemList.delete')}}</el-button></div>
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
        searchText:"",
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false
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
        util.setQuery("adPricesystemList",submitData);
        axios.get('/api/ws/future/basic/adPricesystem',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'adPricesystemForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'adPricesystemForm', query: { id: id }})
        }else if(action=="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/adPricesystem/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/adPricesystem/getQuery').then((response) => {
        this.formData=response.data;
        util.copyValue(this.$route.query, this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

