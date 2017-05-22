<template>
  <div>
    <head-tab active="expressOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('expressOrderList.filter') }}</el-button>
        <el-button  type="primary" @click="exportData">{{$t('expressOrderList.export')}}</el-button>
        <el-button  type="primary" @click="exportEMSData">{{$t('expressOrderList.EMSExportData')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('expressOrderList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.fromDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromDepotName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toDepotName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.expressCompanyName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCompanyName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.extendBusinessIdStart.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendBusinessIdStart" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.printDateRange.label" :label-width="formLabelWidth">
              <date-range-picker v-model="formData.printDateRange"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.extendType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.extendType" clearable filterable :placeholder="$t('expressOrderList.selectExtendType')">
                  <el-option v-for="item in formData.extendTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.outPrint.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outPrint"   >
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
              <el-form-item :label="formLabel.extendBusinessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.extendBusinessId" auto-complete="off" :placeholder="$t('expressOrderList.preciseSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">

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
        <el-table-column prop="expressCompanyName" :label="$t('expressOrderList.expressCompanyName')"></el-table-column>
        <el-table-column prop="fromDepotName" :label="$t('expressOrderList.fromDepotName')"></el-table-column>
        <el-table-column prop="toDepotName" :label="$t('expressOrderList.toDepotName')"></el-table-column>
        <el-table-column prop="realPay" :label="$t('expressOrderList.realPay')"></el-table-column>
        <el-table-column prop="contator" :label="$t('expressOrderList.contact')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('expressOrderList.mobilePhone')"></el-table-column>
        <el-table-column prop="expressPrintQty" :label="$t('expressOrderList.expressPrintQty')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressOrderList.createdDate')"></el-table-column>
        <el-table-column prop="printDate" :label="$t('expressOrderList.printDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressOrderList.remarks')"></el-table-column>
        <el-table-column  :label="$t('expressOrderList.operation')" width="140">
          <template scope="scope">
            <el-button type="text" size="small" v-permit="'crm:expressOrder:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('expressOrderList.edit')}}</el-button>
            <el-button type="text" size="small" v-permit="'crm:expressOrder:edit'" @click.native="itemAction(scope.row.id,'reset')">{{$t('expressOrderList.reset')}}</el-button>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          fromDepotName:'',
          toDepotName:'',
          expressCompanyName:'',
          printDateRange:'',
          extendBusinessId:"",
          extendBusinessIdStart:"",
          extendType:'',
          expressPrint:'',
          outPrint:''
        },formLabel:{

          fromDepotName:{label:this.$t('expressOrderList.fromDepotName')},
          toDepotName:{label:this.$t('expressOrderList.toDepotName')},
          expressCompanyName:{label:this.$t('expressOrderList.expressCompanyName')},
          printDateRange:{label:this.$t('expressOrderList.printDate')},
          extendBusinessId:{label:this.$t('expressOrderList.extendBusinessId')},
          extendBusinessIdStart:{label:this.$t('expressOrderList.extendBusinessIdStart')},
          expressPrint:{label:this.$t('expressOrderList.expressPrint')},
          outPrint:{label:this.$t('expressOrderList.outPrint')},
          extendType:{label:this.$t('expressOrderList.extendType')}
        },

        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("expressOrderList",this.submitData);
        axios.get('/api/ws/future/crm/expressOrder?'+qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'expressOrderForm', query: { id: id }})
        } else if(action=="reset") {
          axios.get('/api/ws/future/crm/expressOrder/resetPrintStatus',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/expressOrder/export?'+qs.stringify(this.submitData)).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});

      },exportEMSData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/expressOrder/exportEMS?'+qs.stringify(this.submitData)).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});;

      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/expressOrder/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

