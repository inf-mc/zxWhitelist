package com.zhuoxin.whitelist.websever;

import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import io.javalin.Javalin;

import java.util.Objects;
import java.util.logging.Level;

public class WebSever {
    private Javalin webSever;

    public WebSever(Whitelist whitelist, DatabaseOperate databaseOperate) {
        this.webSever = Javalin.create().start(whitelist.getConfig().getInt("webSever.port", 2333));

        this.webSever.get("/", ctx -> {
            // 设置响应头，指定字符集为UTF-8
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            ctx.result("此处为zxWhitelist webAPI的根路径");
        });
        this.webSever.get("/wli", ctx -> {
            ctx.header("Content-Type", "application/json; charset=UTF-8");
            ctx.json(databaseOperate.selectAll());
        });
        this.webSever.get("/wla/{name}", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            String name = ctx.pathParam("name");
            // 正则匹配
            if (!name.matches(Objects.requireNonNull(whitelist.getConfig().getString("database.rules.userName")))) {
                ctx.result("用户名不合法");
            }
            if (!databaseOperate.insertName(name)) {
                ctx.result("添加失败，详情看控制台");
            } else {
                ctx.result("添加成功");
            }
        });
        this.webSever.get("/wld/{name}", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");
            String name = ctx.pathParam("name");

            if (!databaseOperate.deleteName(name)) {
                ctx.result("删除失败，详情看控制台");
            } else {
                ctx.result("删除成功");
            }
        });
        whitelist.getLogger().log(Level.INFO, "web sever启动成功");
    }

    public void stopWebSever() {
        if (this.webSever != null) {
            this.webSever.stop();
            this.webSever = null;
        }
    }
}
