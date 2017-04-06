<template>
  <div>
    <head-tab :active="$t('adGoodsOrderList.adGoodsOrderList')"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adGoodsOrder:edit'">{{$t('adGoodsOrderList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('adGoodsOrderList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.id" auto-complete="off" :placeholder="$t('adGoodsOrderList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('adGoodsOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.billType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.billType" filterable clearable :placeholder="$t('adGoodsOrderList.inputKey')">
                  <el-option v-for="item in formProperty.billTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.parentId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.parentId" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.officeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.officeName" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.storeId" filterable clearable :placeholder="$t('adGoodsOrderList.inputKey')">
                  <el-option v-for="item in formProperty.stores" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.processStatus.label" :label-width="formLabelWidth">
                <el-select v-model="formData.processStatus" filterable clearable :placeholder="$t('adGoodsOrderList.inputKey')">
                  <el-option v-for="item in formProperty.processFlows" :key="item.name" :label="item.name" :value="item.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" filterable clearable :placeholder="$t('adGoodsOrderList.inputKey')">
                  <el-option v-for="areaType in formProperty.areaTypes"  :key="areaType.name" :label="areaType.name" :value="areaType.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.billDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.billDate" type="daterange" align="right" :placeholder="$t('adGoodsOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adGoodsOrderList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('adGoodsOrderList.orderCode')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adGoodsOrderList.createdDate')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('adGoodsOrderList.billDate')" ></el-table-column>
        <el-table-column prop="billType" :label="$t('adGoodsOrderList.type')" ></el-table-column>
        <el-table-column prop="outCode" :label="$t('adGoodsOrderList.outCode')" ></el-table-column>
        <el-table-column prop="processStatus" :label="$t('adGoodsOrderList.processStatus')"  ></el-table-column>
        <el-table-column prop="storeName" :label="$t('adGoodsOrderList.storeName')"  ></el-table-column>
        <el-table-column prop="created.extendMap.officeName" :label="$t('adGoodsOrderList.officeName')" ></el-table-column>
        <el-table-column prop="created.extendMap.areaName" :label="$t('adGoodsOrderList.areaName')" ></el-table-column>
        <el-table-column prop="shop.areaType" :label="$t('adGoodsOrderList.areaType')" ></el-table-column>
        <el-table-column prop="shop.name" :label="$t('adGoodsOrderList.shopName')" ></el-table-column>
        <el-table-column prop="amount" :label="$t('adGoodsOrderList.amount')"  ></el-table-column>
        <el-table-column prop="expressCodes" :label="$t('adGoodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('adGoodsOrderList.remarks')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('adGoodsOrderList.createdBy')"></el-table-column>
        <el-table-column fixed="right" :label="$t('adGoodsOrderList.operation')" width="140">
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
        page:{},
        formData:{
          pageNumber:0,
          pageSize:25,
          id:'',
          createdDateBTW:'',
          createdDate:'',
          billType:'',
          remarks:'',
          createdBy:'',
          parentId:'',
          officeName:'',
          storeId:'',
          shopName:'',
          processStatus:'',
          areaType:'',
          billDateBTW:'',
          billDate:''
        },formLabel:{
          id:{label:this.$t('adGoodsOrderList.orderCode')},
          createdDateBTW:{label:this.$t('adGoodsOrderList.createdDate')},
          billType:{label:this.$t('adGoodsOrderList.billType')},
          remarks:{label:this.$t('adGoodsOrderList.remarks')},
          createdBy:{label:this.$t('adGoodsOrderList.createdBy')},
          parentId:{label:this.$t('adGoodsOrderList.parentId')},
          officeName:{label:this.$t('adGoodsOrderList.officeName')},
          storeId:{label:this.$t('adGoodsOrderList.storeName'),value:''},
          shopName:{label:this.$t('adGoodsOrderList.shopName')},
          processStatus:{label:this.$t('adGoodsOrderList.processStatus')},
          areaType:{label:this.$t('adGoodsOrderList.areaType')},
          billDateBTW:{label:this.$t('adGoodsOrderList.billDate')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        this.formData.billDateBTW = util.formatDateRange(this.formData.billDate);
        this.formLabel.storeId.value=util.getLabel(this.formProperty.stores,this.formData.storeId);
        util.setQuery("adGoodsOrderList",this.formData);
        axios.get('/api/crm/adGoodsOrder',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'adGoodsOrderForm'})
      },itemAction:function(id,action){
        console.log(id,action)
        if(action=="修改") {
          this.$router.push({ name: 'adGoodsOrderForm', query: { id: id }})
        }else if(action=="详细"){
          this.$router.push({ name: 'adGoodsOrderDetail', query: { id: id,action:"详细"}})
        }else if(action=="审核"){
          this.$router.push({ name: 'adGoodsOrderDetail', query: { id: id,action:"审核"}})
        }else if(action=="开单"){
          this.$router.push({ name: 'adGoodsOrderBill', query: { id: id}})
        }else if(action=="发货"){
          this.$router.push({ name: 'adGoodsOrderShip', query: { id: id }})
        }else if(action=="签收"){
          this.$router.push({ name: 'adGoodsOrderSign', query: { id: id }})
        }else if(action=="刪除"){
          axios.get('/api/crm/adGoodsOrder/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },getListProperty(){
        axios.get('/api/crm/adGoodsOrder/getListProperty').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getListProperty();
    }
  };
</script>

