package ua.com.juja.yeryery.controller.commands.Utility;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import ua.com.juja.yeryery.model.DataSet;

import java.util.List;
import java.util.Set;

public class TableConstructor {

    private Table table;
    private Set<String> columns;
    private List<DataSet> tableData;

    public TableConstructor(Set<String> columns, List<DataSet> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        table = new Table(columns.size(), BorderStyle.CLASSIC, ShownBorders.ALL);
    }

    public String getTableString() {
        build();
        return table.render();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column : columns) {
            table.addCell(column);
        }
    }

    private void buildRows() {
        for (DataSet row : tableData) {
            for (Object value : row.getValues()) {
                if (value != null) {
                    table.addCell(value.toString());
                } else {
                    table.addCell("");
                }
            }
        }
    }
}