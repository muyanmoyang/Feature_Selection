package muyanmoyang.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 *  获取训练数据加入容器并进行文本预处理、停用词、分词处理
 * @author moyang
 *
 */
public class TestSetSegment {
	
	public static void main(String[] args) throws IOException, SQLException {
		Map<Integer,StringBuffer> trainMap = goodWordsinPieceArticle();  // Map[类别：分词文本 ; ......] 
	}
	/*
	 *  获取训练数据
	 */
	private static Map<Integer,String[]> segmentation() throws IOException{
		
		FileReader reader1 = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/test.txt"));
		BufferedReader BR = new BufferedReader(reader1) ;
		Map<Integer,String[]> trainMap = new HashMap<Integer, String[]>() ;  // 用于存储训练数据
		FileWriter segmentResultWriter = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/segmentResult.txt")) ;
		Analyzer anal = new IKAnalyzer(true);  
		int count = 0;
		String tmpStr = "" ;
		String[] tmpSegment = null ;
		String line ;
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			
			StringReader reader = new StringReader(str[1]);
			TokenStream ts = anal.tokenStream("", reader); 
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class); 
			count ++ ;
			while(ts.incrementToken()){
				segmentResultWriter.write(term.toString() + "|") ;
				segmentResultWriter.flush(); 
				tmpStr += term.toString() + "|" ;
		    }
			tmpSegment = tmpStr.split("\\|") ;
			trainMap.put(count,tmpSegment) ;
			tmpStr = "" ;
			System.out.println("将分词结果写入segmentResult.txt文件" + ",这是第"+ count +"次写入");
			reader.close();
			segmentResultWriter.write("\n") ;
			segmentResultWriter.flush();
		}
		segmentResultWriter.close();
		return trainMap ;
	}
	
	/**
	 * 过滤停用词,返回只具有"好词"的语料内容，并序列化,写入noStopWordsSegments.txt文件
	 * @return Map<Integer,StringBuffer> resultMap
	 * @throws IOException 
	 * @throws SQLException
	 */
	public static Map<Integer,StringBuffer> goodWordsinPieceArticle() throws IOException, SQLException
	{
		//将去除停用词后的语料分词结果写入txt文件
		FileWriter noStopWordsSegmentsWriter = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/noStopWordsSegments.txt")) ;  
		Set<String> stopwordsSet = getStopwordsSet("H:/myeclipseWorkspace/Text_Classify/stopwords.txt") ;//停用词处理 , 获取停用词表
		ArrayList<StringBuffer> list = new ArrayList<StringBuffer>() ;
		Map<Integer,StringBuffer> resultMap = new HashMap<Integer,StringBuffer>() ; 
		Map<Integer, String[]> segmentResult = segmentation() ;//中文分词
		int count = 0;
		
		String regex = "[a-zA-Z]+$";
		String regex2 = "^[A-Za-z0-9]+$" ;
		String regex3 = "^[0-9]+[\u4e00-\u9fa5]+$" ;
		String regex4 = "(?![^a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$)" ;
		String regex5 = "^[0-9|a-zA-Z]+[\\+|.|-]$" ;
		String regex6 = "[\u0800-\u4e00]" ;
		
		for(int i=1 ;i<=segmentResult.size() ;i++)
		{
			String[] contentStr = segmentResult.get(i) ;
			for(int j=0 ;j<contentStr.length ;j++)
			{
				if(contentStr[j].matches(regex) || contentStr[j].matches(regex2) || contentStr[j].matches(regex3) 
						|| contentStr[j].matches(regex4) || contentStr[j].matches(regex5) 
						|| contentStr[j].matches(regex6) || stopwordsSet.contains(contentStr[j])) //如果分词结果中出现停用词，则将其置为null
				{
					contentStr[j] = null ;
				}
			}
			//把过滤后的字符串数组存入到一个字符串中
			StringBuffer finalStr = new StringBuffer() ;
			for(int x=0 ;x<contentStr.length ;x++)
			{
				if(contentStr[x] != null)
				{       
					finalStr = finalStr.append(contentStr[x]).append(" "); //将null用" "代替
				}
			}
			list.add(finalStr) ;
			resultMap.put(i,finalStr) ;
			
		}
		for(int m=0;m<list.size();m++)
		{
			count ++ ;
//			System.out.println(list.get(m));
			System.out.println("将过滤后的结果写入文件，这是第" + count + "次写入");
//			noStopWordsSegmentsWriter.write(count + " "); //将文章的标号写入每行的开始处，用作后面方法读取文章标号参考
			noStopWordsSegmentsWriter.write(list.get(m).toString()); 
			noStopWordsSegmentsWriter.write("\n"); 
			noStopWordsSegmentsWriter.flush(); 
		}
		//测试存储内容
//		for(int i=1 ;i<=resultMap.size() ;i++)
//		{
//			System.out.println("******" + resultMap.get(i)); 
//		}
		noStopWordsSegmentsWriter.close(); 
		return resultMap ;
	}
	
	/**
	 * 停用词处理 , 获取停用词表
	 * @param stopFileDir
	 * @return
	 * @throws IOException 
	 */
	public static Set<String> getStopwordsSet(String stopFileDir) throws IOException
	{
		File readerFile = new File(stopFileDir);
		InputStreamReader stopWordsReader = new InputStreamReader(new FileInputStream(readerFile),"UTF-8"); 
		
		Set<String> stopwordsSet = new HashSet<String>() ;
		BufferedReader BR = new BufferedReader(stopWordsReader) ;
		String stopwordsStr ; 
		while((stopwordsStr = BR.readLine()) != null)
		{
			stopwordsSet.add(stopwordsStr) ;
		}
		Iterator it = stopwordsSet.iterator() ;
		while(it.hasNext())
		{
			Object obj = it.next() ;
		}
		return stopwordsSet ;
	}
}



