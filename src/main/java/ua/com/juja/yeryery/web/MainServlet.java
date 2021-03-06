package ua.com.juja.yeryery.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

    private DatabaseManager manager;
    private String tableName;
    private String definingColumn = "";
    private String definingValue = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

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
            req.setAttribute("items", service.getCommands());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/select")) {
            {
                String command = req.getParameter("command");

                if (command.equals("help")) {
                    req.getRequestDispatcher("help.jsp").forward(req, resp);

                } else {
                    req.setAttribute("command", command);
                    Set<String> tableNames = service.listTables(manager);
                    req.setAttribute("tables", tableNames);

                    if (command.equals("create")) {
                        req.getRequestDispatcher("createName.jsp").forward(req, resp);
                    } else {
                        req.getRequestDispatcher("select.jsp").forward(req, resp);
                    }
                }
            }

        } else if (action.startsWith("/display")) {
            findTable(req);
            req.getRequestDispatcher("display.jsp").forward(req, resp);

        } else if (action.startsWith("/insert")) {
            findTable(req);
            req.setAttribute("columns", service.getColumnNames(manager, tableName));
            req.getRequestDispatcher("insert.jsp").forward(req, resp);

        } else if (action.equals("/update")) {
            findTable(req);
            req.setAttribute("columns", service.getColumnNames(manager, tableName));
            req.getRequestDispatcher("updatePrepare.jsp").forward(req, resp);

        } else if (action.startsWith("/delete")) {
            findTable(req);
            req.setAttribute("columns", service.getColumnNames(manager, tableName));
            req.getRequestDispatcher("delete.jsp").forward(req, resp);

        } else if (action.startsWith("/drop")) {
            findTable(req);
            req.getRequestDispatcher("drop.jsp").forward(req, resp);

        } else if (action.startsWith("/clear")) {
            findTable(req);
            req.getRequestDispatcher("clear.jsp").forward(req, resp);

        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void findTable(HttpServletRequest req) {
        tableName = req.getParameter("tableName");
        req.setAttribute("tableName", tableName);
        req.setAttribute("tableRows", service.constructTable(manager, tableName));
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
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/update")) {

            if (action.equals("/updatePrepare")) {

                definingColumn = req.getParameter("column");
                definingValue = req.getParameter("value");
                req.setAttribute("definingColumn", definingColumn);
                req.setAttribute("definingValue", definingValue);
                req.setAttribute("tableColumns", service.getColumns(manager, tableName));
                req.getRequestDispatcher("updateValues.jsp").forward(req, resp);

            } else if (action.equals("/updateValues")) {

                Map updatedValues = req.getParameterMap();

                try {
                    service.update(manager, tableName, updatedValues, definingColumn, definingValue);
                    resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
                } catch (Exception e) {
                    req.setAttribute("message", e.getMessage());
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }

        } else if (action.startsWith("/delete")) {

            String column = req.getParameter("column");
            String value = req.getParameter("value");

            try {
                service.delete(manager, tableName, column, value);
                resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/drop")) {

            try {
                service.drop(manager, tableName);
                req.getRequestDispatcher("success.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/clear")) {

            try {
                service.clear(manager, tableName);
                req.getRequestDispatcher("success.jsp").forward(req, resp);
            } catch (Exception e) {
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
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
    }
}
