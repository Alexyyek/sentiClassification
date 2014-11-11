package EmotionClassification;

import java.io.IOException;
import java.util.Map;

public class ClassConditionalProbability{
	/**
	 * N(X=xj|C=cj)��ʾ���c�а�������x������
	 * N(C=cj)��ʾ�����ѵ���ı�����
	 * V��ʾ�������
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
