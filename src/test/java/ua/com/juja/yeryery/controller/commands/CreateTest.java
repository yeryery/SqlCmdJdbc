package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.controller.commands.Util.CancelException;
import ua.com.juja.yeryery.model.DatabaseManager;
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
        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList("test", "ttable"));
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
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //newTable
                "Enter the columnNames and its dataTypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back, " +
                //someColumnName|text
                "Your table 'newtable' have successfully created!]");
    }

    @Test
    public void testCreateInputOddNumberOfParameters() {
        //given
        String inputTableName = "newTable";
        String inputNameAndType = "someColumnName|text|thirdParameter";
        when(view.read()).thenReturn(inputTableName).thenReturn(inputNameAndType).thenReturn("cancel");

        //when
        try {
            command.process("create");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //newTable
                "Enter the columnNames and its dataTypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back, " +
                //someColumnName|text|thirdParameter
                "Error! Wrong number of parameters. Expected even number of parameters (2, 4 and so on), and you have entered 3\n" +
                "Try again, Enter the columnNames and its dataTypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
                "or type 'cancel' to go back]");
                //cancel

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
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //newTable
                "Enter the columnNames and its dataTypes of the table you want to create:\n" +
                "columnName1|dataType1|columnName2|dataType2|...\n" +
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
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //1newTable
                "Error! You have entered '1newtable' and name must begin with a letter\n" +
                "Try again, " +
                "Enter the name of your table or type 'cancel' to go back]");
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
        shouldPrint("[Enter the name of your table or type 'cancel' to go back, " +
                //1newTable
                "Error! Table with name 'test' already exists\n" +
                "[test, ttable]\n" +
                "Try again, " +
                "Enter the name of your table or type 'cancel' to go back]");
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
