package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import muyanmoyang.category.VSMofTestSet;

public class CHI_TFIDF {
	public static void main(String[] args) throws IOException {
		TfIdf.computeTfIdf("C:/Users/Administrator/Desktop/论文(改小后的数据集)/" + "CHI特征词.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/CHI_TFIDF/CHI_tfidf.txt",100) ; 
		
		// 测试集TFIDF
		
		VSMofTestSet.computeTfIdf("C:/Users/Administrator/Desktop/论文(改小后的数据集)/" + "CHI特征词.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/noStopWordsSegments.txt", 
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/CHI_TFIDF_测试集/CHI_tfidf_test.txt",100) ;
		
	}

}
