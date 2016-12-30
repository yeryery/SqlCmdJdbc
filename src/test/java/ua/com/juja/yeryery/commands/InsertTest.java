package ua.com.juja.yeryery.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InsertTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private Set<String> tableColumns;
    private List<DataSet> tableContent;
    private String selectedTable;

    @Before
    public void setup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Insert(view, manager);

        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";

        TestTable testTable = new TestTable();
        tableColumns = testTable.getTableColumns();
        tableContent = testTable.getTableContent();
    }

    @Test
    public void testInsert() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("5").thenReturn("Bob");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("insert");

        //then
        shouldPrint("[Please, enter the name or select number of table where you want to insert new row, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                "Enter new values you require, " +
                "id, " +
                //5
                "name, " +
                //Bob
                "You have successfully entered new data into the table 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testInsertWithSqlException() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("notNumber").thenReturn("Bob");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);

        DataSet input = new DataSetImpl();
        input.put("id", "notNumber");
        input.put("name", "Bob");

        doThrow(new SQLException()).when(manager).insert(selectedTable, input);

        try {
            manager.insert(selectedTable, input);
        } catch (SQLException e) {
            view.write("SQL ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                    "  Position: 41");
        }

        //when
        command.process("insert");

        //then
        verify(view).write("SQL ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 41");
    }

    @Test
    public void testCanProcessInsert() {
        //when
        boolean canProcess = command.canProcess("insert");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWrongInput() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
