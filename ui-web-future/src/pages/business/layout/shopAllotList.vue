<template>
  <div>
    <head-tab :active="$t('shopAllotList.shopAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAllot:edit'">{{$t('shopAllotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAllot:view'">{{$t('shopAllotList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.businessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('shopAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.fromShopId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.fromShopId" filterable remote clearable :placeholder="$t('shopAllotList.inputWord')" :remote-method="remoteShop" :loading="remoteLoading" >
                  <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.toShopId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.toShopId" filterable remote clearable :placeholder="$t('shopAllotList.inputWord')" :remote-method="remoteShop" :loading="remoteLoading"  >
                  <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('shopAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('shopAllotList.inputKey')">
                  <el-option v-for="item in formProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off" :placeholder="multiEnterOrComma"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="businessId" :label="$t('shopAllotList.billCode')" sortable width="150"></el-table-column>
        <el-table-column prop="fromShop.name" :label="$t('shopAllotList.fromShop')"  sortable></el-table-column>
        <el-table-column prop="toShop.name" :label="$t('shopAllotList.toShop')" sortable></el-table-column>
        <el-table-column prop="outReturnCode" :label="$t('shopAllotList.outReturnCode')" sortable></el-table-column>
        <el-table-column prop="outSaleCode" :label="$t('shopAllotList.outSaleCode')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAllotList.remarks')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('shopAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopAllotList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('shopAllotList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('shopAllotList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopAllotList.operation')" width="140">
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
        pageHeight:600,
        page:{},
        formData:{
          page:0,
          size:25,
          fromShopId:"",
          toShopId:"",
          businessId:"",
          businessId:"",
          status:"",
          createdDate:'',
          createdDateBTW:'',
        },formLabel:{
          createdDateBTW:{label: this.$t('shopAllotList.createdDate')},
          fromShopId:{label:this.$t('shopAllotList.fromShop')},
          toShopId:{label:this.$t('shopAllotList.toShop')},
          businessId:{label:this.$t('shopAllotList.billCode')},
          businessIds:{label:this.$t('shopAllotList.billCode')},
          status:{label:this.$t('shopAllotList.status')},
        },
        pickerDateOption:util.pickerDateOption,
        shops:[],
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("shopAllotList",this.formData);
        axios.get('/api/crm/shopAllot',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'shopAllotForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'shopAllotForm', query: { id: id }})
        }else{
          this.$router.push({ name: 'shopAllotDetail', query: { id: id,action:action }})
        }
      },remoteShop(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/shop',{params:{name:query}}).then((response)=>{
            this.shops=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.shops = [];
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

