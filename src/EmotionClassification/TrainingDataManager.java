package EmotionClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TrainingDataManager {
	private String[] trainingFileString;// Training corpus collection
	private File trainingFileDir;// Training corpus path
	private String trainingPath = "E:/data/emotion/origin";//trainingCorpus
	public static Map<String, Integer> fileCount = new HashMap<String, Integer>();// the twitter number in each class
	public static Map<String, Map<Integer, Map<String, Double>>> allMap = new HashMap<>();
	public static int allTweetCount;
	private int tweetLine;

	public TrainingDataManager() {
		trainingFileDir = new File(trainingPath);
		if (!trainingFileDir.isDirectory()) {
			throw new IllegalArgumentException("error path : " + trainingPath);
		}
		this.trainingFileString = trainingFileDir.list();
	}

	/**
	 * get the name of Classification
	 * 
	 * @return corpus Classification
	 */
	public String[] getTrainingClassification() {
		return this.trainingFileString;
	}

	/**
	 * get the path of Classification
	 * 
	 * @param Classification
	 * @return
	 */
	public String getFilePath(String Classification) {
		File classDir = new File(trainingFileDir.getPath() + File.separator
				+ Classification);
		String ret = classDir.getAbsolutePath();
		return ret;
	}

	/**
	 * get the number of word in certain Classification
	 * 
	 * @param classFile
	 * @param key
	 * @param wordMap
	 * @return
	 */
	public int getCountContainKeyWord(String key,String classFile, 
			Map<Integer, Map<String, Double>> classMap) {
		int ret = 0;
		Set<Integer> fileList = classMap.keySet();
		for (int file : fileList) {
			Map<String, Double> map = classMap.get(file);
			if (map.containsKey(key)) {
				ret++;
			}
		}
		return ret;
	}
	/**
	 * 
	 * @return training file statistics	
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Map<String, Map<Integer, Map<String, Double>>> getAll()
			throws FileNotFoundException, IOException {
		for (String file : trainingFileString) {
			getWordInClass(file);
		}
		return allMap;
	}
	/**
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void getWordInClass(String path) throws FileNotFoundException,
			IOException {
		allMap.put(path, getReadLine(getFilePath(path)));
		fileCount.put(path, tweetLine);
		getAllTweetCount(tweetLine);
	}
	
	/**
	 * calculate tf value in twitter
	 * @param classPath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Map<Integer, Map<String, Double>> getReadLine(String classPath)throws IOException,FileNotFoundException{
		Map<Integer, Map<String, Double>> classMap = new HashMap<Integer, Map<String, Double>>();
		InputStreamReader inputReader = new InputStreamReader(
				new FileInputStream(classPath), "GBK");
		BufferedReader bReader = new BufferedReader(inputReader);
		String tweet;int line = 0;
		while ((tweet = bReader.readLine()) != null) {
			classMap.put(line, splitWord(tweet));
			line++;
		}
		bReader.close();
		this.tweetLine = line;
		return classMap;
	}
	/**
	 * IKanalyzer
	 * @param line
	 * @return
	 * @throws IOException
	 */
	public Map<String, Double> splitWord(String line) throws IOException{
		Map<String, Double> fileMap = new HashMap<String, Double>();
		Analyzer analyzer = new IKAnalyzer(true);
		StringReader reader = new StringReader(line);
		TokenStream ts = analyzer.tokenStream("", reader);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		ts.reset();
		while(ts.incrementToken()){
			String word = term.toString();
			if (fileMap.containsKey(word)) {
				fileMap.put(word, fileMap.get(word) + 1);
				} else {
				fileMap.put(word, (double) 1);
				}
		}
		analyzer.close();
		return fileMap;
	}
	
	public void getAllTweetCount(int tweet){
		this.allTweetCount +=  tweet;
	}
}
