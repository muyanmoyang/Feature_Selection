package muyanmoyang.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import muyanmoyang.utils.DBUtil;

/**
 * 划分训练数据集和测试集,并分别写入两个文件,总共28000 条短信，训练集取20000(垃圾邮件10000) ， 测试集取8000(垃圾邮件4000) 。
 * @author moyang
 *
 */
public class CreateTrainAndTestSet {
	
	public static void main(String[] args) throws IOException, SQLException { 
//		getSogouTextFromMySQL() ;  // 从数据库获取数据集 
//		preprocess("C:/Users/Administrator/Desktop/论文（特征提取）/短文本/lady.txt"
//				 ,"C:/Users/Administrator/Desktop/论文（特征提取）/短文本/lady_2.txt") ;
		String label = "lady" ;
		divideTrainAndTest("C:/Users/Administrator/Desktop/论文（特征提取）/短文本/" + label + "_2.txt",
							"C:/Users/Administrator/Desktop/论文（特征提取）/短文本/训练集/"+ label +".txt",
				"C:/Users/Administrator/Desktop/论文（特征提取）/短文本/测试集/" + label + ".txt");
		

	}
	
	private static void preprocess(String readerFile,String writerDir) throws IOException{
		InputStreamReader reader = new InputStreamReader(new FileInputStream(readerFile),"GBK") ;
		BufferedReader BR = new BufferedReader(reader) ;
		FileWriter writer = new FileWriter(new File(writerDir)) ;
		String line ;
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			if(str[1].length() > 10){
				writer.write(str[0] + "\t" + str[1] + "\n");
				writer.flush();
			}
		}
		writer.close();
	}
	/*
	 *  对原数据集进行整理,同一类的整理到同一个txt文件
	 */
//	private static void readTxtFromOriginalText(String fileDir) throws IOException{
//		BufferedReader BR ;
//		File file = new File(fileDir);
//		File[] fileList = file.listFiles();
//		int count = 0 ;
//		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/复旦大学中文文本分类语料库/环境.txt")) ;
//		for (int i = 0; i < fileList.length; i++) {
//			if (fileList[i].isFile()) {
//				count ++ ; // 第count篇文本
//				BR = new BufferedReader(new InputStreamReader(new FileInputStream(fileList[i]),"GBK")) ;
//				String line ;
//				while((line=BR.readLine()) != null){
//					if(!line.startsWith("日月光华") && !line.startsWith("--") && !line.startsWith(" 发信人") &&
//							!line.startsWith("标  题") && !line.startsWith("发信站") && !line.startsWith("【") && 
//							!line.startsWith("※ 来源") &&  !line.startsWith("[返回上一页]") && !line.startsWith("【")){
//						writer.write(line) ;
//					}
//				}
//				writer.write("\n");
//				writer.flush();
//			}
//		}
//		writer.close();
//	}
	
	
	/*
	 *  划分训练集 和 测试集
	 */
	private static void divideTrainAndTest(String fileDir,String trainDir,String testDir) throws IOException{
		File readerFile = new File(fileDir);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(readerFile),"GBK") ;
		BufferedReader BR = new BufferedReader(reader) ;
		
		FileWriter trainWriter = new FileWriter(new File(trainDir)) ;
		FileWriter testWriter = new FileWriter(new File(testDir)) ;
		String line ;
		int count = 0 ;
		while((line=BR.readLine()) != null){
			count ++ ;
			if(count <= 4000){
				trainWriter.write(line);
				trainWriter.write("\n");
				trainWriter.flush();
			}
			if(count>4000 && count <=6000 ){
				testWriter.write(line);
				testWriter.write("\n");
				testWriter.flush();
			}
		}
		trainWriter.close();
		testWriter.close();
	} 
	

	/**
	 * 从MySQL数据库获取语料文本，以Map<Integer,String[]>形式保存
	 * @return Map<Integer,String[]>的语料文本
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void getSogouTextFromMySQL() throws SQLException, IOException
	{ 
		ArrayList<String> sougouTextListFromSql = new ArrayList<String>() ;//用来存取从数据库中select出的每条语料记录
		Map<Integer,String[]> sougouTextMapFromSql = new HashMap<Integer, String[]>() ;//保存从数据库中select出的语料记录
		FileWriter fileWriter = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/短文本/lady.txt")) ;
 		
		String url = "jdbc:mysql://localhost:3306/sogou?useUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "123456";
		Connection conn = DBUtil.getConnection(url, username, password);
		
	    Statement stmt = null;  //表示数据库的更新操作  
	    ResultSet result = null; //表示接收数据库的查询结果  
	    stmt = conn.createStatement();
	    result = stmt.executeQuery(""
	    		+ "SELECT newstitle,category FROM sohunews_reduced where category='lady' "
	    		+ "AND LENGTH(newstitle)>10"); //执行SQL 语句，查询数据库  
	    int count = 0 ; //记录ResultSet记录所在的行数
	    int tempcount = 0; //记录ResultSet总行数count的值
	    while(result.next()) 
	    {  
	    	String newstitle = result.getString("newstitle") ;
	    	String category = result.getString("category") ;
	    	
//	    	ResultSetMetaData m = result.getMetaData() ;//获取此 ResultSet 对象的列的编号、类型和属性
//	    	int columns = m.getColumnCount() ;//返回此 ResultSet 对象中的列数
	    	
	    	//将每条语料文本添加到ArrayList中
//	    	if( newscontent.length() > 30 && newscontent.length() < 200){
	    		sougouTextListFromSql.add(category) ;
		    	sougouTextListFromSql.add(newstitle) ;
		    	 
		    	count++ ;
		    	tempcount = count ;
		    	System.out.println("第" + count + "行"); 
		    	
		    	String[] textStringArray = {sougouTextListFromSql.get(0),sougouTextListFromSql.get(1)} ;
		    	
		    	sougouTextMapFromSql.put(count,textStringArray) ; //将语料以Map的形式保存 
		    	sougouTextListFromSql.clear() ; //移除此列表中的所有元素
//	    	}
	    }
	    System.out.println("行数" + tempcount); 
	    
	    for(int i=1 ; i<=tempcount ; i++)
	    {
	    	String[] str = sougouTextMapFromSql.get(i) ;//迭代出Map集合sougouTextMapFromSql
	    	//写入到指定txt文件进行保存
	    	fileWriter.write(str[0] + "\t" + str[1] + "\n") ; 
	    }
	    fileWriter.flush() ;
	    fileWriter.close() ;
	    result.close();  
	    conn.close(); // 4、关闭数据库  
//		return sougouTextMapFromSql ;
	}
	
}
