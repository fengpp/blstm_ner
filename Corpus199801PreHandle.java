package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**********
 * 由原来的199801语料的标注方式转换为BIESO-PER，LOC，ORG标注方式，也就是生成监督训练和测试语料
 * 
 * @author fpp
 * 
 */
public class Corpus199801PreHandle {

	private static int B_PER = 0;
	private static int I_PER = 1;
	private static int E_PER = 2;
	private static int S_PER = 3;
	private static int B_LOC = 4;
	private static int I_LOC = 5;
	private static int E_LOC = 6;
	private static int S_LOC = 7;
	private static int B_ORG = 8;
	private static int I_ORG = 9;
	private static int E_ORG = 10;
	private static int S_ORG = 11;
	private static int O = 12;

	private static int numPerLine = 600000;// 每行存储的个数

	public int generateTrainExample(String srcFile, String xFile, String yFile) {
		int count = 0;
		int i_count = 0;
		int lastcount = 0;
		try {
			// 分好词的中文语料
			BufferedReader srcFileBr = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(srcFile)), "utf-8"));
			// 存储词的文件，也就是训练语料的x
			BufferedWriter xFileBw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(xFile)), "utf-8"));
			// 存储标注的文件，也就是训练语料的y
			BufferedWriter yFileBw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(yFile)), "utf-8"));

			String paragraph = null;
			for (; (paragraph = srcFileBr.readLine()) != null;) {
				// 对每一行用空格切分，得到词和词性数组，对数组元素用/切分，得到词本身和词性,只留下词本身，写入文件
				String[] resultArray = paragraph.trim().split("\\s+");
				if (paragraph.trim().length() > 2) {
					for (int i = 0; i < resultArray.length; i++) {
						// System.out.println(resultArray[i]);
						if (resultArray[i].trim().length() >= 1) {
							if (resultArray[i].trim().contains("/")
									&& resultArray[i].trim().length() > 1) {
								String word = resultArray[i].trim().split("/")[0];
								String pos = resultArray[i].trim().split("/")[1];
								if (count == 0)
									System.out.println(word);
								if (!word.startsWith("199801"))// 去掉19980101-31的数字
								{
									// [全国/n 政协/j]nt
									if (word.startsWith("["))
										xFileBw.write(word.substring(1) + " ");
									else
										xFileBw.write(word + " ");
									count++;
									lastcount = count;
									if (count % numPerLine == 0) {
										// System.out.println(count);
										xFileBw.newLine();
									}
									if (word.startsWith("["))// 如果词是【开头，要么是地名的开头，要么是机构名的开头
									{
										// 一直找到】符号为止，】的后面就是实体标签
										int j = i + 1;
										int huanhang[] = new int[5];
										int hhi = 0;
										i_count = 0;
										String wordNext = "";

										while (!resultArray[j].trim().contains(
												"]")) {
											wordNext = resultArray[j].trim()
													.split("/")[0];
											++i_count;
											xFileBw.write(wordNext + " ");
											count++;
											if (count % numPerLine == 0) {
												// System.out.println(count);
												xFileBw.newLine();
												huanhang[hhi] = i_count;
												++hhi;
											}
											++j;
										}
										i = j;
										String posNext1 = resultArray[j].trim()
												.split("]")[1];
										wordNext = resultArray[j].trim().split(
												"/")[0];
										xFileBw.write(wordNext + " ");
										count++;
										if (count % numPerLine == 0) {
											xFileBw.newLine();
										}

										if (posNext1.equals("ns")) {
											yFileBw.write(B_LOC + " ");
											if (lastcount % numPerLine == 0) {
												// System.out.println(count);
												yFileBw.newLine();
											}
											for (int k = 1, h = 0; k <= i_count; ++k) {
												yFileBw.write(I_LOC + " ");
												if (h < hhi && huanhang[h] == k) {
													++h;
													yFileBw.newLine();
												}
											}
											lastcount = lastcount + 1 + i_count;
											i_count = 0;
											hhi = 0;
											yFileBw.write(E_LOC + " ");
											if (lastcount % numPerLine == 0) {
												// System.out.println(count);
												yFileBw.newLine();
											}
										} else {
											yFileBw.write(B_ORG + " ");
											if (lastcount % numPerLine == 0) {
												// System.out.println(count);
												yFileBw.newLine();
											}
											for (int k = 1, h = 0; k <= i_count; ++k) {
												yFileBw.write(I_ORG + " ");
												if (h < hhi && huanhang[h] == k) {
													++h;
													yFileBw.newLine();
												}
											}
											lastcount = lastcount + 1 + i_count;
											i_count = 0;
											hhi = 0;
											yFileBw.write(E_ORG + " ");
											if (lastcount % numPerLine == 0) {
												// System.out.println(count);
												yFileBw.newLine();
											}

										}
									}

									else if (pos.equals("nr"))// 第一个nr
									{
										if (i + 1 < resultArray.length)// 看看下一个是不是nr
										{
											String posNext = "O";
											String wordNext = "";
											// String wordNext =
											// resultArray[i+1].split("/")[0];
											// if(resultArray[i+1].split("/").length>1)
											// System.out.println("i+1 is"+resultArray[i+1].length());
											posNext = resultArray[i + 1].trim()
													.split("/")[1];
											wordNext = resultArray[i + 1]
													.trim().split("/")[0];
											if (posNext.equals("nr")) {
												++i;

												yFileBw.write(B_PER + " ");
												if (count % numPerLine == 0) {
													// System.out.println(count);
													yFileBw.newLine();
												}
												xFileBw.write(wordNext + " ");
												yFileBw.write(E_PER + " ");
												count++;
												if (count % numPerLine == 0) {
													// System.out.println(count);
													xFileBw.newLine();
													yFileBw.newLine();
												}

											} else {
												yFileBw.write(S_PER + " ");
												if (count % numPerLine == 0) {
													// System.out.println(count);
													yFileBw.newLine();
												}
											}
										} else// 没有下一个
										{
											yFileBw.write(S_PER + " ");
											if (count % numPerLine == 0) {
												// System.out.println(count);
												yFileBw.newLine();
											}
										}
									} else if (pos.equals("ns")) {
										yFileBw.write(S_LOC + " ");
										if (count % numPerLine == 0) {
											// System.out.println(count);
											yFileBw.newLine();
										}
									} else if (pos.equals("nt")) {
										yFileBw.write(S_ORG + " ");
										if (count % numPerLine == 0) {
											// System.out.println(count);
											yFileBw.newLine();
										}
									} else {
										yFileBw.write(O + " ");
										if (count % numPerLine == 0) {
											// System.out.println(count);
											yFileBw.newLine();
										}
									}
									// System.out.println(count);

								}
								// else
								{
									// destFileBw.write(resultArray[i]+" ");
									// count++;
								}
								// System.out.println(resultArray[i]);
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/********
	 * 把每个词标注为BIEOS的形式转换为onehot形式
	 * 
	 * @param yFile
	 * @param yFile_OneHot
	 * @return
	 */
	int y2OneHot(String yFile, String yFile_OneHot) {
		int count = 0;
		try {

			//
			BufferedReader yFileBw = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(yFile)), "utf-8"));
			//
			BufferedWriter yFile_OneHotBw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(
							yFile_OneHot)), "utf-8"));
			String paragraph = null;
			String[] onehots = new String[13];
			Arrays.fill(onehots, "0");
			for (String x : onehots)
				System.out.println(x);
			for (; (paragraph = yFileBw.readLine()) != null;) {
				// 对每一行用空格切分，得到词和词性数组，对数组元素用/切分，得到词本身和词性,只留下词本身，写入文件
				String[] resultArray = paragraph.trim().split("\\s+");
				if (paragraph.trim().length() > 1) {
					for (int i = 0; i < resultArray.length; i++) {
						// System.out.println(resultArray[i]);
						if (resultArray[i].trim().length() >= 1) {
							onehots[Integer.parseInt(resultArray[i].trim())] = "1";
							for (String x : onehots)
								yFile_OneHotBw.write(x + " ");
						}
						Arrays.fill(onehots, "0");
					}
				}
				yFile_OneHotBw.newLine();
			}
			// 词典里的词语总数
			System.out.println(count);

			yFileBw.close();
			yFile_OneHotBw.close();
		} catch (FileNotFoundException e) {
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
		Corpus199801PreHandle pw = new Corpus199801PreHandle();
		int num0 = pw.generateTrainExample("data/corpus/199801.txt",
				"data/trainSet/x_199801.txt", "data/trainSet/y_199801.txt");
		int num1 = pw.y2OneHot("data/trainSet/y_199801.txt",
				"data/trainSet/y_199801_onehot.txt");
	}
}
