<template>
  <div>
    <head-tab active="officeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:office:edit'">{{$t('officeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'sys:office:view'">{{$t('officeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('officeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('officeList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('officeList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('officeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('officeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="parentName" :label="$t('officeList.parentName')" sortable></el-table-column>
        <el-table-column prop="name" :label="$t('officeList.name')" ></el-table-column>
        <el-table-column prop="officeRuleName" label="业务类型" ></el-table-column>
        <el-table-column prop="type" label="类型">
          <template scope="scope">
            {{scope.row.type}}
          </template>
        </el-table-column>
        <el-table-column prop="locked" :label="$t('officeList.locked')">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('officeList.enabled')">
          <template scope="scope">
            <el-tag :type="scope.row.enabled? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="point" :label="$t('officeList.point')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('officeList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('officeList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:office:edit'">{{$t('officeList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:office:delete'">{{$t('officeList.delete')}}</el-button>
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
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
        formVisible: false,
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("officeList",submitData);
        axios.get('/api/basic/sys/office?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        if(column.prop=="parentName"){
           column.sort="parentId"
        }
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'officeForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'officeForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/office/delete',{params:{id:id}}).then((response) =>{
            if(response.data.success){
              this.$message(response.data.message);
            }else {
              tthis.$message.error(response.data.message);
            }
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
        var that=this;
        that.pageHeight = window.outerHeight -320;
        this.initPromise=axios.get('/api/basic/sys/office/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
    });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

