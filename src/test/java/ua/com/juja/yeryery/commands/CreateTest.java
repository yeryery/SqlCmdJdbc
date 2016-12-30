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
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
    }

    @Test
    public void testCreate() throws SQLException {
        //given
        String inputTableName = "newTable";
        String inputNameAndType = "someColumnName|text";
        when(view.read()).thenReturn(inputTableName).thenReturn(inputNameAndType);

        //when
        command.process("create");

        //then
        shouldPrint("[Enter the name of your table or 'cancel' to go back, " +
                //newTable
                "Enter the name of columns and its type for new table: \n" +
                "columnName1|columnType1|columnName2|columnType2|...\n" +
                "or type 'cancel' to go back, " +
                //someColumnName|text
                "Your table 'newTable' have successfully created!]");
    }

    @Test
    public void testCreateWithSqlException() throws SQLException {
        //given
        String inputTableName = "newTable";
        String inputNameAndType = "someColumnName|wrongType";
        when(view.read()).thenReturn(inputTableName).thenReturn(inputNameAndType);

        DataSet dataTypes = new DataSetImpl();
        doThrow(new SQLException()).when(manager).create(inputTableName, dataTypes);

        try {
            manager.create(inputTableName, dataTypes);
        } catch (SQLException e) {
            view.write("ERROR: type \"wrongtype\" does not exist\n" +
                    "  Position: 67");
        }

        //when
        command.process("create");

        //then
        verify(view).write("ERROR: type \"wrongtype\" does not exist\n" +
                    "  Position: 67");

    }

    @Test
    public void testCreateEnterTableNameAndCancel() {
        //given
        String inputTableName = "newTable";
        when(view.read()).thenReturn(inputTableName)
                         .thenReturn("cancel");

        //when
        try {
            command.process("create");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Enter the name of your table or 'cancel' to go back, " +
                //newTable
                "Enter the name of columns and its type for new table: \n" +
                "columnName1|columnType1|columnName2|columnType2|...\n" +
                "or type 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testCreateEnterTableNameStartsWithNumber() {
        //given
        String TableNameWithNumber = "1newTable";
        when(view.read()).thenReturn(TableNameWithNumber)
                         .thenReturn("cancel");

        //when
        try {
            command.process("create");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Enter the name of your table or 'cancel' to go back, " +
                //1newTable
                "Error! You have entered '1newTable' and tablename must begin with a letter\n" +
                "Try again, " +
                "Enter the name of your table or 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testCreateEnterExistsTableName() {
        //given
        String TableNameWithNumber = "test";
        when(view.read()).thenReturn(TableNameWithNumber)
                         .thenReturn("cancel");

        //when
        try {
            command.process("create");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Enter the name of your table or 'cancel' to go back, " +
                //1newTable
                "Error! Table with name 'test' already exists\n" +
                "[test, ttable]\n" +
                "Try again, " +
                "Enter the name of your table or 'cancel' to go back]");
                //cancel
    }

    @Test
    public void testCanProcessCreate() {
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

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
