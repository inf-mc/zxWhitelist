# A whitelist used spigot-api.

* Need to connect to **MySQL** database.  
* Can be operated using web api.  
* The configuration file is in **'plugins\zxWhitelist\config.yml'**.

### Commands

#### 1.Add player

> `whitelistaddplayer 'name'`  
Abbreviated: `wla 'name'`  
API: `localhost:'port'/api/wla`

#### 2. Delete player

> `whitelistdelete 'name'`  
Abbreviated: `wld 'name'`  
API: `localhost:'port'/api/wld`

#### 3. Inquire

> `whitelistinqure`  
Abbreviated: `wli`  
API: `localhost:'port'/api/wli`  

### **⚠**: `add` and `delete` are `'POST'`  
need to use body in json format,such as:  
```json
{
    "name": "lisi"
}
```
