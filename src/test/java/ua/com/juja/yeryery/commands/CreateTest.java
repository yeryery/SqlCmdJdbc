package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

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
    public void testCreate() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                .thenReturn("1")
                .thenReturn("someName")
                .thenReturn("text");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //newTable
                "Please enter the number of columns of your table or '0' to go back, " +
                //1
                "name of column 1, " +
                //someName
                "datatype of column 1, " +
                //text
                "Your table 'newTable' have successfully created!]");
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
    public void testCreateAndNegativeNumberOfColumns() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("-1")
                         .thenReturn("1")
                         .thenReturn("someName")
                         .thenReturn("text");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    //newTable
                    "Please enter the number of columns of your table or '0' to go back, " +
                    //-1
                    "Number must be positive! Try again (or type '0' to go back)., " +
                    "Please enter the number of columns of your table or '0' to go back, " +
                    //1
                    "name of column 1, " +
                    //someName
                    "datatype of column 1, " +
                    //text
                    "Your table 'newTable' have successfully created!]");
    }

    @Test
    public void testCreateAndNotNumberOfColumns() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("notNumber")
                         .thenReturn("1")
                         .thenReturn("someName")
                         .thenReturn("text");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    //newTable
                    "Please enter the number of columns of your table or '0' to go back, " +
                    //-1
                    "This is not number! Try again (or type '0' to go back)., " +
                    "Please enter the number of columns of your table or '0' to go back, " +
                    //1
                    "name of column 1, " +
                    //someName
                    "datatype of column 1, " +
                    //text
                    "Your table 'newTable' have successfully created!]");
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
    public void testCreateEnterTableNameNegativeNumberAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("newTable")
                         .thenReturn("-1")
                         .thenReturn("0");

        //when
        command.process("create");

        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //newTable
                "Please enter the number of columns of your table or '0' to go back, " +
                //-1
                "Number must be positive! Try again (or type '0' to go back)., " +
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
