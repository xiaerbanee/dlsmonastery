<template>
  <div>
    <head-tab active="pricesystemChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:pricesystemChange:edit'">{{$t('pricesystemChangeList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:pricesystemChange:edit'">{{$t('pricesystemChangeList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:pricesystemChange:view'">{{$t('pricesystemChangeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('pricesystemChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('pricesystemChangeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('pricesystemChangeList.inputKey')">
                  <el-option v-for="item in formData.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.pricesystemId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.pricesystemId" filterable clearable :placeholder="$t('pricesystemChangeList.inputKey')">
                  <el-option v-for="pricesystem in formData.pricesystems" :key="pricesystem.name" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('pricesystemChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('pricesystemChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="productName":label="$t('pricesystemChangeList.productName')" width="150" ></el-table-column>
        <el-table-column prop="pricesystemName" :label="$t('pricesystemChangeList.pricesystemName')" ></el-table-column>
        <el-table-column prop="oldPrice" :label="$t('pricesystemChangeList.oldPrice')"  ></el-table-column>
        <el-table-column prop="newPrice" :label="$t('pricesystemChangeList.newPrice')"></el-table-column>
        <el-table-column prop="createdBy" :label="$t('pricesystemChangeList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('pricesystemChangeList.createdDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('pricesystemChangeList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status==='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('pricesystemChangeList.operation')" width="140">
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
        pageHeight:600,
        page:{},
        formData:{
          productName:'',
          createdDate:'',
          status:'',
          pricesystemId:'',
        },
        submitData:{
          page:0,
          size:25,
          productName:'',
          createdDate:'',
          status:'',
          pricesystemId:'',
        },
        formLabel:{
          productName:{label: this.$t('pricesystemChangeList.productName')},
          createdDate:{label: this.$t('pricesystemChangeList.createdDate')},
          status:{label: this.$t('pricesystemChangeList.status')},
          pricesystemId:{label: this.$t('pricesystemChangeList.pricesystemName'),value:""},
        },
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
       util.copyValue(this.formData,this.submitData);
        util.setQuery("pricesystemChangeList",this.submitData);
        axios.get('/api/ws/future/crm/pricesystemChange?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'pricesystemChangeForm'})
      },itemAction:function(id,action){
        this.selects.push(id);
        axios.get('/api/crm/pricesystemChange/audit',{params:{ids:this.selects,pass:action==='通过'?true:false}}).then((response) =>{
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
        axios.get('/api/ws/future/crm/pricesystemChange/audit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },checkSelectable(row) {
        return row.status !== '已通过'
      },
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/pricesystemChange/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
     });
    }
  };
</script>

