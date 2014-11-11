package EmotionClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Lancer {
	private static String testPath = "E:/data/emotion/test/1";

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		TrainingDataManager tdm = new TrainingDataManager();
		Long startTime=System.currentTimeMillis();
		Map<String, Map<Integer, Map<String,Double>>>TF = tdm.getAll();
		BayesClassifier bClassifier = new BayesClassifier();
		File file = new File(testPath);
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(file),"GBK");
		BufferedReader bReader = new BufferedReader(isReader);
		String aline;int i = 1;
		while((aline = bReader.readLine()) != null)
		{
			String text = aline.toString();
			String result = bClassifier.classify(text,TF);
			System.out.println("文档" + i +":" +"此项属于["+result+"]");
			i++;
		}
			isReader.close();bReader.close();
			Long endTime=System.currentTimeMillis();
			System.out.println((endTime - startTime)/1000 + "s");
	}
}
