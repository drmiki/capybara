package ru.drmiki.help_for_capy.controllers;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MyAppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        java.sql.Driver mySqlDriver = null;
        try {
            mySqlDriver = DriverManager.getDriver("jdbc:mysql://localhost:3306/");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            DriverManager.deregisterDriver(mySqlDriver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}