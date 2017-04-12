<template>
  <div>
    <head-tab :active="$t('imeAllotList.imeAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:imeAllot:edit'">{{$t('imeAllotList.imeAllotList')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:imeAllot:edit'">{{$t('imeAllotList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:imeAllot:view'">{{$t('imeAllotList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('imeAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.fromDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.fromDepotName" auto-complete="off" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toDepotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toDepotName" auto-complete="off" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.crossArea.label" :label-width="formLabelWidth">
                <el-select v-model="formData.crossArea" filterable clearable :placeholder="$t('imeAllotList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('imeAllotList.inputKey')">
                  <el-option v-for="item in formProperty.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('imeAllotList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('imeAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('imeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="fromDepot.name" :label="$t('imeAllotForm.fromDepot')" width="150" ></el-table-column>
        <el-table-column prop="toDepot.name" :label="$t('imeAllotForm.toDepot')"></el-table-column>
        <el-table-column prop="productIme.ime" :label="$t('imeAllotForm.ime')"  ></el-table-column>
        <el-table-column prop="productIme.product.name" :label="$t('imeAllotForm.productName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('imeAllotForm.allotDate')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('imeAllotForm.allotEmployee')"></el-table-column>
        <el-table-column prop="status":label="$t('imeAllotForm.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status==='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('imeAllotList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('imeAllotList.operation')" width="140">
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
          pageNumber:0,
          pageSize:25,
          fromDepotName:'',
          toDepotName:'',
          crossArea:'',
          ime:'',
          status:'',
          createdDateBTW:'',
          createdDate:''
        },formLabel:{
          fromDepotName:{label: this.$t('imeAllotList.fromDepot')},
          toDepotName:{label:this.$t('imeAllotList.toDepot')},
          crossArea:{label:this.$t('imeAllotList.crossArea'),value:''},
          ime:{label:this.$t('imeAllotList.ime')},
          status:{label:this.$t('imeAllotList.status')},
          createdDateBTW:{label: this.$t('imeAllotList.createdDate')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.crossArea.value = util.bool2str(this.formData.crossArea);
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);

        util.setQuery("imeAllotList",this.formData);
        axios.get('/api/crm/imeAllot',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'imeAllotForm'})
      },itemAction:function(id,action){
        this.selects.push(id);
        axios.get('/api/crm/imeAllot/audit',{params:{ids:this.selects,pass:action==='通过'?true:false}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchPass(){
        axios.get('/api/crm/imeAllot/audit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },checkSelectable(row) {
        return row.status !== '已通过'
      },getListProperty(){
        axios.get('/api/crm/imeAllot/getListProperty').then((response) =>{
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

