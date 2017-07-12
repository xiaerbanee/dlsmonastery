<template>
  <div>
    <head-tab active="depotChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:depotChange:edit'">{{$t('depotChangeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:depotChange:view'">{{$t('depotChangeList.filterOrExport')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('depotChangeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('depotChangeList.depotName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.depotId" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>
               <el-form-item :label="$t('depotChangeList.type')" :label-width="formLabelWidth">
                 <el-select v-model="formData.type" clearable filterable :placeholder="$t('depotChangeList.selectGroup')">
                   <el-option v-for="type in formData.extra.types" :key="type" :label="type" :value="type"></el-option>
                 </el-select>
               </el-form-item>
              <el-form-item :label="$t('depotChangeList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData" v-permit="'crm:depotChange:view'">{{$t('depotChangeList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('depotChangeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('depotChangeList.unicode')" sortable width="100"></el-table-column>
        <el-table-column prop="type" :label="$t('depotChangeList.type')" sortable width="120"></el-table-column>
        <el-table-column column-key="depotId" prop="depotName" :label="$t('depotChangeList.depotName')" sortable></el-table-column>
        <el-table-column prop="expiryDate" :label="$t('depotChangeList.expiryDate')"sortable width="120"></el-table-column>
        <el-table-column prop="oldValue" :label="$t('depotChangeList.oldValue')" sortable></el-table-column>
        <el-table-column prop="newValue" :label="$t('depotChangeList.newValue')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('depotChangeList.processStatus')" scope="scope" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('depotChangeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:depotChange:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('depotChangeList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.status =='申请中'" v-permit="'crm:depotChange:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('depotChangeList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.status =='申请中'" v-permit="'crm:depotChange:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('depotChangeList.delete')}}</el-button></div>
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
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        pageHeight:600,
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
        util.setQuery("depotChangeList",submitData);
        axios.get('/api/ws/future/crm/depotChange',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'depotChangeForm'})
      },exportData(){
        axios.get('/api/ws/future/crm/depotChange/export?'+qs.stringify(util.deleteExtra(this.formData))).then((response)=> {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
      },itemAction:function(id,action){
        if(action=="detail") {
          this.$router.push({ name: 'depotChangeDetail', query: { id: id,action:"detail" }})
      } else if(action=="audit"){
          this.$router.push({ name: 'depotChangeDetail', query: { id: id,action:"audit" }})
      }else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/depotChange/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
         this.pageHeight = 0.75*window.innerHeight;
        util.copyValue(this.$route.query,this.formData);
        this.initPromise = axios.get('/api/ws/future/crm/depotChange/getQuery').then((response) =>{
        this.formData=response.data;
      });
    },activated(){
        this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

