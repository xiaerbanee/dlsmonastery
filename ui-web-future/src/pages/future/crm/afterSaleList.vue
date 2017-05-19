<template>
  <div>
    <head-tab active="afterSaleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.add')}}</el-button>
        <el-button type="primary" @click="itemEdit" icon="edit" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.edit')}}</el-button>
        <el-button type="primary" @click="itemSyn" icon="plus" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.syn')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:afterSale:view'">{{$t('afterSaleList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('afterSaleList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.toAreaProductIme.label" :label-width="formLabelWidth">
                <el-input v-model="formData.toAreaProductIme" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.badProductIme.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.badProductIme" auto-complete="off" :placeholder="$t('afterSaleList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('afterSaleList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.toCompanyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.toCompanyDate" type="daterange" align="right" :placeholder="$t('afterSaleList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.fromCompanyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.fromCompanyDate" type="daterange" align="right" :placeholder="$t('afterSaleList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.toStoreDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.toStoreDate" type="daterange" align="right" :placeholder="$t('afterSaleList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleList.bill')" sortable></el-table-column>
        <el-table-column prop="badProductIme.ime" :label="$t('afterSaleList.badProductIme')"></el-table-column>
        <el-table-column prop="badProductIme.product.name" :label="$t('afterSaleList.badProductName')" ></el-table-column>
        <el-table-column prop="toAreaProductIme.product.name" :label="$t('afterSaleList.toAreaProductName')"></el-table-column>
        <el-table-column prop="areaDepot.name" :label="$t('afterSaleList.areaDepot')"></el-table-column>
        <el-table-column prop="packageStatus" :label="$t('afterSaleList.package')" ></el-table-column>
        <el-table-column prop="memory" :label="$t('afterSaleList.memory')" ></el-table-column>
        <el-table-column prop="toStoreType":label="$t('afterSaleList.toStoreType')"></el-table-column>
        <el-table-column prop="toStoreRemarks" :label="$t('afterSaleList.toStoreDateRemarks')"></el-table-column>
        <el-table-column prop="toStoreDate" :label="$t('afterSaleList.toStoreDate')"></el-table-column>
        <el-table-column prop="toCompanyDate" :label="$t('afterSaleList.toCompanyDate')" ></el-table-column>
        <el-table-column prop="fromCompanyDate" :label="$t('afterSaleList.fromCompanyDate')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('afterSaleList.createdBy')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('afterSaleList.remarks')"></el-table-column>
        <el-table-column prop="syn" :label="$t('afterSaleList.synFor')">
          <template scope="scope">
            <el-tag :type="scope.row.syn ? 'primary' : 'danger'">{{scope.row.syn | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('afterSaleList.operation')" width="140">
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
          page:0,
          size:25,
          id:'',
          shopName:'',
          badProductIme:'',
          toAreaProductIme:'',
          remarks:'',
          createdBy:'',
          createdDateBTW:'',
          createdDate:'',
          toCompanyDateBTW:'',
          toCompanyDate:'',
          fromCompanyDateBTW:'',
          fromCompanyDate:'',
          toStoreDateBTW:'',
          toStoreDate:'',
        },formLabel:{
          id:{label:this.$t('afterSaleList.bill')},
          shopName:{label:this.$t('afterSaleList.areaDepot')},
          toAreaProductIme:{label:this.$t('afterSaleList.toAreaProductIme')},
          badProductIme:{label:this.$t('afterSaleList.badProductIme')},
          remarks:{label:this.$t('afterSaleList.remarks')},
          createdBy:{label:this.$t('afterSaleList.createdBy')},
          createdDateBTW:{label:this.$t('afterSaleList.createdDate')},
          toCompanyDateBTW:{label:this.$t('afterSaleList.toCompanyDate')},
          fromCompanyDateBTW:{label:this.$t('afterSaleList.fromCompanyDate')},
          toStoreDateBTW:{label:this.$t('afterSaleList.toStoreDate')}
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        this.formData.toCompanyDateBTW = util.formatDateRange(this.formData.toCompanyDate);
        this.formData.fromCompanyDateBTW = util.formatDateRange(this.formData.fromCompanyDate);
        this.formData.toStoreDateBTW = util.formatDateRange(this.formData.toStoreDate);

        util.setQuery("afterSaleList",this.formData);
        axios.get('/api/ws/future/crm/afterSale',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'afterSaleForm'})
      },itemEdit(){
        this.$router.push({ name: 'afterSaleEditForm'})
      },itemSyn(){
        axios.get('/api/ws/future/crm/afterSale/synToFinance').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'afterSaleEditForm', query: { id: id }})
        }else if(action=="刪除"){
          axios.get('/api/crm/afterSale/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }else if(action=="同步"){
          axios.get('/api/crm/afterSale/synToFinance').then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

