<template>
  <div>
    <head-tab active="productMonthPriceList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productMonthPrice:edit'">{{$t('productMonthPriceList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productMonthPrice:view'">{{$t('productMonthPriceList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productMonthPriceList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.month.label" :label-width="formLabelWidth">
                <month-picker  v-model="formData.month" ></month-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productMonthPriceList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productMonthPriceList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('productMonthPriceList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="month" :label="$t('productMonthPriceList.month')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy" :label="$t('productMonthPriceList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productMonthPriceList.createdDate')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('productMonthPriceList.remarks')" sortable></el-table-column>
        <el-table-column fixed="right" :label="$t('productMonthPriceList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:productMonthPrice:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('productMonthPriceList.edit')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import monthPicker from 'components/common/month-picker'
  export default {
    components:{
      monthPicker,
    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          month:''
        },formLabel:{
          month:{label:this.$t('productMonthPriceList.month')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("productMonthPriceList",this.submitData);
        axios.get('/api/ws/future/crm/productMonthPrice?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'productMonthPriceForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'productMonthPriceForm', query: { id: id }})
        }
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/productMonthPrice/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

