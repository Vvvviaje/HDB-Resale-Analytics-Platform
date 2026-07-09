# URA API 项目 - 第一步：数据抓取与存储

这是整个项目的第一个可运行的小版本，目标只有一个：**把 HDB 转售交易数据从政府开放接口抓下来，存进本地数据库，能查询到**。不涉及回归分析，不涉及区块链，先把地基打好。

**数据说明**：全程使用 data.gov.sg 官方公开接口返回的真实 HDB 逐笔成交记录，代码里没有任何模拟/假数据生成逻辑——`HdbDataFetchService` 是直接解析 API 返回的真实 JSON 入库的。

## 你需要先装好的东西（只需要装一次）

1. **JDK 21**（项目已按你 VS Code 里配的 21 调整）
   - 检查是否已装：终端输入 `java -version`，确认输出是 21.x
   - 没装的话去装 [Eclipse Temurin 21](https://adoptium.net/) （免费、开源）

2. **Maven**（用来编译和运行 Java 项目）
   - 检查：终端输入 `mvn -version`
   - 没装的话：Mac 用 `brew install maven`，Windows 去 [maven.apache.org](https://maven.apache.org/download.cgi) 下载解压配环境变量

装好这两个之后，你**不需要装数据库、不需要装 Docker、不需要注册任何账号、不需要任何 API Key**，因为：
- 数据源 data.gov.sg 是完全公开免费的接口，不需要 Key
- 数据库用的是 H2，一个跑在本地文件里的轻量数据库，Maven 会自动帮你下载好，不需要额外安装

## 怎么跑起来

1. 解压项目文件夹，进入项目根目录（能看到 `pom.xml` 那一层）

2. 终端里执行：
   ```bash
   mvn spring-boot:run
   ```
   第一次运行 Maven 会自动下载所有依赖包，需要联网，等个一两分钟。看到日志里出现 `Started UraApiApplication` 就说明启动成功了。

3. 打开浏览器或者用 curl，触发一次数据抓取（先抓 100 条测试）：
   ```bash
   curl -X POST "http://localhost:8080/api/fetch?limit=100"
   ```
   正常会返回类似：
   ```json
   {"message":"抓取完成","本次新增条数":100,"数据库当前总条数":100}
   ```

4. 查一下数据库里的记录是不是真的存进去了：
   ```bash
   curl "http://localhost:8080/api/transactions/recent"
   ```
   会看到刚才抓下来的 HDB 交易记录，包含 town、flat_type、resale_price 等字段。

5. 按区域查询试试：
   ```bash
   curl "http://localhost:8080/api/transactions?town=ANG MO KIO"
   ```

6.（可选）想直接用浏览器看数据库表里的内容，打开：
   ```
   http://localhost:8080/h2-console
   ```
   JDBC URL 填 `jdbc:h2:file:./data/uradb`，用户名 `sa`，密码留空，点连接，就能像操作 Excel 一样看表了。

## 这一步做了什么，为什么这么设计

- **为什么不用 Postgres/AWS**：跟你说的一样，零成本零风险优先，H2 文件数据库完全够用，等项目做大了想换 Postgres 只需要改 `application.properties` 里几行配置，代码完全不用动（这是用 JPA 的好处）。
- **为什么用 `existsBySourceId` 判重**：data.gov.sg 每条记录自带一个 `_id`，我们拿它当"这条记录有没有抓过"的依据，这样你重复调用 `/api/fetch` 不会插入重复数据，为后面"每日定时抓取"做准备。
- **`@EnableScheduling` 先加上但没用**：这是留给下一步的钩子，下一步我们会加一个真正的定时任务，让它每天自动跑一次抓取，不用你手动 curl。

## 下一步预告

跑通这一步、确认你能看到数据之后告诉我，我们再一起做下一步——大概率是：
1. 把"手动 fetch"改成"每天自动 fetch"（`@Scheduled` 定时任务）
2. 开始写多元线性回归模型（用 Apache Commons Math），根据面积、楼层、区域预测价格

有任何报错就把日志贴给我，我帮你看。
