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
        String inputNameType = "someColumnName|text";

        when(view.read()).thenReturn("newTable").thenReturn(inputNameType);

        DataSet dataTypes = new DataSetImpl();

        doNothing().when(manager).create("newTable", dataTypes);

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //newTable
                "Enter name of columns and its type for new table: \n" +
                "columnName1|columnType1|columnName2|columnType2|...\n" +
                "or type 'cancel' to go back., " +
                //someColumnName|text
                "Your table 'newTable' have successfully created!]");
    }

    @Test
    public void testCreateWithSqlException() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        String inputNameType = "someColumnName|WrongType";

        when(view.read()).thenReturn("newTable").thenReturn(inputNameType);

        DataSet dataTypes = new DataSetImpl();

        doThrow(new SQLException()).when(manager).create("newTable", dataTypes);

        try {
            manager.create("newTable", dataTypes);
        } catch (SQLException e) {
            view.write("SQL ERROR: type \"wrongType\" does not exist!\n" +
                    "Try again.");
        }

        //when
        command.process("create");

        //then
        verify(view).write("SQL ERROR: type \"wrongType\" does not exist!\n" +
                "Try again.");

    }

    @Test
    public void testCreateAndCancel() throws SQLException {
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
    public void testCreateEnterTableNameAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("cancel");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    //newTable
                    "Enter name of columns and its type for new table: \n" +
                    "columnName1|columnType1|columnName2|columnType2|...\n" +
                    "or type 'cancel' to go back., " +
                    //0
                    "Table creating canceled]");
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
