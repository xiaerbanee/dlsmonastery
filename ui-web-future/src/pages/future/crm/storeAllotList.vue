<template>
  <div>
    <head-tab active="storeAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:storeAllot:edit'">{{$t('storeAllotList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:storeAllot:view'">{{$t('storeAllotList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:storeAllot:view'">{{$t('storeAllotList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('storeAllotList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('storeAllotList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.status')">
                <el-select v-model="formData.status" clearable>
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.createdBy')">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.remarks')">
                <el-input v-model="formData.remarks" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('storeAllotList.fromStore')">
                <depot-select category="store" v-model="formData.fromStoreId"  @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.toStore')">
                <depot-select category="store" v-model="formData.toStoreId"  @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.outCode')">
                <el-input v-model="formData.outCode" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('storeAllotList.businessId')">
                <el-input type="textarea" v-model="formData.businessIds" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('storeAllotList.sure')}}</el-button>
        </div>
      </search-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('storeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="formatId" column-key="businessId"   :label="$t('storeAllotList.businessId')" sortable ></el-table-column>
        <el-table-column prop="fromStoreName" column-key="fromStoreId"  :label="$t('storeAllotList.fromStore')" sortable ></el-table-column>
        <el-table-column prop="toStoreName" column-key="toStoreId" :label="$t('storeAllotList.toStore')" sortable ></el-table-column>
        <el-table-column prop="outCode" :label="$t('storeAllotList.outCode')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('storeAllotList.status')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy"  :label="$t('storeAllotList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('storeAllotList.createdDate')"  sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" column-key="lastModifiedBy" :label="$t('storeAllotList.lastModifiedBy')"  sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('storeAllotList.lastModifiedDate')" sortable ></el-table-column>
        <el-table-column prop="remarks" :label="$t('storeAllotList.remarks')"></el-table-column>
        <el-table-column :label="$t('storeAllotList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:storeAllot:view'"><el-button   size="small"  @click.native="itemAction(scope.row.id, 'view')">{{$t('storeAllotList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '待发货' || scope.row.status === '发货中'" v-permit="'crm:storeAllot:ship'" ><el-button size="small" @click.native="itemAction(scope.row.id,'ship')">{{$t('storeAllotList.ship')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '待发货'"  v-permit="'crm:storeAllot:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')"> {{$t('storeAllotList.delete')}}</el-button></div>
            <div class="action" v-permit="'crm:storeAllot:ship'"><el-button :style="stypeOfPrintBtn(scope.row.print)" size="small" @click.native="itemAction(scope.row.id,'print')">{{$t('storeAllotList.print')}}</el-button></div>
            <div class="action" v-permit="'crm:storeAllot:ship'"><el-button :style="stypeOfShipPrintBtn(scope.row.shipPrint)" size="small" @click.native="itemAction(scope.row.id, 'shipPrint')">{{$t('storeAllotList.shipPrint')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      depotSelect,
      accountSelect,
    },
    data() {
      return {
        page:{},
        pageHeight:600,
        initPromise:{},
        searchText:"",
        formData:{
            extra:{}
        },
        formVisible: false,
        pageLoading: false,
        multipleSelection:[],
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
        util.setQuery("storeAllotList",submitData);
        axios.get('/api/ws/future/crm/storeAllot',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'storeAllotForm'})
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href="/api/ws/future/crm/storeAllot/export?"+{params:this.submitData};
        }).catch(()=>{});
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'storeAllotForm', query: { id: id }});
        }else if(action==="view"){
          this.$router.push({ name: 'storeAllotDetail', query: { id: id }})
        }else if(action==="ship"){
          this.$router.push({ name: 'storeAllotShip', query: { id: id }});
        }else if(action==="print"){
           window.open('/#/future/crm/storeAllotPrint?id=' + id, '', '');
        }else if(action==="shipPrint"){
           window.open('/#/future/crm/storeAllotShipPrint?id=' + id, '', '');
        }else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/storeAllot/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            })
          }).catch(()=>{});
        }
      },handleSelectionChange(val) {
        let arrs = [];
        for(let each of val){
          arrs.push(each.id)
        }
        this.multipleSelection=arrs;
      },stypeOfPrintBtn(isPrint){
          if(!isPrint){
              return "color:#ff0000;";
          }else {
              return "";
          }
      },stypeOfShipPrintBtn(isShipPrint){
        if(!isShipPrint){
          return "color:#ff0000;";
        }else {
          return "";
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/crm/storeAllot/getQuery').then((response) =>{
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

