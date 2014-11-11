package EmotionClassification;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class BayesClassifier {
	private static TrainingDataManager tdm = new TrainingDataManager();
	private static double zoomFactor = 10000.0f; //放大系数
	/**
	 * 计算给定文本属性向量X在给定的分类Cj中的类条件概率
	 * @param X
	 * @param Cj
	 * @param TF
	 * @return
	 * @throws IOException
	 */
	double calcProd(String[] X,String Cj, Map<Integer, Map<String, Double>>classMap) throws IOException
	{
		double ret = 1.0f;
		
		//利用log运算改成连加提高精确度
		for(int i = 0 ; i < X.length ; i++)
		{
			String Xi = X[i];
			ClassConditionalProbability cs = new ClassConditionalProbability();
			ret += Math.log(cs.calculatePxc(Xi, Cj, classMap));
		}
		//再乘以先验概率
		ret += Math.log(PriorProbability.calculatePc(Cj));
		
//		for(int i = 0 ; i < X.length ; i++)
//		{
//			String Xi = X[i];
//			ClassConditionalProbability cs=new ClassConditionalProbability();
//			ret *= cs.calculatePxc(Xi, Cj, classMap)*zoomFactor;
//		}
//		//再乘以先验概率
//		ret *= PriorProbability.calculatePc(Cj);
		return ret;
	}
	/**
	 * 
	 * @param text
	 * @param TF
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String classify(String text, Map<String, Map<Integer, Map<String, Double>>>TF) throws IOException
	{
		String[] terms = null;//terms 为分词并去除停用词后的文档
		terms = ChineseSpliter(text).split(" ");
		
		String[] Classes = tdm.getTrainingClassification();
		double probility = 0.0F;
		List<ClassifyResult>crs = new ArrayList<ClassifyResult>();//分类结果
		for(int i = 0 ; i < Classes.length ; i++)
		{
			String Ci = Classes[i];
			Map<Integer, Map<String, Double>> classMap = TF.get(Ci);
			probility = calcProd(terms,Ci,classMap);
			//保存分类结果
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;
			cr.probility = probility;
			//System.out.println("In process...");
			//System.out.println(Ci + ": " + probility);
			crs.add(cr);
		}
		//排序
		java.util.Collections.sort(crs,new Comparator() 
		{
			public int compare(final Object o1 , final Object o2)
			{
				final ClassifyResult m1 = (ClassifyResult)o1;
				final ClassifyResult m2 = (ClassifyResult)o2;
				final double ret = m1.probility - m2.probility;
				if(ret < 0){
					return 1;
				}
				else {
					return -1;
				}
			}
		});
		return crs.get(0).classification;
	}

	/**
	 * 
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static String ChineseSpliter(String context)throws IOException 
	{
		String result = null;
		StringBuilder sBuilder = new StringBuilder();
		Analyzer analyzer = new IKAnalyzer(true);

		try {
			StringReader reader = new StringReader(context);
			TokenStream ts = analyzer.tokenStream("", reader);
			CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
			ts.reset();
			while (ts.incrementToken()) {
				String termString = term.toString();
				sBuilder.append(termString + " ");
			}
			result = sBuilder.toString();
			ts.end();
		} catch (IOException e) {
			e.printStackTrace();
		}
		analyzer.close();
		return result;
	}
}
