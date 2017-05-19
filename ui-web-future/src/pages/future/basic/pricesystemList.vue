<template>
  <div>
    <head-tab active="pricesystemList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:pricesystem:edit'">{{$t('pricesystemList.add')}}</el-button>
        <el-button type="primary" @click="toSee" icon="document" v-permit="'crm:pricesystem:view'">{{$t('pricesystemList.toSee')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:pricesystem:view'">{{$t('pricesystemList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('pricesystemList.filter')" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('pricesystemList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('pricesystemList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('pricesystemList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('pricesystemList.name')" sortable width="200"></el-table-column>
        <el-table-column prop="sort" :label="$t('pricesystemList.sort')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('pricesystemList.remarks')"></el-table-column>
        <el-table-column prop="enabled" :label="$t('pricesystemList.enabled')" >
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('pricesystemList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('pricesystemList.lastModifiedDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('pricesystemList.operation')" width="160">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:pricesystem:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('pricesystemList.edit')}}</el-button>
            <el-button size="small" v-permit="'crm:pricesystem:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('pricesystemList.delete')}}</el-button>
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
          page:0,
          size:25,
          name:'',
        },formLabel:{
          name:{label:this.$t('pricesystemList.name')},
          hasIme:{label:this.$t('pricesystemList.isIme')},
          code:{label:this.$t('pricesystemList.code')},
          allowBill:{label:this.$t('pricesystemList.alowBill')},
          productType:{label:this.$t('pricesystemList.productType')},
          allowOrder:{label:this.$t('pricesystemList.allowOrder')},
          outGroupName:{label:this.$t('pricesystemList.outGroupName')},
          netType:{label:this.$t('pricesystemList.netType')}
        },
        offices:[],
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("pricesystemList",this.formData);
        axios.get('/api/ws/future/crm/pricesystem',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'pricesystemForm'})
      },toSee(){
        this.$router.push({name:'pricesystemView'})
      },itemAction:function(id,action){
          if(action=="edit") {
            this.$router.push({ name: 'pricesystemForm', query: { id: id }})
          }else if(action == "delete"){
            util.confirmBeforeDelRecord(this).then(() => {
              axios.get('/api/ws/future/crm/pricesystem/delete', {params: {id: id}}).then((response) => {
                this.$message(response.data.message);
                this.pageRequest();
              })
            })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

