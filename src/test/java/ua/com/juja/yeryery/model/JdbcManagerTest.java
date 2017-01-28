package ua.com.juja.yeryery.model;

import org.junit.*;
import ua.com.juja.yeryery.controller.commands.Utility.ConnectException;
import ua.com.juja.yeryery.integration.Preparator;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class JdbcManagerTest {

    private static final String DATABASE = Preparator.DATABASE;
    private static final String USERNAME = Preparator.USERNAME;
    private static final String PASSWORD = Preparator.PASSWORD;
    private static final String DATABASE_TO_DROP = Preparator.DATABASE_TO_DROP;

    private static DatabaseManager testManager = new JdbcManager();

    @BeforeClass
    public static void setupTestDB() {
        Preparator.setupDB(testManager);
    }

    @AfterClass
    public static void clearAfterTests() {
        Preparator.deleteDB(testManager);
    }

    @Test
    public void testGetTableNames() {
        //when
        Set<String> actualNames = testManager.getTableNames();

        //then
        Set<String> expectedNames = new LinkedHashSet<>(Arrays.asList("test", "users"));
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testConnect() {
        //given
        Preparator.disconnectFromDB(testManager);

        //when
        testManager.connect(DATABASE, USERNAME, PASSWORD);
        Set<String> actualNames = testManager.getTableNames();

        //then
        Set<String> expectedNames = new LinkedHashSet<>(Arrays.asList("test", "users"));
        assertEquals(expectedNames, actualNames);
    }

    @Test(expected = ConnectException.class)
    public void testConnectToNotExistingDatabase() {
        //given
        Preparator.disconnectFromDB(testManager);

        //when
        try {
            testManager.connect("NotExistingDatabase", USERNAME, PASSWORD);
            fail("Expected ConnectException");

        //then
        } catch (Exception e) {
            testManager.connect(DATABASE, USERNAME, PASSWORD);
            throw e;
        }
    }

    @Test(expected = ConnectException.class)
    public void testConnectWithWrongPassword() {
        //given
        Preparator.disconnectFromDB(testManager);

        //when
        try {
            testManager.connect(DATABASE, USERNAME, "WrongPassword");
            fail("Expected ConnectException");

        //then
        } catch (Exception e) {
            testManager.connect(DATABASE, USERNAME, PASSWORD);
            throw e;
        }
    }

    @Test
    public void testGetDatabases() {
        //when
        Set<String> actualDatabases = testManager.getDatabases();

        //then
        assertTrue(actualDatabases.contains(DATABASE));
    }

    @Test
    public void testGetTableColumns() {
        //when
        Set<String> actualColumns = testManager.getTableColumns("users");

        //then
        Set<String> expectedColumns = new LinkedHashSet<>(Arrays.asList("code", "name", "age"));
        assertEquals(expectedColumns, actualColumns);
    }

    @Test
    public void testClearTable() {
        //when
        testManager.clear("test");

        //then
        List<DataSet> expectedContent = new LinkedList<>();
        assertEquals(expectedContent, testManager.getDataContent("test"));

        Preparator.createTableTest(testManager);
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
        testManager.create(TABLE_TO_CREATE, primaryKey, columns);

        //then
        Set<String> expectedColumns = new LinkedHashSet<>(Arrays.asList("key", "login"));
        assertTrue(testManager.getTableNames().contains(TABLE_TO_CREATE));
        assertEquals(expectedColumns, testManager.getTableColumns(TABLE_TO_CREATE));

        testManager.drop(TABLE_TO_CREATE);
    }

    @Test
    public void testDropTable() {
        //when
        testManager.drop("test");

        //then
        assertFalse(testManager.getTableNames().contains("test"));
        Preparator.createTableTest(testManager);
    }

    @Test(expected = RuntimeException.class)
    public void testDropNotExistingTable() {
        //when
        testManager.drop("NotExistingTable");
    }

    @Ignore
    //todo убрать ignore
    public void testCreateDB() {
        //when
        testManager.createDB(DATABASE_TO_DROP);

        //then
        assertTrue(testManager.getDatabases().contains(DATABASE_TO_DROP));
        testManager.dropDB(DATABASE_TO_DROP);
    }

    @Ignore
    public void testDropDB() {
        //given
        testManager.createDB(DATABASE_TO_DROP);

        //when
        testManager.dropDB(DATABASE_TO_DROP);

        //then
        assertFalse(testManager.getDatabases().contains(DATABASE_TO_DROP));
    }

    @Test
    public void testInsert() throws SQLException {
        //given
        DataSet expectedData = new DataSetImpl();
        expectedData.put("code", 100);
        expectedData.put("name", "Jack");
        expectedData.put("age", 25);

        //when
        testManager.insert("users", expectedData);

        //then
        DataSet actualData = testManager.getDataContent("users").get(0);
        assertEquals(expectedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(expectedData.getValues(), actualData.getValues());

        testManager.clear("users");
    }

    @Test
    public void testUpdate() throws SQLException {
        //given
        DataSet updatedData = new DataSetImpl();
        updatedData.put("code", 100);
        updatedData.put("name", "Jack");
        updatedData.put("age", 25);
        testManager.insert("users", updatedData);

        //when
        updatedData.put("code", 150);

        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry("age", 25);

        testManager.update("users", updatedData, definingEntry);

        //then
        DataSet actualData = testManager.getDataContent("users").get(0);
        assertEquals(updatedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(updatedData.getValues(), actualData.getValues());

        testManager.clear("users");
    }

    @Test
    public void testDelete() throws SQLException {
        //given
        DataSet deletedData = new DataSetImpl();
        deletedData.put("code", 100);
        deletedData.put("name", "Jack");
        deletedData.put("age", 25);
        testManager.insert("users", deletedData);

        //when
        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry("age", 25);

        testManager.delete("users", definingEntry);

        //then
        List<DataSet> expectedContent = new LinkedList<>();
        assertEquals(expectedContent, testManager.getDataContent("users"));
    }

    @Test
    public void testGetDataContent() throws SQLException {
        //given
        DataSet expectedData = new DataSetImpl();
        expectedData.put("code", 100);
        expectedData.put("name", "Jack");
        expectedData.put("age", 25);
        testManager.insert("users", expectedData);

        //when
        DataSet actualData = testManager.getDataContent("users").get(0);

        //then
        assertEquals(expectedData.getColumnNames(), actualData.getColumnNames());
        assertEquals(expectedData.getValues(), actualData.getValues());
    }
}
