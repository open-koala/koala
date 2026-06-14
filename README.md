# Koala

Koala 是一个 Maven 多模块项目，包含基础组件、权限、组织、BPM、GQC、监控、OpenCIS、业务日志和插件模块。根 `pom.xml` 负责统一依赖版本、公共插件配置和模块聚合。

## 环境要求

- JDK 8 或兼容 JDK，项目编译目标为 Java 8。
- Maven 3.x。
- 默认使用内存 H2 数据库，启动后会自动建表，进程退出后数据会清空。

## 模块结构

- `koala-commons/`：公共工具、领域支持、测试支持。
- `koala-security/`：权限核心、Shiro 集成和权限 Web。
- `koala-organisation/`：组织、部门、岗位等组织域能力。
- `koala-security-org/`：权限与组织集成 Web。
- `koala-bpm/`：BPM core、designer、form、oss 子系统。
- `koala-gqc/`：通用查询配置与运行模块。
- `koala-monitor/`：监控采集、应用与 Web 控制台。
- `koala-opencis/`：OpenCIS 及 CAS 管理模块。
- `koala-businesslog/`：业务日志 API、实现、Web 和验收测试。
- `koala-plugin/`：代码生成与插件支持。

## 构建命令

在项目根目录执行：

```bash
mvn -DskipTests compile
mvn test
mvn install
```

常用局部构建：

```bash
mvn -pl koala-security-org/koala-security-org-web -am -DskipTests compile
mvn -pl koala-bpm/koala-bpm-form/koala-bpm-form-web -am -DskipTests compile
```

## 数据库 Profile

数据库 profile 名称统一为大小写敏感的 `H2` 和 `MySQL`。

- `H2`：默认激活，适合本地快速启动。
- `MySQL`：连接本地 MySQL，配置在各模块 POM 的 profile 中。

示例：

```bash
mvn -PH2 -DskipTests compile
mvn -PMySQL -DskipTests compile
```

## 本地启动

推荐从根目录使用 `-pl ... -am` 启动目标 Web 模块：

```bash
mvn -Djetty.port=8090 -pl koala-security-org/koala-security-org-web -am jetty:run
mvn -Djetty.port=7652 -pl koala-gqc/koala-gqc-web -am jetty:run
mvn -Djetty.port=7653 -pl koala-monitor/koala-jmonitor-web-mvc -am jetty:run
mvn -Djetty.port=8072 -pl koala-bpm/koala-bpm-form/koala-bpm-form-web -am jetty:run
mvn -Djetty.port=8073 -pl koala-bpm/koala-bpm-designer/koala-bpm-designer-web -am jetty:run
```

如需切换数据库，加上 `-PH2` 或 `-PMySQL`。

## 开发约定

- 依赖版本优先放在根 `pom.xml` 的 properties 或 dependencyManagement 中统一管理。
- 新增 Web 模块启动说明时，使用根目录 `mvn -pl <module> -am jetty:run` 形式。
- 提交前至少执行 `mvn -DskipTests compile`，涉及测试逻辑时执行相关模块测试。
