<template>
  <section class="tree-container">
    <slot name="treeTitle"></slot>
    <el-tree
      ref="treeChange"
      class="tree-common scrollbar"
      :data="treeData"
      show-checkbox
      node-key="id"
      default-expand-all
      @check-change="checkChange"
      :props="defaultProps">
    </el-tree>
  </section>
</template>

<<style lang="sass" type="text/css">
  @import "framework/scss/tree";
</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  export default {
    props:["treeData","treeCheckData"],
    computed: {

    },
    methods:{
      checkChange(){
        this.$store.dispatch("treeChange",{
          type:"CHANGE",
          data:this.$refs.treeChange.getCheckedKeys()
        })
      }
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    watch:{
      treeCheckData:{
        handler:function(val){
          this.$refs.treeChange.setCheckedKeys(val);
        },
        deep:true
      }
    },
    create(){

    }
  }
</script>
