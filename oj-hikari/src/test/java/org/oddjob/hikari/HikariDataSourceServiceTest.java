package org.oddjob.hikari;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

class HikariDataSourceServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(HikariDataSourceServiceTest.class);

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

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close close = console.captureConsole()) {

            oddjob.run();
        }

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        String[] lines = console.getLines();

        assertThat(lines[2], startsWith("Apple  10"));

        oddjob.destroy();

    }
}