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
        String action = getAction(req);

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
            String command = req.getParameter("command");
            req.setAttribute("command", command);
            req.setAttribute("tables", service.listTables(manager));
            req.getRequestDispatcher("select.jsp").forward(req, resp);

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

        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

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
        }

        if (action.startsWith("/insert")) {

            Map parameters = req.getParameterMap();
            try {
                service.insert(manager, tableName, parameters);
                resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
            } catch (SQLException e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }

        if (action.startsWith("/delete")) {

            String column = req.getParameter("column");
            String value = req.getParameter("value");
            service.delete(manager, tableName, column, value);
            resp.sendRedirect(resp.encodeRedirectURL("display?tableName=" + tableName));
        }
    }
}
