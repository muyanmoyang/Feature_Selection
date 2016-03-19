package muyanmoyang.featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sort_CHI {
	/**
	 * 对 CHI值进行降序排列 ,并在每个类中各选出50个特征词
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int label = 1 ;
		Map<String,Double> ChiMap = getMapOfCHI("C:/Users/Administrator/Desktop/论文(改小后的数据集)/CHIofWords_类别" + label + ".txt") ;
		Map<String,Double> sortIgMap = sortCHI(ChiMap ,"C:/Users/Administrator/Desktop/论文(改小后的数据集)/CHI值排序_类别" + label + ".txt") ;
		int CHIthreshold = 100 ;
		featureSelectOfWords("C:/Users/Administrator/Desktop/论文(改小后的数据集)/CHI值排序_类别" + label + ".txt", 
							"C:/Users/Administrator/Desktop/论文(改小后的数据集)/" + "CHI特征词.txt",CHIthreshold);
	}
	
	/**
	 *  获取CHI值Map
	 * @return
	 * @throws IOException 
	 */
	private static Map<String,Double> getMapOfCHI(String CHIFileDir) throws IOException{
		FileReader reader = new FileReader(new File(CHIFileDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		Map<String,Double> CHIMap = new HashMap<String,Double>() ;
		String line ;
		while((line=BR.readLine())!=null){
			String str[] = line.split("\t") ;
			CHIMap.put(str[0],Double.parseDouble(str[1])) ;
		}
		return CHIMap ;
	}
	
	/**
	 *  对IG值进行排序
	 * @throws IOException 
	 */
	private static Map<String,Double> sortCHI(Map<String,Double> CHIMap , String ChiWriter) throws IOException{
		
		FileWriter writer = new FileWriter(new File(ChiWriter)) ;
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(CHIMap.entrySet());
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
	
	/**
	 *  特征词提取
	 * @throws IOException 
	 */
	private static void featureSelectOfWords(String ChiFileDir, String selectWordsWriterDir, int CHIthreshold) throws IOException{
		FileReader reader = new FileReader(new File(ChiFileDir)) ;
		BufferedReader BR = new BufferedReader(reader) ;
		// 将特征选择词写入文件
		FileWriter selectionWordsWriter = new FileWriter(new File(selectWordsWriterDir)) ;
		int count = 0 ;
		String line ;
		while((line=BR.readLine())!=null){
			count ++ ;
			if(count <= CHIthreshold){
				System.out.println("选择了第" + count + "个词");
				selectionWordsWriter.write(line); 
				selectionWordsWriter.write("\n"); 
			}
			selectionWordsWriter.flush();
		}
		selectionWordsWriter.close();
	}
	
}
