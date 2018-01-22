<!--必填项标识 * 表示某个输入项为必填项-->
<!--使用方法 引入组件 import Must from "[path]/must"-->
<!-- <Must></Must>-->
<template>
    <span class="must">
        {{msg}}
    </span>
</template>

<style>
    .must{
        color: #ff0000;
    }
</style>

<script>
    export default {
        data() {
            return {msg: '*'}
        }
    }
</script>
