package EmotionClassification;

import java.io.IOException;
import java.util.Map;

public class ClassConditionalProbability{
	/**
	 * N(X=xj|C=cj)表示类别c中包含属性x的数量
	 * N(C=cj)表示类别中训练文本数量
	 * V表示类别总数
	 */
	private static TrainingDataManager tdm = new TrainingDataManager();
	private static final float M = 0F;
	/**
	 * 
	 * @param x
	 * @param c
	 * @return
	 * @throws IOException
	 */
	public double calculatePxc(String x,String c, Map<Integer, Map<String, Double>>classMap) throws IOException
	{
		double ret = 0F;
		//document frequency
		double Nxc = tdm.getCountContainKeyWord(x,c,classMap);
		double Nc = TrainingDataManager.fileCount.get(c);
		double Nv = tdm.getTrainingClassification().length;
		
		ret = (Nxc + 1) / (Nc + M + Nv);
		return ret;
	}
}
