<template>
  <div>
    <head-tab active="shopImageList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopImage:edit'">{{$t('shopImageList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopImage:view'">{{$t('shopImageList.filter')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopImageList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="SHOP"></depot-select>
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
  import officeSelect from 'components/basic/office-select';
  import depotSelect from 'components/future/depot-select'
  export default {
    components:{officeSelect,depotSelect},
    data() {
      return {
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          officeId:"",
          shopId:""
        },formLabel:{
          officeId:{label:this.$t('shopImageList.areaName')},
          shopName:{label:this.$t('shopImageList.shopName')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData)
        util.setQuery("shopImageList",this.submitData);
        axios.get('/api/ws/future/layout/shopImage',{params:this.submitData}).then((response) => {
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
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopImage/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/layout/shopImage/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

