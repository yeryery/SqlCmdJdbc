package ua.com.juja.yeryery.commands.dialogs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.commands.CancelException;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SelectTableTest {

    private DatabaseManager manager;
    private View view;
    private Dialog dialog;
    private static final String MESSAGE = "Please enter the name or select number of table you want to display";

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        dialog = new DialogImpl(view, manager);
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
    }

    @Test
    public void testSelectTableByNumber() {
        //given
        when(view.read()).thenReturn("1");

        //when
        String actual = dialog.selectTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
        //1
        assertEquals("test", actual);
    }

    @Test
    public void testSelectTableByName() {
        //given
        when(view.read()).thenReturn("test");

        //when
        String actual = dialog.selectTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
        //test
        assertEquals("test", actual);
    }

    @Test
    public void testSelectCancel() {
        //given
        when(view.read()).thenReturn("cancel");

        //when
        try {
            dialog.selectTable(MESSAGE);
            fail();
        } catch (CancelException e) {
            assertEquals("ua.com.juja.yeryery.commands.CancelException", e.toString());
        }

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
    }

    @Test
    public void testSelectZero() {
        //given
        when(view.read()).thenReturn("0");

        //when
        try {
            dialog.selectTable(MESSAGE);
            fail();
        } catch (CancelException e) {
            assertEquals("ua.com.juja.yeryery.commands.CancelException", e.toString());
        }

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
    }

    @Test
    public void testSelectNotExistTable() {
        //given
        when(view.read()).thenReturn("notExist").thenReturn("test");

        //when
        String actual = dialog.selectTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //notExist
                "Table with name 'notExist' doesn't exist!, " +
                "Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
        //test
        assertEquals("test", actual);
    }

    @Test
    public void testSelectOutOfBoundsTableNumber() {
        //given
        when(view.read()).thenReturn("3").thenReturn("1");

        //when
        String actual = dialog.selectTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //notExist
                "There is no table with this number!, " +
                "Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
        //1
        assertEquals("test", actual);
    }


    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
