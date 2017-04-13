<template>
  <div>
    <head-tab :active="$t('productTypeList.productTypeList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productType:edit'">{{$t('productTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productType:view'">{{$t('productTypeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productTypeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.code.label" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productTypeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('productTypeList.name')" sortable width="150"></el-table-column>
        <el-table-column prop="reportName" :label="$t('productTypeList.reportName')"></el-table-column>
        <el-table-column prop="code" :label="$t('productTypeList.code')"></el-table-column>
        <el-table-column prop="productNames" :label="$t('productTypeList.productNames')" width="300"></el-table-column>
        <el-table-column prop="price1" :label="$t('productTypeList.price1')"></el-table-column>
        <el-table-column prop="scoreType" :label="$t('productTypeList.scoreType')">
          <template scope="scope">
            <el-tag :type="scope.row.scoreType? 'primary' : 'danger'">{{scope.row.scoreType | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('productTypeList.remarks')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('productTypeList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productTypeList.createdDate')"></el-table-column>
        <el-table-column prop="locked" :label="$t('productTypeList.locked')">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('productTypeList.enabled')">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('productTypeList.operation')" width="140">
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
          code:'',
          name:'',
        },formLabel:{
          code:{label:this.$t('productTypeList.code')},
          name:{label:this.$t('productTypeList.name')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
    };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("productTypeList",this.formData);
        axios.get('/api/crm/productType',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'productTypeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'productTypeForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/crm/productType/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

