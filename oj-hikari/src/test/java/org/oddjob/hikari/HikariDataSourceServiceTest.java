package org.oddjob.hikari;

import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.state.ParentState;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class HikariDataSourceServiceTest {

    @Test
    public void testService() throws Exception {

        HikariDataSourceService test = new HikariDataSourceService();
        test.setUrl("jdbc:hsqldb:mem:test");
        test.setUser("sa");

        test.run();

        Connection connection = test.getDataSource().getConnection();

        DatabaseMetaData meta = connection.getMetaData();

        assertThat(meta.getDatabaseProductName(), is("HSQL Database Engine"));

        test.close();
    }

    @Test
    void testInOddjob() {

        String config = Objects.requireNonNull(getClass().getResource(
                "/examples/db/hikari_example.xml")).getFile();

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(new File(config));

        oddjob.run();
        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));
    }
}