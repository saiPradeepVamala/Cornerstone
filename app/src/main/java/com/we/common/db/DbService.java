package com.we.common.db;

import java.util.ArrayList;
import java.util.List;

//import com.db4o.Db4oEmbedded;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;
//import com.db4o.config.EmbeddedConfiguration;
//import com.db4o.query.Query;
import com.we.common.utils.WELogger;

public class DbService {

//	private static ObjectContainer objectContainer 			= 		null;
//	private static EmbeddedConfiguration configuration 		= 		null;
	private final String TAG 								= 		getClass().getSimpleName(); 
	
	/*
	 * static and synchronized to ensure that file locked
	 * exceptions will not occur when multiple threads from the same process
	 * attempt to access this class at near the same time.
	 */
//	private static synchronized ObjectContainer getDb() {
//		objectContainer = objectContainer == null ? Db4oEmbedded.openFile(DbService.GetConfig(), DbAdapter.createDbFile()) : objectContainer;
//		return objectContainer;
//	}

//	private static EmbeddedConfiguration GetConfig() {
//		configuration = new DbConfigurationBuilder().getConfiguration();
//		return configuration;
//	}

	/**
	 * This is used by tests only. Do not use this method for any other reason.
	 */
//	public void closeDb() {
//		objectContainer.close();
//		objectContainer = null;
//	}

//	public void setActivationDepth(int depth) {
//		getDb();
//		configuration.common().activationDepth(depth);
//	}

//	public ObjectContainer ObjectContainer() {
//		return getDb();
//	}

	/**
	 * Save an entity to the DB. Entity must be marked with
	 * {@code IDomainEntity}. Remember to set the appropriate rules in the
	 * {@code DbConfigurationBuilder} class so that the data is persisted
	 * properly in child objects.
	 */
	public <T> void Save(DBEntity<T> entity) {
//		try {
//			ObjectContainer db = getDb();
//			db.store(entity);
//			db.commit();
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//		}
	}



	/**
	 * Save List of entities of type T to DB. Each entity must be marked with
	 * {@code IDomainEntity}. Remember to set the appropriate rules in the
	 * {@code DbConfigurationBuilder} class so that the data is persisted
	 * properly in child objects.
	 */
	public <T> void Save(List<T> entity) {
		try {
//			ObjectContainer db = getDb();
//			for (T iDomainEntity : entity) {
//				db.store(iDomainEntity);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}



	public <T> void Delete(Class<T> entity) {
		try {
//			ObjectContainer db = getDb();
//			List<T> results = db.query(entity);
//			for (T iDomainEntity : results) {
//				db.delete(iDomainEntity);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}

	//deleting list of values
	public <T> void Delete(List<T> entity) {
		try {
//			ObjectContainer db = getDb();
//			for (T iDomainEntity : entity) {
//				db.delete(iDomainEntity);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}

	/**
	 * Delete entity of type T containing id from DB. Make sure to use {@link
//	 * DbService.setActivationDepth()} so that the appropriate depth is applied
	 * to the query. Otherwise, cascade deletion will not happen. Don't use this
	 * method to delete items in lists. Doing so will cause the record to be
	 * deleted from the DB while the item remains in the list. To delete items
	 * from lists, use {@code DeleteFromList}
	 */
	public <T> void DeleteById(Class<T> entity, String fieldName, String id) {
		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(id);
//
//			ObjectSet<T> result = query.execute();
//			for (T item : result) {
//				db.delete(item);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}
	
	
	
	public <T> void DeleteById(Class<T> entity, String fieldName, long id) {
		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(id);
//
//			ObjectSet<T> result = query.execute();
//			for (T item : result) {
//				db.delete(item);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}

	/**
	 * Delete entity of type T containing id from DB. Make sure to use {@link
//	 * DbService.setActivationDepth()} so that the appropriate depth is applied
	 * to the query. Otherwise, cascade deletion will not happen. Don't use this
	 * method to delete items in lists. Doing so will cause the record to be
	 * deleted from the DB while the item remains in the list. To delete items
	 * from lists, use {@code DeleteFromList}
	 */
	public <T> void DeleteByEntryTime(Class<T> entity, String fieldName, Long entryTime) {
		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(entryTime);
//
//			ObjectSet<T> result = query.execute();
//			for (T item : result) {
//				db.delete(item);
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}

	/**
	 * Get List of entities of type T from DB. Make sure to use {@link
//	 * DbService.setActivationDepth()} so that the appropriate depth is applied
	 * to the query. Otherwise, the data that is returned may not be hydrated
	 * down to the depth that is needed.
	 */
//	public <T> List<T> Get(Class<T> entity) {
//		try {
//			ObjectContainer db = getDb();
//			return db.query(entity);
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//			return new ArrayList<T>();
//		}
//	}

//	public <T>  ObjectSet<T> GetEntity(Class<T> entity) {
//		try {
//			ObjectContainer db = getDb();
//			return db.query(entity);
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//			return null;
//		}
//	}


//	public <T> int GetLocationsSize(Class<T> entity) {
//		try {
////			ObjectContainer db = getDb();
////			return db.query(entity).size();
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//			return -1;
//		}
//	}

	/**
	 * Get entity of type T containing id from DB. Make sure to use {@link
//	 * DbService.setActivationDepth()} so that the appropriate depth is applied
	 * to the query. Otherwise, the data that is returned may not be hydrated
	 * down to the depth that is needed.
	 */
//	public <T> T GetById(Class<T> entity, String fieldName, long id) {
//		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(id);
//			ObjectSet<T> result = query.execute();
//			if (result == null || result.size() == 0) { return null; }
//			return result.get(0);
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//			return null;
//		}
//	}

//	public <T> T GetById(Class<T> entity, String fieldName, String id) {
//		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(id);
//			ObjectSet<T> result = query.execute();
//			if (result == null || result.size() == 0) { return null; }
//			return result.get(0);
//		}
//		catch (Exception e) {
//			WELogger.infoLog(TAG,""+e);
//			return null;
//		}
//	}


//	public <T> List<T> getResultsById(Class<T> entity, String fieldName, String value) {
//		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(value);
//			ObjectSet<T> result = query.execute();
//			if (result == null || result.size() == 0) { return null; }
//			List<T> finalResults = new ArrayList<T>();
//			for (T t : result) {
//				finalResults.add(t);
//			}
//			return finalResults;
//		}
//		catch (Exception exception) {
//			WELogger.errorLog(TAG, "Exception while getting results by ID :", exception);
//			return null;
//		}
//	}


//	public <T> List<T> getResultsById(Class<T> entity, String fieldName, long value) {
//		try {
//			ObjectContainer db = getDb();
//			Query query = db.query();
//			query.constrain(entity);
//			query.descend(fieldName).constrain(value);
//			ObjectSet<T> result = query.execute();
//			if (result == null || result.size() == 0) { return null; }
//			List<T> finalResults = new ArrayList<T>();
//			for (T t : result) {
//				finalResults.add(t);
//			}
//			return finalResults;
//		}
//		catch (Exception exception) {
//			WELogger.errorLog(TAG, "Exception in getResultsById()  :  ", exception);
//			return null;
//		}
//	}


//	public <T> List<T>  getResultsInRange(Class<T> entity, String fieldName, double value, int incrementBy, int decrementBy) {
//		double decrementValue	= 	value-decrementBy;
//		double incrementValue 	=	value+incrementBy;
//		ObjectContainer db = getDb();
//		Query query = db.query();
//		query.constrain(entity);
//		query.descend(fieldName).constrain(decrementValue).greater()
//		.and(query.descend(fieldName).constrain(incrementValue).smaller());
//		List<T>  result = query.execute();
//		if (result == null || result.size() == 0) { return null; }
//		else
//			return result;
//	};
	/**
	 * Remove the list @param toDelete from @param childList and then save @param
	 * parent. The parent and the two lists must be associated with each other.
	 * The removal does NOT delete the objects from the DB; instead, they are
	 * orphaned. If the developer wishes for the the objects to also be deleted
	 * from the DB, then set @param shouldDeleteChildFromDb to true. All
	 * entities MUST implement  To ensure that child
	 * objects are updated and deleted, make sure to modify
	 * {@link DbConfigurationBuilder} so that it properly handles each entity.
	 * Remember to set the appropriate rules in the
	 * {@code DbConfigurationBuilder} class so that the data is deleted in child
	 * objects properly.
	 */
	public <T, U> void DeleteFromList(DBEntity<T> parent, List<U> childList, List<U> toDelete, boolean shouldDeleteChildFromDb) {
		try {
//			childList.removeAll(toDelete);
//			ObjectContainer db = getDb();
//			Save(parent);
//			if (shouldDeleteChildFromDb) {
//				for (U iDomainEntity : toDelete) {
//					db.delete(iDomainEntity);
//				}
//			}
//			db.commit();
		}
		catch (Exception e) {
			WELogger.infoLog(TAG,""+e);
		}
	}
}
