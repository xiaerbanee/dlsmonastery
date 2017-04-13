<template>
  <div>
    <head-tab :active="$t('accountChangeList.accountChangeList')"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{$t('accountChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('accountChangeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('accountChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
            <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('accountChangeList.selectDateRange')"  :picker-options="pickerDateOption"></el-date-picker>
          </el-form-item>
          <el-form-item :label="formLabel.createdByName.label" :label-width="formLabelWidth">
            <el-input v-model="formData.createdByName" auto-complete="off" :placeholder="$t('accountChangeList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
            <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('accountChangeList.inputKey')">
              <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
            <el-select v-model="formData.type" clearable filterable :placeholder="$t('accountChangeList.selectGroup')">
              <el-option v-for="type in formProperty.types" :key="type" :label="type" :value="type"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountChangeList.id')" sortable></el-table-column>
        <el-table-column  prop="account.extendMap.areaName" :label="$t('accountChangeList.areaName')" sortable></el-table-column>
        <el-table-column  prop="created.loginName" :label="$t('accountChangeList.applyAccount')" sortable></el-table-column>
        <el-table-column  prop="createdDate" :label="$t('accountChangeList.applyDate')" sortable></el-table-column>
        <el-table-column  prop="type" :label="$t('accountChangeList.type')" sortable></el-table-column>
        <el-table-column  prop="oldValue" :label="$t('accountChangeList.oldValue')" sortable></el-table-column>
        <el-table-column  prop="newValue" :label="$t('accountChangeList.newValue')" sortable></el-table-column>
        <el-table-column  prop="processStatus" :label="$t('accountChangeList.processStatus')" sortable ></el-table-column>
        <el-table-column :label="$t('accountChangeList.operation')" width="140">
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
          createdDate:"",
          officeId:'',
          createdDateBTW:'',
          createdByName:'',
          type:''
        },formLabel:{
          createdDateBTW:{label:this.$t('accountChangeList.createdDate')},
          createdByName:{label:this.$t('accountChangeList.createdBy')},
          type:{label:this.$t('accountChangeList.type')},
          officeId:{label:this.$t('accountChangeList.areaName'),value:''}
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
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        this.formLabel.officeId.value=util.getLabel(this.formProperty.areas, this.formData.officeId);
        util.setQuery("accountChangeList",this.formData);
        axios.get('/api/hr/accountChange',{params:this.formData}).then((response) => {
          console.log(response.data);
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
        this.$router.push({ name: 'accountChangeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'accountChangeForm', query: { id: id }})
        } else if(action="删除") {
          axios.get('/api/hr/accountChange/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }},created () {
      this.pageHeight = window.outerHeight -120;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/hr/accountChange/getListProperty').then((response) =>{
        this.formProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

