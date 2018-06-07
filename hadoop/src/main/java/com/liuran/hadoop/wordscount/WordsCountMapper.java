package com.liuran.hadoop.wordscount;

import com.liuran.hadoop.utils.HadoopUtils;
import com.liuran.hadoop.utils.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordsCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private String info;

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        value.set(StringUtils.cancelSpecialSymbols(value.toString()));
        Text text = new Text();
        IntWritable intWritable = new IntWritable();
        String[] array = value.toString().split(" ");
        for (String word : array){
            if (word == null || "".equals(word.trim())){
                continue;
            }
            for (String str : StringUtils.humpSpile(word)){
                if (str.length() < 2){
                    continue;
                }
                text.set(str);
                intWritable.set(1);
                context.write(text, intWritable);
            }
            //计数器
            context.getCounter("map", getInfo()).increment(1);
        }
    }

    private String getInfo(){
        if (info != null){
            return info;
        }

        info = HadoopUtils.getInfo(this, "map");
        return info;
    }
}