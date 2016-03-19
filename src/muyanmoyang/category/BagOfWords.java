package muyanmoyang.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import muyanmoyang.utils.KeyUtils;

/**
 *  建立词袋模型
 * @author moyang
 *
 */
public class BagOfWords {
	
	public static void main(String[] args) throws IOException, SQLException {
		Vector<String> characterList = getCharacterList("C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/noStopWordsSegments.txt",
				"C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/词语集合.txt") ;   // 获取词语集合
		Map<String,Vector<KeyUtils>> bagOfWordsMap = ConstructBofWords("C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/noStopWordsSegments.txt", characterList) ;
	}
	
	/**
	 * 获取词语列表,对所有文章中的词语进行去重处理，并写入最后的词语集合.txt文件,作为统计词语出现次数的输入
	 * @return Vector<String> 返回词语的Vector集合
	 * @throws IOException
	 */
	public static Vector<String> getCharacterList(String fileDir,String characterListDir) throws IOException
	{
		FileWriter fileWriter = new FileWriter(new File(characterListDir)) ;
		FileReader fileReader = new FileReader(new File(fileDir)) ;
	
		BufferedReader BR = new BufferedReader(fileReader) ;
		ArrayList<String> list = new ArrayList<String>() ;
		long count = 0 ;
		long count2 = 0 ;
		String line ;
		Vector<String> newVector = new Vector<String>();
		while((line=BR.readLine()) != null)
		{
			count ++ ;
			System.out.println("获取词语集合，遍历各文本：添加到第" + count + "篇文本");
			String str[] = line.split(" ");
			for(int i=0; i<str.length; i++){
				list.add(str[i]) ;
			}
		}
		for(int i=0; i<list.size(); i++)
	    {
			count2 ++ ;
			System.out.println("对词语进行去重处理，处理到了第" + count2 + "个词语");
			if(!newVector.contains(list.get(i)))
				newVector.add(list.get(i));
	    }
		
		for(int i=0; i<newVector.size(); i++)
		{
			fileWriter.write(newVector.get(i));
			fileWriter.write("\n") ;
		}
		fileWriter.close();
		return newVector;  
	}
	
	//TODO  -----------------------构建词袋模型---------------------------------	
		/**
		 * 构建倒排表： key=word,val= a list of pairs which consists of articleid ,and count, count=tf
		 * 词袋子模型的格式Map<String,Vector<KeyUtils>>：主键为该词，KeyUtils中的第一个int 为文章标号，第二个词为在该文中出现的次数，
		 * Map<String,Vector<KeyUtils>>统计的是这个词在那些文章中出现，出现过几次。
		 * @param textDir             -------->  存放预处理后文本内容的文件 
		 * @param characterFeatureDir -------->  存放词语列表的文件
		 * @return  Map<String,Vector<KeyUtils>>        返回词袋模型Map , 格式如下：
		 * 												Map{ 词语 : Vector[KeyUtils(文章编号1：出现次数1) , KeyUtils(文章编号2：出现次数2)......]  }
		 * @throws SQLException 
		 * @throws IOException 
		 */
		public static Map<String,Vector<KeyUtils>> ConstructBofWords(String textDir,Vector<String> characterList) throws IOException, SQLException
		{
			// 将词袋模型落地到磁盘
			FileWriter mapWriter = new FileWriter(new File("C:/Users/Administrator/Desktop/论文(改小后的数据集)/测试集/词袋.txt")) ;
			
			
			Vector vec = new Vector() ;
			ArrayList list3 = new ArrayList() ;
			ArrayList tmpList = new ArrayList() ; 
			HashMap<String,Vector<KeyUtils>> myMap = new HashMap<String,Vector<KeyUtils>>() ;
			int articleNo = 0 ; //文章标号
			int timeOfCharacter = 0 ;//词语出现次数
			
			
			ArrayList<String[]> tempList = new ArrayList<String[]>();//每篇文章的词语放在一个String[]里面，以List形式保存
			
			// 获取每篇文本的内容（已经过分词处理、停用词处理、特殊字符处理）
			FileReader fileReader = new FileReader(new File(textDir)); 
			BufferedReader BR = new BufferedReader(fileReader) ;
			String  line;
			StringBuffer buffer = new StringBuffer();
			int count = 0 ;
			while((line=BR.readLine()) != null)
			{
				buffer.append(line); //得到过滤后的字符串
				String[] resultTxtString = buffer.toString().split(" ") ;//切分出词语的String[]数组
				tempList.add(resultTxtString) ;
				buffer.delete(0,buffer.length()-1);
			}	
			
			System.out.println("开始构建倒排表....");
			for(int i=0 ;i<characterList.size() ;i++) // -----------对于每个词语-------------
			{
				System.out.println("共开始统计第" + (i+1) + "个词语"); 
				//统计词语出现次数
				String character = characterList.get(i) ;
				for(int j=0; j<tempList.size(); j++) // ------------对于每篇文章
				{
					String[] str = tempList.get(j) ;
					for(int k=0; k<str.length; k++) //  -------------对于每篇文章中的每个词
					{
						if(character.equals(str[k]))
						{
							timeOfCharacter ++ ;
//							count = timeOfCharacter ;
						}
					}
					if(vec == null)
					{
						vec = new Vector() ;
					}
					if(timeOfCharacter != 0)
					{
						vec.add(new KeyUtils(j+1,timeOfCharacter)) ;
					}
					myMap.put(character,vec) ;
					timeOfCharacter = 0 ; 
				}
				vec = null ;
			}
			
			int showInHowManyArticles = 0 ;//统计每个词在多少篇文章中出现过
			Set<Entry<String, Vector<KeyUtils>>> set = myMap.entrySet() ;
			Iterator<Entry<String, Vector<KeyUtils>>> it = set.iterator() ;
			while(it.hasNext())
			{
				count ++ ;
				System.out.println("将词袋写入文件，开始写入第" + count + "个词语");
				Entry<String, Vector<KeyUtils>> entry = it.next() ;
				Vector<KeyUtils> list2 = entry.getValue() ;
//				System.out.println("list2的大小:" + list2.size()); 
				mapWriter.write(entry.getKey() + " :");
				for(int i=0; i<list2.size(); i++)
				{
					showInHowManyArticles ++ ; //该词出现的文章数+1
					mapWriter.write(list2.get(i).getX() + " " + list2.get(i).getY() + ";"); //将该词统计的结果写入文件对应行
				}
				mapWriter.write(" " + showInHowManyArticles); 
				mapWriter.write("\n") ;
				mapWriter.flush();
				showInHowManyArticles = 0 ;
			}	
			
			mapWriter.close();
			return myMap ;
		}
}
