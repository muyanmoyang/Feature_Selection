package muyanmoyang.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 *  KNN算法分类————相似度排序选出近邻、分类结果
 * @author hadoop
 *
 */
public class KNN_Classification {
	
	/**
	 *  对每篇测试文本进行相似度排序
	 * @param distanceMap
	 * @param threshold
	 * @throws IOException 
	 */
	public static void sortOfDistance(int threshold, String distanceDir,String sortDisDir) throws IOException{
		FileWriter writer = new FileWriter(new File(sortDisDir)) ;
		FileReader reader = new FileReader(new File(distanceDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<String,Double> distanceMap = new HashMap<String,Double>() ;
		
		String line ;
		if((line=BR.readLine()) == null){
			writer.write("null");
		}
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			distanceMap.put(str[0],Double.parseDouble(str[1])) ;
		}
		
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(distanceMap.entrySet());
        //sort list based on comparator
        Collections.sort(list, new Comparator(){
             public int compare(Object o1, Object o2) 
             {
            	 return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
             }
        });
 
        //put sorted list into map again
        for (Iterator it = list.iterator(); it.hasNext();) {
        	Map.Entry entry = (Map.Entry)it.next();
        	writer.write(entry.getKey() + "\t" + entry.getValue());
        	writer.write("\n"); 
        	writer.flush();
        }
        writer.close();
	}
	
	/**
	 *  选出K个近邻,对于没有近邻的文本进行处理
	 * @param sortDisDir
	 * @param k
	 * @throws IOException 
	 */
	public static void selectKNeighbors(int k ,String sortDisDir, String kNeighborDir) throws IOException{
		FileWriter writer = new FileWriter(new File(kNeighborDir)) ;
		FileReader reader = new FileReader(new File(sortDisDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		String line ;
		int count = 0 ;
		while((line=BR.readLine()) != null){
			count ++ ;
			if(! line.equals("null")){
				String str[] = line.split("\t") ;
				if(count <= k){
					writer.write(str[0] + "\t" + str[1] + "\n") ;
					writer.flush();
				}
			}else{
				writer.write("no neighbor");
			}
		}
	}
	
	/**
	 *  对 待分类文本 进行分类
	 * @param args
	 * @throws IOException
	 */
	public static String classification(String kNeighborDir,int i) throws IOException{
		Map<Integer,Double> neighborMap = new HashMap<Integer,Double>() ;   // 训练文本序号 ： 相似度
		FileReader reader = new FileReader(new File(kNeighborDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<Integer,String> messageAndLabel = getLabel() ; // 获取文本与对应类别
		
		String line ;
		if((line=BR.readLine()) == null){               // 如果无近邻，返回0类
//			if(i>0 && i<289){
//				return "finance" ;
//			}
//			if(i>289 && i<580){
//				return "IT" ;
//			}
//			if(i>580 && i<902){
//				return "society" ;
//			}
//			else{
//				return "sports" ;
//			}
			return "null" ;
		}
		while((line=BR.readLine()) != null){
			String str[] = line.split("\t") ;
			neighborMap.put(Integer.parseInt(str[0]), Double.parseDouble(str[1])) ;  // 近邻--->  训练集文本序号 ： 相似度值
		}
		
		Iterator<Map.Entry<Integer, Double>> it = neighborMap.entrySet().iterator() ;
		
		int ITClass = 0, financeClass = 0, entClass = 0, sportsClass = 0, ladyClass = 0; 
		while (it.hasNext()) {
			Entry<Integer, Double> entry = it.next(); 
			if(messageAndLabel.get(entry.getKey()).equals("ent")){
				entClass ++ ;
			}
			if(messageAndLabel.get(entry.getKey()).equals("finance")){
				financeClass ++ ;
			}
			if(messageAndLabel.get(entry.getKey()).equals("sports")){
				sportsClass ++ ;
			}
			if(messageAndLabel.get(entry.getKey()).equals("IT")){
				ITClass ++ ;
			}
			if(messageAndLabel.get(entry.getKey()).equals("lady")){
				ladyClass ++ ;
			}
		}
		int temp , temp1, temp2, temp3 ;
		temp = entClass > financeClass? entClass : financeClass ;
		temp1 = sportsClass > temp ? sportsClass : temp;
		temp2 = ITClass > temp1 ? ITClass : temp1;
		temp3 = ladyClass > temp2 ? ladyClass : temp2; 
		
		if(ITClass == temp3){
			return "IT" ;
		}
		if(financeClass == temp3){
			return "finance" ;
		}
		if(entClass == temp3){
			return "ent" ;
		}
		if(ladyClass == temp3){
			return "lady" ;
		}else{
			return "sports" ;
		}
	}
	
	/**
	 * 转存类别列表
	 * @return
	 * @throws IOException
	 */
	public static Map<Integer,String> getLabel() throws IOException{
		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/类别列表.txt")) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<Integer,String> messageAndLabel = new HashMap<Integer,String>() ;
		String textLine ;
		while((textLine = BR.readLine())!= null){
			String str[] = textLine.split(" ") ;
			messageAndLabel.put(Integer.parseInt(str[0]),str[1]) ;
		}
		return messageAndLabel ;
	}
	
	/**
	 *  获取真实的类别结果
	 * @param testSetDir
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static Map<Integer,String> getTestRealResult(String testSetDir) throws NumberFormatException, IOException{
		FileReader reader = new FileReader(new File(testSetDir)) ; 
		BufferedReader BR = new BufferedReader(reader) ;
		Map<Integer,String> testResultMap = new LinkedHashMap<Integer,String>() ;
		String line ;
		int count = 0 ;
		while((line=BR.readLine()) != null){ 
			count ++ ;
			String str[] = line.split("\t") ; 
			testResultMap.put(count,str[0]) ;
		}
		return testResultMap ;
	}
	
	/**
	 * 计算召回率
	 * @param args
	 * @throws IOException
	 */
	public static Double computeRecallrate(String classLabel, String resultDir, Map<Integer,String> testResultMap) throws IOException{
		
		FileReader reader = new FileReader(new File(resultDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<Integer,String> resultMap = new LinkedHashMap<Integer,String>() ;
		
		Double resultCount = 0.0 ; // 记录正确分类的个数
		Double recall = 0.0 ; // 召回率
		String line ;
		while((line = BR.readLine()) != null){ 
			String str[] = line.split("\t") ;
			resultMap.put(Integer.parseInt(str[0]),str[1]) ;    // 记录了训练出来的分类结果
		}
		
//		Iterator<Map.Entry<Integer, Integer>> it = testResultMap.entrySet().iterator() ; // 迭代测试集的真正类别结果
//		while(it.hasNext()){
//			Entry<Integer, Integer> entry = it.next() ;
//			if(entry.getValue() == classLabel){
//				classLabelNum ++ ;
//			}
//		}
		
		//  训练准确的个数
		for(Map.Entry<Integer, String> entry1 : testResultMap.entrySet()){
            String m1value = entry1.getValue() ;
            if(m1value.equals(classLabel)){
            	String m2value = resultMap.get(entry1.getKey()) ;
                if (m1value.equals(m2value)){             // 若结果相同
                	resultCount ++ ;
                }
            }
        } 
		recall = resultCount / 2000.0  ;   // 召回率
		System.out.print(recall + "\t") ;
		return recall ;
	}
	
	/**
	 *  计算准确率
	 * @param classLabel
	 * @param resultDir
	 * @param testResultMap    记录了真正的分类结果 <----------来源于测试集
	 * @throws IOException
	 */
	public static Double computePrecision(String classLabel, String resultDir, Map<Integer,String> testResultMap) throws IOException{
		
		FileReader reader = new FileReader(new File(resultDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		
		Map<Integer,String> resultMap = new LinkedHashMap<Integer,String>() ;  // 记录了训练出来的分类结果
		
		Double resultCount = 0.0 ; // 记录正确分类的个数 
		Double numOfError = 0.0 ; // 被错分的classlabel类数目
		Double precision = 0.0 ; // 召回率
		
		String line ;
		while((line = BR.readLine()) != null){
			String str[] = line.split("\t") ;
			resultMap.put(Integer.parseInt(str[0]),str[1]) ;  // // 记录了训练出来的分类结果
		}
		
		Iterator<Map.Entry<Integer, String>> it = resultMap.entrySet().iterator() ; // 迭代测试集结果
		while(it.hasNext()){
			Entry<Integer, String> entry = it.next() ;
			if(entry.getValue().equals(classLabel)){
				if(testResultMap.get(entry.getKey()).equals(classLabel)){
					resultCount ++ ;
				}else{
					numOfError ++ ;
				}
			}
		}
		
		precision = resultCount / (resultCount + numOfError) ;  // 准确率
		System.out.print(precision + "\t") ;
		return precision ;
	}
	
	
	
	/*
	 *  分类、指标计算
	 */
	public static void main(String[] args) throws IOException {
		int threshold = 500 ;
		int k ;
		List<String> classList = new ArrayList<String>() ; 
		classList.add("ent");
		classList.add("finance");
		classList.add("sports");
		classList.add("IT");
		classList.add("lady");
		
		Double recall, precision ,Fscore ;
		
		for(int m=9; m<=10; m++){
			k = m * 10 ;
			File file = new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_"+ k) ;
			if(!file.exists()){
				file.mkdir() ;
			}
			for(int i=1; i<=10000; i++){
				
				//  Ⅰ相似度排序
//				System.out.println("共10000篇测试文本，排序到了第:" + i + "篇文本......") ;
//				sortOfDistance(threshold, "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/第" + i +"篇测试文本相似度.txt",
//						"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/已排序/已排序_第" + i +"篇测试文本相似度.txt") ;
				
				//  Ⅱ 选取K近邻
//				System.out.println("选取KNN近邻，共10000篇测试文本，处理到了第:" + i + "篇文本......"); 
				selectKNeighbors(k ,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/已排序/已排序_第" + i +"篇测试文本相似度.txt",
						"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_"+ k +"/第" + i +"篇测试文本相似度.txt") ;
			}
			FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_" + k + "/结果.txt"));
			for(int i=1;i<=10000;i++){
				 //  Ⅲ 分类结果
//				System.out.println("分类，共10000篇测试文本，处理到了第:" + i + "篇文本......"); 
				String predictLabel = classification("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_" + k + "/第" + i +"篇测试文本相似度.txt",i);
				writer.write(i + "\t" + predictLabel + "\n") ;
				writer.flush();
			}
			writer.close();
			
			for(int i=0; i<classList.size(); i++){
				String classLabel = classList.get(i) ;
				// Ⅳ 计算指标
//				System.out.println("计算类别#" + classLabel + "#的指标 ----> 特征词取" + threshold + "个，近邻取" + k + "个......") ;
				// 真实类别结果集
				Map<Integer, String> testResultMap = getTestRealResult("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/test.txt") ;
				// 准确率
				precision = computePrecision(classLabel,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_" + k + "/结果.txt", testResultMap);
				// 召回率
				recall = computeRecallrate(classLabel,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/近邻_" + k + "/结果.txt", testResultMap);
				// F值
				Fscore = 2*precision*recall / (precision + recall) ;
				System.out.print(Fscore + "\t") ;
			}
			System.out.println();
		}
	}
}



