package muyanmoyang.featureSelection_impr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 *  扩展测试集
 */
public class FrequentItems{
	
	public static void main(String[] args) throws IOException {
//		removeSameOfTrain() ;
//		getTextWithNum();
		
//		contransformNum2Word();
		
//		filterFrequent();
		
		extendWithFrequentItems();   // 对测试集进行扩展
		
		combineLine() ;  // 对扩展后的样本进行合并  
		fullInTest();  // 用原始测试集填补为null的文本
	} 
	
	/*
	 *  根据频繁项集对数据集文本进行词扩展
	 */
	private static void extendWithFrequentItems() throws IOException {
		// TODO Auto-generated method stub 
				BufferedReader BR2 = new BufferedReader(new InputStreamReader(new FileInputStream(
						"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/过滤的频繁项集_word.txt"),"UTF-8")) ;
				BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
						"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/noStopWordsSegments.txt"),"GBK")) ;
				
				String line3 ;
				BufferedReader BR3 = new BufferedReader(new InputStreamReader(new FileInputStream(
						"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集中的词倾向的类别.txt"),"UTF-8")) ;
				Map<String,String> frequentWord = new LinkedHashMap<String,String>() ;
				while((line3=BR3.readLine()) != null){
					String str[] = line3.split("\t") ;
					System.out.println(str.length);
					frequentWord.put(str[0],str[1]) ;
				}
				
				FileWriter writer = null ;
				
				String line ,line2;
				Map<Integer,String[]> frequentWordMap = new LinkedHashMap<Integer,String[]>() ;
				
				int count2 = 0;
				while((line2=BR2.readLine()) != null){
					count2 ++ ;
					String str[] = line2.split(" ") ;
					frequentWordMap.put(count2,str) ;
				}
				int count = 0 ;
				String trainLabel = null ; 
				while((line=BR.readLine()) != null){
					String str1[] = line.split(" ") ;
					System.out.println(line);
					count ++ ;
					if(count <= 2000){
						trainLabel = "ent" ;
					}
					if(count > 2000 && count <= 4000){
						trainLabel = "finance" ;
					}
					if(count > 4000 && count <= 6000){
						trainLabel = "sports" ;
					}
					if(count > 6000 && count <= 8000){
						trainLabel = "IT" ;
					}
					if(count > 8000 && count <= 10000){
						trainLabel = "lady" ;
					}
					System.out.println("合并到了第" + count +"篇文本......") ;
					writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/扩展/"+count+"_新的测试数据集.txt")) ;
					for(int i=0;i<frequentWordMap.size(); i++){
						String str2[] = frequentWordMap.get(i+1) ;
						// 频繁项词的倾向类别 = 训练文本所属类别
//						System.out.println(frequentWord.get(str2[0]) + "************************"); 
						if(frequentWord.get(str2[0]).equals(trainLabel)){ 
							List<String> result = combineWithFrequent(str1,str2) ;
							if(result != null){
								for(int j=0; j<result.size(); j++){
									writer.write(result.get(j) + " ");
								}
								writer.write("\n");
								writer.flush();
							}
						}
					}
				}
				writer.close();
	}
	
	private static List<String> combineWithFrequent(String[] str1, String[] str2) {
		// TODO Auto-generated method stub
		List<String> list1 = new ArrayList<String>() ;
		List<String> list2 = new ArrayList<String>() ;
		List<String> tempList = new ArrayList<String>() ;
		boolean flag = false ;
		int num = 0 ;
		for(int i=0;i<str1.length; i++){
			list1.add(str1[i]) ;
		}
		for(int i=0;i<str2.length; i++){
			list2.add(str2[i]) ;
		}
		for(int i=0;i<str2.length;i++){
			if(list1.contains(str2[i])){
//				num ++ ;
				tempList.add(str2[i]) ;
//				if(num > 1){
					flag = true ;
//				}
			}
		}
		if(flag == true){
			list2.removeAll(tempList) ;
			list1.addAll(list2) ;
			return list1 ;
		}else{
			return null ;
		}
		
	}

	/*
	 *  根据CHI值计算出来的类别偏向进行频繁项集过滤
	 */
	private static void filterFrequent() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集中的词倾向的类别.txt"),"GBK")) ;
		BufferedReader BR2 = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集_word.txt"),"UTF-8")) ;
		FileWriter writer = new FileWriter(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/过滤的频繁项集_word.txt")) ;
		String line ;
		Map<String,String> frequentWordMap = new LinkedHashMap<String,String>() ;
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			frequentWordMap.put(str[0],str[1]) ;
		}
		String line2 ;
		while((line2=BR2.readLine()) != null){
			boolean flag = true ;
			String str[] = line2.split(" ") ;
			for(int i=0; i<str.length; i++){
				for(int j=i+1; j<str.length; j++){
					if(!frequentWordMap.get(str[i]).equals(frequentWordMap.get(str[j]))){
						flag = false ;
					}
				}
			}
			if(flag == true){
				writer.write(line2 + "\n");
				writer.flush();
			}
		}
		writer.close();
	}


	/*
	 *  将频繁项集里的序号转化为词语集合
	 */
	private static void contransformNum2Word() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集.txt"),"UTF-8")) ;
		BufferedReader BR2 = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词语集合.txt"),"GBK")) ;
		FileWriter writer = new FileWriter(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集_word.txt")) ;	
		String line ;
		int count = 0 ;
		Map<Integer,String> wordMap = new LinkedHashMap<Integer,String>() ;
 		while((line=BR2.readLine())!=null){
			count ++ ;
			wordMap.put(count,line) ;
		}
 		String line2 ;
 		while((line2=BR.readLine())!=null){
			String str[] = line2.split("\t") ;
			String str2[] =str[0].split(",") ; 
			for(int i=0; i<str2.length; i++){
				writer.write(wordMap.get(Integer.parseInt(str2[i])) + " ");
			}
			writer.write("\n");
			writer.flush();
		}
 		writer.close();
	}

	/*
	 *  将训练文本转成词语序号组成的文本
	 */
	private static void getTextWithNum() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/newNoStopWordsSegments.txt"),"UTF-8")) ;
		BufferedReader BR2 = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词语集合.txt"),"GBK")) ;
		FileWriter writer = new FileWriter(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/textWithWordNum.txt")) ;	
		String line ;
		int count = 0 ;
		Map<String,Integer> wordMap = new LinkedHashMap<String,Integer>() ;
 		while((line=BR2.readLine())!=null){
			count ++ ;
			wordMap.put(line,count) ;
		}
 		String line2 ;
 		while((line2=BR.readLine())!=null){
			String str[] = line2.split(" ") ;
			for(int i=0; i<str.length; i++){
				writer.write(wordMap.get(str[i]) + " ");
			}
			writer.write("\n");
			writer.flush();
		}
 		writer.close();
	}

	/*
	 *  去除原始训练数据集分词后的文本中的重复词语
	 */
	private static void removeSameOfTrain() throws IOException { 
		// TODO Auto-generated method stub 
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/noStopWordsSegments.txt"),"GBK")) ;
		FileWriter writer = new FileWriter(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/newNoStopWordsSegments.txt")) ;	
		String line ;
		while((line=BR.readLine())!=null){
			List<String> list = new ArrayList<String>() ;
			String str[] = line.split(" ") ;
			for(int i=0;i<str.length; i++){
				if(!list.contains(str[i])){
					list.add(str[i]) ;
				}
			}
			for(int i=0; i<list.size(); i++){
				writer.write(list.get(i) + " ") ;
			}
			writer.write("\n");
			writer.flush();
		}
		writer.close();
	}


	/*
	 *  合并扩展文本
	 */
	private static void combineLine() throws IOException{
		FileWriter writer = new FileWriter(new File(
					"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/新的测试集.txt")) ;	
		for(int i=1; i<=10000; i++){
			System.out.println("处理到了第" + i + "篇测试文本......") ;
			BufferedReader BR = new BufferedReader(new FileReader(new File(
					"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/扩展/" + i + "_新的测试数据集.txt"))) ;
			String line = BR.readLine() ;
			String result = line ;
			while((line=BR.readLine())!=null){
				result = combine(result,line) ;
			}
			writer.write(result + "\n");
			writer.flush();
		}
		writer.close();
	}

	private static String combine(String result, String line) {
		// TODO Auto-generated method stub
		String str1[] = result.split(" ") ;
		String newStr = "" ;
		List<String> list = new ArrayList<String>() ;
		for(int i=0; i<str1.length; i++){
			list.add(str1[i]) ;
		}
		String str2[] = line.split(" ") ;
		for(int i=0;i<str2.length; i++){
			if(!list.contains(str2[i])){ // 如果line中出现result里面没有的词语
				list.add(str2[i]) ;
			}
		}
		
		for(int i=0; i<list.size(); i++){
			newStr = newStr + list.get(i) + " " ;
		}
		return newStr ;
	}
	
	/*
	 *  填补为null的文本
	 */
	private static void fullInTest() throws IOException{ 
		FileWriter writer = new FileWriter(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/新的测试集2.txt")) ;
		BufferedReader BR = new BufferedReader(new FileReader(new File(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/新的测试集.txt"))) ;
		BufferedReader BR2 = new BufferedReader(new InputStreamReader(new FileInputStream(
					"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/noStopWordsSegments.txt"), "GBK"));
		Map<Integer,String> noStopWordsMap = new LinkedHashMap<Integer,String>() ;
		String line ;
		int count =0 ;
		while((line=BR2.readLine()) != null){
			count ++ ;
			noStopWordsMap.put(count ,line) ;
		}
		
		String line2 ;
		int count2 = 0;
		while((line2=BR.readLine())!=null){
			count2 ++ ;
			System.out.println("处理到了第" + count2 + "篇测试文本......") ;
			if(!line2.equals("null")){
				writer.write(line2 + "\n"); 
				writer.flush();
			}else{
				writer.write(noStopWordsMap.get(count2) + "\n");
				writer.flush();
			}
		}
		writer.close();
	}

	public static List<String> getWordOfFrequent() throws IOException { 
		// TODO Auto-generated method stub
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集_word.txt"), "UTF-8"));
		String line ;
		List<String> list = new ArrayList<String>() ;
		while((line=BR.readLine())!=null){
			String str[] = line.split(" ") ;
			for(int i=0;i<str.length; i++){
				if(!list.contains(str[i])){
					list.add(str[i]) ;
				}
			}
		}
		return list ;
	}
	
	
}