package com.zjcds.template.simpleweb.datastore.impl;

import com.zjcds.template.simpleweb.datastore.DatastoreConnection;
import com.zjcds.template.simpleweb.datastore.MetaDataNavigator;
import org.apache.metamodel.schema.Table;

/**
 * created date：2017-08-05
 *
 * @author niezhegang
 */
public class MetaDataNavigatorImpl implements MetaDataNavigator {

    private DatastoreConnection datastoreConnection;

    public MetaDataNavigatorImpl(DatastoreConnection datastoreConnection) {
        this.datastoreConnection = datastoreConnection;
    }

    @Override
    public Table getTable(String tableName) {
        return getTable(datastoreConnection.getDataContext().getDefaultSchema().getName(),tableName);
    }

    @Override
    public Table getTable(String schemaName, String tableName) {
        return datastoreConnection.getDataContext().getTableByQualifiedLabel(schemaName+"."+tableName);
    }
}
