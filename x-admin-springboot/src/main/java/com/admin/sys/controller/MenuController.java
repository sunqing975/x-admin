package com.admin.sys.controller;

import com.admin.common.vo.Result;
import com.admin.sys.entity.Menu;
import com.admin.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
@Api(tags = {"菜单相关接口列表"})
@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有的菜单数据")
    @GetMapping("/getAllMenu")
    public Result<List<Menu>> getAllMenu(){
        List<Menu> list = menuService.getAllMenu();
        return Result.success(list);
    }
}
