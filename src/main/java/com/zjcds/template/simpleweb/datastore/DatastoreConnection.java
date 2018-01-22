package com.zjcds.template.simpleweb.datastore;

import org.apache.metamodel.DataContext;

public interface DatastoreConnection {

    DataContext getDataContext();

    Datastore getDatastore();

    

}
