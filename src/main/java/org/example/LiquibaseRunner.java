package org.example;


import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.connection.ConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class LiquibaseRunner {

    public static void main(String[] args) throws Exception {

        Map<String, Object> config = new HashMap<>();

        Scope.child(config, () -> {
            Connection connection = ConnectionManager.get();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        });

    }

}

