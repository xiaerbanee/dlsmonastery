<template>
  <div>
    <head-tab active="priceChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:priceChange:edit'">{{$t('priceChangeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:priceChange:view'">{{$t('priceChangeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('priceChangeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData"  :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('priceChangeList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('priceChangeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('priceChangeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('priceChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="name"  :label="$t('priceChangeList.name')" sortable width="200"></el-table-column>
        <el-table-column prop="productTypeName" :label="$t('priceChangeList.productType')"width="500"></el-table-column>
        <el-table-column prop="checkPercent" :label="$t('priceChangeList.checkPercent')"></el-table-column>
        <el-table-column prop="priceChangeDate" :label="$t('priceChangeList.priceChangeDate')"></el-table-column>
        <el-table-column prop="uploadEndDate"  :label="$t('priceChangeList.uploadEndDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('priceChangeList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('priceChangeList.createdBy')"></el-table-column>
        <el-table-column prop="status" :label="$t('priceChangeList.status')"></el-table-column>
        <el-table-column  :label="$t('priceChangeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:priceChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('priceChangeList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:priceChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('priceChangeList.delete')}}</el-button></div>
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
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '28%',
        formVisible: false,
        loading:false
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
        util.setQuery("priceChangeList",submitData);
        axios.get('/api/ws/future/crm/priceChange',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'priceChangeForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'priceChangeForm', query: { id: id}})
        } else if(action=="delete") {
            util.confirmBeforeDelRecord(this).then(() => {
              axios.get('/api/ws/future/crm/priceChange/delete',{params:{id:id}}).then((response) =>{
                this.$message(response.data.message);
                this.pageRequest();
              })
            }).catch(()=>{

            });
        }
      }
    },created () {
         this.pageHeight = 0.75*window.innerHeight;
        this.initPromise=axios.get('/api/ws/future/crm/priceChange/getQuery').then((response) =>{
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

