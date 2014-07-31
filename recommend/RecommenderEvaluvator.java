package com.yelp.recommend;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

/**
 * A very important class that is going to provide us with the evaluations of
 * our recommendation algorithms.
 * 
 * @author Vasanth
 * 
 */
public class RecommenderEvaluvator {

	public static void main(String args[]) {
		//Input files to recommender system
		String recsFile = "data/phoenix/vote_phoenix_25.csv";
		final String restaurant_filename = "data/phoenix/business_list_phoenix.txt";

		/*
		 * creating a RecommenderBuilder Objects with overriding the
		 * buildRecommender method this builder object is used as one of the
		 * parameters for RecommenderEvaluator - evaluate method
		 */

		// for Recommendation evaluations
		RecommenderBuilder userSimRecBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel dm)
					throws TasteException {
				
				// The Similarity metrics used in the recommender - Item based
				// ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
				// ItemSimilarity sim = new TanimotoCoefficientSimilarity(dm);
				// ItemSimilarity sim = new CityBlockSimilarity(dm);
				// ItemSimilarity sim = new EuclideanDistanceSimilarity(dm);
				// ItemSimilarity sim = new UncenteredCosineSimilarity(dm);
				// ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
				
				/* ItemSimilarity sim = null; 
				 try { 
					 sim = new HybridRestaurantSimilarity(dm,restaurant_filename); 
				 } catch (IOException e) {
					 e.printStackTrace(); 
				 }*/
				 
				
				// The Similarity metrics used in the recommender - User based
				// UserSimilarity sim = new LogLikelihoodSimilarity(dm);
				// UserSimilarity sim = new TanimotoCoefficientSimilarity(dm);
				// UserSimilarity sim = new CityBlockSimilarity(dm);
				// UserSimilarity sim = new EuclideanDistanceSimilarity(dm);
				// UserSimilarity sim = new UncenteredCosineSimilarity(dm);
				// UserSimilarity sim = new PearsonCorrelationSimilarity(dm);
				// UserSimilarity sim = new SpearmanCorrelationSimilarity(dm);
				
				/*
				 * The Neighborhood algorithms used in your recommender not
				 * required if you are evaluating your item based
				 * recommendations
				 */
				// UserNeighborhood neighborhood =new NearestNUserNeighborhood(5, sim, dm);
				// UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, sim, dm);

				// Recommender used in your real time implementation
				// Recommender recommender =new GenericUserBasedRecommender(dm, neighborhood, sim);
				// GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);
				 RestaurantRecommender recommender = new RestaurantRecommender(dm,restaurant_filename);
				
				return recommender;
			}
		};

		try {

			// Use this only if the code is for unit tests and other examples to
			// guarantee repeated results
			RandomUtils.useTestSeed();

			// Creating a data model to be passed on to RecommenderEvaluator -
			// evaluate method
			FileDataModel dataModel = new AlphaItemFileDataModel(new File(
					recsFile));

			/*
			 * There are many kinds of evaluators available:
			 * 1. RMSRecommenderEvaluator - gives an evaluation score
			 * 2. AverageAbsoluteDifferenceRecommenderEvaluator - gives an evaluation score
			 * 3. GenericRecommenderIRStatsEvaluator - Used for getting precision, recall, F-1 score and so on
			 */
			 RecommenderEvaluator evaluator1 = new RMSRecommenderEvaluator(); 
			 AverageAbsoluteDifferenceRecommenderEvaluator evaluator2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
			
			double RMScore = evaluator1.evaluate(userSimRecBuilder,null,dataModel, 0.7, 0.3);
			double AADScore = evaluator2.evaluate(userSimRecBuilder,null,dataModel, 0.7, 0.3);
			System.out.println("RMS Evaluation score : "+RMScore);
			System.out.println("AAB Evaluation score : "+AADScore);
			
			RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
			IRStatistics stats = statsEvaluator.evaluate(userSimRecBuilder,
					null, dataModel, null, 10,
					GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.4);
			System.out.println("Precision : " + stats.getPrecision());
			System.out.println("Recall : " + stats.getRecall());
			System.out.println("F-1 Score : " + stats.getF1Measure());
			System.out.println("Fallout : " + stats.getFallOut());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
