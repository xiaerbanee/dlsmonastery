<template>
  <div>
    <head-tab active="expressCompanyList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:expressCompany:edit'">{{$t('expressCompanyList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:expressCompany:view'">{{$t('expressCompanyList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('expressCompanyList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('expressCompanyList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressCompanyList.sure')}}</el-button>
        </div>
      </el-dialog>
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
          name:''
        },formLabel:{
          name:{label:this.$t('expressCompanyList.name')},
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("expressCompanyList",this.formData);
        axios.get('/api/ws/future/basic/expressCompany',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'expressCompanyForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'expressCompanyForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/ws/future/basic/expressCompany/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/ws/future/basic/expressCompany/getQuery').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

