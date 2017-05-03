package com.itxlong.util;

import org.junit.Test;

import java.text.ParseException;

public class TestLogParser {

    // 测试解析样例数据
    @Test
    public void TestParse() throws ParseException {
        final String S1 = "174.118.204.192 [28/Apr/2017:00:14:04 +0800] \"POST /app/common/user/appLogin/appLogin HTTP/1.1\" 195byte 200 28ms";
        LogParser parser = new LogParser();
        final String[] array = parser.parse(S1);
        System.out.println("样例数据： "+S1);
        System.out.format("解析结果：  ip=%s, time=%s, url=%s, size=%s, status=%s, traffic=%s", array[0], array[1], array[2], array[3], array[4], array[5]);
    }
}
