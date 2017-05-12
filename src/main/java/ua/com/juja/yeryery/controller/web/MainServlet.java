package ua.com.juja.yeryery.controller.web;

import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.service.Service;
import ua.com.juja.yeryery.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class MainServlet extends HttpServlet {

    private Service service;
    private DatabaseManager manager;
    private String tableName;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        String command = "";

        manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            }
            return;
        }

        if (manager == null) {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return;
        }

        if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/select")) {
            command = req.getParameter("command");
            req.setAttribute("command", command);
            req.setAttribute("tables", service.listTables(manager));

            if (command.equals("create")) {
                req.getRequestDispatcher("createName.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("select.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/display")) {
            tableName = req.getParameter("tableName");
            req.setAttribute("tableName", tableName);
            req.setAttribute("tableRows", service.constructTable(manager, tableName));
            req.getRequestDispatcher("display.jsp").forward(req, resp);

        } else if (action.startsWith("/insert")) {
            tableName = req.getParameter("tableName");
            req.setAttribute("tableName", tableName);
            req.setAttribute("tableRows", service.constructTable(manager, tableName));
            req.setAttribute("columns", service.getColumnNames(manager, tableName));
            req.getRequestDispatcher("insert.jsp").forward(req, resp);

        } else if (action.startsWith("/delete")) {
            tableName = req.getParameter("tableName");
            req.setAttribute("tableName", tableName);
            req.setAttribute("tableRows", service.constructTable(manager, tableName));
            req.setAttribute("columns", service.getColumnNames(manager, tableName));
            req.getRequestDispatcher("delete.jsp").forward(req, resp);

        } else if (action.startsWith("/drop") || action.startsWith("/clear")) {
            tableName = req.getParameter("tableName");
            req.setAttribute("tableName", tableName);
            req.setAttribute("command", command);
            req.getRequestDispatcher("confirm.jsp").forward(req, resp);

        } else if (action.startsWith("/confirm")) {
            req.setAttribute("tableName", tableName);
            req.setAttribute("command", command);

            if (command.equals("drop")) {
                service.drop(manager, tableName);
            } else if (command.equals("clear")) {
                service.clear(manager, tableName);
            }

            req.getRequestDispatcher("success.jsp").forward(req, resp);

        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                DatabaseManager manager = service.connect(databaseName, userName, password);
                req.getSession().setAttribute("db_manager", manager);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/insert")) {

            Map parameters = req.getParameterMap();

            try {
                service.insert(manager, tableName, parameters);
                resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
            } catch (SQLException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/delete")) {

            String column = req.getParameter("column");
            String value = req.getParameter("value");

            try {
                service.delete(manager, tableName, column, value);
                resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
            } catch (SQLException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/createName")) {

            tableName = req.getParameter("tableName");
            String size = req.getParameter("size");
            req.setAttribute("tableName", tableName);
            req.setAttribute("size", size);
            req.getRequestDispatcher("createColumns.jsp").forward(req, resp);

        } else if (action.startsWith("/createColumns")) {

            String primaryKeyName = req.getParameter("keyName");
            String primaryKeyType = req.getParameter("keyType");
            String[] columnNames = req.getParameterValues("columnName");
            String[] columnTypes = req.getParameterValues("columnType");

            try {
                service.create(manager, tableName, columnNames, columnTypes, primaryKeyName, primaryKeyType);
                resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
            } catch (SQLException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
    }
}
