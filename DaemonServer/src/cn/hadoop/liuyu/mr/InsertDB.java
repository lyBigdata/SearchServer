package cn.hadoop.liuyu.mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


/**
 * 将MapReduce统计的词频信息插入数据库
 * @author liuyu
 *
 */
public class InsertDB {
	private Connection db = null;// mysql数据库连接对象
	
	/**
	 * @function 连接mysql数据库
	 * @param path HDFS文件路径
	 * @param dbhost  mysql接口
	 * @param dbname  数据库名称
	 * @param dbuser 用户名称
	 * @param dbpassword  用户密码
	 * @param fs  FileSystem 对象
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	
	public  InsertDB(String path, String dbhost, String dbname, String dbuser,
			String dbpassword, FileSystem fs) throws ClassNotFoundException,
			SQLException, IOException {
		Class.forName("com.mysql.jdbc.Driver");
		String cs = MessageFormat
				.format("jdbc:mysql://{0}/{1}?user={2}&password={3}&autoReconnect=true",
						new Object[] { dbhost, dbname, dbuser, dbpassword });
		db = DriverManager.getConnection(cs);
		reset();
	}
	 
	
	/**
	 * 重置表，删除表中的数据，清空表记录
	 * @throws SQLException 
	 */
	private void  reset() throws SQLException{
		truncate("dictionary");
	}

	/**
	 * 清空name表中的所有记录
	 * @param name 表名称
	 * @throws SQLException 
	 */
	private void truncate(String  name) throws SQLException {
		//Statement 是 Java 执行数据库操作的一个重要方法，用于在已经建立数据库连接的基础上，向数据库发送要执行的SQL语句。
		//Statement对象，用于执行不带参数的简单SQL语句。
		Statement s=db.createStatement();  //创建数据库操作对象
		s.execute("truncate table ".concat(name)); //向已经建立数据链接的数据库，发送可执行的SQL语句
		s.close();  //关闭
	}

	//读取HDFS数据，并解析，然后插入数据库
	/**
	 * 
	 * @param path 待解析的数据源，hdfs文件
	 * @param fs   文件系统对象
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws SQLException 
	 */
	private void processData(String path, FileSystem fs) throws IllegalArgumentException, IOException, SQLException{
		FSDataInputStream in = fs.open(new Path(path));  //打开hdfs文件，作文输入流
		BufferedReader br=new BufferedReader(new InputStreamReader(in));  //创建缓冲读取器
		String line=br.readLine();//读取一行数据
		while(null!=line){
			String[]  wAndf=line.split("\\s+");
			if(wAndf.length==2){
				String keyWord=wAndf[0];  //词
				int frequency=Integer.parseInt(wAndf[1]);  //词频
				insertDictionary(keyWord,frequency);  //组装数据，插入数据库
			} else {
				System.err.println("invalid data: " + line);
			}
			line = br.readLine();//继续读取下一行
		}
	}


	/**
	 * @function 填充记录字段的值
	 * @param keyword 关键字
	 * @param frequency 词频
	 * @throws SQLException
	 */
	private void insertDictionary(String keyWord, int frequency) throws SQLException {
		DataRecord dr = new DataRecord();//DataRecord类继承自hashMap
		dr.put("keyword", keyWord);  //存入数据
		dr.put("frequency", frequency);
		insert("dictionary", dr);  //插入数据
	}
	
	/**
	 * 
	 * @param table 表名
	 * @param row   数据记录
	 * @throws SQLException
	 */
	private void insert(String table, DataRecord row) throws SQLException {
		Statement s = db.createStatement();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		sql.append(table);
		sql.append(" ");
		sql.append(row.toString());
		s.execute(sql.toString());  //向数据库发送可执行的sql语句
	}


	/**
	 * 
	 * 生成一条数据记录
	 *
	 */
	class DataRecord extends HashMap<String, Object> {
		@Override
		public String toString() {
			StringBuffer retVal = new StringBuffer();
			// 生成表的字段
			retVal.append("(");
			boolean first = true;
			for (String key : keySet()) {
				if (first) {
					first = false;
				} else {
					retVal.append(",");
				}
				retVal.append(key);
			}
			//生成表字段对应的值
			retVal.append(") values (");
			first = true;
			for (String key : keySet()) {
				Object o = get(key);
				if (first) {
					first = false;
				} else {
					retVal.append(",");
				}
				if (o instanceof Long) {
					retVal.append(((Long) o).toString());
				} else if (o instanceof Integer) {
					retVal.append(((Integer) o).toString());
				}  else if (o instanceof String) {
					retVal.append("'");
					retVal.append(o.toString());
					retVal.append("'");
				}
			}
			retVal.append(")");
			//返回一条sql格式的数据记录
			return retVal.toString();
		}
	}

	
	public static void main(String[] args) throws IOException {
		String[] args0 = {
				"hdfs://master:9000/advance/search/out/part-r-00000",
				"localhost:3306", "searchServerDB", "root", "12035318" };
		if (args0.length == 5) {
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(
					URI.create("hdfs://master:9000"),conf);
			try {
				InsertDB db = new InsertDB(args0[0], args0[1], args0[2],args0[3], args0[4], fs);
				db.processData(args0[0], fs);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err
					.println("参数不正确");
		}
	}
}


