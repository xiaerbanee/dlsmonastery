<template>
  <div>
    <head-tab active="pricesystemChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:pricesystemChange:edit'">{{$t('pricesystemChangeList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:pricesystemChange:edit'">{{$t('pricesystemChangeList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:pricesystemChange:view'">{{$t('pricesystemChangeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('pricesystemChangeList.filter')" v-model="formVisible" size="tiny" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('pricesystemChangeList.productName')" :label-width="formLabelWidth">
                <product-select v-model="formData.productId" @afterInit="setSearchText"></product-select>
              </el-form-item>
              <el-form-item :label="$t('pricesystemChangeList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('pricesystemChangeList.status')" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('pricesystemChangeList.inputKey')">
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('pricesystemChangeList.pricesystemName')" :label-width="formLabelWidth">
                <el-select v-model="formData.pricesystemId" filterable clearable :placeholder="$t('pricesystemChangeList.inputKey')">
                  <el-option v-for="pricesystem in formData.extra.pricesystems" :key="pricesystem.name" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('pricesystemChangeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('pricesystemChangeList.loading')"  @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column column-key="productId" prop="productName" :label="$t('pricesystemChangeList.productName')" width="150" sortable></el-table-column>
        <el-table-column column-key="pricesystemId" prop="pricesystemName" :label="$t('pricesystemChangeList.pricesystemName')" sortable></el-table-column>
        <el-table-column prop="oldPrice" :label="$t('pricesystemChangeList.oldPrice')"></el-table-column>
        <el-table-column prop="newPrice" :label="$t('pricesystemChangeList.newPrice')"></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('pricesystemChangeList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('pricesystemChangeList.createdDate')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('pricesystemChangeList.status')" width="120" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('pricesystemChangeList.operation')" width="160">
          <template scope="scope">
            <div class="action" v-if="scope.row.status == '申请中'" v-permit="'crm:pricesystemChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'pass')">{{$t('pricesystemChangeList.pass')}}</el-button></div>
            <div class="action" v-if="scope.row.status == '申请中'" v-permit="'crm:pricesystemChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'notPass')">{{$t('pricesystemChangeList.noPass')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>

<script>
  import productSelect from 'components/future/product-select'
  export default {
    components:{
      productSelect
    },
    data() {
      return {
        pageHeight:600,
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        selects:[],
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
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
        util.copyValue(this.formData,submitData);
        util.setQuery("pricesystemChangeList",submitData);
        axios.get('/api/ws/future/crm/pricesystemChange',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'pricesystemChangeForm'})
      },itemAction:function(id,action){
        axios.get('/api/ws/future/crm/pricesystemChange/audit',{params:{id:id,pass:action=='pass'?true:false}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },selectionChange(selection){
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchPass(){
        if(!this.selects || this.selects.length < 1){
          this.$message(this.$t('pricesystemChangeList.noSelectionFound'));
          return ;
        }
        util.confirmBeforeBatchPass(this).then(() => {
          axios.get('/api/ws/future/crm/pricesystemChange/batchAudit', {
            params: {
              ids: this.selects,
              pass: true
            }
          }).then((response) => {
            this.$message(response.data.message);
            this.pageRequest();
          });
        });
      },checkSelectable(row) {
        return row.status =='申请中';
      },
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/pricesystemChange/getQuery').then((response) =>{
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

