package ua.com.juja.yeryery.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class JdbcManagerTest {

    private static final String DATABASE = Preparator.TEST_DB;
    private static final String USERNAME = Preparator.USERNAME;
    private static final String PASSWORD = Preparator.PASSWORD;

    private static final String DATABASE_TO_DROP = "databasetodrop";
    private static final DatabaseManager MANAGER = Preparator.TEST_MANAGER;

    private static final String TEST_TABLE = "test";
    private static final String USERS_TABLE = "users";

    @BeforeClass
    public static void setupTestDB() throws SQLException {
        Preparator.setupDB();
    }

    @AfterClass
    public static void clearAfterTests() {
        Preparator.deleteDB();
    }

    @Test
    public void testGetTableNames() throws SQLException {
        //when
        Set<String> actualNames = MANAGER.getTableNames();

        //then
        Set<String> expectedNames = new LinkedHashSet<>(Arrays.asList(TEST_TABLE, USERS_TABLE));
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testConnect() throws SQLException {
        //given
        Preparator.connectToDefaultDB();

        //when
        MANAGER.connect(DATABASE, USERNAME, PASSWORD);
        Set<String> actualNames = MANAGER.getTableNames();

        //then
        Set<String> expectedNames = new LinkedHashSet<>(Arrays.asList(TEST_TABLE, USERS_TABLE));
        assertEquals(expectedNames, actualNames);
    }

    @Test(expected = SQLException.class)
    public void testConnectToNotExistingDatabase() throws SQLException {
        //given
        Preparator.connectToDefaultDB();

        //when
        try {
            MANAGER.connect("NotExistingDatabase", USERNAME, PASSWORD);
            fail("Expected ConnectException");

        //then
        } catch (Exception e) {
            MANAGER.connect(DATABASE, USERNAME, PASSWORD);
            throw e;
        }
    }

    @Test(expected = SQLException.class)
    public void testConnectWithWrongPassword() throws SQLException {
        //given
        Preparator.connectToDefaultDB();

        //when
        try {
            MANAGER.connect(DATABASE, USERNAME, "WrongPassword");
            fail("Expected ConnectException");

        //then
        } catch (Exception e) {
            MANAGER.connect(DATABASE, USERNAME, PASSWORD);
            throw e;
        }
    }

    @Test
    public void testGetDatabases() throws SQLException {
        //when
        Set<String> actualDatabases = MANAGER.getDatabases();

        //then
        assertTrue(actualDatabases.contains(DATABASE));
    }

    @Test
    public void testGetTableColumns() throws SQLException {
        //when
        Set<String> actualColumns = MANAGER.getTableColumns(USERS_TABLE);

        //then
        Set<String> expectedColumns = new LinkedHashSet<>(Arrays.asList("code", "name", "age"));
        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testClearTable() throws SQLException {
        //when
        MANAGER.clear(TEST_TABLE);

        //then
        List<DataSet> expectedContent = new LinkedList<>();
        assertEquals(expectedContent, MANAGER.getDataContent(TEST_TABLE));

        Preparator.createTableTest();
    }

    @Test
    public void testCreateTable() throws SQLException {
        //given
        DataEntry primaryKey = new DataEntryImpl();
        primaryKey.setEntry("key", "integer");

        DataSet columns = new DataSetImpl();
        columns.put("login", "text");

        //when
        final String TABLE_TO_CREATE = "tabletocreate";
        MANAGER.create(TABLE_TO_CREATE, primaryKey, columns);

        //then
        Set<String> expectedColumns = new LinkedHashSet<>(Arrays.asList("key", "login"));
        assertTrue(MANAGER.getTableNames().contains(TABLE_TO_CREATE));
        assertEquals(expectedColumns, MANAGER.getTableColumns(TABLE_TO_CREATE));

        MANAGER.drop(TABLE_TO_CREATE);
    }

    @Test
    public void testDropTable() throws SQLException {
        //when
        MANAGER.drop(TEST_TABLE);

        //then
        assertFalse(MANAGER.getTableNames().contains(TEST_TABLE));
        Preparator.createTableTest();
    }

    @Test(expected = SQLException.class)
    public void testDropNotExistingTable() throws SQLException {
        //when
        MANAGER.drop("NotExistingTable");
    }

    @Ignore
    @Test
    public void testCreateDB() throws SQLException {
        //when
        MANAGER.createDB(DATABASE_TO_DROP);

        //then
        assertTrue(MANAGER.getDatabases().contains(DATABASE_TO_DROP));
        MANAGER.dropDB(DATABASE_TO_DROP);
    }

    @Ignore
    @Test
    public void testDropDB() throws SQLException {
        //given
        MANAGER.createDB(DATABASE_TO_DROP);

        //when
        MANAGER.dropDB(DATABASE_TO_DROP);

        //then
        assertFalse(MANAGER.getDatabases().contains(DATABASE_TO_DROP));
    }

    @Test
    public void testInsert() throws SQLException {
        //given
        DataSet expectedData = getDataSet();

        //when
        MANAGER.insert(USERS_TABLE, expectedData);

        //then
        DataSet actualData = MANAGER.getDataContent(USERS_TABLE).get(0);
        assertEquals(expectedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(expectedData.getValues(), actualData.getValues());

        MANAGER.clear(USERS_TABLE);
    }

    @Test
    public void testUpdate() throws SQLException {
        //given
        DataSet updatedData = getDataSet();
        MANAGER.insert(USERS_TABLE, updatedData);

        //when
        updatedData.put("code", 150);

        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry("age", 25);

        MANAGER.update(USERS_TABLE, updatedData, definingEntry);

        //then
        DataSet actualData = MANAGER.getDataContent(USERS_TABLE).get(0);
        assertEquals(updatedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(updatedData.getValues(), actualData.getValues());

        MANAGER.clear(USERS_TABLE);
    }

    @Test
    public void testDelete() throws SQLException {
        //given
        DataSet deletedData = getDataSet();
        MANAGER.insert(USERS_TABLE, deletedData);

        //when
        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry("age", 25);

        MANAGER.delete(USERS_TABLE, definingEntry);

        //then
        List<DataSet> expectedContent = new LinkedList<>();
        assertEquals(expectedContent, MANAGER.getDataContent(USERS_TABLE));
    }

    @Test
    public void testGetDataContent() throws SQLException {
        //given
        DataSet expectedData = getDataSet();
        MANAGER.insert(USERS_TABLE, expectedData);

        //when
        DataSet actualData = MANAGER.getDataContent(USERS_TABLE).get(0);

        //then
        assertEquals(expectedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(expectedData.getValues(), actualData.getValues());
    }

    private DataSet getDataSet() {
        DataSet expectedData = new DataSetImpl();
        expectedData.put("code", 100);
        expectedData.put("name", "Jack");
        expectedData.put("age", 25);
        return expectedData;
    }
}
