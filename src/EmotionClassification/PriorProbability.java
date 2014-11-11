package EmotionClassification;

public class PriorProbability {
	/**
	 * P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
	 *���У�N(C=c<sub>j</sub>)��ʾ���c<sub>j</sub>�е�ѵ���ı�������
	 * N��ʾѵ���ı�����������
	 */
	/**
	 * Prior
	 * @param c certain Classification
	 * @return the prior probability
	 */
	public static double calculatePc(String c)
	{
		double ret = 0F;
		double Nc = TrainingDataManager.fileCount.get(c);
		double N = TrainingDataManager.allTweetCount;
		ret = Nc / N;
		return ret;
	}
}
