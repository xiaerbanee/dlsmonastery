<template>
  <div>
    <head-tab active="demoPhoneTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhoneType:edit'">{{$t('demoPhoneTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:demoPhoneType:view'">{{$t('demoPhoneTypeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('demoPhoneTypeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('demoPhoneTypeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('demoPhoneTypeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('demoPhoneTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('demoPhoneTypeList.name')"></el-table-column>
        <el-table-column prop="limitQty" :label="$t('demoPhoneTypeList.limitQty')"></el-table-column>
        <el-table-column prop="productTypeNames" :label="$t('demoPhoneTypeList.productTypeNames')"></el-table-column>
        <el-table-column prop="applyEndDate" :label="$t('demoPhoneTypeList.applyEndDate')"></el-table-column>
        <el-table-column prop="renewEndDate" :label="$t('demoPhoneTypeList.renewEndDate')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('demoPhoneTypeList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('demoPhoneTypeList.createdDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('demoPhoneTypeList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('demoPhoneTypeList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'danger' : 'primary'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('demoPhoneTypeList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'danger' : 'primary'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('demoPhoneTypeList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:demoPhoneType:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('demoPhoneTypeList.edit')}}</el-button>
            <el-button size="small" v-permit="'crm:demoPhoneType:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('demoPhoneTypeList.delete')}}</el-button>
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
          name:"",
        },formLabel:{
          name:{label:this.$t('demoPhoneTypeList.name')},
        },
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("demoPhoneTypeList",this.submitData);
        axios.get('/api/ws/future/crm/demoPhoneType',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'demoPhoneTypeForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'demoPhoneTypeForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/demoPhoneType/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/demoPhoneType/getQuery').then((response) => {
        that.formData = response.data;
        util.copyValue(that.$route.query, that.formData);
        that.pageRequest();
      })
    }
  };
</script>


