<template>
  <div>
    <head-tab active="shopAdList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopAd:view'">{{$t('shopAdList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopAd:view'">{{$t('shopAdList.batchBlack')}}</el-button>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAd:edit'">{{$t('shopAdList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAd:view'">{{$t('shopAdList.filterOrExport')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAdList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" placeholder="输入非零部分"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="SHOP"></depot-select>
              </el-form-item>
              <el-form-item :label="formLabel.specialArea.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.specialArea"></bool-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopAdTypeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.shopAdTypeId" filterable clearable :placeholder="$t('expressOrderList.inputKey')">
                  <el-option v-for="shopAdType in formData.shopAdTypes" :key="shopAdType.id" :label="shopAdType.name" :value="shopAdType.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy" :multiple="multiple"></account-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData" v-permit="'crm:shopAd:view'">{{$t('expressOrderList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('expressOrderList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')"  @selection-change="handleSelectionChange" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="50" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="id" :label="$t('shopAdList.code')" sortable width=120></el-table-column>
        <el-table-column prop="officeName"  :label="$t('shopAdList.areaName')"></el-table-column>
        <el-table-column prop="shopName"  :label="$t('shopAdList.shopName')"></el-table-column>
        <el-table-column prop="specialArea" :label="$t('shopAdList.specialArea')">
          <template scope="scope">
            <el-tag :type="scope.row.specialArea ? 'primary' : 'danger'">{{scope.row.specialArea | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="shopAdTypeName" :label="$t('shopAdList.shopAdType')" width="120"></el-table-column>
        <el-table-column prop="lengthWidthQty" :label="$t('shopAdList.lengthAndWidthAndQty')" width="120"></el-table-column>
        <el-table-column prop="area" :label="$t('shopAdList.totalArea')"></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdList.price')"></el-table-column>
        <el-table-column prop="content" :label="$t('shopAdList.content')" width="120"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopAdList.processStatus')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('expressOrderList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressOrderList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('expressOrderList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('expressOrderList.lastModifiedDate')" sortable width=120></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressOrderList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressOrderList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:shopAd:view'" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopPrintList.detail')}}</el-button>
            <el-button size="small" v-if="scope.row.isAuditable" v-permit="'crm:shopAd:edit'" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopBuildList.audit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopAd:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopBuildList.edit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopAd:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopBuildList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';
  import boolSelect from 'components/common/bool-select';
  export default {
    components:{officeSelect,accountSelect,depotSelect,boolSelect},
    data() {
      return {
        multiple:true,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          officeId:"",
          id:"",
          shopId:"",
          specialArea:'',
          shopAdTypeId:'',
          createdBy:"",
          createdDate:""
        },formLabel:{
          officeId:{label:this.$t('shopAdList.areaName'),value:""},
          id:{label:this.$t('shopAdList.adCode'),value:""},
          shopName:{label:this.$t('shopAdList.shopName')},
          specialArea:{label:this.$t('shopAdList.specialArea'),value:''},
          shopAdTypeId:{label:this.$t('shopAdList.shopAdType'),value:''},
          createdBy:{label:this.$t('shopAdList.createdBy')},
          createdDate:{label:this.$t('shopAdList.createdDate')}
        },
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
        this.formLabel.specialArea.value = util.bool2str(this.formData.specialArea);
        this.formData.createdDate=util.formatDateRange(this.formData.createdDate);
        util.copyValue(this.formData,this.submitData)
        util.setQuery("shopAdList",this.submitData);
        axios.get('/api/ws/future/layout/shopAd',{params:this.submitData}).then((response) => {
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
        window.location.href= "/api/ws/future/layout/shopAd/export?"+qs.stringify(this.formData);
      },batchPass(){
      axios.get('/api/ws/future/layout/shopAd/batchAudit',{params:{pass:true,ids : this.multipleSelection}}).then((response) =>{
        this.$message(response.data.message);
        this.pageRequest();
      })
    },batchBack(){
        axios.get('/api/ws/future/layout/shopAd/batchAudit',{params:{pass:false,ids : this.multipleSelection}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAdd(){
        this.$router.push({ name: 'shopAdForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopAdForm', query: { id: id }})
        }else if(action=="detail"){
          this.$router.push({ name: 'shopAdDetail', query: { id: id }})
        }else if(action =="audit"){
            this.$router.push({name:'shopAdDetail',query:{id:id,action:action}})
        }else if(action=="delete") {
          axios.get('/api/crm/shopAd/delete',{params:{id:id}}).then((response) =>{
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
      },checkSelectable(row) {
        return row.processStatus !== '已通过'
      }
    },created () {
        var that = this;
        that.pageHeight = window.outerHeight -320;
        axios.get('/api/ws/future/layout/shopAd/getQuery').then((response)=>{
          that.formData=response.data;
          util.copyValue(this.$route.query,that.formData);
          that.pageRequest();
      })
    }
  };
</script>

