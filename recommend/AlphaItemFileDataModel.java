package com.yelp.recommend;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

/**
 * This class is used to handle the alphanumeric IDs in restaurants and user
 * names. Mahout only supports long values for user id and item id. Hence the
 * need to convert alphanumeric IDs to long IDs and then do the reverse mapping
 * as well.
 * 
 * @author Vasanth
 * 
 */
public class AlphaItemFileDataModel extends FileDataModel {
	
	//Initialize migrator classes that we will use to convert IDs to long values
	private static final long serialVersionUID = 1387667251255514876L;
	private ItemUserMemIDMigrator memIdMigtr;
	private ItemUserMemIDMigrator userIdMigtr;

	public AlphaItemFileDataModel(File dataFile) throws IOException {
		super(dataFile);
	}

	@Override
	protected long readItemIDFromString(String value) {
		long retValue = 0;
		if (memIdMigtr == null)
			memIdMigtr = new ItemUserMemIDMigrator();
		try {
			retValue = memIdMigtr.singleInit(value);
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	@Override
	protected long readUserIDFromString(String value) {
		long retValue = 0;
		if (userIdMigtr == null)
			userIdMigtr = new ItemUserMemIDMigrator();
		try {
			retValue = userIdMigtr.singleInit(value);
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	//method that does the reverse mapping of restaurant ID from long->alphanumeric(actual)
	String getItemIDAsString(long itemId) throws TasteException {
		return memIdMigtr.toStringID(itemId);
	}

	//method that does the reverse mapping of user ID from long->alphanumeric(actual)
	String getUserIDAsString(long userId) throws TasteException {
		return userIdMigtr.toStringID(userId);
	}

}