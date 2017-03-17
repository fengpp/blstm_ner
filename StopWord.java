package experiment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
public class StopWord {
	 //停用词词表
    public static final String stopWordTable = "." + File.separator + "data\\stopwords_data" + File.separator + "stopwords_ch_1893.txt";

	/**去掉停用词
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//源文件和目的文件
        //String srcFile = "." + File.separator + "data\\word_data\\fish" + File.separator + "fishAll.txt";
        //String destFile = "." + File.separator + "data\\stopwords_data" + File.separator + "fishAll.txt";
        //System.out.println(stopWordTable+"        "+srcFile+"       "+destFile);
        //new StopWord().fileExcludeStopWord(srcFile, destFile);
        //将五个词典里的词进行停用词处理
        String srcFile_199801 = "." + File.separator + "data\\dictionary" + File.separator + "dictionary_199801.txt";
        String destFile_199801 = "." + File.separator + "data\\stopwords_data" + File.separator + "dictionary_stopwords_199801.txt";
        new StopWord().fileExcludeStopWord(srcFile_199801, destFile_199801);
        
        String srcFile_hit = "." + File.separator + "data\\dictionary" + File.separator + "dictionary_hit.txt";
        String destFile_hit = "." + File.separator + "data\\stopwords_data" + File.separator + "dictionary_stopwords_hit.txt";
        new StopWord().fileExcludeStopWord(srcFile_hit, destFile_hit);
        
        String srcFile_train_msar = "." + File.separator + "data\\dictionary" + File.separator + "dictionary_train_msar.txt";
        String destFile_train_msar = "." + File.separator + "data\\stopwords_data" + File.separator + "dictionary_stopwords_train_msar.txt";
        new StopWord().fileExcludeStopWord(srcFile_train_msar, destFile_train_msar);
        
        String srcFile_test_msar = "." + File.separator + "data\\dictionary" + File.separator + "dictionary_test_msar.txt";
        String destFile_test_msar = "." + File.separator + "data\\stopwords_data" + File.separator + "dictionary_stopwords_test_msar.txt";
        new StopWord().fileExcludeStopWord(srcFile_test_msar, destFile_test_msar);
        
        String srcFile_sogou = "." + File.separator + "data\\dictionary" + File.separator + "dictionary_sogou.txt";
        String destFile_sogou = "." + File.separator + "data\\stopwords_data" + File.separator + "dictionary_stopwords_sogou.txt";
        new StopWord().fileExcludeStopWord(srcFile_sogou, destFile_sogou);
        
        
        
        
	}
	public void fileExcludeStopWord(String srcFile,String destFile){
        try {
            //读取原文件和停用词表
            BufferedReader srcFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcFile)),"utf-8"));
            BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopWordTable)),"utf-8"));
            
            //将去除停用词的文本信息存入输出文件
            BufferedWriter destFileBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),"utf-8"));
            
            //用来存放停用词的集合
            Set<String> stopWordSet = new HashSet<String>();
            
            //初始化停用词集
            String stopWord = null;
            for(; (stopWord = StopWordFileBr.readLine()) != null;){
                stopWordSet.add(stopWord);        
            }            
            System.out.println("total："+stopWordSet.size());
            String paragraph = null;
            for(; (paragraph = srcFileBr.readLine()) != null;){
                //对读入的文本进行分词
                //SegTag segTag = new SegTag(1);// 分词路径的数目          
                //SegResult segResult = segTag.split(paragraph);
                //String spiltResultStr = segResult.getFinalResult();    
                //得到分词后的词汇数组，以便后续比较
                String[] resultArray = paragraph.split(" ");
                                
                //过滤停用词            
                for(int i = 0; i< resultArray.length; i++){
                    //System.out.println(resultArray[i]);
                    if(stopWordSet.contains(resultArray[i])){
                        resultArray[i] = null;
                    }
                    //System.out.println(resultArray[i]);    
                }
                
                //把过滤后的字符串数组存入到一个字符串中
                StringBuffer finalStr = new StringBuffer();
                for(int i = 0; i< resultArray.length; i++){
                    if(resultArray[i] != null && resultArray[i].trim().length()>=1){
                        finalStr = finalStr.append(resultArray[i]).append(" ");
                    }
                }
                
                //将过滤后的文本信息写入到指定文件中
                destFileBw.write(finalStr.toString());
                destFileBw.newLine();
                //输出最后的去停用词之后的结果
                System.out.println(finalStr);
            }
            
            //关闭输入流
            destFileBw.close();
            StopWordFileBr.close();
            srcFileBr.close();            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
