<template>
  <div>
    <head-tab active="afterSaleAreaList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.add')}}</el-button>
        <el-button type="primary" @click="itemEdit" icon="edit" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.edit')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:afterSale:view'">{{$t('afterSaleList.filter')}}</el-button>
        <search-tag  :submitData="formData" :formLabel="formLabel"></search-tag>
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
        <el-table-column type="expand">
          <template scope="scope">
            <el-form label-position="left"  class="demo-table-expand">
              <el-form-item label="窜货机串码">
                <span>{{ scope.row.ime }}</span>
              </el-form-item>
              <el-form-item label="窜货机门店">
                <span>{{ scope.row.fleeShopName }}</span>
              </el-form-item>
              <el-form-item label="联系人">
                <span>{{ scope.row.contact }}</span>
              </el-form-item>
              <el-form-item label="电话">
                <span>{{ scope.row.mobilePhone }}</span>
              </el-form-item>
              <el-form-item label="地址">
                <span>{{ scope.row.address }}</span>
              </el-form-item>
              <el-form-item label="收购价">
                <span>{{ scope.row.buyAmount }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" sortable></el-table-column>
        <el-table-column prop="badProductName" label="货品型号"></el-table-column>
        <el-table-column prop="badDepotName" label="坏机门店" ></el-table-column>
        <el-table-column prop="badType" label="坏机类型"></el-table-column>
        <el-table-column prop="packageStatus" :label="$t('afterSaleList.package')" ></el-table-column>
        <el-table-column prop="memory" :label="$t('afterSaleList.memory')" ></el-table-column>
        <el-table-column prop="badProductIme"label="坏机串码"></el-table-column>
        <el-table-column prop="replaceProductName" label="替换机型号"></el-table-column>
        <el-table-column prop="replaceProductIme" label="替换机串码"></el-table-column>
        <el-table-column prop="fromDepotName" label="来源" ></el-table-column>
        <el-table-column prop="toDepotName" label="所在地"></el-table-column>
        <el-table-column prop="detailRemarks" :label="$t('afterSaleList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('afterSaleList.operation')" width="140">
          <template scope="scope">
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
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/crm/afterSale/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
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

