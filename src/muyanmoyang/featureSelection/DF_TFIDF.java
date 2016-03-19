package muyanmoyang.featureSelection;

import java.io.IOException;

import muyanmoyang.category.VSMofTestSet;

public class DF_TFIDF {
	public static void main(String[] args) throws IOException {
		TfIdf.computeTfIdf("C:/Users/Administrator/Desktop/论文(改小后的数据集)/DFofWords.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/DF_TFIDF/DF_tfidf.txt",100) ; 
		
		// 测试集TFIDF
		
		VSMofTestSet.computeTfIdf("C:/Users/Administrator/Desktop/论文(改小后的数据集)/DFofWords.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/DF_TFIDF_测试集/DF_tfidf_test.txt",100) ;
		
	}
}
