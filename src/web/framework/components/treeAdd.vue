<template>
  <section class="tree-container">
    <slot name="treeTitle"></slot>
    <el-tree
      ref="treeAdd"
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
    props:["treeData"],
    methods:{
      checkChange(){
        this.$store.dispatch("treeChange",{
          type:"ADD",
          data:this.$refs.treeAdd.getCheckedKeys()
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
        handler:function(val,old){
          this.$refs.treeAdd.setCheckedKeys(val.menus);
        },
        deep:true
      }
    },
    create(){

    }
  }
</script>
