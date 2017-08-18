<template>
	<el-row class="container">
		<div v-title="'业务-'+account.companyName"></div>
		<el-col :span="24" class="header">
			<el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
				{{collapsed?'':sysName}}
			</el-col>
			<el-col :span="10">
				<el-row>
					<el-col :span="2">
						<div class="tools" @click.prevent="collapse">
							<i class="fa fa-align-justify"></i>
						</div>
					</el-col>
					<el-col :span="22">
						<el-menu theme="light"  class="menus" mode="horizontal" >
							<el-menu-item :index="index+''" :key="item.code" v-for="(item,index) in backend.backendModuleList"  :data-code="item.code" @click.native="changeModule">{{item.name}}</el-menu-item>
						</el-menu>
					</el-col>
				</el-row>
			</el-col>
			<el-col :span="4" class="userinfo">
				<span class="inter"><a href="javscript:void(0);" @click="changeLang('zh-cn')">中文</a> / <a href="javscript:void(0);" @click="changeLang('id')">Indonesia</a></span>
				<el-dropdown trigger="hover">
					<span class="el-dropdown-link userinfo-inner"><img :src="this.sysUserAvatar" /> {{account.loginName}}</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item>我的消息</el-dropdown-item>
						<el-dropdown-item>设置</el-dropdown-item>
						<el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</el-col>
		</el-col>
		<el-col :span="24" class="main">
			<aside :class="collapsed?'menu-collapsed':'menu-expanded'">
				<!--导航菜单-未折叠-->
				<el-menu :default-active="$route.path" class="el-menu-vertical-demo" unique-opened  v-show="!collapsed">
					<template v-for="(module,i) in backend.backendModuleList" v-if="module.code == activeModule">
						<template v-for="(item,index) in module.menuCategoryList">
							<el-submenu :index="index+''">
								<template slot="title"><i :class="'fa fa-'+module.icon"></i>{{item.name}}</template>
								<el-menu-item v-for="(menu,index) in item.menuList" :index="menu.code" :key="menu.code" :data-code="menu.code" :class="code === menu.code?'is-active':''"   @click="jump(menu,$event)">{{menu.name}}</el-menu-item>
							</el-submenu>
						</template>
					</template>
				</el-menu>
				<!--导航菜单-折叠后-->
				<ul  class="el-menu el-menu-vertical-demo collapsed" v-show="collapsed" ref="menuCollapsed">
					<template v-for="modules in backend.backendModuleList" v-if="modules.code == activeModule">
						<li v-for="(module,i) in modules.menuCategoryList"  class="el-submenu item">
							<template >
								<div class="el-submenu__title" style="padding-left: 20px;" @mouseover="showMenu(i,true)" @mouseout="showMenu(i,false)"><i :class="'fa fa-'+module.icon"></i></div>
								<ul class="el-menu submenu" :class="'submenu-hook-'+i" @mouseover="showMenu(i,true)" @mouseout="showMenu(i,false)">
									<li v-for="menu in module.menuList"  :key="menu.code" class="el-menu-item" style="padding-left: 40px;" :class="$route.path==menu.path?'is-active':''" @click="$router.push({name:menu.code})">{{menu.name}}</li>
								</ul>
							</template>
						</li>
					</template>
				</ul>
			</aside>
			<section class="content-container">
				<div class="grid-content bg-purple-light">
					<el-col :span="24" class="content-wrapper">
						<su-keep-alive>
							<router-view ></router-view>
						</su-keep-alive>
					</el-col>
				</div>
			</section>
		</el-col>
	</el-row>
</template>

<script>
    import Vue from 'vue';
    import suKeepAlive from 'components/common/su-keep-alive.vue';
    import { mapState } from 'vuex'
	import img from '../assets/user.png'
	export default {
        components:{
            suKeepAlive,
        },
		data() {
			return {
                code:'',
			    backend:{},
                activeModule:'',
				sysName:'BUSINESS',
				collapsed:true,
				sysUserName: '',
				sysUserAvatar: img
			}
		},computed: mapState({
            account: state => state.global.account,
            menus: state => state.global.menus,
            lang: state => state.global.lang,
        }),
		methods: {
            jump(m,e){
                this.code = e.index;
                this.$router.push({name:e.index})
			},
			//退出登录
            logout() {
			    var that = this;
                that.$confirm('确认退出吗?', '提示', {
                }).then(() => {
                    that.$store.dispatch('clearGlobal');
                    axios.post('/user/logout').then(()=>{
                        axios.post('/logout').then(()=>{
                            that.$router.push({path: "/login"});
                        }).catch(function () {
                        });
                    }).catch(function () {
                    });
                }).catch(() => {});
            },
				changeLang(lang) {
					this.$store.dispatch('setLang',lang);
					Vue.config.lang = lang;
					this.$router.push({ name: "index"});
				},
			//折叠导航栏
			collapse:function(){
				this.collapsed=!this.collapsed;
			},
			showMenu(i,status){
				this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-'+i)[0].style.display=status?'block':'none';
			},changeModule(event) {
                this.activeModule=event.target.dataset.code;
            }
		},
		mounted() {

			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.sysUserName = user.name || '';
				this.sysUserAvatar = user.avatar || '';
			}

		},created() {
		    var that = this;
            axios.get('/user/isLogin').then((response)=>{
                if(response.data) {
                    //初始化菜单
                    if(this.menus.length != 0) {
                        for (var i in this.menus) {
                            var backend= this.menus[i];
                            if(backend.code=="businessManager") {
                                this.backend = backend;
                                this.activeModule = backend.backendModuleList[0].code;
                            }
                        }
                        that.$router.push({path: "/index"});
                        that.$store.dispatch('setTabs',new Map());

                    }
                } else {
                    that.$store.dispatch('clearGlobal');
                    that.$router.push({path: "/login"});
                }
            });
		}
	}

</script>

<style scoped lang="scss">
	@import '~scss_vars';
	
	.container {
		position: absolute;
		top: 0px;
		bottom: 0px;
		width: 100%;
		aside,.logo{
			transition: .1s ease;
		}
		.header {
			height: 60px;
			line-height: 60px;
			background: $color-primary;
			color:#fff;
			.userinfo {
				text-align: right;
				padding-right: 35px;
				float: right;
				.userinfo-inner {
					cursor: pointer;
					color:#fff;
					img {
						width: 40px;
						height: 40px;
						border-radius: 20px;
						margin: 10px 0px 10px 10px;
						float: right;
					}
				}
			}
			.logo {
				//width:230px;
				height:60px;
				font-size: 22px;
				padding-left:20px;
				padding-right:20px;
				border-color: rgba(238,241,146,0.3);
				border-right-width: 1px;
				border-right-style: solid;
				img {
					width: 40px;
					float: left;
					margin: 10px 10px 10px 18px;
				}
				.txt {
					color:#fff;
				}
			}
			.logo-width{
				width:230px;
			}
			.logo-collapse-width{
				width:60px
			}
			.tools{
				padding: 0px 23px;
				width:14px;
				height: 60px;
				line-height: 60px;
				cursor: pointer;
			}
		}
		.main {
			display: flex;
			// background: #324057;
			position: absolute;
			top: 60px;
			bottom: 0px;
			overflow: hidden;
			aside {
				flex:0 0 230px;
				width: 230px;
				// position: absolute;
				// top: 0px;
				// bottom: 0px;
				.el-menu{
					height: 100%;
				}
				.el-menu-vertical-demo {
					width:100%!important;
				}
				.collapsed{
					width:60px;
					.item{
						position: relative;
					}
					.submenu{
						position:absolute;
						top:0px;
						left:60px;
						z-index:99999;
						height:auto;
						display:none;
					}

				}
			}
			.menu-collapsed{
				flex:0 0 60px;
				width: 60px;
			}
			.menu-expanded{
				flex:0 0 230px;
				width: 230px;
			}
			.content-container {
				// background: #f1f2f7;
				flex:1;
				// position: absolute;
				// right: 0px;
				// top: 0px;
				// bottom: 0px;
				// left: 230px;
				overflow: auto;
				padding: 20px;
				.breadcrumb-container {
					//margin-bottom: 15px;
					.title {
						width: 200px;
						float: left;
						color: #475669;
					}
					.breadcrumb-inner {
						float: right;
					}
				}
				.content-wrapper {
					background-color: #fff;
					box-sizing: border-box;
				}
			}
		}
	}
	.el-menu--horizontal.menus{
		background: transparent;
		.el-menu-item{
			color:#fff;
		}
		.el-menu-item:hover{
			background: none;
			color:#184E7E;
			border-bottom: 5px solid #184E7E;
		}
		.el-menu-item.is-active{
			color:#184E7E;
		}
	}
	.inter a{
		color:#fff;
		text-decoration: none;
	}
</style>