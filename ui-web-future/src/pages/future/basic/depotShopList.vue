<template>
  <div>
    <head-tab active="depotShopList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加门店属性</el-button>
        <el-button type="primary" @click="itemAddDepot" icon="plus" >添加门店</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData">
          <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
            <el-input v-model="formData.name" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">过滤</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="depotName" label="门店名称" sortable width="120"></el-table-column>
        <el-table-column prop="areaType" label="地区属性"  />
        <el-table-column prop="carrierType" label="运营商营业厅类型"  />
        <el-table-column prop="turnoverType" label="营业额分类"  />
        <el-table-column prop="channelType" label="运营商类型" />
        <el-table-column prop="salePointType" label="门店类型"  />
        <el-table-column prop="townType" label="乡镇类型"  />
        <el-table-column prop="shopArea" label="面积大小"  />
        <el-table-column prop="remarks" label="备注"></el-table-column>
        <el-table-column fixed="right" label="操作" width="140">
          <template scope="scope">
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'edit')">修改</el-button>
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>
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
          name:"",
        },formLabel:{
          name:{label:"名称"},
        },
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
        util.setQuery("depotShopList",this.formData);
        console.log(this.formData)
        axios.get('/api/ws/future/basic/depotShop',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'depotShopForm'})
      },itemAddDepot(){
        this.$router.push({ name: 'shopForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'depotShopForm', query: { id: id }})
        }else if(action=="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/depotShop/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          });
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      console.log(this.formData)
      this.pageRequest();
    }
  };
</script>

