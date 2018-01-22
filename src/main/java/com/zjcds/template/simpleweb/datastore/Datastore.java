package com.zjcds.template.simpleweb.datastore;

import com.zjcds.template.simpleweb.datastore.enums.DsType;
import org.apache.metamodel.util.HasName;

import java.io.Serializable;

public interface Datastore<T extends DatastoreConnection> extends DatastoreMonitor,Serializable, HasName {

    @Override
    String getName();

    String getDescription();

    DsType getDatastoreType();

    T getDatastoreConnection();

    MetaDataNavigator getMetaDataNavigator();

}
