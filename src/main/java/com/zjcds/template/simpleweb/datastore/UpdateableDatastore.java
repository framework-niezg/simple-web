package com.zjcds.template.simpleweb.datastore;


public interface UpdateableDatastore<T extends UpdateableDatastoreConnection> extends Datastore<T> {


    T getUpdateableDatastoreConnection() ;

}
