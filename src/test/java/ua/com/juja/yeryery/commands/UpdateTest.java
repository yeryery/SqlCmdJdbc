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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateTest {

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
        command = new Update(view, manager);

        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";

        TestTable testTable = new TestTable();
        tableColumns = testTable.getTableColumns();
        tableContent = testTable.getTableContent();
    }

    @Test
    public void testUpdateStringValues() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("id|1").thenReturn("name|Bob");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //id|2
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //name|Mike
                "You have successfully updated table 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testUpdateIntValue() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").thenReturn("id|5");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("update");

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //id|5
                "You have successfully updated table 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testUpdateSelectTableAndCancel() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("cancel");

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateAndInputNotExistingValueString() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|notExistingValue").
                thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|notExistingValue
                "Error! Column 'name' doesn't contain value 'notExistingValue'\n" +
                "Try again, " +
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateAndInputNotExistingValueInt() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("id|22").
                thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //id|22
                "Error! Column 'id' doesn't contain value '22'\n" +
                "Try again, " +
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateDefineUpdatedRowAndCancel() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").
                thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateDefineUpdatedRowAndInputThreeParameters() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").
                thenReturn("id|22|thirdParameter").thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike " +
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //id|22|thirdParameter
                "Error! Wrong number of parameters. Expected even number of parameters (2, 4 and so on) and you have entered 3\n" +
                "Try again, " +
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateInputNotExistingUpdatedColumn() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").
                thenReturn("notExistingColumn|2").thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //notExistingColumn|2
                "Error! Table 'test' doesn't contain column 'notExistingColumn'\n" +
                "Try again, " +
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateNewValueEqualsToUpdated() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").
                thenReturn("id|2").thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        try {
            command.process("update");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Please, enter the name or select number of table you want to update, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnName and defining value of updated row: columnName|value\n" +
                "or type 'cancel' to go back, " +
                //name|Mike
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //id|2
                "Error! Your entries are equivalent to the updated\n" +
                "Try again, " +
                "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testUpdateSQLException() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("name|Mike").
                thenReturn("id|notNumber").thenReturn("cancel");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        DataSet newValues = new DataSetImpl();
        newValues.put("id", "notNumber");
        DataEntry definingEntry = new DataEntryImpl("name", "Mike");

        doThrow(new SQLException()).when(manager).update(selectedTable, newValues, definingEntry);

        try {
            manager.update(selectedTable, newValues, definingEntry);
        } catch (SQLException e) {
            view.write("SQL ERROR: column \"id\" is of type integer but expression is of type character varying!\n" +
                    "Try again.");
        }

        //when
        command.process("update");

        //then
        verify(view).write("SQL ERROR: column \"id\" is of type integer but expression is of type character varying!\n" +
                "Try again.");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessUpdate() {
        //when
        boolean canProcess = command.canProcess("update");

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
}
