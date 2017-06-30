<template>
  <div>
    <head-tab active="afterSaleHeadList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.add')}}</el-button>
        <el-button type="primary" @click="itemEdit" icon="edit" v-permit="'crm:afterSale:edit'">{{$t('afterSaleList.edit')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:afterSale:view'">{{$t('afterSaleList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('afterSaleList.filter')" v-model="formVisible" size="large" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="坏机门店" :label-width="formLabelWidth">
                <el-input v-model="formData.badDepotName" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="坏机串码" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.imeStr" auto-complete="off" :placeholder="$t('afterSaleList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item label="替换机串码" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.replaceProductImeStr" auto-complete="off" :placeholder="$t('afterSaleList.blankOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleList.remarks')" :label-width="formLabelWidth">
                <el-input v-model="formData.detailRemarks" auto-complete="off" :placeholder="$t('afterSaleList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="录入时间" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.inputDateRanger" ></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="expand">
          <template scope="scope">
            <el-form label-position="left"  class="demo-table-expand">
              <el-row :gutter="4">
                <el-col :span="8">
                  <el-form-item label="窜货机串码">
                    <span>{{ scope.row.ime }}</span>
                  </el-form-item>
                  <el-form-item label="窜货机门店">
                    <span>{{ scope.row.fleeShopName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="联系人">
                    <span>{{ scope.row.contact }}</span>
                  </el-form-item>
                  <el-form-item label="电话">
                    <span>{{ scope.row.mobilePhone }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="地址">
                    <span>{{ scope.row.address }}</span>
                  </el-form-item>
                  <el-form-item label="收购价">
                    <span>{{ scope.row.buyAmount }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
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
        <el-table-column :label="$t('afterSaleList.operation')" width="140">
          <template scope="scope">
            <div class="action" >
              <el-button size="small"  @click.native="itemAction(scope.row.detailId, 'detail')">删除</el-button>
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
        searchText:"",
        page:{},
        initPromise:{},
        formData:{
        },
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("afterSaleHeadList",this.submitData);
        axios.get('/api/ws/future/crm/afterSale?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'afterSaleHeadInput',query:{action:"add"}})
      },itemEdit(){
        this.$router.push({ name: 'afterSaleHeadInput',query:{action:"update"}})
      },itemSyn(){
        axios.get('/api/ws/future/crm/afterSale/synToFinance').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAction:function(detailId,action){
        if(action=="detail"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/afterSale/delete',{params:{detailId:detailId}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }else if(action=="同步"){
          axios.get('/api/ws/future/crm/afterSale/synToFinance').then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/afterSale/getQuery').then((response) =>{
        this.formData=response.data;
        this.formData.type="总部录入"
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

