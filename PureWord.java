package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PureWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PureWord pw=new PureWord();
		int num0 =pw.generatePureWord("data/corpus/199801.txt","data/pureWord/pureWord_199801.txt");
		int num1 =pw.generatePureWord("data/corpus/hit.txt","data/pureWord/pureWord_hit.txt");
		int num3 =pw.generatePureWord("data/corpus/train_msar.txt","data/pureWord/pureWord_train_msar.txt");
		int num4 =pw.generatePureWord("data/corpus/testrightSeg.txt","data/pureWord/pureWord_testrightSeg.txt");
	}

	/******
	 * 将一个分好词、标注了词性或命名实体的文件里所有的词语写入文件，这个文件里是纯文本，没有pos这种标注
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public int generatePureWord(String srcFile, String destFile) {
		int count = 0;
		try {
			// 分好词的中文语料
			BufferedReader srcFileBr = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(srcFile)), "utf-8"));

			// 存储
			BufferedWriter destFileBw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(
							destFile)), "utf-8"));

			String paragraph = null;
			for (; (paragraph = srcFileBr.readLine()) != null;) {
				// 对每一行用空格切分，得到词和词性数组，对数组元素用/切分，得到词本身和词性,只留下词本身，写入文件
				String[] resultArray = paragraph.split(" ");
				for (int i = 0; i < resultArray.length; i++) {
					// System.out.println(resultArray[i]);
					if (resultArray[i].length() >= 1) {
						if(resultArray[i].contains("/") && resultArray[i].length() >1)
						{
							String word = resultArray[i].split("/")[0];
							// 如果word是【开头的，把【去掉
							if (word.startsWith("["))
								word = word.substring(1);
							String pos = resultArray[i].split("/")[1];
							if (!pos.equals("m"))// 去掉数字
							{
								destFileBw.write(word+" ");
								count++;
							}
						}
						else
						{
							destFileBw.write(resultArray[i]+" ");
							count++;
						}
						// System.out.println(resultArray[i]);
					}
				}
				destFileBw.newLine();
			}
			// 词典里的词语总数
			System.out.println(count);
			// �ر�������
			destFileBw.close();
			srcFileBr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
