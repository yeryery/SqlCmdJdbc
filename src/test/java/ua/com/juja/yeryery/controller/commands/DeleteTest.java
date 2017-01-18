package ua.com.juja.yeryery.controller.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.model.*;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DeleteTest {

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
        command = new Delete(view, manager);

        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";

        TestTable testTable = new TestTable();
        tableColumns = testTable.getTableColumns();
        tableContent = testTable.getTableContent();
    }

    @Test
    public void testDeleteByStringValue() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("delete");

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back, " +
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
        when(view.read()).thenReturn(selectedTable).thenReturn("id|1");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("delete");

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //id|1
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
    public void testDeleteThreeParameters() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike|something")
                .thenReturn("cancel");

        //when
        try {
            command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike|something
                "Error! Wrong number of parameters. Expected 2 parameters, and you have entered 3\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testDeleteNotExistingColumn() throws SQLException {
        //given
        String selectedTable = "test";

        when(view.read()).thenReturn(selectedTable).thenReturn("notExistingColumn|Mike")
                .thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);

        //when
        try {
            command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //notExistingColumn|Mike
                "Error! Table 'test' doesn't contain column 'notExistingColumn'\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testDeleteNotExistingValue() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|notExistingValue")
                .thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|notExistingValue
                "Error! Column 'name' doesn't contain value 'notExistingValue'\n" +
                "Try again, " +
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testDeleteSelectTableAndCancel() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("cancel");

        //when
        try {
            command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of the row you want to delete: columnName|value\n" +
                "or type 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testDeleteAndSelectNotExistingTable() throws SQLException {
        //given
        when(view.read()).thenReturn("notExistingTable").thenReturn("cancel");

        //when
        try {
            command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //notExistingTable
                "Error! Table with name 'notExistingTable' doesn't exist\n" +
                "Try again, " +
                "Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
    }

    @Test
    public void testDeleteAndSelectWrongTableNumber() throws SQLException {
        //given
        when(view.read()).thenReturn("22").thenReturn("cancel");

        //when
        try {
             command.process("delete");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //22
                "Error! There is no table with number 22\n" +
                "Try again, " +
                "Select the table you need for 'delete' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
    }

    @Test
    public void testCanProcessDelete() {
        //when
        boolean canProcess = command.canProcess("delete");

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
