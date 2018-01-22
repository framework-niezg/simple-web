package com.zjcds.template.simpleweb.datastore.impl;

import com.zjcds.template.simpleweb.datastore.Datastore;
import com.zjcds.template.simpleweb.datastore.DatastoreConnection;
import lombok.Getter;
import lombok.Setter;

/**
 * created date：2017-08-05
 * @author niezhegang
 */
@Setter
@Getter
public abstract class AbstractDataStore<T extends DatastoreConnection> implements Datastore<T> {

    private String description;

    private String name;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }
}
