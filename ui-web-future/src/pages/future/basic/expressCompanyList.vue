<template>
  <div>
    <head-tab active="expressCompanyList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:expressCompany:edit'">{{$t('expressCompanyList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:expressCompany:view'">{{$t('expressCompanyList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('expressCompanyList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('expressCompanyList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('expressCompanyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('expressCompanyList.expressType')" :label-width="formLabelWidth">
                <el-select v-model="formData.expressType" filterable clearable :placeholder="$t('expressCompanyList.inputKey')">
                  <el-option v-for="expressType in formData.extra.expressTypeList" :key="expressType" :label="expressType" :value="expressType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('expressCompanyList.reachPlace')" :label-width="formLabelWidth">
                <el-input v-model="formData.reachPlace" auto-complete="off" :placeholder="$t('expressCompanyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('expressCompanyList.mobilePhone')" :label-width="formLabelWidth">
                <el-input v-model="formData.mobilePhone" auto-complete="off" :placeholder="$t('expressCompanyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('expressCompanyList.contator')" :label-width="formLabelWidth">
                <el-input v-model="formData.contator" auto-complete="off" :placeholder="$t('expressCompanyList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressCompanyList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressCompanyList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('expressCompanyList.unicode')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('expressCompanyList.name')" sortable></el-table-column>
        <el-table-column prop="expressType" :label="$t('expressCompanyList.expressType')" sortable></el-table-column>
        <el-table-column prop="reachPlace" :label="$t('expressCompanyList.reachPlace')"sortable></el-table-column>
        <el-table-column prop="address" :label="$t('expressCompanyList.address')" sortable></el-table-column>
        <el-table-column prop="phone" :label="$t('expressCompanyList.phone')"sortable></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('expressCompanyList.mobilePhone')" sortable></el-table-column>
        <el-table-column prop="contator" :label="$t('expressCompanyList.contact')" sortable></el-table-column>
        <el-table-column prop="shouldGetRule" :label="$t('expressCompanyList.shouldGetRule')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressCompanyList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressCompanyList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:expressCompany:edit'"><el-button size="small" @click.native="itemEdit(scope.row.id)">{{$t('expressCompanyList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:expressCompany:delete'"><el-button size="small"  @click.native="itemDelete(scope.row.id)">{{$t('expressCompanyList.delete')}}</el-button></div>
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
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false,
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
        util.setQuery("expressCompanyList",submitData);
        axios.get('/api/ws/future/basic/expressCompany',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'expressCompanyForm'})
      },itemEdit:function(id){
          this.$router.push({ name: 'expressCompanyForm', query: { id: id }});
      },itemDelete:function(id){
        util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/basic/expressCompany/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/expressCompany/getQuery').then((response) =>{
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

