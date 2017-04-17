<template>
  <div>
    <head-tab :active="$t('shopAdList.shopAdList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopAd:view'">{{$t('shopAdList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopAd:view'">{{$t('shopAdList.batchBlack')}}</el-button>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAd:edit'">{{$t('shopAdList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAd:view'">{{$t('shopAdList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAdList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('expressOrderList.inputKey')">
                  <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" placeholder="输入非零部分"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.specialArea.label" :label-width="formLabelWidth">
                <el-radio-group v-model="formData.specialArea">
                  <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="formLabel.shopAdTypeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.shopAdTypeId" filterable clearable :placeholder="$t('expressOrderList.inputKey')">
                  <el-option v-for="shopAdType in formProperty.shopAdTypes" :key="shopAdType.id" :label="shopAdType.name" :value="shopAdType.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('expressOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('expressOrderList.likeSearch')"></el-input>
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
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="extendMap.formatId" :label="$t('shopAdList.code')" sortable width=120></el-table-column>
        <el-table-column prop="shop.area.name"  :label="$t('shopAdList.areaName')"></el-table-column>
        <el-table-column prop="shop.name"  :label="$t('shopAdList.shopName')"></el-table-column>
        <el-table-column prop="specialArea" :label="$t('shopAdList.specialArea')">
          <template scope="scope">
            <el-tag :type="scope.row.specialArea ? 'primary' : 'danger'">{{scope.row.specialArea | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="shopAdType.name" :label="$t('shopAdList.shopAdType')" width="120"></el-table-column>
        <el-table-column prop="extendMap.lengthAndWidthAndQty" :label="$t('shopAdList.lengthAndWidthAndQty')" width="120"></el-table-column>
        <el-table-column prop="extendMap.area" :label="$t('shopAdList.totalArea')"></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdList.price')"></el-table-column>
        <el-table-column prop="content" :label="$t('shopAdList.content')" width="120"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopAdList.processStatus')" width="120"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('expressOrderList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressOrderList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="lastModified.loginName" :label="$t('expressOrderList.lastModifiedBy')" width=120></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('expressOrderList.lastModifiedDate')" sortable width=140></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressOrderList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressOrderList.operation')" width="140">
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
          officeId:"",
          id:"",
          shopName:"",
          specialArea:'',
          shopAdTypeId:'',
          createdBy:"",
          createdDateBTW:"",
          createdDate:""
        },formLabel:{
          officeId:{label:this.$t('shopAdList.areaName'),value:""},
          id:{label:this.$t('shopAdList.adCode'),value:""},
          shopName:{label:this.$t('shopAdList.shopName')},
          specialArea:{label:this.$t('shopAdList.specialArea'),value:''},
          shopAdTypeId:{label:this.$t('shopAdList.shopAdType'),value:''},
          createdBy:{label:this.$t('shopAdList.createdBy')},
          createdDateBTW:{label:this.$t('shopAdList.createdDate')}
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
        this.formLabel.specialArea.value = util.bool2str(this.formData.specialArea);
        this.formLabel.officeId.value = util.getLabel(this.formProperty.areas,this.formData.officeId);
        this.formLabel.shopAdTypeId.value = util.getLabel(this.formProperty.shopAdTypes,this.formData.shopAdTypeId);
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("shopAdList",this.formData);
        axios.get('/api/crm/shopAd',{params:this.formData}).then((response) => {
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
        window.location.href= "/api/crm/shopAd/export?"+qs.stringify(this.formData);
      },batchPass(){
      axios.get('/api/crm/shopAd/batchAudit',{params:{pass:true,ids : this.multipleSelection}}).then((response) =>{
        this.$message(response.data.message);
        this.pageRequest();
      })
    },batchBack(){
        axios.get('/api/crm/shopAd/batchAudit',{params:{pass:false,ids : this.multipleSelection}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAdd(){
        this.$router.push({ name: 'shopAdForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'shopAdForm', query: { id: id }})
        }else if(action=="详细"){
          this.$router.push({ name: 'shopAdDetail', query: { id: id }})
        }else if(action=="删除") {
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
      },getQuery(){
        axios.get('/api/crm/shopAd/getQuery').then((response)=>{
          this.formProperty=response.data;
          this.pageRequest();
        })
      },checkSelectable(row) {
        return row.processStatus !== '已通过'
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getQuery();
    }
  };
</script>

