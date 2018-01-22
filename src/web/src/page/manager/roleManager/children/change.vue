<template>
  <div id="changeRole">
    <el-row >
      <el-col :span="12">
        <el-row>
          <el-col :span="24">
            <div class="detail">
              <label class="explain">角色账号</label>
              <label class="result">
                <el-input  size="mini" :value="currentRole.name" name="name" :disabled="true"></el-input>
              </label>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="detail">
              <label class="explain">角色描述</label>
              <label class="result">
                <el-input placeholder="请输入角色描述" size="mini" :value="currentRole.desc" name="desc" @change.native="inputChange"></el-input>
              </label>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="12">
        <TreeChange v-bind:treeData="treeData" v-bind:treeCheckData="treeCheckData">
          <template slot="treeTitle">
            <label class="tree-title">角色关联菜单</label>
          </template>
        </TreeChange>
      </el-col>
    </el-row>
  </div>
</template>

<style type="text/css" lang="scss">
  @import "framework/scss/detail"
</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Util from "framework/util/util"
  import TreeChange from "framework/components/treeChange"

  export default {
    data() {
      return {
        namespace: "roleManager"
      }
    },
    components: {
      TreeChange
    },
    computed: {
      ...mapGetters("roleManager", {
        treeData: 'treeData',
        treeCheckData:"treeCheckData",
        currentRole: "currentRole",
      })
    },
    methods: {
      inputChange(e) {
        let srcElement = e.srcElement || e.target, name = srcElement.getAttribute("name"), value = srcElement.value;
        this.$store.dispatch(this.namespace + '/setChangeInfo', {[name]: value});
      }
    },
    created() {
      this.$store.dispatch(this.namespace + '/getTreeData');
    }
  }
</script>
