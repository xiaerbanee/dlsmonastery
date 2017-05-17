<template>
  <div style="margin-top:10px;text-align:right;">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 25, 50, 100]"
        :page-size="page.size"
        :current-page="page.number+1"
        :layout="innerLayout"
        :total="page.totalElements">
    </el-pagination>
  </div>
</template>
<script>
export default {
  props: ['page','layout'],
  data() {
    return {
      innerLayout:null
    };
  },methods: {
    handleSizeChange (val) {
      this.$emit('pageChange',0,val);
    },handleCurrentChange(val) {
      if(this.page.number !== undefined && this.page.number != val - 1) {
        this.$emit('pageChange',val-1,this.page.size);
      }
    }
 },created () {
    if(this.layout==null || this.layout=="") {
      this.innerLayout ="sizes, prev, pager, next, jumper, total";
    } else {
        this.innerLayout = this.layout;
    }
  }
};
</script>
