一、项目描述：通过对Tomcat日志进行分析，计算网站关键指标，供运营者决策。

二、开发步骤

2.1、把日志数据上传到HDFS中进行处理

  （1）日志服务器数据较小、压力较小，可以直接使用shell命令把数据上传到HDFS中
  
  （2）日志服务器非常多、数据量大，使用flume进行数据处理

2.2、使用MapReduce对HDFS中的原始日志数据进行清洗

注意：此处MapReduce使用的是java代码。参见【https://github.com/itxlong/xxbbs/tree/master/MapReducer/LogCleaner】

2.3、使用Hive对清洗后的数据进行统计分析

2.4、使用Sqoop把Hive产生的统计结果导出到Mysql中

2.5、如果用户需要查看详细数据的话，可以使用HBase进行展现
