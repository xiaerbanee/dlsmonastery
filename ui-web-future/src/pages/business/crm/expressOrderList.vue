<template>
  <div>
    <head-tab :active="$t('expressOrderList.expressOrderList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('expressOrderList.filterOrExport') }}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('expressOrderList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.expressCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.fromDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromDepotName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toDepotName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.expressCompanyName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCompanyName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.extendBusinessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendBusinessId" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.printDateBTW.label" :label-width="formLabelWidth">
              <el-date-picker v-model="formData.printDate" type="daterange" align="right"  :placeholder="$t('expressOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.expressCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.extendType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.extendType" clearable filterable :placeholder="$t('expressOrderList.selectExtendType')">
                  <el-option v-for="item in formProperty.extendTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.isOutPrint.label" :label-width="formLabelWidth">
                <el-select v-model="formData.isOutPrint"   >
                  <el-option :label="$t('expressOrderList.true')" value="true">{{$t('expressOrderList.true')}}</el-option>
                  <el-option :label="$t('expressOrderList.false')" value="false">{{$t('expressOrderList.false')}}</el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.expressPrint.label" :label-width="formLabelWidth">
                <el-select v-model="formData.expressPrint"   >
                  <el-option :label="$t('expressOrderList.true')" value="true">{{$t('expressOrderList.true')}}</el-option>
                  <el-option :label="$t('expressOrderList.false')" value="false">{{$t('expressOrderList.false')}}</el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData">{{$t('expressOrderList.export')}}</el-button>
          <el-button @click="EMSexportData">{{$t('expressOrderList.EMSExportData')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('expressOrderList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="expand">
          <template scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item :label="$t('expressOrderList.address')">
                <span>{{ props.row.address }}</span>
              </el-form-item>
              <el-form-item :label="$t('expressOrderList.expressPrintDate')">
                <span>{{ props.row.expressPrintDate }}</span>
              </el-form-item>
              <el-form-item :label="$t('expressOrderList.outPrintDate')">
                <span>{{ props.row.outPrintDate }}</span>
              </el-form-item>
              <el-form-item :label="$t('expressOrderList.shouldGet')">
                <span>{{ props.row.shouldGet }}</span>
              </el-form-item>
              <el-form-item :label="$t('expressOrderList.shouldPay')">
                <span>{{ props.row.shouldPay }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="extendType" :label="$t('expressOrderList.extendType')"  width="150"></el-table-column>
        <el-table-column prop="extendBusinessId" :label="$t('expressOrderList.extendBusinessId')"></el-table-column>
        <el-table-column prop="expressCompany.name" :label="$t('expressOrderList.expressCompanyName')"></el-table-column>
        <el-table-column prop="fromDepot.name" :label="$t('expressOrderList.fromDepotName')"></el-table-column>
        <el-table-column prop="toDepot.name" :label="$t('expressOrderList.toDepotName')"></el-table-column>
        <el-table-column prop="realPay" :label="$t('expressOrderList.realPay')"></el-table-column>
        <el-table-column prop="contator" :label="$t('expressOrderList.contact')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('expressOrderList.mobilePhone')"></el-table-column>
        <el-table-column prop="expressPrintQty" :label="$t('expressOrderList.expressPrintQty')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressOrderList.createdDate')"></el-table-column>
        <el-table-column prop="printDate" :label="$t('expressOrderList.printDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressOrderList.remarks')"></el-table-column>
        <el-table-column  :label="$t('expressOrderList.operation')" width="140">
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
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
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
          expressCode:'',
          fromDepotName:'',
          toDepotName:'',
          expressCompanyName:'',
          printDate:"",
          printDateBTW:'',
          extendBusinessId:"",
          extendType:this.$t('expressOrderList.extendForMobilePhone'),
          expressPrint:'',
          isOutPrint:''
        },formLabel:{
          expressCode:{label:this.$t('expressOrderList.expressCode')},
          fromDepotName:{label:this.$t('expressOrderList.fromDepotName')},
          toDepotName:{label:this.$t('expressOrderList.toDepotName')},
          expressCompanyName:{label:this.$t('expressOrderList.expressCompanyName')},
          printDateBTW:{label:this.$t('expressOrderList.printDate')},
          extendBusinessId:{label:this.$t('expressOrderList.startExtendBusinessId')},
          expressCode:{label:this.$t('expressOrderList.expressCode')},
          expressPrint:{label:this.$t('expressOrderList.expressPrint')},
          isOutPrint:{label:this.$t('expressOrderList.isOutPrint')},
          extendType:{label:this.$t('expressOrderList.extendType')}
        },
        formProperty:{},
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        loading:false,
        isPageChange:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("expressOrderList",this.formData);
        this.formData.printDateBTW=util.formatDateRange(this.formData.printDate);
        axios.get('/api/crm/expressOrder',{params:this.formData}).then((response) => {
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

      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'expressOrderForm', query: { id: id }})
        }else if(action=='重置'){
          axios.get('/api/crm/expressOrder/resetPrintStatus',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },exportData(){
        window.location.href= "/api/crm/expressOrder/export?"+qs.stringify(this.formData);
      },EMSexportData(){
        window.location.href= "/api/crm/expressOrder/EMSexport?"+qs.stringify(this.formData);
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/expressOrder/getQuery').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

