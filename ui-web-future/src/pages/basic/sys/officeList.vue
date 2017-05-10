<template>
  <div>
    <head-tab active="officeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:office:edit'">{{$t('officeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:office:view'">{{$t('officeList.filter')}}</el-button>
        <search-tag  :formData="submitData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('officeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('officeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('officeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('officeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="parentName" :label="$t('officeList.parentName')" sortable></el-table-column>
        <el-table-column prop="name" :label="$t('officeList.name')" ></el-table-column>
        <el-table-column prop="officeRuleName" label="业务类型" ></el-table-column>
        <el-table-column prop="type" label="类型">
          <template scope="scope">
            {{$t('OfficeRuleEnum.'+ scope.row.type)}}
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
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('officeList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('officeList.delete')}}</el-button>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:''
        },formLabel:{
          name:{label:this.$t('officeList.name')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("officeList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/office?'+qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        if(column.prop=="parent.name"){
           column.sort="parentId"
        }
        this.formData.order=util.getOrder(column);
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
          axios.get('/api/basic/sys/office/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.formData=this.submitData;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

