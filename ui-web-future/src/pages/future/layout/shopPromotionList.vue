<template>
  <div>
    <head-tab active="shopPromotionList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopPromotion:edit'">{{$t('shopPromotionList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopPromotion:view'">{{$t('shopPromotionList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopPromotionList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.businessId.label" :label-width="formLabelWidth">
                 <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('shopPromotionList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.activityType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.activityType" filterable clearable :placeholder="$t('shopPromotionList.inputKey')">
                  <el-option v-for="type in formData.activityTypeList" :key="type" :label="type" :value="type"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopId.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="adShop"></depot-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopPromotionList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopPromotionList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="businessId" :label="$t('shopPromotionList.businessId')" sortable width="150"></el-table-column>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('shopPromotionList.shopName')" sortable></el-table-column>
        <el-table-column prop="activityType" :label="$t('shopPromotionList.activityType')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('shopPromotionList.amount')" sortable></el-table-column>
        <el-table-column prop="dayAmount" :label="$t('shopPromotionList.dayAmount')" sortable></el-table-column>
        <el-table-column prop="salerComment" :label="$t('shopPromotionList.salerComment')" sortable></el-table-column>
        <el-table-column prop="materialComment" :label="$t('shopPromotionList.materialComment')" sortable></el-table-column>
        <el-table-column prop="activityImage1" :label="$t('shopPromotionList.activityImage1')" sortable></el-table-column>
        <el-table-column prop="activityImage2":label="$t('shopPromotionList.activityImage2')" sortable></el-table-column>
        <el-table-column prop="activityImage3" :label="$t('shopPromotionList.activityImage3')" sortable></el-table-column>
        <el-table-column prop="comment" :label="$t('shopPromotionList.comment')" sortable></el-table-column>
        <el-table-column prop="phone" :label="$t('shopPromotionList.phone')" sortable></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('shopPromotionList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopPromotionList.createdDate')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopPromotionList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopPromotionList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopPromotion:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopPromotionList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:shopPromotion:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopPromotionList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  export default {
    components:{depotSelect},
    data() {
      return {
        page:{},
        formData:{

        },submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          businessId:'',
          activityType:'',
          shopId:''
        },
        formLabel:{
          businessId:{label:this.$t('shopPromotionList.businessId'),value:""},
          activityType:{label:this.$t('shopPromotionList.activityType'),value:""},
          shopId:{label:this.$t('shopPromotionList.shopName'),value:""},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("shopPromotionList",this.submitData);
        axios.get('/api/ws/future/layout/shopPromotion',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'shopPromotionForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopPromotionForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopPromotion/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/layout/shopPromotion/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

