package EmotionClassification;
/**
 * 分类结果
 * @author Alex_young
 *
 */
public class ClassifyResult {
	public double probility;
	public String classification;
	public ClassifyResult()
	{
		this.probility = 0;
		this.classification = null;
	}
}
