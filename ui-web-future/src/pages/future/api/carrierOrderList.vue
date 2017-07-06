<template>
  <div>
    <head-tab active="carrierOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'api:carrierOrder:edit'">{{$t('productTypeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" >{{$t('productTypeList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'api:carrierOrder:view'">{{$t('productTypeList.export')}}</el-button>
        <el-button type="primary" @click="carrierShip" v-permit="'api:carrierOrder:ship'">商城发货</el-button>
        <el-dropdown split-button type="primary"  @command="handleCommand">
          {{command?command:'没有选中任何选项'}}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="空值">空值</el-dropdown-item>
            <el-dropdown-item command="已导入">已导入</el-dropdown-item>
            <el-dropdown-item command="待付款">待付款</el-dropdown-item>
            <el-dropdown-item command="验证中">验证中</el-dropdown-item>
            <el-dropdown-item command="验证失败">验证失败</el-dropdown-item>
            <el-dropdown-item command="问题单号">问题单号</el-dropdown-item>
            <el-dropdown-item command="坏单">坏单</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('productTypeList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="订货单号">
                <el-input v-model="formData.businessIdStr" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="formData.carrierOrderStatus" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="carrierOrderStatus in formData.extra.carrierOrderStatusList" :key="carrierOrderStatus" :label="carrierOrderStatus" :value="carrierOrderStatus"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="门店">
                <el-input v-model="formData.depotName" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="发货时间" >
                <date-range-picker v-model="formData.shipDate"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="订单备注">
                <el-input v-model="formData.remarks" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="订单状态">
                <el-select v-model="formData.goodsOrderStatus" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="goodsOrderStatus in formData.extra.goodsOrderStatusList" :key="goodsOrderStatus" :label="goodsOrderStatus" :value="goodsOrderStatus"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="商城单号">
                <el-input v-model="formData.code" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="创建时间">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productTypeList.sure')}}</el-button>
        </div>
      </search-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="goodsOrderStatus" label="状态" ></el-table-column>
        <el-table-column prop="formatId" label="货品订货单号" width="150" ></el-table-column>
        <el-table-column prop="areaName" label="办事处" ></el-table-column>
        <el-table-column prop="depotName" label="门店" width="150"></el-table-column>
        <el-table-column prop="carrierShopName" label="商城门店"></el-table-column>
        <el-table-column prop="shipDate" label="发货时间" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" label="更新时间" sortable></el-table-column>
        <el-table-column prop="code" label="商城单号" ></el-table-column>
        <el-table-column prop="status" label="状态" ></el-table-column>
        <el-table-column prop="remarks" label="订单备注"></el-table-column>
        <el-table-column fixed="right" :label="$t('productTypeList.operation')" >
          <template scope="scope">
            <div class="action" v-permit="'api:carrierOrder:view'"><el-button size="small" @click.native="itemAction(scope.row.goodsOrderId,'detail')">详细</el-button></div>
            <div class="action" v-permit="'api:carrierOrder:edit'"><el-button size="small" @click.native="itemAction(scope.row.goodsOrderId,'edit')">修改</el-button></div>
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
          extra:{}
        },
        inputForm:{},
        detailData:{},
        initPromise:{},
        searchText:'',
        command:"",
        formVisible: false,
        detailVisible:false,
        pageHeight: 600,
        pageLoading: false
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("carrierOrderList",submitData);
        axios.get('/api/ws/future/api/carrierOrder?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        });

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
        this.$router.push({ name: 'carrierOrderForm'})
      },itemAction:function(goodsOrderId,action){
        if(action =="detail"){
          this.$router.push({ name: 'goodsOrderDetail', query: { id: goodsOrderId }})
        }else if(action=="edit"){
         this.detailVisible=true;
         let page=this.page.content;
         for(let item in page){
           if(goodsOrderId==page[item].goodsOrderId){
             this.inputForm=page[item];
           }
         }
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
            window.location.href='/api/ws/future/api/carrierOrder/export?'+qs.stringify(util.deleteExtra(this.formData))
        }).catch(()=>{});
      },carrierShip(){
        this.$router.push({ name: 'carrierOrderShip'})
      },
      handleCommand(command) {
          if(!this.formData.businessIdStr){
            this.$message.error('没有输入订货单号，无法进行状态修改');
          }else {
            axios.get('/api/ws/future/api/carrierOrder/batchStatus',{params:{businessIdStr:this.formData.businessIdStr,status:command}}).then((response)=> {
              this.$message(response.data.message);
              this.pageRequest();
            });
          }
      },formSubmit(){
            axios.post('/api/ws/future/api/carrierOrder/updateStatusAndRemarks', qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.detailVisible=false;
            })
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/api/carrierOrder/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>
