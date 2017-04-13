<template>
  <div>
    <head-tab :active="$t('storeAllotList.storeAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAllot:edit'">{{$t('storeAllotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAllot:view'">{{$t('storeAllotList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('storeAllotList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('storeAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status">
                  <el-option v-for="item in formProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.fromStoreId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.fromStoreId" filterable clearable :placeholder="$t('storeAllotList.inputKey')">
                  <el-option v-for="fromStore in formProperty.stores" :key="fromStore.id" :label="fromStore.name" :value="fromStore.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.toStoreId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.toStoreId" filterable clearable :placeholder="$t('storeAllotList.inputKey')">
                  <el-option v-for="toStore in formProperty.stores" :key="toStore.id" :label="toStore.name" :value="toStore.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData">{{$t('storeAllotList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('storeAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('storeAllotList.loading')"  @selection-change="handleSelectionChange" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="businessId" :label="$t('storeAllotList.businessId')" sortable width=120></el-table-column>
        <el-table-column prop="fromStore.name" :label="$t('storeAllotList.fromStore')"></el-table-column>
        <el-table-column prop="toStore.name" :label="$t('storeAllotList.toStore')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('storeAllotList.outCode')" width="120"></el-table-column>
        <el-table-column prop="status" :label="$t('storeAllotList.status')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('storeAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('storeAllotList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="lastModified.loginName" :label="$t('storeAllotList.lastModifiedBy')" width=120></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('storeAllotList.lastModifiedDate')" sortable width=140></el-table-column>
        <el-table-column prop="remarks" :label="$t('storeAllotList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('storeAllotList.operation')" width="140">
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
          outCode:"",
          businessId:'',
          status:'',
          createdBy:"",
          createdDateBTW:"",
          createdDate:"",
          remarks:"",
          toStoreId:"",
          fromStoreId:""
        },formLabel:{
          outCode:{label:this.$t('storeAllotList.outCode')},
          businessId:{label:this.$t('storeAllotList.businessId')},
          status:{label:this.$t('storeAllotList.status')},
          createdBy:{label:this.$t('storeAllotList.createdBy')},
          remarks:{label:this.$t('storeAllotList.remarks')},
          fromStoreId:{label:this.$t('storeAllotList.fromStore'),value:""},
          toStoreId:{label:this.$t('storeAllotList.toStore'),value:""},
          createdDateBTW:{label:this.$t('storeAllotList.createdDate')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        multipleSelection:[],
        pickerDateOption:util.pickerDateOption
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        this.formLabel.fromStoreId.value =util.getLabel(this.formProperty.stores,this.formData.fromStoreId);
        this.formLabel.toStoreId.value =util.getLabel(this.formProperty.stores,this.formData.toStoreId)

        util.setQuery("storeAllotList",this.formData);
        axios.get('/api/crm/storeAllot',{params:this.formData}).then((response) => {
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
      },exportData(){
        window.location.href= "/api/crm/storeAllot/export?"+qs.stringify(this.formData);
      },itemAdd(){
        this.$router.push({ name: 'storeAllotForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'storeAllotForm', query: { id: id }})
        }else if(action=="详细"){
          this.$router.push({ name: 'storeAllotDetail', query: { id: id }})
        }else if(action=="发货"){
          this.$router.push({ name: 'storeAllotShip', query: { id: id }})
        }else if(action=="删除") {
          axios.get('/api/crm/storeAllot/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },handleSelectionChange(val) {
        var arrs=[];
        for(var i in val){
          arrs.push(val[i].id)
        };
        this.multipleSelection=arrs;
      },getListProperty(){
        axios.get('/api/crm/storeAllot/getListProperty').then((response)=>{
          this.formProperty=response.data;
          this.pageRequest();
        })
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getListProperty();
    }
  };
</script>

