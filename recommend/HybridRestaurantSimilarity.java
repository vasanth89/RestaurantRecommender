package com.yelp.recommend;

import java.io.IOException;
import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.similarity.AbstractItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 * This is the class which combines the usual similarity metric with the
 * attributes. The logic is to combine two similarity values computed
 * individually and then combine them together.
 * 
 * @author Vasanth
 * 
 */
final class HybridRestaurantSimilarity extends AbstractItemSimilarity {

	private final ItemSimilarity cfSimilarity;
	private final ItemSimilarity contentSimilarity;

	/*
	 * Constructor which mentions the two similarity metrics(Item Based only)
	 * Fixed -> RestaurantSimilarity which considers the attributes of
	 * restaurant. Content based approach Variable -> Similarity which works on
	 * ratings provided by user for a restaurant
	 */
	HybridRestaurantSimilarity(DataModel dataModel, String filename)
			throws IOException, TasteException {
		super(dataModel);
		// cfSimilarity = new LogLikelihoodSimilarity(dataModel);
		// cfSimilarity = new TanimotoCoefficientSimilarity(dataModel);
		// cfSimilarity = new CityBlockSimilarity(dataModel);
		// cfSimilarity = new EuclideanDistanceSimilarity(dataModel);
		// cfSimilarity = new UncenteredCosineSimilarity(dataModel);
		cfSimilarity = new PearsonCorrelationSimilarity(dataModel);
		contentSimilarity = new RestaurantSimilarity(filename);
	}

	@Override
	public double itemSimilarity(long itemID1, long itemID2)
			throws TasteException {
		return contentSimilarity.itemSimilarity(itemID1, itemID2)
				* cfSimilarity.itemSimilarity(itemID1, itemID2);
	}

	@Override
	public double[] itemSimilarities(long itemID1, long[] itemID2s)
			throws TasteException {
		double[] result = contentSimilarity.itemSimilarities(itemID1, itemID2s);
		double[] multipliers = cfSimilarity.itemSimilarities(itemID1, itemID2s);
		for (int i = 0; i < result.length; i++) {
			result[i] *= multipliers[i];
		}
		return result;
	}

	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		cfSimilarity.refresh(alreadyRefreshed);
	}

}
