package com.zjcds.template.simpleweb.datastore;

import org.apache.metamodel.UpdateableDataContext;

public interface UpdateableDatastoreConnection extends DatastoreConnection {

    UpdateableDataContext getUpdateableDataContext();

}
