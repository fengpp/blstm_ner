package experiment;

import java.io.File;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class Segment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//分词21-28，31-38，。。。。。一直到98
		/*for(int i=2;i<=9;i++)
		{
			for(int j=1;j<=8;j++)
			{
				try{
					segment2Words("data\\raw_data\\sogouCA_2\\news.allsites."+String.valueOf(i)+j+"0806.txt","data\\word_data\\sogouCA\\news.allsites."+String.valueOf(i)+j+"0806.txt");
					}catch(Error e)
					{
						System.out.println(i+" "+j+"hahahhhah");
						
					}
			}
			
		}
		System.out.println("after    hahahhhah");*/
		segment2Words_All("data/raw_data/sogouCA_2","data/word_data/sogouCA");
		//segment2Words("data\\raw_data\\fish\\fishAll.txt","data\\word_data\\fish\\fishAll.txt");
		
	}
	/******
	 * 对路径下的所有文件分词
	 * @param srcPath
	 * @param desPath
	 */
	public static void segment2Words_All(String srcPath,String desPath)
	{
		File[] files = new File(srcPath).listFiles();  
		for (File file : files) 		{
			
			segment2Words(srcPath+"/"+file.getName(), desPath+"/"+file.getName());
            
        }	
	}
	
	// 定义接口CLibrary，继承自com.sun.jna.Library
			public interface CLibrary extends Library {
				// 定义并初始化接口的静态变量
				CLibrary Instance = (CLibrary) Native.loadLibrary(
						"D:\\nowing\\crf\\ICTCLAS2015\\lib\\win64\\NLPIR", CLibrary.class);
				
				public int NLPIR_Init(String sDataPath, int encoding,
						String sLicenceCode);
						
				public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
				public String NLPIR_FileProcess(String sSourceFilename,String sResultFilename,int bPostagged);
				public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
						boolean bWeightOut);
				public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
						boolean bWeightOut);
				public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
				public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
				public String NLPIR_GetLastErrorMsg();
				public void NLPIR_Exit();
			}
	/************
	 * 利用北大分词软件进行分词，只要词，不要词性
	 * @param sSourceFilename：分之前的文件
	 * @param sResultFilename：分之后的文件
	 */
	public static void segment2Words(String sSourceFilePath,String sResultFilePath)
	{
		String argu ="D:\\nowing\\crf\\ICTCLAS2015";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;			
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return;
		}			
		try {				
			//对文件分词				
			CLibrary.Instance.NLPIR_FileProcess(sSourceFilePath, sResultFilePath, 1);
			CLibrary.Instance.NLPIR_Exit();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
}
