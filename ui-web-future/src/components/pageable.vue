<template>
  <div style="margin-top:10px;text-align:right;">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 25, 50, 100]"
        :page-size="page.size"
        :current-page="page.number+1"
        layout="sizes, prev, pager, next, jumper, total"
        :total="page.totalElements">
    </el-pagination>
  </div>
</template>
<script>
export default {
  props: ['page'],
  data() {
    return {
    };
  },methods: {
    handleSizeChange (val) {
      this.$emit('pageChange',0,val);
    },handleCurrentChange(val) {
      if(this.page.page != val - 1) {
        this.$emit('pageChange',val-1,this.page.size);
      }
    }
 }
};
</script>
