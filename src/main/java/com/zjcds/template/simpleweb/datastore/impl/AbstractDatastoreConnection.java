package com.zjcds.template.simpleweb.datastore.impl;

import com.zjcds.template.simpleweb.datastore.Datastore;
import lombok.Getter;
import lombok.Setter;

/**
 * created date：2017-08-05
 *
 * @author niezhegang
 */
@Setter
@Getter
public class AbstractDatastoreConnection {

    private Datastore datastore;

    public AbstractDatastoreConnection(Datastore datastore ) {
        this.datastore = datastore;
    }
}
