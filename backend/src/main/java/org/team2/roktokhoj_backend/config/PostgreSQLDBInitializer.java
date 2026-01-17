package org.team2.roktokhoj_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.*;

@Configuration
@Slf4j
public class PostgreSQLDBInitializer {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        String dbName = extractDatabaseName(datasourceUrl);
        String baseUrl = datasourceUrl.substring(0, datasourceUrl.lastIndexOf('/')) + "/postgres";

        try (Connection conn = DriverManager.getConnection(baseUrl, username, password);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'"
            );

            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                log.info("Database '{}' created successfully", dbName);
            } else {
                log.info("Database '{}' already exists", dbName);
            }
        } catch (SQLException e) {
            log.error("Error creating database: {}", e.getMessage(), e);
        }

        return DataSourceBuilder
                .create()
                .url(datasourceUrl)
                .username(username)
                .password(password)
                .build();
    }

    private String extractDatabaseName(String url) {
        return url.substring(url.lastIndexOf('/') + 1).split("\\?")[0];
    }
}
