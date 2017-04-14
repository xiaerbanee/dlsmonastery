<template>
  <div>
    <head-tab :active="$t('employeePhoneList.employeePhoneList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:employeePhoneDeposit:edit'">{{$t('employeePhoneList.employeePhoneDepositList')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:employeePhoneDeposit:view'">{{$t('employeePhoneList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('employeePhoneList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('employeePhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.depotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('employeePhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.areaName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.areaName" auto-complete="off" :placeholder="$t('employeePhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('employeePhoneList.selectStatus')">
                  <el-option  v-for="item in searchProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('employeePhoneList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('employeePhoneList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="areaName" :label="$t('employeePhoneList.areaName')"  width="150"></el-table-column>
        <el-table-column prop="depot.name" :label="$t('employeePhoneList.depotName')"></el-table-column>
        <el-table-column prop="employee.name" :label="$t('employeePhoneList.employeeName')" width="100"></el-table-column>
        <el-table-column prop="depositAmount" :label="$t('employeePhoneList.depositAmount')"  width="100"></el-table-column>
        <el-table-column prop="status" :label="$t('employeePhoneList.status')"  width="100"></el-table-column>
        <el-table-column prop="uploadTime" :label="$t('employeePhoneList.uploadTime')"></el-table-column>
        <el-table-column prop="product.name" :label="$t('employeePhoneList.productName')" width="120"></el-table-column>
        <el-table-column prop="jobPrice" :label="$t('employeePhoneList.jobPrice')" ></el-table-column>
        <el-table-column prop="imeStr" :label="$t('employeePhoneList.imeStr')" width="120"></el-table-column>
        <el-table-column prop="retailPrice" :label="$t('employeePhoneList.retailPrice')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('employeePhoneList.createdDate')" width="120"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('employeePhoneList.createdBy')"></el-table-column>
        <el-table-column fixed="right" :label="$t('employeePhoneList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>             <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
          createdDate:'',
          createdDateBTW:'',
          name:'',
          status:'',
          areaName:'',
          depotName:""
        },formLabel:{
          name:{label:this.$t('employeePhoneList.name')},
          status:{label:this.$t('employeePhoneList.status')},
          areaName:{label:this.$t('employeePhoneList.areaName')},
          depotName:{label:this.$t('employeePhoneList.depotName')},
          createdDateBTW:{label:this.$t('employeePhoneList.createdDate')}
        },
        searchProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("employeePhoneList",this.formData);
        axios.get('/api/basic/hr/employeePhone',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'employeePhoneDepositList'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'employeePhoneForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/basic/hr/employeePhone/getListProperty').then((response) =>{
        this.searchProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

