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
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import muyanmoyang.featureSelection_impr.KNN_Impro;

/**
 *  KNN 算法-----相似度计算
 * @author moyang
 *
 */
public class KNN {
	
    //训练集文档的向量表示
    static HashMap<String, Double> dist = new HashMap<String, Double>();  // 等分类文档互每个训练集文档的距离
//    static Vector<Double[]> testVector = new Vector<Double[]>(); // 待分类文档的向量表示 

    /**
     *  读取测试集TFIDF值
     * @param testFileDir
     * @param threshold
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    public static Vector<Double[]> initTestVector(String testFileDir,int threshold) throws NumberFormatException, IOException {
    	Vector<Double[]> testVector = new Vector<Double[]>(); // 待分类文档的向量表示 
        FileReader fr = new FileReader(new File(testFileDir));
        BufferedReader br = new BufferedReader(fr);
        
        String line ;
        int count = 0 ;
        while ((line = br.readLine()) != null) {
        	
        	Double weightDouble[] = new Double[threshold];
        	count ++ ;
        	System.out.println("加载待分类文档的向量,加载到了第：" + count + "篇测试文档......") ;
        	String weight[] = line.split(" ") ;
        	for(int i=0; i<weight.length ;i++){
        		weightDouble[i] = Double.parseDouble(weight[i]) ;
        	}
        	testVector.add(weightDouble) ;
        	System.gc();
        } 
//        writer.close();
        
        br.close() ;
        return testVector ;
    }
    
    /**
     *  读取训练集TFIDF值
     * @param testFileDir
     * @param threshold
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    public static Vector<Double[]> initTrainVector(String trainFile,int threshold) throws NumberFormatException, IOException {
    	Vector<Double[]> trainVector = new Vector<Double[]>();
    	FileWriter writer = null ; 
    	
    	FileReader fr = new FileReader(trainFile);
    	BufferedReader br = new BufferedReader(fr);
    	
    	int count = 0; 
    	String line ;
    	while ((line = br.readLine()) != null) {
    		Double weightDouble[] = new Double[threshold];
    		count ++ ;
//    		System.out.println("加载训练文档的向量,加载到了第：" + count + "篇训练文档......") ;
    		String weight[] = line.split(" ") ;
        	for(int i=0; i<weight.length ;i++){
        		weightDouble[i] = Double.parseDouble(weight[i]) ;
        	}
        	trainVector.add(weightDouble);
    		System.gc();
    	}
        
        br.close() ;
        return trainVector ;
    }

    /**
     * 计算相似度
     * @param trainFile
     * @param testVector
     * @param distanceFileDir
     * @param threshold
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void caculateDist(Vector<Double[]> trainVector ,Vector<Double[]> testVector,String distanceFileDir, int threshold) throws NumberFormatException, IOException {
       
    	FileWriter writer = null ;  
    	double distance = 0.0 ;     //
    	boolean flag = false ;     // 标记是否已经计算过了相似度
    	
    	for(int i=0; i<testVector.size(); i++){
//    		System.out.println("计算测试每篇测试文档与训练文档的相似度,计算到了第:" + (i+1) + "篇测试文档......") ;
    		Double[] testLine = testVector.get(i) ;
    		
    		 
    		writer = new FileWriter(new File(distanceFileDir + "第" + (i+1) +"篇测试文本相似度.txt")) ;
    		for(int j=0; j<trainVector.size(); j++){
//    			System.out.println("正在和第" + (j+1) + "篇训练文档进行相似度计算......") ;
    			Double[] trainLine = trainVector.get(j) ;
    			for(int k=0;k<trainLine.length; k++){
    				if(flag==false && trainLine[k]!=0.0 && testLine[k]!=0.0){
    					distance = cos(trainLine, testLine);       // 相似度值
    					flag = true ;
    				}
    			}
    			if(distance > 0.0 && distance != 1.0){
    				writer.write((j+1) + "\t" + distance);
    				writer.write("\n"); 
    				writer.flush();
    			}
    			distance = 0.0 ;
    			flag = false ;  // 计算过了上一篇测试文本的相似度，开始计算下一篇，将flag置为false
    		 }
    	 }
    	 writer.close();
    	
//    	 dist.put(filename, d);
//    	 System.out.println("到"+filename+"的距离是"+d);
    }

    // 计算两个向量的夹角的余弦。如果此值的绝对值越大，说明夹角越小，越相似，距离越近。
    public static double cos(Double[] trainLine, Double[] testLine) { 
        double res = 0.0;
        double mul = 0.0;
        double p1 = 0.0, p2 = 0.0;
        int len = trainLine.length ;
        
        for (int i = 0; i < len; i++) {
//        	System.out.println("在读取第" + (i+1) + "个向量......"); 
            Double x = trainLine[i] ;
            Double y = testLine[i] ;
            mul += x * y;                // (Xi * Yi) 求和
            p1 += Math.pow(x, 2);          //  (Xi)的平方求和
            p2 += Math.pow(y, 2);          //  (Yi)的平方求和
        }	
        res = Math.abs(mul) / Math.sqrt(p1 * p2);  //cos(θ) 相似度计算
        return res;
    }

  //Map按value进行排序 
	public static ArrayList<Entry<String,Double>> sort(HashMap<String,Double> arg){
		ArrayList<Entry<String,Double>> al=new ArrayList<Entry<String,Double>>(arg.entrySet());
		Collections.sort(al, new Comparator<Map.Entry<String,Double>>(){
			public int compare(Map.Entry<String, Double> o1,Map.Entry<String,Double> o2){
				double res = o2.getValue()-o1.getValue();
				if(res < 0)
					return -1;
				else if(res > 0)
					return 1;
					else
						return 0;
			}
		});
		return al;
	}
    
    public static void main(String[] args) throws NumberFormatException, IOException{
        
    	long start = System.currentTimeMillis() ;
    	int threshold = 500 ; // 选取的特征词数量
    	
        String testFile = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/测试集/测试集TFIDF_" + threshold + "特征词/TFIDF.txt" ;   // 测试集的向量文件
//        String testFile = "C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试_测试集TFIDF.txt" ;   // 测试集的向量文件
        Vector<Double[]> testVector = initTestVector(testFile,threshold) ;       // 测试文本的向量表示，每个Double数组代表每篇文本的TF/IDF值集合
        
        String trainFile = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/"+ threshold + "特征词/TFIDF.txt";   // 训练集的向量文件
//        String trainFile = "C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试_训练集TFIDF.txt";   // 训练集的向量文件
        Vector<Double[]> trainVector = initTrainVector(trainFile,threshold) ;       // 测试文本的向量表示，每个Double数组代表每篇文本的TF/IDF值集合
        
//        System.out.println(testVector.size()) ; 
//        FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/测试.txt")) ;
//        for(int i=0; i<testVector.size(); i++){
//        	Double[] tmp = testVector.get(i);
//        	for(int j=0; j<tmp.length; j++){
//        		writer.write(tmp[j] + " ");
//        	}
//        	writer.write("\n");
//        	writer.flush();
//        }
//        writer.close();
       
        String distanceDir = "C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/相似度"+ threshold + "/" ;
        caculateDist(trainVector,testVector,distanceDir, threshold);  // 计算相似度
//        knnClassification(3);      // KNN分类
        long end = System.currentTimeMillis() ;
        System.out.println("计算时间：" + (end - start)/1000 + "秒......");
        
        
    }
    
    
    
}