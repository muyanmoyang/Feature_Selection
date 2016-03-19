package muyanmoyang.featureSelection_impr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import muyanmoyang.category.KNN_Classification;

public class KNN_classification_Impro {
	
	public static void main(String[] args) throws IOException { 
		int threshold = 3000  ;
		int k = 10 ;
		List<String> classList = new ArrayList<String>() ; 
		classList.add("ent");
		classList.add("finance");
		classList.add("sports");
		classList.add("IT");
		classList.add("lady");
		Double recall, precision ,Fscore ;
		for(int i=1; i<=10000; i++){
			
			//  Ⅰ相似度排序
			System.out.println("共10000篇测试文本，排序到了第:" + i + "篇文本......") ;
			KNN_Classification.sortOfDistance(threshold, "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/第" + i +"篇测试文本相似度.txt",
					"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/已排序/已排序_第" + i +"篇测试文本相似度.txt") ;
			
			//  Ⅱ 选取K近邻
//			System.out.println("选取KNN近邻，共10000篇测试文本，处理到了第:" + i + "篇文本......"); 
//			KNN_Classification.selectKNeighbors(k ,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/已排序/已排序_第" + i +"篇测试文本相似度.txt",
//					"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/近邻_"+ k +"/第" + i +"篇测试文本相似度.txt") ;
			
		}
//		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/近邻_" + k + "/改进结果.txt"));
//		for(int i=1;i<=10000;i++){
//			//  Ⅲ 分类结果
//				System.out.println("分类，共10000篇测试文本，处理到了第:" + i + "篇文本......"); 
//				String predictLabel = KNN_Classification.classification("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/近邻_" + k + "/第" + i +"篇测试文本相似度.txt",i);
//				writer.write(i + "\t" + predictLabel + "\n") ;
//				writer.flush();
//		}
//		writer.close();
//		
//		for(int i=0; i<classList.size(); i++){
//			String classLabel = classList.get(i) ;
//			// Ⅳ 计算指标
////			System.out.println("计算类别#" + classLabel + "#的指标 ----> 特征词取" + threshold + "个，近邻取" + k + "个......") ;
//			// 真实类别结果集
//			Map<Integer, String> testResultMap = KNN_Classification.getTestRealResult("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/test.txt") ;
//
//			// 准确率
//			precision = KNN_Classification.computePrecision(classLabel,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/近邻_" + k + "/改进结果.txt", testResultMap);
//			// 召回率
//			recall = KNN_Classification.computeRecallrate(classLabel,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/近邻_" + k + "/改进结果.txt", testResultMap);
//			
//			// F值
//			Fscore = 2*precision*recall / (precision + recall) ;
//			System.out.print(Fscore + "\t") ;
//		}
		
	}
}
