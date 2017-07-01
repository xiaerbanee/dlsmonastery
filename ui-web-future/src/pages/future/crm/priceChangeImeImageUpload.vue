<template>
  <div>
    <head-tab active="priceChangeImeImageUpload"></head-tab>
    <div class="header">
      <label>图片上传</label>
    </div>
    <div class="outer">
      <div class="uploadBox">
        <el-upload
          class="upload-demo"
          drag
          action="#"
          multiple
          :file-list="imageList"
          :on-change="changeHandler"
          :on-remove="removeHandler"
          :auto-upload="false"
          list-type="picture"
          ref="imgUploader">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text ">将文件拖到此处，或<em>点击上传</em></div>
          <div  class="el-upload__tip numTips" slot="tip">已选中{{imageList.length}}张,共{{imgListSize}}KB</div>
        </el-upload>
        <el-button v-if="imageList.length" style="margin-left: 10px;" size="small" type="success" @click="submitUpload" class="trigger">上传到服务器</el-button>
      </div>
    </div>
  </div>
</template>
<script>
  import $util from '../../../utils/util.js'
  export default {
    data(){
      return {
        imageList:[],
        imgListSize:0
      }
    },
    methods:{
      changeHandler(file,fileList){
          this.imageList = fileList;
          this.imgListSize = $util.getImgListSize(fileList);
      },
      removeHandler(file,fileList){
        this.imageList = fileList;
        this.imgListSize = $util.getImgListSize(fileList);
      },
      submitUpload(){
        var uploader = this.$refs.imgUploader;
      }
    }
  }
</script>
<style>
  .header{
    text-align: center;
    margin-bottom: 15px;
    width: 280px;
    height: 40px;
    line-height: 40px;
    background: lightblue;
    margin: 0 auto;
    border-radius: 10px;
    margin-bottom: 10px;
  }
  .outer{
    width: 1000px;
    border:2px solid #ddd;
    box-shadow: 0 0 10px 0 #ccc;
    margin: 0 auto;
    border-radius: 10px;
  }
  /*.outer:after{
    content: '';
    display: block;
    clear: both;
  }*/
  label{
    font-size:26px;
    font-family: '微软雅黑';
  }
  .uploadBox{
    padding-bottom: 10px;
  }
  .el-upload{
    width: 100%;
  }
  .el-upload-dragger{
    padding-top: 60px;
    width: 100%;
    height: 270px;
    border:none;
    border-bottom: 2px solid #ddd;
    border-radius: 0;
  }
  .el-upload-dragger:hover{
   border-color:#ddd;
  }
  .el-upload-dragger .el-upload__text{
    font-size: 20px;
  }
  .el-upload__tip,.numTips{
    margin-left: 10px;
  }
  .el-upload-list--picture{
    max-height: 400px;
    overflow: auto;
    padding: 5px 10px;
  }
  .el-upload-list--picture .el-upload-list__item{
    width: 45%;
    display: inline-block;
    margin-left: 30px;
  }
  .trigger{
    margin-top: 5px;
  }
</style>
