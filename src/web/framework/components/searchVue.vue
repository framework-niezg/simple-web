<template>
  <el-row ref="form" :inline="true"  class="basic-form">
    <div v-for="item in searchOptions" class="inline-block">
      <el-col v-if="item.type === 'input'">
        <label class="label-text">{{item.text}}</label>
        <input style="display:none"/>
        <el-input  auto-complete="new-password" class="basic-input" size="mini" :name="item.name" @change.native="inputChange"></el-input>
      </el-col>
      <el-col v-if="item.type === 'select'">
        <label class="label-text">{{item.text}}</label>
        <el-select :value="searchOptions[item.name]"  class="basic-select" size="mini" placeholder="请选择">
          <el-option
            v-for="type in item.data"
            :key="type.value"
            :label="type.label"
            :value="type.value">
          </el-option>
        </el-select>
      </el-col>
      <el-col v-if="item.type === 'button'">
        <el-button type="primary" class="basic-btn" @click.native="onClickHandler" :name="item.event" size="mini">{{item.text}}</el-button>
      </el-col>
    </div>
  </el-row>
</template>

<style scoped>
  .inline-block{
    display: inline-block;
  }
  .basic-select{
    width:150px;
  }
  .basic-input{
    width:140px;
    margin-left:5px;
  }
  .basic-btn{
    margin-left:5px!important;
  }
  .basic-form{
    margin-top: 5px;
    padding-bottom: 5px;
  }
  .label-text{
    font-size: 14px;
    vertical-align: -moz-middle-with-baseline;
    vertical-align: -webkit-baseline-middle;
    display: inline-block;
  }
</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  export default {
    name:"searchOptions",
    props: ['current'],
    computed: {
      ...mapGetters({
        searchOptions: "searchOptions"
      }),
    },
    methods: {
      inputChange(e) {
        let srcElement = e.srcElement || e.target, name = srcElement.getAttribute("name"), value = srcElement.value;
        this.$store.dispatch(this.current + '/resetParam', {[name]: value});
      },
      onClickHandler(e) {
        let srcElement = e.srcElement || e.target, eventName = srcElement.getAttribute("name");
        eventName = !eventName ? srcElement.parentNode.getAttribute("name") : eventName;//兼容element-ui 组合标签
        switch (eventName) {
          case"submit":
            this.onSubmit();
            break;
          case"add":
            this.onAdd();
            break;
          default:
            this.onClickDefault(eventName);
            break;
        }
      },
      onSubmit(){
        this.$store.dispatch(this.current+"/getItems");
      }
    },
    data() {
        return{

        }
    },
    created() {

    }
  }
</script>
