<template>
  <div class="wrapper" v-show="show" @mousewheel="scale" @click="hide">
    <div class="imgWrapper" ref="imgWrapper">
      <img ref="img" width="100%" :src="src" alt="">
    </div>
    <div class="btnGroup">
      <div class="tips">提示：滚动鼠标可进行放大缩小，点击阴影处关闭。</div>
      <el-button type="success" @click="rotateLeft">向左旋转90°</el-button>
      <el-button type="success" @click="rotateRight">向右旋转90°</el-button>
    </div>
  </div>
</template>
<style>
  .wrapper{
    width: 100%;
    background:rgba(0,0,0,.3) ;
    position: fixed;
    left:0;
    right:0;
    top:0;
    bottom:0;
    z-index:2015;
  }
  .imgWrapper{
    width:30% ;
    margin:8% auto;
  }
  .btnGroup{
    position: fixed;
    bottom:10px;
    right:20px;
  }
  .btnGroup{
    width: 150px;
  }
  .btnGroup button{
    display: block;
    margin-top: 5px;
  }
  .btnGroup .el-button+.el-button{
    margin-left: 0;
  }
  .tips{
    color:#fff;
  }
</style>
<script>
  export default {
    props: {
      show: {
        type: Boolean,
        required: true
      },
      src:{
        type:String,
        required: true
      }
    },
    data(){
      return{
        deg:0,
        scales:1,
      }
    },
    methods:{
      hide(e){
        if(e.target.nodeName === "DIV"){
          this.$emit('close')
          this.$refs.img.style='';
        }
      },
      rotateLeft(){
        this.deg -= 90;
        this.$refs.img.style.transform = `rotate(${this.deg}deg)`
      },
      rotateRight(){
        this.deg += 90;
        this.$refs.img.style.transform = `rotate(${this.deg}deg)`
      },
      scaleBigger(){
        this.scales += 0.1;
        this.$refs.img.style.transform = `scale(${this.scales},${this.scales})`;
      },
      scaleSmaller(){
        this.scales -= 0.1;
        this.$refs.img.style.transform = `scale(${this.scales},${this.scales})`;
      },
      scale(e){
        if(e.deltaY>0){
          this.scaleBigger()
        }else{
          this.scaleSmaller()
        }
      }
    },
    watch: {
      show(show) {
        show && this.$nextTick(() => {
          this.$el.focus();
        })
      }
    }
  }
</script>
