<template>
  <div>
    <head-tab active="shopAdTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAdType:edit'">{{$t('shopAdTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAdType:view'">{{$t('shopAdTypeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAdTypeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('shopAdTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.totalPriceType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.totalPriceType" filterable clearable :placeholder="$t('shopAdTypeList.inputKey')">
                  <el-option v-for="item in formData.totalPriceTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopAdTypeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopAdTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('shopAdTypeList.name')" sortable ></el-table-column>
        <el-table-column prop="totalPriceType" :label="$t('shopAdTypeList.totalPriceType')" sortable></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdTypeList.price')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAdTypeList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopAdTypeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopAdType:edit'"><el-button size="small" @click.native="itemEdit(scope.row.id)">{{$t('shopAdTypeList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:shopAdType:delete'"><el-button size="small" @click.native="itemDelete(scope.row.id)">{{$t('shopAdTypeList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page:{},
        formData:{},
        formLabel:{
          totalPriceType:{label:this.$t('shopAdTypeList.totalPriceType')},
          name:{label:this.$t('shopAdTypeList.name')},
        },
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          name:'',
          totalPriceType:'',
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    }, methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("shopAdTypeList",this.submitData);
        axios.get('/api/ws/future/basic/shopAdType',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },
      sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },
      search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'shopAdTypeForm'})
      },itemEdit:function(id){
        this.$router.push({ name: 'shopAdTypeForm', query: { id: id }})
      },itemDelete:function(id){
       util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/basic/shopAdType/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/shopAdType/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

