package com.zjcds.template.simpleweb.datastore.factory;

import com.zjcds.template.simpleweb.datastore.impl.JdbcDatastore;

import javax.sql.DataSource;

/**
 * created date：2017-08-05
 * @author niezhegang
 */
public abstract class DataStoreFactory {

    public static JdbcDatastore createJdbcDatastore(String name,DataSource dataSource,String desc){
        return new JdbcDatastore.Builder()
                                    .name(name)
                                    .dataSource(dataSource)
                                    .description(desc)
                                    .build();
    }

    public static JdbcDatastore createJdbcDatastore(String name,DataSource dataSource){
        return createJdbcDatastore(name,dataSource,null);
    }

}
