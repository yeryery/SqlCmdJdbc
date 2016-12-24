package ua.com.juja.yeryery.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.*;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

public class DeleteTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    private final String tableName = "test";

    private final String column1 = "id";
    private final int value11 = 1;
    private final int value12 = 2;
    private final String column2 = "name";
    private final String value21 = "John";
    private final String value22 = "Mike";

    private Set<String> tableColumns;

    private DataSet dataSet1;
    private DataSet dataSet2;
    private List<DataSet> tableContent;

    @Before
    public void setup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Delete(view, manager);

        tableColumns = new LinkedHashSet<>();
        tableColumns.add(column1);
        tableColumns.add(column2);

        dataSet1 = new DataSetImpl();
        dataSet1.put(column1, value11);
        dataSet1.put(column2, value21);

        dataSet2 = new DataSetImpl();
        dataSet2.put(column1, value12);
        dataSet2.put(column2, value22);

        tableContent = new LinkedList<DataSet>();
        tableContent.add(dataSet1);
        tableContent.add(dataSet2);
    }

    @Test
    public void testDeleteByStringValue() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column2;
        Object definingValue = value22;
        DataEntry definingEntry = new DataEntryImpl(columnName, definingValue);

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue);
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);
        doNothing().when(manager).delete(tableName, definingEntry);

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //name|Mike
                "You have successfully deleted data from 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testDeleteByIntValue() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column1;
        Object definingValue = value12;
        DataEntry definingEntry = new DataEntryImpl(columnName, definingValue);

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue);
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);
        doNothing().when(manager).delete(tableName, definingEntry);

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //id|2
                "You have successfully deleted data from 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testDeleteAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        when(view.read()).thenReturn("cancel");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteSelectTableAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        when(view.read()).thenReturn(tableName).thenReturn("cancel");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteThreeParameters() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column1;
        Object definingValue = value12;
        String excessParameter = "something";

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue + "|" + excessParameter)
                            .thenReturn("cancel");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //id|2|something
                "Wrong number of parameters. Expected 2, and you have entered 3!, " +
                "Try again., " +
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteNotExistingColumn() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = "notExistingColumn";
        Object definingValue = value12;

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue)
                            .thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //id|2|something
                "Table 'test' doesn't contain column 'notExistingColumn'!, " +
                "Try again., " +
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteNotExistingValue() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName = column1;
        Object definingValue = "notExistingValue";

        when(view.read()).thenReturn(tableName).thenReturn(columnName + "|" + definingValue)
                            .thenReturn("cancel");
        when(manager.getTableColumns(tableName)).thenReturn(tableColumns);
        when(manager.getDataContent(tableName)).thenReturn(tableContent);

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //id|2|something
                "Column 'id' doesn't contain value 'notExistingValue'!, " +
                "Try again., " +
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Row removal canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
