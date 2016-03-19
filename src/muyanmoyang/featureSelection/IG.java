package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import muyanmoyang.utils.KeyUtils;

/**
 * 信息增益，特征词选择
 * @author moyang
 *
 */
public class IG {
	/*
	 * 计算某个特征词出现的概率，该词出现的文档数 / 总的文档数
	 */
	private static Double computePofWord(Map<String,String> characterAndTimes,String word) throws IOException{
		
		System.out.println(characterAndTimes.get(word));
		return (Double.parseDouble(characterAndTimes.get(word)) / 20000.0) ;
	}
	
	/*
	 *  获取词语和该词语出现的文本数目
	 */
	private static Map<String,String> numOfAppearInText() throws IOException{
		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词袋.txt")) ;
		BufferedReader BR = new BufferedReader(reader) ;
		
		Map<String,String> dicMap = new HashMap<String,String>() ;
		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词语-出现的文章数.txt")) ;
		String line ;
		while((line = BR.readLine()) != null){
			String str[] = line.split(" ") ;
			dicMap.put(str[0],str[str.length-1]) ;
			writer.write(str[0] + " " + str[str.length-1]); 
			writer.write("\n"); 
			writer.flush();
		}
		writer.close();
		return dicMap ;
	}
	
	/**
	 * 计算 P(Ci|t)表示出现T的时候，类别Ci出现的概率，只要用出现了T并且属于类别Ci的文档数除以出现了T的文档数
	 * @param word               词语
	 * @param Ci                 类别  
	 * @param bagOfWordsMap      词袋模型
	 * @param characterAndTimes  词语 ： 出现的文章数目 
	 * @return                   返回P(Ci|word)
	 * @throws IOException
	 */
	private static Double computePofCiInFeatureT(String word ,String Ci,Map<String,Vector<KeyUtils>> bagOfWordsMap,
			Map<String,String> characterAndTimes,List<String> classLabel) throws IOException{

		Double titleCount = 0.0 ; 				// 出现了该word并且属于类别Ci的文档数
		Vector<KeyUtils> vector = bagOfWordsMap.get(word) ;
		for(int i=0; i<vector.size(); i++){
			KeyUtils keyutils = vector.get(i) ;
			if(classLabel.get(keyutils.getX()-1).equals(Ci)){   // 出现了该word，并且该word属于Ci类的数目
				titleCount ++  ;
			}
		}
		if(titleCount == 0.0){
			return Double.MIN_VALUE ;
		}else{
			Double wordOfNumArticle = Double.parseDouble((characterAndTimes.get(word))) ; // 出现了该word，并且该word属于Ci类的数目 / 出现了该word的文档数
			return (titleCount / wordOfNumArticle) ;
		}
	}
	
	/**
	 * 计算 P(Ci|T拔)表示不出现T的时候，类别Ci出现的概率，只要用不出现T并且属于类别Ci的文档数除以不出现T的文档数
	 * @param word               词语
	 * @param Ci                 类别  ： "1" or "0"
	 * @param bagOfWordsMap      词袋模型
	 * @param characterAndTimes  词语 ： 出现的文章数目 
	 * @return                   返回P(Ci|word)
	 * @return
	 */
	private static Double computePofCiWithoutFeatureT(String word ,String Ci,Map<String,Vector<KeyUtils>> bagOfWordsMap,
			Map<String,String> characterAndTimes,List<String> classLabel) {
		// TODO Auto-generated method stub
		Double titleCount = 0.0 ; 				// 不出现该word并且属于类别Ci的文档数
		
		List<Integer> articleList = new ArrayList<Integer>() ;
		List<Integer> tempList = new ArrayList<Integer>() ; // 记录word出现的文本序号
		Vector<KeyUtils> vector = bagOfWordsMap.get(word) ;
		for(int i=1; i<=20000; i++){
			articleList.add(i) ;
		}
		for(int i=0; i<vector.size(); i++){
			tempList.add(vector.get(i).getX()) ;
		}
		articleList.removeAll(tempList) ; //构建不含word的文本序号集合 , tempList 记录的是word没有出现过的文本序号

		for(int i=0; i<articleList.size(); i++){
			if(classLabel.get(articleList.get(i)-1).equals(Ci)){   // 不出现该word，并且属于Ci类
				titleCount ++  ;
			}
		}
		
		Double wordOfNumArticle = 20000.0 - Double.parseDouble((characterAndTimes.get(word))) ; // 不出现该word的文档数
		return (titleCount / wordOfNumArticle) ;  // 不出现该word，并且属于Ci类的文档数 / 不出现该word的文档数
	}
	
	
	/*
	 *   转存类别列表
	 */
	public static List<String> getLabel() throws IOException{
		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/train.txt")) ;
		BufferedReader BR = new BufferedReader(reader) ;
		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/类别列表.txt")) ;
		int count = 0 ;
		List<String> list = new ArrayList<String>() ;
		String textLine ;
		while((textLine = BR.readLine())!= null){
			String str[] = textLine.split("\t") ;
//			System.out.println(count + ":" + str.length); 
			writer.write(count+1 + " " + str[0] + "\n");  //将类别落地磁盘 
			writer.flush();
			list.add(str[0]);
			count ++ ;
		}
		writer.close();
		return list ;
	}
	
	
//	private static Map<String,String>  wordAndNumofarticle() throws IOException{
//		FileReader reader = new FileReader(new File("C:/Users/Administrator/Desktop/论文(改小后的数据集)/词袋.txt")) ;
//		BufferedReader BR = new BufferedReader(reader) ;
//		
//		Map<String,String> dicMap = new HashMap<String,String>() ;
//		String line2 ;
//		while((line2 = BR.readLine()) != null){
//			String str[] = line2.split(" ") ;
//			dicMap.put(str[0],str[str.length-1]) ; // 词：出现的总的文档数
//		}
//		return dicMap ;
//	}
	
	
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
			System.out.println("读取词袋文件加载进Map集合，读取到了第" + count + "行......") ;
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
	 *  获取词语集合
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	private static Vector<String> getCharacterList(String characterDir) throws IOException {
		// TODO Auto-generated method stub
		Vector<String> vector = new Vector<String>() ;
		FileReader reader = new FileReader(new File(characterDir)) ;
		BufferedReader BR = new BufferedReader(reader) ; 
		String line ;
		while((line=BR.readLine())!=null){
			vector.add(line) ;
		}
		return vector;
	}
	
	/*
	 * 计算信息增益(IG)
	 */
	public static void main(String[] args) throws IOException, SQLException { 
//		computePofWord() ;
		Vector<String> characterList = getCharacterList("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词语集合.txt") ;  
		Map<String,Vector<KeyUtils>> bagOfWordsMap = readBofWords("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/词袋.txt") ; 
		
		// 将各个词语的IG值写入文件
		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/IGOfWords.txt")) ;
		
		Double PofWord ,PofC1InFeatureT ,PofC0InFeatureT, PofC0WithoutFeatureT, PofC1WithoutFeatureT,PofC2InFeatureT,PofC3InFeatureT,PofC2WithoutFeatureT,PofC3WithoutFeatureT,PofC4InFeatureT,PofC4WithoutFeatureT;
		double IG1, IG2, IG3, IG;
		//ent
		double pOfC0 = 4000.0 / 20000.0;
		//finance
		double pOfC1 =  pOfC0;
		//sports
		double pOfC2 = pOfC0 ;
		//IT
		double pOfC3 = pOfC0 ;
		//lady
		double pOfC4 = pOfC0 ;
		
		String line ;
		int count = 0 ;  //执行计数器
		Map<String,String> characterAndTimes = numOfAppearInText() ;  //获取词语和该词语出现的文本数目   : 词语 --- 出现的次数
		List<String> classLabel = getLabel() ;//类别数组
		for(int i=0; i<characterList.size(); i++){
			count ++ ;
			System.out.println("计算每个特征词的信息增益值IG，已处理到了第" + count + "个词语......");
			PofWord = computePofWord(characterAndTimes,characterList.get(i)) ;
			System.out.println("PofWord:   " + PofWord); 
			
			// IT
			PofC0InFeatureT = computePofCiInFeatureT(characterList.get(i),"ent",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("ent    " + PofC0InFeatureT);
			//finance
			PofC1InFeatureT = computePofCiInFeatureT(characterList.get(i),"finance",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("finance    " + PofC1InFeatureT);
			//society
			PofC2InFeatureT = computePofCiInFeatureT(characterList.get(i),"sports",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("sports    " + PofC2InFeatureT);
			//sports
			PofC3InFeatureT = computePofCiInFeatureT(characterList.get(i),"IT",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("IT    " + PofC3InFeatureT);
			// lady
			PofC4InFeatureT = computePofCiInFeatureT(characterList.get(i),"lady",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("lady    " + PofC4InFeatureT);
			
			//TODO   计算
			// IT
			PofC0WithoutFeatureT = computePofCiWithoutFeatureT(characterList.get(i),"ent",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("ent    " + PofC0WithoutFeatureT); 
			//finance
			PofC1WithoutFeatureT = computePofCiWithoutFeatureT(characterList.get(i),"finance",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("finance    " + PofC1WithoutFeatureT);
			//society
			PofC2WithoutFeatureT = computePofCiWithoutFeatureT(characterList.get(i),"sports",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("sports    " + PofC2WithoutFeatureT); 
			//sports
			PofC3WithoutFeatureT = computePofCiWithoutFeatureT(characterList.get(i),"IT",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("IT    " + PofC3WithoutFeatureT);
			//lady
			PofC4WithoutFeatureT = computePofCiWithoutFeatureT(characterList.get(i),"lady",bagOfWordsMap,characterAndTimes,classLabel) ;
			System.out.println("lady    " + PofC4WithoutFeatureT);
			
			
			IG1 = (-1) * (pOfC0 * (Math.log((double)pOfC0)/ Math.log((double)2)) 
					+ pOfC1 * (Math.log((double)pOfC1)/ Math.log((double)2)) + pOfC2 * (Math.log((double)pOfC2)/ Math.log((double)2)) 
					+ pOfC3 * (Math.log((double)pOfC3)/ Math.log((double)2))+ pOfC4 * (Math.log((double)pOfC4)/ Math.log((double)2)));
			IG2 = PofWord * ((PofC0InFeatureT * (Math.log((double)PofC0InFeatureT)/ Math.log((double)2))) +
					(PofC1InFeatureT * (Math.log((double)PofC1InFeatureT)/ Math.log((double)2)))
					+(PofC2InFeatureT * (Math.log((double)PofC2InFeatureT)/ Math.log((double)2)))
					+(PofC3InFeatureT * (Math.log((double)PofC3InFeatureT)/ Math.log((double)2)))
					+(PofC4InFeatureT * (Math.log((double)PofC4InFeatureT)/ Math.log((double)2)))) ;
			//-------------------------------------------------------------------------------------------------------------
			IG3 = (1-PofWord) * ((PofC0WithoutFeatureT * (Math.log((double)(PofC0WithoutFeatureT))/ Math.log((double)2))) +
					((PofC1WithoutFeatureT) * (Math.log((double)(PofC1WithoutFeatureT))/ Math.log((double)2)))
					+(PofC2WithoutFeatureT * (Math.log((double)(PofC2WithoutFeatureT))/ Math.log((double)2)))
					+(PofC3WithoutFeatureT * (Math.log((double)(PofC3WithoutFeatureT))/ Math.log((double)2)))
					+(PofC4WithoutFeatureT * (Math.log((double)(PofC4WithoutFeatureT))/ Math.log((double)2)))) ;
			//-------------------------------------------------------------------------------------------------------------
			IG = IG1 + IG2 + IG3;
			System.out.println("信息增益值：" + IG) ; 
			writer.write(characterList.get(i) + ":" + IG) ;
			writer.write("\n");
			writer.flush();
		}
		writer.close();
	}

}


