<template>
  <div>
    <head-tab active="priceChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:priceChange:edit'">{{$t('priceChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:priceChange:view'">{{$t('priceChangeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('priceChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('priceChangeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('priceChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('priceChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="name"  :label="$t('priceChangeList.name')" sortable width="200"></el-table-column>
        <el-table-column prop="productTypeName" :label="$t('priceChangeList.productType')"width="500"></el-table-column>
        <el-table-column prop="checkPercent" :label="$t('priceChangeList.checkPercent')"></el-table-column>
        <el-table-column prop="priceChangeDate" :label="$t('priceChangeList.priceChangeDate')"></el-table-column>
        <el-table-column prop="uploadEndDate"  :label="$t('priceChangeList.uploadEndDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('priceChangeList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('priceChangeList.createdBy')"></el-table-column>
        <el-table-column prop="status" :label="$t('priceChangeList.status')"></el-table-column>
        <el-table-column  :label="$t('priceChangeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-if="scope.row.status === '上报中'" v-permit="'crm:priceChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('priceChangeList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '上报中'" v-permit="'crm:priceChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('priceChangeList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '上报中'" v-permit="'crm:priceChange:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopImageList.delete')}}</el-button></div>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:''
        },formLabel:{
          name:{label:this.$t('priceChangeList.name')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData)
        util.setQuery("priceChangeList",this.submitData);
        axios.get('/api/ws/future/crm/priceChange',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'priceChangeForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'priceChangeForm', query: { id: id,action:action}})
        } else if(action=="delete") {
          axios.get('/api/ws/future/crm/priceChange/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }else if(action =='audit'){
          this.$router.push({ name: 'priceChangeForm', query: { id: id,action:action}})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/priceChange/getQuery',{params:this.formData}).then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

