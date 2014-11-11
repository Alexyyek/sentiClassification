package PolarityClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Polarity {
	public static void main(String[] args) throws IOException {
		File file = new File("F:/WeiboEmotion/汉语情感词极值表/汉语情感词极值表23419.txt");
		Map<String, Double>moodValue = new HashMap<String, Double>();
		File expression = new File("F:/WeiboEmotion/表情词语.txt");
		FileReader eReader = new FileReader(expression);
		BufferedReader ebReader = new BufferedReader(eReader);
		List<String>sadList = new ArrayList<String>();
		List<String>happyList = new ArrayList<String>();
		String eword = null;
		while((eword = ebReader.readLine())!=null)
		{
			String[]word = eword.split("\t\t");
			if(word[1].equals("负面"))
			{
				sadList.add(word[0]);
			}else if(word[1].equals("正面"))
			{
				happyList.add(word[0]);
			}
		}
		
		File sadFile = new File("F:/WeiboEmotion/date/sad/悲伤.txt");
		FileWriter pWriter = new FileWriter(new File("F:/WeiboEmotion/date/sad/Positive.txt"));
		FileWriter nWriter = new FileWriter(new File("F:/WeiboEmotion/date/sad/negative.txt"));
		FileWriter neWriter = new FileWriter(new File("F:/WeiboEmotion/date/sad/neutral.txt"));
		FileReader readerValue = new FileReader(file);
		BufferedReader bReaderValue = new BufferedReader(readerValue);
		String aline;
		while ((aline = bReaderValue.readLine()) != null) 
		{
			String[] word = aline.split("\t");
			double num = Double.parseDouble(word[1]);
			moodValue.put(word[0], num);
		}
		FileReader reader = new FileReader(sadFile);
		BufferedReader bReader = new BufferedReader(reader);
		while((aline = bReader.readLine())!=null)
		{
			double value = 0;
			List<String>list = segString(aline);
			for(Iterator<String>iterator = list.iterator();iterator.hasNext();)
			{
				String word = iterator.next();
				if(moodValue.containsKey(word))
				{
					value += moodValue.get(word);
				}
			}
			
			if(judge(list,sadList))
			{
				nWriter.append(aline + "\r\n");
			}
			else{
				if(value > 0)
				{
					pWriter.append(aline + "\r\n");
				}else if(value < 0)
				{
					nWriter.append(aline + "\r\n");
				}else {
					neWriter.append(aline + "\r\n");
				}
			}
		}
		bReader.close();
		ebReader.close();
		bReaderValue.close();
		pWriter.flush();
		nWriter.flush();
		pWriter.close();
		nWriter.close();
		neWriter.flush();
		neWriter.close();
	}
	public static boolean judge(List<String>wlist,List<String>eList)
	{
		for(Iterator<String>word = wlist.iterator();word.hasNext();)
		{
			String keyword = word.next();
			if(eList.contains(keyword))
			{
				return true;
			}
		}
		return false;
	}
	public static List<String>segString(String content) throws IOException
	{	
		List<String> wordList = new ArrayList<String>();
		Analyzer analyzer = new IKAnalyzer(false);
		try {
			StringReader reader = new StringReader(content);
			TokenStream ts = analyzer.tokenStream("", reader);
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
			ts.reset();
			while (ts.incrementToken()) 
			{
				String termString = term.toString();
				wordList.add(termString);
			}
			ts.end();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		analyzer.close();
		return wordList;
	}
}
