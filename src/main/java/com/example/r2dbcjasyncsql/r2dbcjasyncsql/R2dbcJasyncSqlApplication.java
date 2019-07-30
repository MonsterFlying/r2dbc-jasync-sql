package com.example.r2dbcjasyncsql.r2dbcjasyncsql;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.r2dbc.mysql.MysqlConnectionFactoryProvider;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.function.ReactiveDataAccessStrategy;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;



@SpringBootApplication
public class R2dbcJasyncSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcJasyncSqlApplication.class, args);
    }


    @RestController
    @RequiredArgsConstructor
    @RequestMapping("city")
    public class CityController {


        private final DatabaseClient connection;
        @GetMapping("list")
        public Flux<BasisCity> list() {

       /*     Flux<BasisCity> all1 = connection.select()
                    .from(BasisCity.class)
                    .fetch()
                    .all();
            all1.subscribe(p -> System.out.println(p.getCityCode()));*/
            return connection
                    .select()
                    .from(BasisCity.class)
                    .fetch()
                    .all();

        }
    }

    @Configuration
    public class JasynSqlDataSourceConfig {
        String url = "jdbc:mysql://10.2.10.59:3306/oem_cms?user=dfroot&password=H@slPZtn7eNKFP9bIEOso0I5nsz7YiZJ";
        @Bean
        public DatabaseClient connection() {
            ConnectionFactoryOptions factoryOptions = ConnectionFactoryOptions.builder()
                    .option(ConnectionFactoryOptions.HOST, "10.2.10.59")
                    .option(ConnectionFactoryOptions.PASSWORD, "H@slPZtn7eNKFP9bIEOso0I5nsz7YiZJ")
                    .option(ConnectionFactoryOptions.DATABASE, "oem_cms")
                    .option(ConnectionFactoryOptions.PORT, 3306)
                    .option(ConnectionFactoryOptions.USER, "dfroot")
                    .build();
            JasyncConnectionFactory jasyncConnectionFactory = new MysqlConnectionFactoryProvider().create(factoryOptions);

            return DatabaseClient.builder()
                    .connectionFactory(jasyncConnectionFactory)
                    .dataAccessStrategy(new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE)).build();
        }

    }


}

@Data
@Table("basis_city")
class BasisCity {

    private Long id;

    private String cityCode;

    private String cityParent;

    private Long cityLevel;


}
