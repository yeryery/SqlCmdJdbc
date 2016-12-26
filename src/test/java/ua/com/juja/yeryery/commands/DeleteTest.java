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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

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

        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
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
        when(view.read()).thenReturn(selectedTable).thenReturn("id|1");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

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
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //name|Mike|something
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
        String selectedTable = "test";

        when(view.read()).thenReturn(selectedTable).thenReturn("notExistingColumn|Mike")
                .thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);

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
                //notExistingColumn|Mike
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
        when(view.read()).thenReturn(selectedTable).thenReturn("name|notExistingValue")
                .thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

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
                //name|notExistingValue
                "Column 'name' doesn't contain value 'notExistingValue'!, " +
                "Try again., " +
                "Enter columnName and defining value of deleted row: columnName|value\n" +
                "or type 'cancel' to go back., " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteSelectTableAndCancel() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("cancel");

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
    public void testDeleteAndSelectNotExistingTable() throws SQLException {
        //given
        when(view.read()).thenReturn("notExistingTable").thenReturn("cancel");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                "Table with name 'notExistingTable' doesn't exist!, " +
                //notExistingTable
                "Try again., " +
                "Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteAndSelectWrongTableNumber() throws SQLException {
        //given
        when(view.read()).thenReturn("22").thenReturn("cancel");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                "There is no table with number 22!, " +
                //22
                "Try again., " +
                "Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Row removal canceled]");
    }

    @Test
    public void testDeleteAndSelectCancel() throws SQLException {
        //given
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
    public void testDeleteAndSelectZero() throws SQLException {
        //given
        when(view.read()).thenReturn("0");

        //when
        command.process("delete");

        //then
        shouldPrint("[Enter the name or select number of table where you want to delete rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //0
                "Row removal canceled]");
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
