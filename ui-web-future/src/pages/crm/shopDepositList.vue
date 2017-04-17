<template>
  <div>
    <head-tab :active="$t('shopDepositList.shopDepositList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopDeposit:edit'">{{$t('shopDepositList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopDeposit:view'">{{$t('shopDepositList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopDepositList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('shopDepositList.inputKey')">
                  <el-option v-for="item in formProperty.types" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('shopDepositList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopDepositList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopDepositList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('shopDepositList.billCode')" sortable width="100"></el-table-column>
        <el-table-column prop="shop.name":label="$t('shopDepositList.shopName')" sortable></el-table-column>
        <el-table-column prop="shop.area.name" :label="$t('shopDepositList.areaName')" ></el-table-column>
        <el-table-column prop="shop.office.name" :label="$t('shopDepositList.officeName')"></el-table-column>
        <el-table-column prop="type" :label="$t('shopDepositList.type')"></el-table-column>
        <el-table-column prop="amount" :label="$t('shopDepositList.amount')"></el-table-column>
        <el-table-column prop="leftAmount" :label="$t('shopDepositList.leftAmount')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('shopDepositList.outCode')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('shopDepositList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopDepositList.createdDate')"></el-table-column>
        <el-table-column prop="lastModified.fullName" :label="$t('shopDepositList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopDepositList.lastModifiedDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopDepositList.remarks')"></el-table-column>
        <el-table-column prop="created" :label="$t('shopDepositList.locked')"width="100">
          <template scope="scope">
            <el-tag :type="scope.row.created ? 'primary' : 'danger'">{{scope.row.created | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('shopDepositList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopDepositList.operation')" width="140">
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
        pageLoading: false,
        page:{},
        formData:{
          page:0,
          size:25,
          shopName:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          type:'',
          remarks:''
        },formLabel:{
          shopName:{label:this.$t('shopDepositList.shopName')},
          createdBy:{label:this.$t('shopDepositList.createdBy')},
          createdDateBTW:{label:this.$t('shopDepositList.createdDate')},
          type:{label:this.$t('shopDepositList.type')},
          remarks:{label:this.$t('shopDepositList.remarks')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        util.setQuery("shopDepositList",this.formData);
        axios.get('/api/crm/shopDeposit',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'shopDepositForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'shopDepositForm', query: { id: id }})
        }else if(action=="删除"){
            axios.get("/api/crm/shopDeposit/delete",{params:{id:id}}).then((response)=>{
                this.$message(response.data.message);
                this.pageRequest();
            })
        }
      },getQuery(){
        axios.get('/api/crm/shopDeposit/getQuery').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getQuery();
    }
  };
</script>

