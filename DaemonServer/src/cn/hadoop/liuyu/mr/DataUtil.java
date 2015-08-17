package cn.hadoop.liuyu.mr;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @function  使用正则表达式，解析term值，即就是用户的搜索词
 * @author liuyu
 */

public class DataUtil {
	//返回搜索关键字
	public  static String  getTerm(String   search) throws UnsupportedEncodingException{
		Pattern pattern = Pattern.compile("(?<=term=).*(?=\\s+HTTP)");  //注册匹配的正则表达式
		Matcher match = pattern.matcher(search);   //匹配search
		String result = "";
		if (match.find()) {
			result = match.group().trim();
		}
		result=java.net.URLDecoder.decode (result,"UTF-8");
		return result;	
	}
 
}
