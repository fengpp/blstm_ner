package experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Combine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			convert2ABigFile("data/word_data/sogouCA_All/combineAll_1_16.txt","data/word_data/sogouCA",1,1613212);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***************
	 * 将从from个开始到to个结束个文件合并为一个文件
	 * @param des_aBigFile  
	 * @param src_path
	 * @param from
	 * @param to
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static void convert2ABigFile(String des_aBigFile, String src_path,int from,int to) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {    	
    	File des=new File(des_aBigFile);
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
		int index=1;
		for (File file : files) 
		{
			if(index<=to && index>=from)
			{
				System.out.println(file.getName());
				fis=new FileInputStream(file);
				isr=new InputStreamReader(fis, "UTF-8"); 
				br=new BufferedReader(isr);
				while((temp=br.readLine())!=null)
				{				
					bw.write(temp);			
				}			
				bw.newLine();
			}
			index++;
        }  
		bw.close();
		osw.close();
		os.close();
		br.close();
		isr.close();
		fis.close();
	}
}
