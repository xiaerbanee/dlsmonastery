<template>
  <div>
    <head-tab active="bankInList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:bankIn:edit'">{{$t('bankInList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:bankIn:audit'">{{$t('bankInList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bankIn:view'">{{$t('bankInList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('bankInList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="10">
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.billDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker  v-model="formData.billDate" type="daterange" align="right" :placeholder="$t('bankInList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.amount.label" :label-width="formLabelWidth">
                <el-input v-model="formData.amount" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.inputDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker  v-model="formData.inputDate" type="daterange" align="right" :placeholder="$t('bankInList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.bankName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.bankName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.processStatus.label" :label-width="formLabelWidth">
                <el-select v-model="formData.processStatus" clearable filterable :placeholder="$t('bankInList.selectProcessStatus')">
                  <el-option v-for="status in formProperty.processStatus" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker  v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('bankInList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.serialNumber.label" :label-width="formLabelWidth">
                <el-input v-model="formData.serialNumber" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('bankInList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('bankInList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="id" :label="$t('bankInList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="shop.name" :label="$t('bankInList.shopName')" sortable></el-table-column>
        <el-table-column prop="realShopName" :label="$t('bankInList.realShopName')" width="140" sortable></el-table-column>
        <el-table-column prop="bank.name" :label="$t('bankInList.bankName')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('bankInList.amount')" sortable></el-table-column>
        <el-table-column prop="serialNumber" :label="$t('bankInList.serialNumber')"sortable></el-table-column>
        <el-table-column prop="billDate" :label="$t('bankInList.billDate')" width="140" sortable></el-table-column>
        <el-table-column prop="inputDate" :label="$t('bankInList.inputDate')" width="140" sortable></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('bankInList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('bankInList.createdDate')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('bankInList.outCode')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('bankInList.processStatus')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('bankInList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('bankInList.operation')" width="140">
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
          id:'',
          shopName:"",
          billDate:"",
          billDateBTW:"",
          amount:"",
          inputDate:"",
          inputDateBTW:"",
          outCode:"",
          bankName:"",
          processStatus:"",
          createdBy:"",
          createdDate:"",
          createdDateBTW:"",
          serialNumber:""
        },formLabel:{
          id:{label:this.$t('bankInList.id')},
          shopName:{label:this.$t('bankInList.shopName')},
          billDateBTW:{label:this.$t('bankInList.billDate')},
          amount:{label:this.$t('bankInList.amount')},
          inputDateBTW:{label:this.$t('bankInList.inputDate')},
          outCode:{label:this.$t('bankInList.outCode')},
          bankName:{label:this.$t('bankInList.bankName')},
          processStatus:{label:this.$t('bankInList.processStatus')},
          createdBy:{label:this.$t('bankInList.createdBy')},
          createdDateBTW:{label:this.$t('bankInList.createdDate')},
          serialNumber:{label:this.$t('bankInList.serialNumber')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        selects:new Array(),
        pickerDateOption:util.pickerDateOption,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.billDateBTW = util.formatDateRange(this.formData.billDate);
        this.formData.inputDateBTW = util.formatDateRange(this.formData.inputDate);
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        util.setQuery("bankInList",this.formData);
        axios.get('/api/crm/bankIn',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'bankInForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'bankInForm', query: { id: id }})
        }else if(action=="详细"){
          this.$router.push({ name: 'bankInDetail', query: { id: id,action:action }})
        }else if(action=="审核"){
          this.$router.push({ name: 'bankInDetail', query: { id: id,action:action }})
        }else if(action=="删除"){
            axios.get("/api/crm/bankIn/delete",{params:{id:id}}).then((response)=>{
                this.$message(response.data.message);
                this.pageRequest();
          })
        }
      },checkSelectable(row) {
        return row.processStatus !== '已通过' && row.processStatus !== '未通过'
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchPass(){
        axios.get('/api/crm/bankIn/batchAudit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },getQuery(){
        axios.get('/api/crm/bankIn/getQuery').then((response) =>{
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

