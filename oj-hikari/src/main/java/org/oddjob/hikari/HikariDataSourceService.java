package org.oddjob.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

/**
 * A Service for use in Oddjob that provides a data source.
 */
public class HikariDataSourceService implements Runnable, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(HikariDataSourceService.class);

    private String name;

    private volatile int size;

    private volatile Properties properties;

    private volatile String url;

    private volatile String user;

    private volatile String password;

    private volatile HikariDataSource dataSource;

    @Override
    public void run() {

        HikariConfig config = Optional.ofNullable(this.properties)
                .map(HikariConfig::new)
                .orElseGet(HikariConfig::new);

        Optional.ofNullable(this.url).ifPresent(config::setJdbcUrl);
        Optional.ofNullable(this.user).ifPresent(config::setUsername);
        Optional.ofNullable(this.password).ifPresent(config::setPassword);

        this.dataSource = new HikariDataSource(config);

        logger.info("Created datasource from URL {{}} and User {{}}", config.getJdbcUrl(), config.getUsername());
    }

    @Override
    public void close() {
        dataSource.close();
        dataSource = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return name == null ? getClass().getSimpleName() : name;
    }
}
