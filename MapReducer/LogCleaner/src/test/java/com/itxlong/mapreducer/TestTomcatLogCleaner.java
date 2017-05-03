package com.itxlong.mapreducer;

import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

public class TestTomcatLogCleaner {

    @Test
    public void TestRun() throws Exception{
        //HDFS地址
        final String INPUT_PATH = "hdfs://master:9000/tomcat_logs";
        final String OUTPUT_PATH = "hdfs://master:9000/tomcat_cleaned";

        String[] strs = {INPUT_PATH, OUTPUT_PATH};
        ToolRunner.run(new TomcatLogCleaner(), strs);
    }
}
