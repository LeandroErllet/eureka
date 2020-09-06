package br.com.craftlife.eureka.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseConfiguration {

    private String driver = "com.mysql.jdbc.Driver";

    private String dialect = "org.hibernate.dialect.MySQLDialect";

    private String url = null;

    private String user = null;

    private String password = null;

    private String redisUrl = null;


}
