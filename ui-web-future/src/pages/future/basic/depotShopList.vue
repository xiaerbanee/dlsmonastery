<template>
  <div>
    <head-tab active="depotShopList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加m'd</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData">

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
        <el-table-column prop="businessType" label="经营方式"  />
        <el-table-column prop="channelType" label="运营商类型" />
        <el-table-column prop="salePointType" label="门店类型"  />
        <el-table-column prop="townType" label="乡镇类型"  />
        <el-table-column prop="shopArea" label="面积大小"  />
        <el-table-column prop="remarks" label="备注"></el-table-column>
        <el-table-column fixed="right" label="操作" width="140">
          <template scope="scope">
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
        },formLabel:{
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
        util.setQuery("productList",this.formData);
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
      this.pageRequest();
    }
  };
</script>

