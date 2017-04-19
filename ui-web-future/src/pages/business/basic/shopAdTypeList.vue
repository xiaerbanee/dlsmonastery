<template>
  <div>
    <head-tab active="shopAdTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAdType:edit'">{{$t('shopAdTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAdType:view'">{{$t('shopAdTypeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAdTypeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.totalPriceType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.totalPriceType" filterable clearable :placeholder="$t('shopAdTypeList.inputKey')">
                  <el-option v-for="item in formProperty.totalPriceTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('shopAdTypeList.likeSearch')"></el-input>
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
        <el-table-column prop="totalPriceType" :label="$t('shopAdTypeList.totalPriceType')"></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdTypeList.price')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAdTypeList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopAdTypeList.operation')" width="140">
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
          totalPriceType:'',
          name:'',
        },formLabel:{
          totalPriceType:{label:this.$t('shopAdTypeList.totalPriceType'),value:''},
          name:{label:this.$t('shopAdTypeList.name')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
    };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.totalPriceType.value = util.getLabel(this.formProperty.totalPriceTypes, this.formData.totalPriceType);
        util.setQuery("shopAdTypeList",this.formData);
        axios.get('/api/crm/shopAdType',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'shopAdTypeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'shopAdTypeForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/crm/shopAdType/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },getQuery(){
        axios.get('/api/crm/shopAdType/getQuery').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
      });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getQuery();
    }
  };
</script>

