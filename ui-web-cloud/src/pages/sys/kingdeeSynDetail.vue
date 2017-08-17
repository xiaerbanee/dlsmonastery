<template>
  <div>
    <head-tab active="kingdeeSynDetail"></head-tab>
    <div>
      <el-form :model="kingdeeSyn" ref="inputForm" label-width="150px">
        <el-row >
          <el-col :span="12">
            <el-form-item  :label="'单据类型'+' : '">
              {{kingdeeSyn.formId}}
            </el-form-item>
            <el-form-item :label="'内容'+' : '">
              {{kingdeeSyn.content}}
            </el-form-item>
            <el-form-item :label="'结果'+' : '">
              {{kingdeeSyn.result}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="'单据编码'+' : '">
              {{kingdeeSyn.billNo}}
            </el-form-item>
            <el-form-item :label="'单据分类'+' : '">
              {{kingdeeSyn.extendType}}
            </el-form-item>
            <el-form-item :label="'OA单据ID'+' : '">
              {{kingdeeSyn.extendId}}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        kingdeeSyn:{}
      }
    },
    methods:{
      initPage(){
        axios.get('/api/global/cloud/sys/kingdeeSyn/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.kingdeeSyn = response.data;
          console.log(this.kingdeeSyn)
        });
      }
    },created(){
    this.initPage();
    },activated () {
      if (!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
