package muyanmoyang.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import muyanmoyang.featureSelection.TfIdf;

/**
 *  测试集的VSM模型构建
 * @author moyang
 *
 */
public class VSMofTestSet {
	public static void main(String[] args) throws IOException {
		int threshold = 3000 ;
		
		TfIdf.computeTfIdf("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + threshold + "特征词/" + "特征词选择_" + threshold + ".txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/测试集TFIDF_" + threshold + "特征词/TFIDF.txt",threshold) ;
	}
	

	/**
	 *  计算测试集TF/IDF值
	 * @throws IOException 
	 */
	public static void computeTfIdf(String selectWordsDir,String testDir ,String TFIDFWriterDir,int threshold) throws IOException{
		FileReader reader = new FileReader(new File(selectWordsDir)) ; 
		BufferedReader BR = new BufferedReader(reader) ; 
		FileWriter TFIDFWriter = new FileWriter(new File(TFIDFWriterDir)) ;
		
		String featureWords[] = new String[threshold] ; // 加载特征词进数组 
		Map<String,Integer> wordAndTimeMap = getMapOfWordAndTime() ;  // 特征词 ： 在多少篇文本中出现过
		int count = 0 ;
		String line;
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			featureWords[count] = str[0] ;
			count ++ ;
		}
		
		ArrayList<String[]> testTextWithSegment = getTextOfTest(testDir) ;   // 获取测试数据集
		System.out.println("testTextWithSegment: " + testTextWithSegment.size()); 
		Double TF , IDF , TFIDF;
		
			for(int i=0; i<testTextWithSegment.size(); i++){             // 对于每篇文本
				System.out.println("处理到了第" + (i+1) + "篇文本......") ;
				for(int k=0; k<featureWords.length; k++){                         // 对于每一个特征词
					String[] lineWithSegment = testTextWithSegment.get(i) ;  
					String word = featureWords[k] ;
//					System.out.println("词语：" + word); 
					TF = computeTF(word,lineWithSegment) ;
					IDF = computeIDF(word,wordAndTimeMap) ;
					TFIDF = TF * IDF ;
					TFIDFWriter.write(TFIDF + " ");
				}
				TFIDFWriter.write("\n");   
				TFIDFWriter.flush();
			}
		TFIDFWriter.close() ;
	}
	
	private static Map<String,Integer> getMapOfWordAndTime() throws IOException{
		Map<String,Integer> map = new HashMap<String,Integer>() ;
		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词语-出现的文章数.txt"));
		BufferedReader BR = new BufferedReader(reader) ;
 		String line ;
		while((line=BR.readLine()) != null){
			String str[] = line.split(" ") ;
			map.put(str[0],Integer.parseInt(str[1])) ;
		}
		return map ;
	}
	
	/**
	 * 获取训练集，每篇文本存入一个数组，再加载进List集合
	 * @param trainDir
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<String[]> getTextOfTest(String testDir) throws IOException{
		
		FileReader reader = new FileReader(new File(testDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		ArrayList<String[]> list = new ArrayList<String[]>() ; 
		String line ;
		while((line=BR.readLine()) != null){
			String str[] = line.split(" ") ;
			list.add(str) ;
		}
		return list ;
	}
	
	/**
	 *  计算TF值
	 * @param selectWords
	 * @param trainTextWithSegment
	 * @return
	 */
	private static Double computeTF(String word , String[] lineWithSegment) {
		// TODO Auto-generated method stub
		Double TF = 0.0 ; // 记录一个特征词在该文本中出现的次数
		for(int i=0; i<lineWithSegment.length; i++){
			if(word.equals(lineWithSegment[i])){
				TF ++ ; 
			}
		}
		return TF / lineWithSegment.length ;
	}
	
	/**
	 *  计算word的IDF值
	 * @param word
	 * @return
	 */
	private static Double computeIDF(String word ,Map<String,Integer> wordAndTimeMap) {
		// TODO Auto-generated method stub
		Double IDF ; 
		int time = wordAndTimeMap.get(word) ;
		IDF = Math.log((double)(2938.0 / time + 0.01)) ;
		return IDF;
	}
}
