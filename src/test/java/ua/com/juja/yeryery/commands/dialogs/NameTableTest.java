package ua.com.juja.yeryery.commands.dialogs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameTableTest {

    private DatabaseManager manager;
    private View view;
    private Dialog dialog;
    private static final String ACTION = "create";
    private static final String MESSAGE = String.format("Please enter the name of table you want to %s or 'cancel' to go back", ACTION);

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        dialog = new DialogImpl(view, manager);
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
    }

    @Test
    public void testNameTableWithOriginalName() {
        //given
        when(view.read()).thenReturn("myTable");

        //when
        String actual = dialog.nameTable(MESSAGE);

        //then
        verify(view).write("Please enter the name of table you want to create or 'cancel' to go back");
        //myTable
        assertEquals("myTable", actual);
    }

    @Test
    public void testNameTableWithExistingName() {
        //given
        when(view.read()).thenReturn("test").thenReturn("correctName");

        //when
        dialog.nameTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //test
                "Table with name 'test' already exists!\n" +
                "[test, ttable], " +
                "Try again., " +
                "Please enter the name of table you want to create or 'cancel' to go back]");
    }

    @Test
    public void testNameTableWhenNameStartsWithNumber() {
        //given
        when(view.read()).thenReturn("1name").thenReturn("correctName");

        //when
            dialog.nameTable(MESSAGE);

        //then
        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //1name
                "You have entered '1name' and table name must begin with a letter!, " +
                "Try again., " +
                "Please enter the name of table you want to create or 'cancel' to go back]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
