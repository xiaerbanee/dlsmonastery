<template>
  <div>
    <head-tab active="depotChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:depotChange:edit'">{{$t('depotChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:depotChange:view'">{{$t('depotChangeList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('depotChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.depotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('depotChangeList.likeSearch')"></el-input>
              </el-form-item>
               <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                 <el-select v-model="formData.type" clearable filterable :placeholder="$t('depotChangeList.selectGroup')">
                   <el-option v-for="type in formProperty.types" :key="type" :label="type" :value="type"></el-option>
                 </el-select>
               </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('depotChangeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData" v-permit="'crm:depotChange:view'">{{$t('depotChangeList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('depotChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('depotChangeList.unicode')" sortable width="100"></el-table-column>
        <el-table-column prop="type" :label="$t('depotChangeList.type')" width="120"></el-table-column>
        <el-table-column prop="depot.name" :label="$t('depotChangeList.depotName')"></el-table-column>
        <el-table-column prop="expiryDate" :label="$t('depotChangeList.expiryDate')"sortable width="120"></el-table-column>
        <el-table-column prop="oldValue" :label="$t('depotChangeList.oldValue')"></el-table-column>
        <el-table-column prop="newValue" :label="$t('depotChangeList.newValue')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('depotChangeList.processStatus')"></el-table-column>
        <el-table-column fixed="right" :label="$t('depotChangeList.operation')" width="140">
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
          depotName:'',
          type:'',
          createdDateBTW:'',
          createdDate:'',
        },formLabel:{
          depotName:{label: this.$t('depotChangeList.depotName'),value:""},
          type:{label: this.$t('depotChangeList.type'),value:""},
          createdDateBTW:{label: this.$t('depotChangeList.createdDate')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("depotChangeList",this.formData);
        axios.get('/api/crm/depotChange',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'depotChangeForm'})
      },exportData(){
				window.location.href= "/api/crm/depotChange/export?"+qs.stringify(this.formData);
      },itemAction:function(id,action){
        if(action=="详细") {
          this.$router.push({ name: 'depotChangeDetail', query: { id: id,action:"detail" }})
      } else if(action=="审核"){
          this.$router.push({ name: 'depotChangeDetail', query: { id: id,action:"audit" }})
      }else if(action=="删除") {
          axios.get('/api/crm/depotChange/delete',{params:{id:id}}).then((response) =>{
           this.$message(response.data.message);
           this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/depotChange/getQuery').then((response) =>{
        this.formProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

