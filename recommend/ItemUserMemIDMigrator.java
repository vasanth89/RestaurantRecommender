package com.yelp.recommend;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.AbstractIDMigrator;

/**
 * This class is written extending Mahout's inbuilt migrator that converts strings to long values.
 * @author Vasanth
 *
 */
public class ItemUserMemIDMigrator extends AbstractIDMigrator {

	//Map to store long -> alphanumerics
	private final FastByIDMap<String> longToString;

	public ItemUserMemIDMigrator() {
		this.longToString = new FastByIDMap<String>(6000);
	}

	public void storeMapping(long longID, String stringID) {
		synchronized (longToString) {
			longToString.put(longID, stringID);
		}
	}

	@Override
	public String toStringID(long longID) throws TasteException {
		synchronized (longToString) {
			return longToString.get(longID);
		}
	}

	//Convert to long ID and store it in our map
	public long singleInit(String stringID) throws TasteException {
		long retValue = toLongID(stringID);
		storeMapping(retValue, stringID);
		return retValue;
	}

}