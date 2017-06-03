<template>
  <div>
    <head-tab active="storeAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:storeAllot:edit'">{{$t('storeAllotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:storeAllot:view'">{{$t('storeAllotList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:storeAllot:view'">{{$t('storeAllotList.export')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('storeAllotList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth" >
                <el-select v-model="formData.status" clearable>
                  <el-option v-for="item in formData.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" ></account-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.fromStoreId.label" :label-width="formLabelWidth">
                <depot-select category="store" v-model="formData.fromStoreId"  ></depot-select>
              </el-form-item>
              <el-form-item :label="formLabel.toStoreId.label" :label-width="formLabelWidth">
                <depot-select category="store" v-model="formData.toStoreId"  ></depot-select>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('storeAllotList.sure')}}</el-button>
        </div>
      </el-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('storeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="formatId" column-key="businessId"   :label="$t('storeAllotList.businessId')" sortable ></el-table-column>
        <el-table-column prop="fromStoreName" column-key="fromStoreId"  :label="$t('storeAllotList.fromStore')" sortable ></el-table-column>
        <el-table-column prop="toStoreName" column-key="toStoreId" :label="$t('storeAllotList.toStore')" sortable ></el-table-column>
        <el-table-column prop="outCode" :label="$t('storeAllotList.outCode')" width="120" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('storeAllotList.status')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy"  :label="$t('storeAllotList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('storeAllotList.createdDate')"  sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" column-key="lastModifiedBy" :label="$t('storeAllotList.lastModifiedBy')"  sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('storeAllotList.lastModifiedDate')" sortable ></el-table-column>
        <el-table-column prop="remarks" :label="$t('storeAllotList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('storeAllotList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:storeAllot:view'"><el-button   size="small"  @click.native="itemAction(scope.row.id, 'view')">{{$t('storeAllotList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '待发货' || scope.row.status === '发货中'" v-permit="'crm:storeAllot:ship'" ><el-button size="small" @click.native="itemAction(scope.row.id,'ship')">{{$t('storeAllotList.ship')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '待发货'"  v-permit="'crm:storeAllot:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')"> {{$t('storeAllotList.delete')}}</el-button></div>
            <div class="action" v-permit="'crm:storeAllot:ship'"><el-button :style="stypeOfPrintBtn(scope.row.isPrint)" size="small" @click.native="itemAction(scope.row.id,'print')">{{$t('storeAllotList.print')}}</el-button></div>
            <div class="action" v-permit="'crm:storeAllot:ship'"><el-button :style="stypeOfShipPrintBtn(scope.row.isShipPrint)" size="small" @click.native="itemAction(scope.row.id, 'shipPrint')">{{$t('storeAllotList.shipPrint')}}</el-button></div>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          outCode:"",
          businessIds:'',
          status:'',
          createdDateRange:"",
          remarks:"",
          toStoreId:"",
          fromStoreId:"",
          createdBy:"",
        },formLabel:{
          outCode:{label:this.$t('storeAllotList.outCode')},
          businessIds:{label:this.$t('storeAllotList.businessId')},
          status:{label:this.$t('storeAllotList.status')},
          remarks:{label:this.$t('storeAllotList.remarks')},
          fromStoreId:{label:this.$t('storeAllotList.fromStore')},
          toStoreId:{label:this.$t('storeAllotList.toStore')},
          createdDateRange:{label:this.$t('storeAllotList.createdDate')},
          createdBy:{label:this.$t('storeAllotList.createdBy')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        multipleSelection:[],
      };
    },
    methods: {

      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("storeAllotList",this.submitData);
        axios.get('/api/ws/future/crm/storeAllot',{params:this.submitData}).then((response) => {
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
          axios.get('/api/ws/future/crm/storeAllot/export',{params:this.submitData}).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});

      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'storeAllotForm', query: { id: id }});
        }else if(action==="view"){
          this.$router.push({ name: 'storeAllotDetail', query: { id: id }})
        }else if(action==="ship"){
          this.$router.push({ name: 'storeAllotShip', query: { id: id }});
        }else if(action==="print"){
        }else if(action==="shipPrint"){
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

      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/storeAllot/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

