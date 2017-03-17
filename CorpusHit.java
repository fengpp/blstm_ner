package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**********
 * 把哈工大的语料中由原来的标注方式转换为BIESO-PER，LOC，ORG标注方式，也就是生成监督训练和测试语料
 * @author fpp
 *
 */
public class CorpusHit {

	
	private static int B_PER=0;
	private static int I_PER=1;
	private static int E_PER=2;
	private static int S_PER=3;
	private static int B_LOC=4;
	private static int I_LOC=5;
	private static int E_LOC=6;
	private static int S_LOC=7;
	private static int B_ORG=8;
	private static int I_ORG=9;
	private static int E_ORG=10;
	private static int S_ORG=11;
	private static int O=12;
	private static int numPerLine=2000;//每行存储的个数
	public int generateTrainExample(String srcFile, String xFile, String yFile)
	{
		int count = 0;
		int lastcount;
		int i_count=0;
		int i,j;
		try {
			// 分好词的中文语料
			BufferedReader srcFileBr = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(srcFile)), "utf-8"));
			// 存储词的文件，也就是训练语料的x
			BufferedWriter xFileBw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(
							xFile)), "utf-8"));
			// 存储标注的文件，也就是训练语料的y
						BufferedWriter yFileBw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(new File(
										yFile)), "utf-8"));

			String paragraph = null;
			for (; (paragraph = srcFileBr.readLine()) != null;)
			{
				// 对每一行用空格切分，得到词和词性数组，对数组元素用/切分，得到词本身和词性,只留下词本身，写入文件
				String[] resultArray = paragraph.trim().split("\\s+");
				if(paragraph.trim().length()>2)
				{
					for (i = 0; i < resultArray.length; i++)
					{
						// System.out.println(resultArray[i]);
						if (resultArray[i].trim().length() >= 1)
						{
							if(resultArray[i].trim().contains("/") && resultArray[i].trim().length() >1)
							{
								String word = resultArray[i].trim().split("/")[0];
								String pos = resultArray[i].trim().split("/")[1];
								if(count==0) System.out.println(word);
								if (!word.startsWith("199801"))// 去掉19980101-31的数字
								{
									xFileBw.write(word+" ");
									++count;
									lastcount=count;
									if(count%numPerLine==0)
									{
										xFileBw.newLine();
									}
									
									//[全国/n  政协/j]nt
									if(pos.equals("nf"))
									{
										yFileBw.write("B_PER"+" ");
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}
										j=i+1;
										while(!resultArray[j].trim().contains("ne")){
											word = resultArray[j].trim().split("/")[0];
											xFileBw.write(word+" ");
											++count;
											if(count%numPerLine==0)
											{
												xFileBw.newLine();
											}
											yFileBw.write("I_PER"+" ");
											++lastcount;
											if(lastcount%numPerLine==0)
											{
												yFileBw.newLine();
											}
											++j;
										}
										i=j;
										word = resultArray[i].trim().split("/")[0];
										xFileBw.write(word+" ");
										++count;
										if(count%numPerLine==0)
										{
											xFileBw.newLine();
										}

										yFileBw.write("E_PER"+" ");
										++lastcount;
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}

									}
									else if(pos.equals("pf")){
										yFileBw.write("B_LOC"+" ");
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}
										j=i+1;
										
										while(!resultArray[j].trim().contains("pe")){
											word = resultArray[j].trim().split("/")[0];
											xFileBw.write(word+" ");
											++count;
											if(count%numPerLine==0)
											{
												xFileBw.newLine();
											}
											yFileBw.write("I_LOC"+" ");
											++lastcount;
											if(lastcount%numPerLine==0)
											{
												yFileBw.newLine();
											}
											++j;
										}
										i=j;
										word = resultArray[i].trim().split("/")[0];
										xFileBw.write(word+" ");
										++count;
										if(count%numPerLine==0)
										{
											xFileBw.newLine();
										}

										yFileBw.write("E_LOC"+" ");
										++lastcount;
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}
									}
									else if(pos.equals("of")){
										yFileBw.write("B_ORG"+" ");
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}
										j=i+1;
										while(!resultArray[j].trim().contains("oe")){
											word = resultArray[j].trim().split("/")[0];
											xFileBw.write(word+" ");
											++count;
											if(count%numPerLine==0)
											{
												xFileBw.newLine();
											}
											yFileBw.write("I_ORG"+" ");
											++lastcount;
											if(lastcount%numPerLine==0)
											{
												yFileBw.newLine();
											}
											++j;
										}
										word = resultArray[i].trim().split("/")[0];
										xFileBw.write(word+" ");
										++count;
										if(count%numPerLine==0)
										{
											xFileBw.newLine();
										}

										yFileBw.write("E_ORG"+" ");
										++lastcount;
										if(lastcount%numPerLine==0)
										{
											yFileBw.newLine();
										}
										i=j;
									}
									else if(pos.equals("n")){
										yFileBw.write("S_PER"+" ");
										if(count%numPerLine==0)
										{
											yFileBw.newLine();
										}
									}
									else if(pos.equals("P")){
										yFileBw.write("S_LOC"+" ");
										if(count%numPerLine==0)
										{
											yFileBw.newLine();
										}
									}
									else if(pos.equals("O")){
										yFileBw.write("S_ORG"+" ");
										if(count%numPerLine==0)
										{
											yFileBw.newLine();
										}
									}
									else{
										yFileBw.write("O"+" ");
										if(count%numPerLine==0)
										{
											yFileBw.newLine();
										}
									}
								}
							}
						}
					}
				}
			

			} 
			// 词典里的词语总数
						System.out.println(count);
						// �ر�������
						xFileBw.close();
						yFileBw.close();
						srcFileBr.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
/**
 * @param args
 */
public static void main(String[] args) {
	// TODO Auto-generated method stub

	CorpusHit pw=new CorpusHit();
	int num0 =pw.generateTrainExample("data/corpus/hit.txt","data/trainSet/x_hit.txt","data/trainSet/y_hit.txt");
	
}
}

