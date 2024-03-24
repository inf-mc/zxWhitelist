package com.zhuoxin.whitelist.websever;

import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import io.javalin.Javalin;

import java.util.Objects;
import java.util.logging.Level;

public class WebSever {
    public WebSever(Whitelist whitelist, DatabaseOperate databaseOperate) {
        Javalin webSever = Javalin.create().start(2333);

        webSever.get("/", ctx -> {
            // 设置响应头，指定字符集为UTF-8
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            ctx.result("此处为zxWhitelist webAPI的根路径");
        });
        webSever.get("/wli", ctx -> {
            ctx.header("Content-Type", "application/json; charset=UTF-8");
            ctx.json(databaseOperate.selectAll());
        });
        webSever.get("/wla/{name}", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            String name = ctx.pathParam("name");
            // 正则匹配
            if (!name.matches(Objects.requireNonNull(whitelist.getConfig().getString("database.rules.userName")))) {
                ctx.result("用户名不合法");
            }
            if (!databaseOperate.insertName(name)) {
                ctx.result("添加失败，详情看控制台");
            }else {
                ctx.result("添加成功");
            }
        });
        webSever.get("/wld/{name}", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            String name = ctx.pathParam("name");

            if (!databaseOperate.deleteName(name)) {
                ctx.result("删除失败，详情看控制台");
            }else {
                ctx.result("删除成功");
            }
        });
        whitelist.getLogger().log(Level.INFO, "web sever启动成功");
    }
}
