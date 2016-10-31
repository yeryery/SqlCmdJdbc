package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Create(view, manager);
    }

    @Test
    public void testCreate() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        String columnName1 = "someColumnName";
        String dataType1 = "notDataType";

        when(view.read()).thenReturn("newTable")
                .thenReturn("1")
                .thenReturn(columnName1)
                .thenReturn(dataType1);

        DataSet dataTypes = new DataSetImpl();
        dataTypes.put(columnName1, dataType1);

        doNothing().when(manager).create("newTable", dataTypes);

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //newTable
                "Please enter the number of columns of your table or '0' to go back, " +
                //1
                "name of column 1, " +
                //someName
                "dataType of column 1, " +
                //text
                "Your table 'newTable' have successfully created!]");
    }

    @Test
    public void testCreateWithSqlException() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        String columnName1 = "someColumnName";
        String dataType1 = "wrongType";

        when(view.read()).thenReturn("newTable")
                .thenReturn("1")
                .thenReturn(columnName1)
                .thenReturn(dataType1);

        DataSet dataTypes = new DataSetImpl();
        dataTypes.put(columnName1, dataType1);

        doThrow(new SQLException()).when(manager).create("newTable", dataTypes);

        try {
            manager.create("newTable", dataTypes);
        } catch (SQLException e) {
            view.write("SQL ERROR: type \"wrongType\" does not exist\n" +
                    "  Position: 67");
        }

        //when
        command.process("create");

        //then
        verify(view).write("SQL ERROR: type \"wrongType\" does not exist\n" +
                "  Position: 67");

    }

    @Test
    public void testCreateAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    "Table creating canceled]");
    }

    @Test
    public void testCreateAndEnterNegativeNumberOfColumns() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("-1")
                         .thenReturn("0");

        //when
        command.process("create");

        //then
        verify(view).write("Number must be positive! Try again (or type '0' to go back).");
    }

    @Test
    public void testCreateAndNotNumberOfColumns() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("notNumber")
                         .thenReturn("0");

        //when
        command.process("create");

        //then
        verify(view).write("Please enter the name of table you want to create or 'cancel' to go back");
    }

    @Test
    public void testCreateEnterTableNameAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("0");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    //newTable
                    "Please enter the number of columns of your table or '0' to go back, " +
                    //0
                    "The creating of table 'newTable' is cancelled]");
    }

    @Test
    public void testCreateEnterTableNameNotNumberOfColumnsAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("notNumber")
                         .thenReturn("0");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //newTable
                "Please enter the number of columns of your table or '0' to go back, " +
                //notNumber
                "This is not number! Try again (or type '0' to go back)., " +
                "Please enter the number of columns of your table or '0' to go back, " +
                //0
                "The creating of table 'newTable' is cancelled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessList() {
        //when
        boolean canProcess = command.canProcess("create");

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
