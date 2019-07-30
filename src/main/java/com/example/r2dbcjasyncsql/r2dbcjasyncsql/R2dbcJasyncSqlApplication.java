package com.example.r2dbcjasyncsql.r2dbcjasyncsql;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


interface CityRepository extends ReactiveCrudRepository<BasisCity, Long> {

    @Query("SELECT * FROM basis_city")
    Flux<BasisCity> list();

}

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
    @EnableR2dbcRepositories
    public class JasynSqlDataSourceConfig extends AbstractR2dbcConfiguration {
        String url = "jdbc:mysql://10.2.10.59:3306/oem_cms?user=dfroot&password=H@slPZtn7eNKFP9bIEOso0I5nsz7YiZJ";

        @Override
        public ConnectionFactory connectionFactory() {
            com.github.jasync.sql.db.Configuration configuration = new com.github.jasync.sql.db.Configuration(
                    "dfroot",
                    "10.2.10.59",
                    3306,
                    "H@slPZtn7eNKFP9bIEOso0I5nsz7YiZJ",
                    "oem_cms"
            );
            MySQLConnectionFactory mySQLConnectionFactory = new MySQLConnectionFactory(configuration);
            return new JasyncConnectionFactory(mySQLConnectionFactory);
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
