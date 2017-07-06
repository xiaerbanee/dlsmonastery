<template>
  <div>
    <head-tab active="recruitList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhone:edit'">{{$t('recruitList.add')}}</el-button>
        <el-button type="primary" @click="batchEdit" icon="edit" v-permit="'crm:demoPhone:edit'">{{$t('recruitList.batchEdit')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:demoPhone:view'">{{$t('recruitList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible = false" :title="$t('recruitList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('recruitList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"   :element-loading-text="$t('recruitList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" ></el-table-column>
        <el-table-column  prop="name"  :label="$t('recruitList.name')"></el-table-column>
        <el-table-column prop="sex" :label="$t('recruitList.sex')"></el-table-column>
        <el-table-column prop="education" :label="$t('recruitList.education')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('recruitList.mobilePhone')"></el-table-column>
        <el-table-column prop="firstAppointDate" :label="$t('recruitList.firstAppointDate')"></el-table-column>
        <el-table-column prop="secondAppointDate" :label="$t('recruitList.secondAppointDate')"></el-table-column>
        <el-table-column prop="physicalAppointDate" :label="$t('recruitList.physicalAppointDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressOrderList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>             <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import SearchDialog from "../../../components/common/search-dialog.vue";

  export default {
    components: {SearchDialog},
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
        },submitData:{
          page:0,
          size:25,
        },formLabel:{
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        selects:new Array(),
        pickerDateOption:util.pickerDateOption
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("recruitList",this.submitData);
        util.copyValue(this.formData,this.submitData)
        axios.get('/api/basic/hr/recruit?'+qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'recruitForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'recruitForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/hr/recruit/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchEdit(){
        if(this.selects.length>1){
          this.$router.push({ name: 'recruitBatchForm', query: { ids: this.selects }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -325;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>


