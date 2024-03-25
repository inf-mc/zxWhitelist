package com.zhuoxin.whitelist.websever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuoxin.whitelist.User;
import com.zhuoxin.whitelist.Whitelist;
import com.zhuoxin.whitelist.dbtool.DatabaseOperate;
import io.javalin.Javalin;

import java.util.Objects;
import java.util.logging.Level;

public class WebSever {
    private Javalin webSever;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSever(Whitelist whitelist, DatabaseOperate databaseOperate) {
        this.webSever = Javalin.create().start(whitelist.getConfig().getInt("webSever.port", 2333));

        this.webSever.get("/api", ctx -> {
            // 设置响应头，指定字符集为UTF-8
            ctx.header("Content-Type", "text/plain; charset=UTF-8");

            ctx.result("此处为zxWhitelist webAPI的根路径");
        });

        this.webSever.get("/api/wli", ctx -> {
            ctx.header("Content-Type", "application/json; charset=UTF-8");

            ctx.json(databaseOperate.selectAll());
        });

        this.webSever.post("/api/wla", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");

            // 解析JSON内容到User对象
            User user = objectMapper.readValue(ctx.body(), User.class);
            String name = user.getName();

            // 正则匹配
            if (!name.matches(Objects.requireNonNull(whitelist.getConfig().getString("database.rules.userName")))) {
                ctx.result("用户名不合法");
            }
            if (!databaseOperate.insertName(name)) {
                ctx.result("添加失败，详情看服务器控制台");
            } else {
                whitelist.getLogger().log(Level.INFO, "通过api添加用户 "+name);
                ctx.result("添加成功");
            }
        });

        this.webSever.post("/api/wld", ctx -> {
            ctx.header("Content-Type", "text/plain; charset=UTF-8");

            User user = objectMapper.readValue(ctx.body(), User.class);
            String name = user.getName();

            if (!databaseOperate.deleteName(name)) {
                ctx.result("删除失败，详情看服务器控制台");
            } else {
                whitelist.getLogger().log(Level.INFO, "通过api删除用户 "+name);
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
