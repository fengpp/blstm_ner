package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * 给一个分好词、标注好词性的文本，将词写入词典，词典用文件存储
 * @author fpp
 *
 */
public class Dictionary {
	

	/******
	 * 将一个分好词、标注了词性或命名实体的文件里所有的词语写入词典，这个词典命名为常用词词典
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public int[] createDictionary(String srcFile,String destFile)
	{
		int[] count={0,0};//词语总数count【0】和词典里的词的总数（词典是去重的）count【1】
		try {
		//分好词的中文语料
        BufferedReader srcFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),"utf-8"));
        
        //存储词典
        BufferedWriter destFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),"utf-8"));
        
        //暂存词典
        Map<String,Integer> dictionarySet = new HashMap<String,Integer>();
        
        
        String paragraph = null;
        for(; (paragraph = srcFileBr.readLine()) != null;)
        {
            //对每一行用空格切分，得到词和词性数组，对数组元素用/切分，得到词本身和词性
            String[] resultArray = paragraph.split(" ");                        
            for(int i = 0; i< resultArray.length; i++)
            {
            	
                //System.out.println(resultArray[i]);
            	if(resultArray[i].length()>1)
            	{
            		String word = resultArray[i].split("/")[0];
            		//如果word是【开头的，把【去掉
            		if(word.startsWith("["))
            			word = word.substring(1);
                	String pos = resultArray[i].split("/")[1];
                	if(!pos.equals("m"))//去掉数字
                	{
                		if(dictionarySet.containsKey(word))
                        {
                        	Integer num=dictionarySet.get(word) +1;
                        	dictionarySet.put(word, num);
                        }
                        else
                        {
                        	dictionarySet.put(word, 1);
                        }
                	}                    
                    //System.out.println(resultArray[i]);
            	}            	    
            }            
            
        }
            
        Set<String> keys = dictionarySet.keySet();
        Iterator<String> iter = keys.iterator();
        
      //把暂存在map字典里的内容写入文件
        while(iter.hasNext())
        {
        	String keywords = iter.next();
        	Integer nums = dictionarySet.get(keywords);
        	count[0]+=nums;
        	destFileBw.write(keywords);
            destFileBw.newLine();
            count[1]++;
        }           
            
            //词典里的词语总数
            System.out.println(count[1]);        
        
        //�ر�������
        destFileBw.close();        
        srcFileBr.close();            
        
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch(Exception e){
        e.printStackTrace();
    }
		return count;
}
	/******
	 * 将一个分好词的搜狗新闻文件里所有的词语写入词典，这个词典命名为常用词词典
	 * 搜狗的这个语料是未标注语料，格式与前三个标注语料不相同，所以单独写了个函数建立词典
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public int[] createDictionary_sogou(String srcFile,String destFile)
	{
		int[] count={0,0};//词语总数count【0】和词典里的词的总数（词典是去重的）count【1】
		try {
		//分好词的中文语料
        BufferedReader srcFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),"utf-8"));
        
        //存储词典的文件
        BufferedWriter destFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),"utf-8"));
        
        //暂存词典
        Map<String,Integer> dictionarySet = new HashMap<String,Integer>();
        
        
        String paragraph = null;
        for(; (paragraph = srcFileBr.readLine()) != null;)
        {
            //对每一行用空格切分
            String[] resultArray = paragraph.split(" ");                        
            for(int i = 0; i< resultArray.length; i++)
            {
                //System.out.println(resultArray[i]);
            	if(resultArray[i].length()>=1)
            	{
            		
                		if(dictionarySet.containsKey(resultArray[i]))
                        {
                        	Integer num=dictionarySet.get(resultArray[i]) +1;
                        	dictionarySet.put(resultArray[i], num);
                        }
                        else
                        {
                        	dictionarySet.put(resultArray[i], 1);
                        }
                	                    
                    //System.out.println(resultArray[i]);
            	}            	    
            }            
            
        }
            
        Set<String> keys = dictionarySet.keySet();
        Iterator<String> iter = keys.iterator();
        
      //把暂存在map字典里的内容写入文件
        while(iter.hasNext())
        {
        	String keywords = iter.next();
        	Integer nums = dictionarySet.get(keywords);
        	count[0]+=nums;
        	destFileBw.write(keywords);
            destFileBw.newLine();
            count[1]++;
        }
            
            
            //词典里的词语总数
            System.out.println(count);        
        
        //�ر�������
        destFileBw.close();
        
        srcFileBr.close();            
        
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch(Exception e){
        e.printStackTrace();
    }
		return count;
}
	
	//把人民日报（199801）、哈工大（hit）和微软亚洲研究院（msra）三种标注、外加sogou新闻语料生成的词典合并为一个大词典
	//不这样做，还是只合并训练和测试的时候用的语料就够了，也就是只合并人民日报，哈工大和msra的训练语料
	public int mergeDictionary(String src_path, String destFile)
	{
		int count = 0;
		File des=new File(destFile);
		//暂存词典
        Map<String,Integer> dictionarySet = new HashMap<String,Integer>();
        
		try{
			
	
    	if(!des.exists())
    		des.createNewFile();
		OutputStream os=new FileOutputStream(des);
		OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
		BufferedWriter bw=new BufferedWriter(osw);
		
    	File[] files = new File(src_path).listFiles(); 
    	FileInputStream fis = null;   
    	InputStreamReader isr =null;     	
		BufferedReader br=null;
		String temp="";
		for(File file : files) 
		{
			
				System.out.println(file.getName());
				fis=new FileInputStream(file);
				isr=new InputStreamReader(fis, "UTF-8"); 
				br=new BufferedReader(isr);
				while((temp=br.readLine())!=null)
				{				
					dictionarySet.put(temp.trim(),0);								
				}				
        }  
		Set<String> keys = dictionarySet.keySet();
        Iterator<String> iter = keys.iterator();
        
      //把暂存在map字典里的内容写入文件
        while(iter.hasNext())
        {
        	String keywords = iter.next();
        	Integer nums = dictionarySet.get(keywords);
        	bw.write(keywords);
            bw.newLine();
            count++;
        }
		bw.close();
		osw.close();
		os.close();
		br.close();
		isr.close();
		fis.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Dictionary dic = new Dictionary();
		//199801
		int[] num0 = dic.createDictionary("data/corpus/199801.txt","data/dictionary/dictionary_199801.txt");
		//hit
		int[] num1 = dic.createDictionary("data/corpus/hit.txt","data/dictionary/dictionary_hit.txt");
		//msra
		//int[] num2 = dic.createDictionary("data/corpus/test.txt","data/dictionary/dictionary_test.txt");
		int[] num3 = dic.createDictionary("data/corpus/testrightSeg1.txt","data/dictionary/dictionary_testrightSeg1.txt");
		int[] num4 = dic.createDictionary("data/corpus/train_msar.txt","data/dictionary/dictionary_train_msar.txt");
	
		//sogou
		int[] num5 = dic.createDictionary_sogou("data/corpus/sogou.txt","data/dictionary/dictionary_sogou.txt");
	
		System.out.println("1998人民日报:"+num0[0]+" 哈工大："+num1[0]+" "+"msar 的测试集： "+num3[0]+"msar的训练集： "+num4[0]+"搜狗： "+num5[0]);
		
		//合并词典
		//int num6 = dic.mergeDictionary("data/dictionary","data/dictionary/dictionary_merge.txt");
		//System.out.println(num6);
	}

}
