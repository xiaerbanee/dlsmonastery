<template>
  <div>
    <head-tab active="shopGoodsDepositList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopGoodsDeposit:edit'">{{$t('shopGoodsDepositList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopGoodsDeposit:audit'">{{$t('shopGoodsDepositList.batchPass')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:shopGoodsDeposit:view'">{{$t('shopGoodsDepositList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:shopGoodsDeposit:view'">{{$t('shopGoodsDepositList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('shopGoodsDepositList.filter')" v-model="formVisible" size="tiny" class="search-form" ref="searchDialog"  z-index="1500">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopGoodsDepositList.shopName')">
                <el-input v-model="formData.shopName" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.remarks')">
                <el-input v-model="formData.remarks" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.bank')">
                <el-input v-model="formData.bankName" :placeholder="$t('shopGoodsDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.status')">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('shopGoodsDepositList.inputStatus')">
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.Deposit')">
                <el-input v-model="formData.amount" :placeholder="$t('shopGoodsDepositList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopGoodsDepositList.outBillType')">
                <el-select v-model="formData.outBillType" clearable filterable :placeholder="$t('shopGoodsDepositList.inputStatus')">
                  <el-option v-for="item in formData.extra.outBillTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopGoodsDepositList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight"  style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('shopGoodsDepositList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column prop="formatId" column-key="id"  :label="$t('shopGoodsDepositList.code')" sortable></el-table-column>
        <el-table-column prop="shopName" column-key="shopId"  :label="$t('shopGoodsDepositList.shopName')" sortable></el-table-column>
        <el-table-column prop="shopAreaName"  :label="$t('shopGoodsDepositList.areaName')"  ></el-table-column>
        <el-table-column prop="departMent" column-key="departMent" :label="$t('shopGoodsDepositList.department')" sortable></el-table-column>
        <el-table-column prop="bankName" column-key="bankId" :label="$t('shopGoodsDepositList.bank')"  sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('shopGoodsDepositList.amount')" sortable></el-table-column>
        <el-table-column prop="outCode" :label="$t('shopGoodsDepositList.outCode')"  sortable></el-table-column>
        <el-table-column prop="outBillType" :label="$t('shopGoodsDepositList.outBillType')" sortable></el-table-column>
        <el-table-column prop="billDate"  :label="$t('shopGoodsDepositList.billDate')" sortable></el-table-column>
        <el-table-column prop="createdByName"  column-key="createdBy" :label="$t('shopGoodsDepositList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopGoodsDepositList.createdDate')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('shopGoodsDepositList.status')" width="120" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status==='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('shopGoodsDepositList.remarks')" sortable></el-table-column>
        <el-table-column prop="locked" :label="$t('shopGoodsDepositList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  fixed="right" :label="$t('shopGoodsDepositList.operation')" >
          <template scope="scope">
            <div class="action" v-if="scope.row.editable" v-permit="'crm:shopGoodsDeposit:edit'"><el-button size="small" @click.native="itemAction(scope.row.id, 'edit')">{{$t('shopGoodsDepositList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:shopGoodsDeposit:delete'"><el-button size="small" @click.native="itemAction(scope.row.id, 'delete')">{{$t('shopGoodsDepositList.delete')}}</el-button></div>
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
        initPromise:{},
        searchText:'',
        pageLoading: false,
        page:{},
        formData:{
          extra:{},
        },
        selects:[],
        formVisible: false,
        pageHeight: 600,
        formLabelWidth:"28%"

      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;

        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("shopGoodsDepositList",submitData);
        axios.get('/api/ws/future/crm/shopGoodsDeposit?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'shopGoodsDepositForm'});
      },itemBatchAdd(){
        this.$router.push({ name: 'shopGoodsDepositBatchForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'shopGoodsDepositForm', query: { id: id}});
        } else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/shopGoodsDeposit/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/shopGoodsDeposit/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      } ,selectionChange(selection){
        console.log(selection);
        this.selects=[];
        for(let each of selection){
          this.selects.push(each.id)
        }
      },batchPass(){

        if(!this.selects || this.selects.length < 1){
          this.$message(this.$t('shopGoodsDepositList.noSelectionFound'));
          return ;
        }

        util.confirmBeforeBatchPass(this).then(() => {
          this.submitDisabled = true;
          this.pageLoading = true;
          axios.get('/api/ws/future/crm/shopGoodsDeposit/batchAudit',{params:{ids:this.selects}}).then((response) =>{
            this.$message(response.data.message);
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          });
        }).catch(()=>{});

      },checkSelectable(row) {
        return row.status !== '已通过'
      }

    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/crm/shopGoodsDeposit/getQuery').then((response) =>{
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

