<template>
  <div>
    <head-tab active="shopPromotionList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopPromotion:edit'">{{$t('shopPromotionList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopPromotion:view'">{{$t('shopPromotionList.filter')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
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
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="SHOP"></depot-select>
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
        <el-table-column prop="shopName" :label="$t('shopPromotionList.shopName')" ></el-table-column>
        <el-table-column prop="activityType" :label="$t('shopPromotionList.activityType')" ></el-table-column>
        <el-table-column prop="amount" :label="$t('shopPromotionList.amount')" ></el-table-column>
        <el-table-column prop="dayAmount" :label="$t('shopPromotionList.dayAmount')" ></el-table-column>
        <el-table-column prop="salerComment" :label="$t('shopPromotionList.salerComment')" ></el-table-column>
        <el-table-column prop="materialComment" :label="$t('shopPromotionList.materialComment')" ></el-table-column>
        <el-table-column prop="activityImage1" :label="$t('shopPromotionList.activityImage1')" ></el-table-column>
        <el-table-column prop="activityImage2":label="$t('shopPromotionList.activityImage2')" ></el-table-column>
        <el-table-column prop="activityImage3" :label="$t('shopPromotionList.activityImage3')" ></el-table-column>
        <el-table-column prop="comment" :label="$t('shopPromotionList.comment')" ></el-table-column>
        <el-table-column prop="phone" :label="$t('shopPromotionList.phone')" ></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('shopPromotionList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopPromotionList.createdDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopPromotionList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopPromotionList.operation')" width="160">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:shopPromotion:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopPromotionList.edit')}}</el-button>
            <el-button size="small" v-permit="'crm:shopPromotion:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopPromotionList.delete')}}</el-button>
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
          businessId:'',
          activityType:'',
          shopId:''
        },
        formLabel:{
          businessId:{label:this.$t('shopPromotionList.businessId'),value:""},
          activityType:{label:this.$t('shopPromotionList.activityType'),value:""},
          shopName:{label:this.$t('shopPromotionList.shopName'),value:""},
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
        this.formData.order=util.getOrder(column);
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
          axios.get('/api/ws/future/layout/shopPromotion/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
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

