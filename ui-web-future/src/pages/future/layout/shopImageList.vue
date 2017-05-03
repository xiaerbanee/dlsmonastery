<template>
  <div>
    <head-tab active="shopImageList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopImage:edit'">{{$t('shopImageList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopImage:view'">{{$t('shopImageList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopImageList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('shopImageList.inputKey')">
                  <el-option v-for="area in formProperty.areaList" :key="area.id" :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopImageList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopImageList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopImageList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="areaName" :label="$t('shopImageList.areaName')" sortable width="150"></el-table-column>
        <el-table-column prop="officeName" :label="$t('shopImageList.officeName')"></el-table-column>
        <el-table-column prop="shopName"  :label="$t('shopImageList.shopName')"></el-table-column>
        <el-table-column prop="imageType"  :label="$t('shopImageList.imageType')"></el-table-column>
        <el-table-column prop="image"  :label="$t('shopImageList.image')"></el-table-column>
        <el-table-column prop="imageSize"  :label="$t('shopImageList.imageSize')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopImageList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopImageList.operation')" width="160">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:shopImage:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopImageList.edit')}}</el-button>
            <el-button size="small" v-permit="'crm:shopImage:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopImageList.delete')}}</el-button>
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
          shopName:""
        },formLabel:{
          officeId:{label:this.$t('shopImageList.areaName'),value:""},
          shopName:{label:this.$t('shopImageList.shopName'),value:""}
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
        this.formLabel.officeId.value = util.getLabel(this.formProperty.areaList, this.formData.officeId);
        util.setQuery("shopImageList",this.formData);
        axios.get('/api/ws/future/layout/shopImage',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'shopImageForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopImageForm', query: { id: id }})
        } else if(action=="delete") {
          axios.get('/api/ws/future/layout/shopImage/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },getQuery(){
        axios.get('/api/ws/future/layout/shopImage').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

