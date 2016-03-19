package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sort_IG {
	/**
	 * 对 IG值进行降序排列 ， 根据IG值大小进行特征词选择
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Map<String,Double> IgMap = getMapOfIG("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/IGOfWords.txt") ;
		Map<String,Double> sortIgMap = sortIG(IgMap ,"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/IG值排序.txt") ;
		int IGthreshold = 3000 ;
		featureSelectOfWords("C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/IG值排序.txt", 
							"C:/Users/Administrator/Desktop/论文（特征提取）/新闻标题/训练集/" + IGthreshold + "特征词/" + "特征词选择_" + IGthreshold + ".txt",IGthreshold);
	}
	
	/**
	 *  获取IG值Map
	 * @return
	 * @throws IOException 
	 */
	private static Map<String,Double> getMapOfIG(String IGFileDir) throws IOException{
		FileReader reader = new FileReader(new File(IGFileDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<String,Double> IGMap = new HashMap<String,Double>() ;
		String line ;
		while((line=BR.readLine())!=null){
			String str[] = line.split(":") ;
			IGMap.put(str[0],Double.parseDouble(str[1])) ;
		}
		return IGMap ;
	}
	
	/**
	 *  对IG值进行排序
	 * @throws IOException 
	 */
	private static Map<String,Double> sortIG(Map<String,Double> IGMap , String IgWriter) throws IOException{
		
		FileWriter writer = new FileWriter(new File(IgWriter)) ;
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(IGMap.entrySet());
        //sort list based on comparator
        Collections.sort(list, new Comparator(){
             public int compare(Object o1, Object o2) 
             {
            	 return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
             }
        });
 
        //put sorted list into map again
        Map sortedMap = new HashMap(); 
        for (Iterator it = list.iterator(); it.hasNext();) {
        	Map.Entry entry = (Map.Entry)it.next();
        	sortedMap.put(entry.getKey(), entry.getValue()) ; 
        	writer.write(entry.getKey() + "\t" + entry.getValue());
        	writer.write("\n"); 
        	writer.flush();
        }
        writer.close();
        return sortedMap;
	}
	
//	/**
//	 *  特征词提取
//	 * @throws IOException 
//	 */
//	private static void featureSelectOfWords(Map<String,Double> sortIgMap, String selectWordsWriterDir) throws IOException{
//		// 将特征选择词写入文件
//		int count = 0 ;
//		FileWriter selectionWordsWriter = new FileWriter(new File(selectWordsWriterDir)) ;
//		for(int i=0; i<sortIgMap.size(); i++){
//			if(i < 5000){
//				count ++ ;
//				System.out.println("选择了第" + count + "个词语");
//				Iterator<Map.Entry<String, Double>> it = sortIgMap.entrySet().iterator();
//				while (it.hasNext()) {
//				   Map.Entry<String, Double> entry = it.next();
//				   selectionWordsWriter.write(entry.getKey() + "\t" + entry.getValue()); 
//				}
//			}
//			selectionWordsWriter.flush();
//		}
//		selectionWordsWriter.close();
//	}
	
	/**
	 *  特征词提取
	 * @throws IOException 
	 */
	private static void featureSelectOfWords(String IgFileDir, String selectWordsWriterDir, int IGthreshold) throws IOException{
		FileReader reader = new FileReader(new File(IgFileDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		// 将特征选择词写入文件
		FileWriter selectionWordsWriter = new FileWriter(new File(selectWordsWriterDir)) ;
		int count = 0 ;
		String line ;
		while((line=BR.readLine())!=null){
			count ++ ;
			if(count <= IGthreshold){
				System.out.println("选择了第" + count + "个词");
				selectionWordsWriter.write(line); 
				selectionWordsWriter.write("\n"); 
			}
			selectionWordsWriter.flush();
		}
		selectionWordsWriter.close();
	}
	
	
}
