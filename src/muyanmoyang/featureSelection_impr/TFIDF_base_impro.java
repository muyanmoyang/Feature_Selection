package muyanmoyang.featureSelection_impr;

import java.io.IOException;

import muyanmoyang.featureSelection.TfIdf;

public class TFIDF_base_impro {
	public static void main(String[] args) throws IOException {
		
		int threshold = 3000 ;    // 选择保存threshold个特征词的文本
		TfIdf.computeTfIdf("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + threshold + "特征词/" + "特征词选择_"+ threshold + ".txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/新的测试集2.txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/改进_测试集TFIDF_" + threshold + "特征词/TFIDF.txt",threshold) ;
		TfIdf.computeTfIdf("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + threshold + "特征词/" + "特征词选择_"+ threshold + ".txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/新的训练集2.txt", 
				"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/改进_训练集TFIDF_" + threshold + "特征词/TFIDF.txt",threshold) ;
		
	}
}
