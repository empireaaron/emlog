#  **emlog**  【技术QQ群：456742016】
### 框架简介

- emlog是一个基于Flume+elasticsearch的日志收集agent项目,它主要通过快速配置与项目整合内嵌的形式实现对普通服务器或者云部署服务的日志实时收集、分析、预警.

- emlog包括以下五大模块，日志Agent模块、API运营日志分析模块（）、监控预警模块（规划中）、运营日志展示模块(兼容kibana展示)、flume插件扩展开发模块.

### 
    1、日志Agent模块：log4j、log4j2、logback等日志框架的扩展内嵌形式日志收集器等功能； 
    2、API运营日志分析模块：对API等运营日志进行分析、统计
    3、监控预警模块：支持用户预警自定义配置，通过日志分析模块的数据进行实时预警；
    4、运营日志管理模块：对收集的日志进行查询展示、运维日志索引库管理等功能;
    5、flume插件扩展开发模块：对flume进行组件扩展支持

### 框架规划   
- emlog將打造一个实时收集、分析、预警、展示的全链路的日志系统；助力各大企业快速接入日志系统，继而提高开发人员的问题排查效率、系统优化。

- 技术
### 

    emlog-agent：log4j、log4j2、logback append + disruptor + flume Agent
    flume：flume-ng-elasticsearch-sink + flume-hdfs-sink + flume-ng-kafka-sink
    kafka：实时分析数据缓冲MQ
    flink：实时计算框架
    hadoop：日志长期归档存储（hdfs）分析等
    elasticsearch：短期实时日志存储
    InfluxDB：API访问数据存储
   
- em团队后期两款产品：
###
    1、实时日志系统；
    2、监控指标系统；

### emlog生态圈

    01、规划中
    02、......
    03、.......
    
### 项目计划表【欢迎有志者加盟】
**一阶段：** 

    01、emlog-agent----已完成
    02、flume-ng-elasticsearch-sink----已完成（扩展调试）
    03、flume-ng-kafka-sink----进行中(扩展调试)(ing)
    04、flume ng interceptor（支持flume独立非内嵌日志融合）扩展开发----规划中
    05、运营日志展示模块(兼容kibana展示)----规划中
    06、文档编写----进行中
    
 **二阶段** 

    07、API运营日志分析模块----延期执行
    08、监控预警模块----延期执行
    
### emlog最新动态

    1.emlog-agent[2021-02]
    2.flume-ng-elasticsearch-sink[2021-02]
    3.flume-ng-kafka-sink----进行中(扩展调试)(ing)[2021-03]
    4.flume ng interceptor（支持flume独立非内嵌日志融合）扩展开发-待启动[2021-03-10]
 