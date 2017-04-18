<template>
  <div>
    <head-tab :active="$t('expressList.expressList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:express:edit'">{{$t('expressList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:express:view'">{{$t('expressList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('expressList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.code.label" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('expressList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.extendBusinessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendBusinessId" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.storeId" filterable clearable :placeholder="$t('expressList.inputKey')">
                  <el-option v-for="item in formProperty.stores" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.expressCompanyId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.expressCompanyId" filterable clearable :placeholder="$t('expressList.inputKey')">
                  <el-option v-for="item in formProperty.expressCompanys" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.extendType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.extendType" filterable clearable :placeholder="$t('expressList.inputKey')">
                  <el-option v-for="item in formProperty.extendTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="code" :label="$t('expressList.code')" sortable width="150"></el-table-column>
        <el-table-column prop="expressOrder.expressCompany.name" :label="$t('expressList.expressCompanyName')" sortable></el-table-column>
        <el-table-column prop="expressOrder.extendType" :label="$t('expressList.extendType')"></el-table-column>
        <el-table-column prop="expressOrder.extendBusinessId" :label="$t('expressList.extendBusinessId')"></el-table-column>
        <el-table-column prop="expressOrder.fromDepot.name" :label="$t('expressList.fromDepotName')"></el-table-column>
        <el-table-column prop="expressOrder.toDepot.name" :label="$t('expressList.toDepotName')"></el-table-column>
        <el-table-column prop="expressOrder.contator" :label="$t('expressList.contact')"></el-table-column>
        <el-table-column prop="expressOrder.mobilePhone" :label="$t('expressList.mobilePhone')"></el-table-column>
        <el-table-column prop="expressOrder.address" :label="$t('expressList.address')"></el-table-column>
        <el-table-column prop="weight" :label="$t('expressList.weight')"></el-table-column>
        <el-table-column prop="qty" :label="$t('expressList.qty')"></el-table-column>
        <el-table-column prop="shouldPay" :label="$t('expressList.shouldPay')"></el-table-column>
        <el-table-column prop="realPay" :label="$t('expressList.realPay')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('expressList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressList.createdDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressList.operation')" width="140">
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
          shopName:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          extendBusinessId:'',
          storeId:'',
          expressCompanyId:'',
          extendType:''
        },formLabel:{
          code:{label:this.$t('expressList.code')},
          shopName:{label:this.$t('expressList.toDepotName')},
          createdDateBTW:{label: this.$t('expressOrderList.createdDate')},
          createdBy:{label: this.$t('expressOrderList.createdBy')},
          extendBusinessId:{label:this.$t('expressList.extendBusinessId')},
          storeId:{label:this.$t('expressList.fromDepotName'),value:''},
          expressCompanyId:{label:this.$t('expressList.code'),value:''},
          extendType:{label:this.$t('expressList.extendType')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        isPageChange:false,
        pageLoading: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        this.formLabel.storeId.value=util.getLabel(this.formProperty.stores,this.formData.storeId);
        this.formLabel.expressCompanyId.value=util.getLabel(this.formProperty.expressCompanys,this.formData.expressCompanyId);

        util.setQuery("expressList",this.formData);
        axios.get('/api/crm/express',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        if(this.isPageChange){
          this.formData.page = pageNumber;
          this.formData.size = pageSize;
          this.pageRequest();
        }
        this.isPageChange = true;
        },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'expressForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'expressForm', query: { id: id }})
        }
      },getQuery(){
        axios.get('/api/crm/express/getQuery').then((response) =>{
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

