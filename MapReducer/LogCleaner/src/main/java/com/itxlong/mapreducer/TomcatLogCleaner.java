package com.itxlong.mapreducer;

import com.itxlong.util.LogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class TomcatLogCleaner extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        final Job job = new Job(new Configuration(), TomcatLogCleaner.class.getSimpleName());
        job.setJarByClass(TomcatLogCleaner.class);
        FileInputFormat.setInputPaths(job, args[0]);
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.waitForCompletion(true);
        return 0;
    }

    public static void main(String[] args) throws Exception{
        ToolRunner.run(new TomcatLogCleaner(), args);
    }

    static class MyMapper extends Mapper<LongWritable, Text, LongWritable, Text>{
        LogParser logParser = new LogParser();
        Text v2 = new Text();

        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] parsed = logParser.parse(value.toString());
            System.out.println(parsed[0]+"\t"+parsed[1]+"\t"+parsed[2]);

            //过掉开头的特定格式字符串
            if(parsed[2].startsWith("GET /")){
                parsed[2] = parsed[2].substring("GET /".length());
            }
            else if(parsed[2].startsWith("POST /")){
                parsed[2] = parsed[2].substring("POST /".length());
            }

            //过滤结尾的特定格式字符串
            if(parsed[2].endsWith(" HTTP/1.1")){
                parsed[2] = parsed[2].substring(0, parsed[2].length()-" HTTP/1.1".length());
            }

            v2.set(parsed[0]+"\t"+parsed[1]+"\t"+parsed[2]);
            context.write(key, v2);
        }
    }

    static class MyReducer extends Reducer<LongWritable, Text, Text, NullWritable> {
        protected void reduce(LongWritable k2, java.lang.Iterable<Text> v2s, org.apache.hadoop.mapreduce.Reducer<LongWritable,Text,Text,NullWritable>.Context context) throws java.io.IOException ,InterruptedException {
            for (Text v2 : v2s) {
                context.write(v2, NullWritable.get());
            }
        }
    }
}
