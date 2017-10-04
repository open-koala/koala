# 下载源码
## OSChina
```
https://gitee.com/openkoala/koala.git
```
## GitHub
```
https://github.com/open-koala/koala.git
```
# 使用maven进行构建
切换到项目根目录，使用命令行工具。
## 编译
```
mvn clean
```
## 安装
```
mvn install
```
## 启动
切换到带有web的maven子模块根目录。
```
mvn jetty:run
```
例如启动权限模块。
```
cd koala-security/koala-security-web/
mvn jetty:run
```