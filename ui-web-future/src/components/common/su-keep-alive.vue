<script>


  export default {
    name: 'su-keep-alive',
    abstract: true,



    methods: {



  getFirstComponentChild (children) {
        if (Array.isArray(children)) {
          for (var i = 0; i < children.length; i++) {
            var c = children[i];
            if (this.isDef(c) && this.isDef(c.componentOptions)) {
              return c
            }
          }
        }
      },
      isDef (v) {
        return v !== undefined && v !== null
      },
      pruneCacheEntry (vnode) {
        if (vnode) {
          vnode.componentInstance.$destroy();
        }
      },
    },
    created () {
      this.cache = Object.create(null)
    },
    destroyed () {
      for (const key in this.cache) {
        this.pruneCacheEntry(this.cache[key])
      }
    },
    render: function render () {

      var vnode = this.getFirstComponentChild(this.$slots.default);
      var componentOptions = vnode && vnode.componentOptions;
      if (componentOptions) {
        var key = vnode.key == null
          // same constructor may get registered as different local components
          // so cid alone is not enough (#3269)
          ? componentOptions.Ctor.cid + (componentOptions.tag ? ("::" + (componentOptions.tag)) : '')
          : vnode.key;
        if (this.cache[key] && this.$store.state.global.keep) {
          vnode.componentInstance = this.cache[key].componentInstance;

        } else {

          this.cache[key] = vnode;
        }
        this.$store.dispatch("setKeep", false);
        vnode.data.keepAlive = true;
      }

      return vnode
    }
  }
</script>
