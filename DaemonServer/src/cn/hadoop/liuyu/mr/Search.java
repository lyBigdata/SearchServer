package cn.hadoop.liuyu.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * MapReduce程序，分析记录有用户行为的tomcat服务器日志文件，统计用户的热搜词,即统计日志中搜索关键字的词频
 * @author liuyu
 */

public class Search extends Configured implements Tool {

	/**
	 * 
	 * 解析日志，统计出搜索关键字term
	 *
	 */
	public static class Map extends Mapper<Object, Text, Text, IntWritable> {
		private IntWritable one = new IntWritable(1);
		private  Text outKey=new Text();

		/**
		 * 使用默认的输入格式解析类，将输入文件的每一行解析成（key,value）对，key:偏移量，value:每一行的内容
		 */
		public void map(Object key, Text value, Context context)
				throws IOException {
			String line = value.toString();
			try {
				//从每一行日志中,应用正则表达式，解析出搜索关键词，解析出的内容即为用户实际输入的内容，未实现关键字的提取，及分词（提取关键词）
				String term = DataUtil.getTerm(line);
				if(!term.equals("")){
					outKey.set(term);
					context.write(outKey, one);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 统计每个关键字词频
	 *
	 */
	public static class Reduce extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {

			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Path out = new Path(args[1]);
		FileSystem fs = out.getFileSystem(conf);
		if (fs.isDirectory(out)) {
			fs.delete(out, true);
		}
		Job job = Job.getInstance(conf);
		job.setJarByClass(Search.class);

		job.setMapperClass(Map.class);//Mapper
		//job.setCombinerClass(Reduce.class);//Combiner
		job.setReducerClass(Reduce.class);//Reduce

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);// 提交任务
		return 0;
	}

	public static void main(String[] args) throws Exception {
		String[] args0 = {
				"hdfs://master:9000/advance/search",
				"hdfs://master:9000/advance/search/out" };
		int ec = ToolRunner.run(new Configuration(), new Search(), args0);
		System.exit(ec);
	}
}

