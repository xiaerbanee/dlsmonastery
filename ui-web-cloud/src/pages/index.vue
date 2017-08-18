<template>
    <div>
        <head-tab active="index"></head-tab>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span style="line-height: 36px;">公告栏</span>
            </div>
            <div  class="text item">
                * 欢迎使用金蝶辅助工具系统！我是强大金蝶财务系统的一个扩展小系统，可以简化你在金蝶系统繁琐操作
            </div>
            <div  class="text item">
                * 您可以称我为 “金蝶工具系统” 或者 “cloud系统”
            </div>
            <div  class="text item">
                * 如果系统出现问题可以在 cloud群里（161226826）反馈问题，我们会有强大团队人员为你服务。
            </div>
        </el-card>
    </div>
</template>
<script>
    export default {
        data() {
            return {
            };
        },
        methods: {
        },created () {
        },activated(){
        }
    };
</script>
<style>
    .text {
        font-size: 14px;
    }

    .item {
        padding: 18px 0;
    }
    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
    .box-card {
        width: 480px;
        margin: 20px;
    }
</style>

