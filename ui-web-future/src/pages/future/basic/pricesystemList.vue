<template>
  <div>
    <head-tab active="pricesystemList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:pricesystem:edit'">{{$t('pricesystemList.add')}}</el-button>
        <!--<el-button type="primary" @click="toSee" icon="document" v-permit="'crm:pricesystem:view'">{{$t('pricesystemList.toSee')}}</el-button>-->
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:pricesystem:view'">{{$t('pricesystemList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('pricesystemList.filter')" v-model="formVisible"  size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="$t('pricesystemList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('pricesystemList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('pricesystemList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('pricesystemList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('pricesystemList.name')" sortable width="200"></el-table-column>
        <el-table-column prop="sort" :label="$t('pricesystemList.sort')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('pricesystemList.remarks')"></el-table-column>
        <el-table-column prop="enabled" :label="$t('pricesystemList.enabled')" >
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column column-key="lastModifiedBy" prop="lastModifiedByName" :label="$t('pricesystemList.lastModifiedBy')" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('pricesystemList.lastModifiedDate')" sortable></el-table-column>
        <el-table-column fixed="right" :label="$t('pricesystemList.operation')" width="160">
          <template scope="scope">
            <div class="action"  v-permit="'crm:pricesystem:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('pricesystemList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:pricesystem:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('pricesystemList.delete')}}</el-button></div>
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
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
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
        util.setQuery("pricesystemList",submitData);
        axios.get('/api/ws/future/basic/pricesystem',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'pricesystemForm'})
      },toSee(){
        /*this.$router.push({name:'pricesystemView'})*/
      },itemAction:function(id,action){
          if(action=="edit") {
            this.$router.push({ name: 'pricesystemForm', query: { id: id }})
          }else if(action == "delete"){
            util.confirmBeforeDelRecord(this).then(() => {
              axios.get('/api/ws/future/basic/pricesystem/delete', {params: {id: id}}).then((response) => {
                this.$message(response.data.message);
                this.pageRequest();
              })
            })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/pricesystem/getQuery').then((response) => {
          this.formData = response.data;
          util.copyValue(this.$route.query, this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

