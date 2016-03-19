package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import muyanmoyang.featureSelection_impr.FrequentItems;
import muyanmoyang.utils.KeyUtils;

/**
 *  CHI开方检验特征提取算法
 * @author moyang
 *
 */
public class CHI {
	
	/*
	 *   转存类别列表
	 */
	public static List<String> getLabel() throws IOException{
		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/类别列表.txt")) ;
		BufferedReader BR = new BufferedReader(reader) ;
		int count = 0 ;
		List<String> list = new ArrayList<String>() ;
		String textLine ;
		while((textLine = BR.readLine())!= null){
			String str[] = textLine.split(" ") ;
//			System.out.println(count + ":" + str.length); 
			list.add(str[1]);
			count ++ ;
		}
		return list ;
	}
	
	/**
	 *  读取词袋模型，存储到集合Map
	 * @param fileOfBag   词袋文件
	 * @return Map<String, Vector<KeyUtils>> 词袋模型集合   实例：[  要见 :11458 1;12248 1;111129 1;146004 1;315091 1;390447 1;446011 1;500701 1;625715 1; 9 ]
	 * @throws IOException 
	 */
	private static Map<String, Vector<KeyUtils>> readBofWords(String fileOfBag) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Vector<KeyUtils>>  bagMap = new HashMap<String, Vector<KeyUtils>>() ; // 词袋模型
		Vector<KeyUtils> vector = new Vector<KeyUtils>() ;
		FileReader reader = new FileReader(new File(fileOfBag)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		int count = 0 ;
		String line ;
		while((line=BR.readLine())!=null){
			count ++ ;
			System.out.println("读取词袋文件加载进Map集合，词袋文件共35248行，读取到了第" + count + "行......") ;
			if(vector == null)
			{
				vector = new Vector<KeyUtils>() ;
			}
			String str[] = line.split(" :") ;
			String str2 = str[1] ;
			String keyUtilsStr[] = str2.split(";") ;
			for(int i=0; i<keyUtilsStr.length-1; i++){
				String tempStr[] = keyUtilsStr[i].split(" ") ;
				KeyUtils keyUtils = new KeyUtils(Integer.parseInt(tempStr[0]),Integer.parseInt(tempStr[1])) ;
				vector.add(keyUtils) ;
			}
			bagMap.put(str[0],vector) ;
			vector = null ;
		}
		return bagMap;
	}
	
	
	/**
	 * 计算某个词语的相关统计量
	 * @param word          ： String                        词语集合
	 * @param bagOfWordsMap ： Map<String,Vector<KeyUtils>>  词袋统计子模型
	 * @param classLabel    ： List<String>                  类别数组
	 * @param label         ： String                        类别
	 */
	private static Double computeBasicStatistics(String word, Map<String,Vector<KeyUtils>> bagOfWordsMap, List<String> classLabel,String label){
		
		Double A = 0.0 ,B =0.0 , C = 0.0 , D = 0.0 ;
		Double chiSquare ;
		List<Integer> tempList = new ArrayList<Integer>() ; // 记录word出现的文本序号
		List<Integer> articleList = new ArrayList<Integer>() ; // 不包含word的文本序号
		for(int i=1; i<=20000; i++){
			articleList.add(i) ;
		}
		
		Vector<KeyUtils> vectorKeyUtils = bagOfWordsMap.get(word) ;
			
		for(int k=0; k<vectorKeyUtils.size(); k++){
			tempList.add(vectorKeyUtils.get(k).getX()) ; //记录word出现的文本序号
		}
		articleList.removeAll(tempList) ; //构建不含word的文本序号集合 , tempList 记录的是word没有出现过的文本序号
		
		for(int j=0; j<vectorKeyUtils.size(); j++){  // 包含word的文本中 label数目A 和非label类的数目B
			KeyUtils keyUtils = vectorKeyUtils.get(j) ;
			if(classLabel.get(keyUtils.getX()-1).equals(label)){
				A ++ ;
			}else{
				B ++ ;
			}
		}	
		
		for(int i=0; i<articleList.size(); i++){   // 不包含word的文本中 ，label数目 C 和 非label数目D
			if(classLabel.get(articleList.get(i)-1).equals(label)){   // 
				C ++  ;
			}else{
				D ++ ;
			}
		}
		chiSquare = 20000.0 * Math.pow((A*D - B*C), 2) / ((A + B) * (C + D)) ;
		System.out.println(chiSquare + "*****************") ;
		return chiSquare ;         // 返回word对于类label的CHI值
	}
	
	
	public static void main(String[] args) throws IOException {
//		String label ;
		Double chiSquare_IT, chiSquare_finance,chiSquare_ent,chiSquare_sports ,chiSquare_lady;
		String word ;
		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/频繁项集中的词倾向的类别.txt")) ;
		//获取词语集合
		List<String> characterList =  FrequentItems.getWordOfFrequent(); 
		// 获取词袋模型
		Map<String,Vector<KeyUtils>> bagOfWordsMap = readBofWords("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词袋.txt") ;
		// 获取类别数组
		List<String> classLabel = getLabel() ;
		
		for(int i=0; i<characterList.size(); i++){
			System.out.println("计算频繁项集每个词语的CHI值，计算到了第" + (i+1) + "个词语......") ;
			word = characterList.get(i) ;  // 获取每一个word
			System.out.println(word);
			chiSquare_ent = computeBasicStatistics(word, bagOfWordsMap, classLabel, "ent") ; // 计算出word对于label类的CHI值
			chiSquare_finance = computeBasicStatistics(word, bagOfWordsMap, classLabel, "finance") ; // 计算出word对于label类的CHI值
			chiSquare_IT = computeBasicStatistics(word, bagOfWordsMap, classLabel, "IT") ; // 计算出word对于label类的CHI值
			chiSquare_sports = computeBasicStatistics(word, bagOfWordsMap, classLabel, "sports") ; // 计算出word对于label类的CHI值
			chiSquare_lady = computeBasicStatistics(word, bagOfWordsMap, classLabel, "lady") ; // 计算出word对于label类的CHI值
			
			Double temp , temp1, temp2 ,temp3;
			temp = chiSquare_ent > chiSquare_finance? chiSquare_ent : chiSquare_finance ;
			temp1 = chiSquare_IT > temp ? chiSquare_IT : temp;
			temp2 = chiSquare_sports > temp1 ? chiSquare_sports : temp1;
			temp3 = chiSquare_lady > temp2 ? chiSquare_lady : temp2;
			if(chiSquare_IT == temp3){
				writer.write(word + "\t" + "IT" + "\n");
			}else if(chiSquare_finance == temp3){
				writer.write(word + "\t" + "finance" + "\n");
			}else if(chiSquare_ent == temp3){
				writer.write(word + "\t" + "ent" + "\n");
			}else if(chiSquare_lady == temp3){
				writer.write(word + "\t" + "lady" + "\n");
			}else{
				writer.write(word + "\t" + "sports" + "\n");
			}
		}
		writer.close();
	}
	

}



