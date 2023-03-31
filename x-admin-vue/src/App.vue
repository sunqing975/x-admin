<template>
    <div id="app">
        <router-view />
    </div>
</template>

<script>
export default {
    name: 'App',
};
</script>
<style>
/* 修改整体的页边距 */
.app-main {
    padding: 10px;
}
.el-card {
    margin-bottom: 10px;
}
</style>