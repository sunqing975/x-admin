<template>
    <div>
        <!-- 搜索栏 -->
        <el-card id="search">
            <el-row>
                <el-col :span="20">
                    <!-- clearable 用于一键删除 -->
                    <el-input v-model="searchModel.roleName" placeholder="角色名称" clearable></el-input>
                    <el-button type="primary" @click="getRoleByCondition" round icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="4" align="right">
                    <el-button type="primary" icon="el-icon-plus" @click="openRoleWin(null)" circle></el-button>
                </el-col>
            </el-row>
        </el-card>
        <!-- 搜索结果 -->
        <el-card>
            <el-table :data="roleList" stripe style="width: 100%">
                <!-- 生成序号 -->
                <el-table-column type="index" label="#" width="80">
                    <template slot-scope="scope">
                        <!-- slot-scope 作用域插槽 可以用于获取当前行的索引号 -->
                        <!-- (pageNo-1)*pageSize + index +1 -->
                        {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="roleId" label="角色ID"> </el-table-column>
                <el-table-column prop="roleName" label="角色名称"> </el-table-column>

                <el-table-column prop="roleDesc" label="角色描述"> </el-table-column>

                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="primary" @click="openRoleWin(scope.row.roleId)" icon="el-icon-edit" size="mini" circle></el-button>
                        <el-button type="danger" @click="deleteRoleById(scope.row)" icon="el-icon-delete" size="mini" circle></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- 分页功能 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="searchModel.pageNo"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="searchModel.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
        >
        </el-pagination>

        <!-- 角色表单 -->
        <!-- :title 加上: 表示这个对应一个变量 -->
        <el-dialog :title="title" @close="clearForm" :visible.sync="dialogFormVisible">
            <el-form :model="roleForm" :rules="rules" ref="roleFormRef">
                <el-form-item label="角色名称" :label-width="formLabelWidth" prop="roleName">
                    <el-input v-model="roleForm.roleName" autocomplete="off" clearable></el-input>
                </el-form-item>
                <el-form-item label="角色描述" :label-width="formLabelWidth" prop="roleDesc">
                    <el-input v-model="roleForm.roleDesc" autocomplete="off" clearable></el-input>
                </el-form-item>
                <el-form-item label="权限设置" :label-width="formLabelWidth" prop="menuIdList">
                    <el-tree :data="menuList" :props="menuProps" show-checkbox default-expand-all node-key="menuId" ref="menuRef"></el-tree>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRole">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
// @表示src目录
import roleApi from '@/api/roleManger';
import menuApi from '@/api/menuManger';
export default {
    data() {
        return {
            total: 0,
            searchModel: {
                pageNo: 1,
                pageSize: 5,
            },
            roleList: [],
            title: '',
            dialogFormVisible: false,
            roleForm: {},
            formLabelWidth: '120px',
            rules: {
                roleName: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' },
                    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
                ],
                roleDesc: [
                    { required: true, message: '请输入角色描述', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
                ],
            },
            menuProps: {
                children: 'children',
                label: 'title',
            },
            menuList: [],
        };
    },
    // 这个叫构造函数
    created() {
        this.getRoleByCondition();
        this.getAllMenu();
    },
    methods: {
        handleSizeChange(pageSize) {
            this.searchModel.pageSize = pageSize;
            this.getRoleByCondition();
        },
        handleCurrentChange(pageNo) {
            this.searchModel.pageNo = pageNo;
            this.getRoleByCondition();
        },
        getRoleByCondition() {
            roleApi.getRoleByCondition(this.searchModel).then(response => {
                console.log(response);
                this.roleList = response.data.rows;
                this.total = response.data.total;
            });
        },

        openRoleWin(id) {
            if (id == null) {
                this.title = '新增角色';
            } else {
                this.title = '修改角色';
                // 根据id查询用户数据
                roleApi.getRoleById(id).then(response => {
                    this.roleForm = response.data;
                    this.$refs.menuRef.setCheckedKeys(response.data.menuIdList);
                });
            }

            this.dialogFormVisible = true;
        },
        clearForm() {
            this.roleForm = {};
            this.$refs.roleFormRef.clearValidate();
            // 将角色表单的树形控件在关闭后，清除选中
            this.$refs.menuRef.setCheckedKeys([]);
        },
        saveRole() {
            // 触发表单验证
            this.$refs.roleFormRef.validate(valid => {
                if (valid) {
                    let checkedKeys = this.$refs.menuRef.getCheckedKeys();
                    let halfCheckedKeys = this.$refs.menuRef.getHalfCheckedKeys();
                    this.roleForm.menuIdList = checkedKeys.concat(halfCheckedKeys);
                    // console.log(this.roleForm.menuIdList);
                    // return;
                    // 提交表单数据
                    roleApi.saveRole(this.roleForm).then(response => {
                        // 成功提示
                        this.$notify({
                            title: '成功',
                            message: response.message,
                            type: 'success',
                        });
                        // 关闭窗口
                        this.clearForm();
                        this.dialogFormVisible = false;
                        // 刷新表格
                        this.getRoleByCondition();
                    });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        deleteRoleById(role) {
            this.$confirm(`您确认删除角色 ${role.roleName} ?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            })
                .then(() => {
                    roleApi.deleteRoleById(role.roleId).then(response => {
                        this.$message({
                            type: 'success',
                            message: response.message,
                        });
                        this.getRoleByCondition();
                    });
                })
                .catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除',
                    });
                });
        },
        getAllMenu() {
            menuApi.getAllMenu().then(response => {
                this.menuList = response.data;
            });
        },
    },
};
</script>
<style>
#search .el-input {
    width: 200px;
    margin-right: 10px;
}
.el-dialog .el-input {
    width: 80%;
}
.el-dialog .el-tree {
    width: 80%;
}
</style>