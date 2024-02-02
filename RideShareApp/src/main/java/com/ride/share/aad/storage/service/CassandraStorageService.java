package com.ride.share.aad.storage.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.config.props.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class CassandraStorageService {
    private static CqlSession cqlSession;

    public static String getKEYSPACE() {
        return AppProperties.CASS_KEYSPACE;
    }

    public static void init() {
        cqlSession = CqlSession.builder().withKeyspace(getKEYSPACE()).build();
    }

    public static CqlSession getCqlSession() {
        return cqlSession;
    }

    public static void main(String[] args) {
        init();
        ResultSet execute = cqlSession.execute("Select * from ride_data");
        for (Row row : execute) {
            System.out.println(row.getFormattedContents());
        }
    }

}
