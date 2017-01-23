package ua.com.juja.yeryery.controller.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Display.class)
public class DisplayTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private Command command;
    private static final String TABLE = "test";
    private static final String ACTION = "display";

    @Before
    public void setup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        command = new Display(view, manager);
    }

    @Test
    public void testDisplayTableData() throws Exception {
        //given
        mockMethods();
        when(manager.getDataContent(TABLE)).thenReturn(TestTable.getTableContent());

        //when
        command.process(ACTION);

        //then
        shouldPrint("[+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
    }

    @Test
    public void testDisplayEmptyTableData() throws Exception {
        //given
        mockMethods();
        when(manager.getDataContent(TABLE)).thenReturn(TestTable.getEmptyTable());

        //when
        command.process(ACTION);

        //then
        shouldPrint("[+--+----+\n" +
                "|id|name|\n" +
                "+--+----+]");
    }

    private void mockMethods() throws Exception {
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.selectTable(ACTION)).thenReturn(TABLE);
        when(manager.getTableColumns(TABLE)).thenReturn(TestTable.getTableColumns());
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
