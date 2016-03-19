package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  IF/IDF值计算
 * @author moyang
 *
 */
public class TfIdf {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis() ;
		int threshold = 3000 ;    // 选择保存threshold个特征词的文本
		computeTfIdf("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + threshold + "特征词/" + "特征词选择_"+ threshold + ".txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + threshold + "特征词/" + "TFIDF.txt",threshold) ; 
		long end = System.currentTimeMillis() ;
		System.out.println("处理时间:" + (end - start)/1000 + "s"); 
	}
	
	/**
	 *  获取选出的特征词集合加载进内存
	 * @throws IOException 
	 */
	private static Map<String,Double> getMapOfSelectWords(String selectWordsFileDir) throws IOException{
		Map<String,Double> selectWordsMap = new HashMap<String, Double>() ;
		FileReader reader = new FileReader(new File(selectWordsFileDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		String line ;
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			selectWordsMap.put(str[0],Double.parseDouble(str[1])) ;
		}
		return selectWordsMap;
	}
	
	/**
	 * 获取训练集，每篇文本存入一个数组，再加载进List集合
	 * @param trainDir
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<String[]> getTextOfTrain(String trainDir) throws IOException{
		
//		FileReader reader = new FileReader(new File(trainDir)) ;
//		BufferedReader BR = new BufferedReader(reader) ;
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(trainDir), "UTF-8"));
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
		IDF = Math.log((double)(20000.0 / time + 0.01)) ;
		return IDF;
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
	 *  计算TF/IDF值
	 * @throws IOException 
	 */
	public static void computeTfIdf(String selectWordsDir,String trainDir ,String TFIDFWriterDir,int threshold) throws IOException{
		FileReader reader = new FileReader(new File(selectWordsDir)) ; 
		BufferedReader BR = new BufferedReader(reader) ; 
//		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(selectWordsDir), "GBK"));
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
		
		ArrayList<String[]> trainTextWithSegment = getTextOfTrain(trainDir) ;   // 获取训练数据集
		System.out.println("trainTextWithSegment: " + trainTextWithSegment.size()); 
		Double TF , IDF , TFIDF;
		
			for(int i=0; i<trainTextWithSegment.size(); i++){             // 对于每篇文本
				System.out.println("处理到了第" + (i+1) + "篇文本......") ;
				for(int k=0; k<featureWords.length; k++){                         // 对于每一个特征词
					String[] lineWithSegment = trainTextWithSegment.get(i) ;  
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



	
	
	
}
