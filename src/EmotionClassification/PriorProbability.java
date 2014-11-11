package EmotionClassification;

public class PriorProbability {
	/**
	 * P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
	 *其中，N(C=c<sub>j</sub>)表示类别c<sub>j</sub>中的训练文本数量；
	 * N表示训练文本集总数量。
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
