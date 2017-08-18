<template>
    <div>
        <head-tab active="index"></head-tab>
        <el-row>
            <el-col :span="18"></el-col>
            <el-col :span="6">
                <el-card class="box-card margin" v-if="account.admin">
                    <div slot="header" class="clearfix">
                        <span style="font-size:20px">数据同步</span>
                    </div>
                    <table class="table">
                        <tbody >
                        <el-button type="primary"  @click="synFlush">同步</el-button>
                        </tbody>
                    </table>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    import { mapState } from 'vuex'
    export default {

        data() {
            return {
            };
        },
        computed: mapState({
            account: state => state.global.account,
        }),
        methods: {
            synFlush(){
                axios.get('/api/basic/sys/cache/init').then((response) =>{
                    this.$message(response.data.message);
                })
                axios.get('/api/ws/future/basic/cache/init').then((response) =>{
                    this.$message(response.data.message);
                })
                axios.get('/api/general/sys/cache/init').then((response) =>{
                    this.$message(response.data.message);
                })
            }
        },created () {
        },activated(){
        }
    };
</script>

