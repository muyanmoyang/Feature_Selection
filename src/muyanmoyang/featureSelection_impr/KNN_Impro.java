package muyanmoyang.featureSelection_impr;

import java.io.IOException;
import java.util.Vector;
import muyanmoyang.category.KNN;

public class KNN_Impro {
	public static void main(String[] args) throws NumberFormatException, IOException { 
		long start = System.currentTimeMillis() ;

    	int threshold = 3000 ; // 选取的特征词数量   
    	
        String testFile = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/改进_测试集TFIDF_" + threshold + "特征词/TFIDF.txt" ;   // 测试集的向量文件
        Vector<Double[]> testVector = KNN.initTestVector(testFile,threshold) ;       // 测试文本的向量表示，每个Double数组代表每篇文本的TF/IDF值集合
        
        String trainFile = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/改进_训练集TFIDF_" + threshold + "特征词/TFIDF.txt";   // 训练集的向量文件
        Vector<Double[]> trainVector = KNN.initTrainVector(trainFile,threshold) ;       // 测试文本的向量表示，每个Double数组代表每篇文本的TF/IDF值集合
       	//计算相似度
        String distanceDir = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/改进相似度"+ threshold + "/" ;
        KNN.caculateDist(trainVector,testVector,distanceDir, threshold);  // 计算相似度
        
        long end = System.currentTimeMillis() ;
        System.out.println("计算时间：" + (end - start)/1000 + "秒......");                
      
        System.gc(); 
        
	}
}
