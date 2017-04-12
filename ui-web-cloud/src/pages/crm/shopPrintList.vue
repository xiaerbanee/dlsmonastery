<template>
  <div>
    <head-tab :active="$t('shopPrintList.shopPrintList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopPrint:edit'">{{$t('shopPrintList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopPrint:view'">{{$t('shopPrintList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopPrintList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('shopPrintList.inputKey')">
                  <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.printType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.printType" filterable clearable :placeholder="$t('shopPrintList.inputKey')">
                  <el-option v-for="printType in formProperty.printTypes" :key="printType.name" :label="printType.name" :value="printType.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.processStatus.label" :label-width="formLabelWidth">
              <el-select v-model="formData.processStatus" filterable clearable :placeholder="$t('shopPrintList.inputKey')">
                <el-option v-for="item in formProperty.processStatus" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
              <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('shopPrintList.likeSearch')"></el-input>
            </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopPrintList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopPrintList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('shopPrintList.code')" sortable width="150"></el-table-column>
        <el-table-column prop="office.name" :label="$t('shopPrintList.officeId')"></el-table-column>
        <el-table-column prop="printType" :label="$t('shopPrintList.printType')"></el-table-column>
        <el-table-column prop="qty" :label="$t('shopPrintList.qty')"></el-table-column>
        <el-table-column prop="content":label="$t('shopPrintList.content')"></el-table-column>
        <el-table-column prop="address" :label="$t('shopPrintList.address')"></el-table-column>
        <el-table-column prop="contator" :label="$t('shopPrintList.contact')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('shopPrintList.mobilePhone')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('shopPrintList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopPrintList.createdDate')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopPrintList.processStatus')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopPrintList.operation')" width="140">
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
          pageNumber:0,
          pageSize:25,
          officeId:"",
          printType:"",
          processStatus:"",
          createdBy:""
        },formLabel:{
          officeId:{label:this.$t('shopPrintList.officeId'),value:""},
          printType:{label:this.$t('shopPrintList.printType'),value:""},
          processStatus:{label:this.$t('shopPrintList.processStatus')},
          createdBy:{label:this.$t('shopPrintList.createdBy')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.officeId.value = util.getLabel(this.formProperty.areas,this.formData.officeId);
        util.setQuery("shopPrintList",this.formData);
        axios.get('/api/crm/shopPrint',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'shopPrintForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'shopPrintForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/crm/shopPrint/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }else if(action == "详细"){
           this.$router.push({ name: 'shopPrintDetail', query: { id: id }})
        }
      },getListProperty(){
        axios.get('/api/crm/shopPrint/getListProperty').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getListProperty();
    }
  };
</script>

