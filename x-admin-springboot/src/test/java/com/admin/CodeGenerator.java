package com.admin;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String moduleName = "sys";
        String mapperLoaclcation = "C:\\dev\\code\\github.com\\Java\\Web\\SpringBoot\\x-admin\\x-admin-springboot\\src\\main\\resources\\mapper\\" + moduleName;
        String tables = "x_user,x_role,x_menu,x_user_role,x_role_menu";
        FastAutoGenerator.create("jdbc:mysql:///xdb", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("sunqing") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            // .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\dev\\code\\github.com\\Java\\Web\\SpringBoot\\x-admin\\x-admin-springboot\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.admin") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLoaclcation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("x_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
