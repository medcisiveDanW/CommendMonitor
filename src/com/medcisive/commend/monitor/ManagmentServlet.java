/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medcisive.commend.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.medcisive.utility.LogUtility;
import com.medcisive.utility.PropertiesUtility;
import com.medcisive.utility.Util;
import com.medcisive.utility.sql2.SQLTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author vhapalchambj
 */
public class ManagmentServlet extends HttpServlet {
    private static Gson _gson = new Gson();
    private static ManagmentDBC _dbc = null;
    private static Object _result = null;
    private static java.sql.Timestamp _today = new java.sql.Timestamp(System.currentTimeMillis());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(true);
        _result = null;
        String option = (String) request.getParameter("option");
        if(_dbc==null) {
            System.out.println("Creating new DBC!!!!");
            _dbc = new ManagmentDBC();
        }
        if (option.equalsIgnoreCase("getToday")) {
            _result = _getToday();
        } else if (option.equalsIgnoreCase("getExternalProviders")) {
            _result = _getExternalProviders(request);
        } else if (option.equalsIgnoreCase("getInternalProviders")) {
            _result = _getInternalProviders(request);
        } else if (option.equalsIgnoreCase("getExternalClinics")) {
            _result = _getExternalClinics(request);
        } else if (option.equalsIgnoreCase("getInternalClinics")) {
            _result = _getInternalClinics(request);
        } else if (option.equalsIgnoreCase("addProvider")) {
            _addProvider(request);
        } else if (option.equalsIgnoreCase("addClinic")) {
            _addClinic(request);
        } else if (option.equalsIgnoreCase("removeProvider")) {
            _removeProvider(request);
        } else if (option.equalsIgnoreCase("removeClinic")) {
            _removeClinic(request);
        } else if (option.equalsIgnoreCase("getProperties")) {
            _result = _getProperties();
        } else if (option.equalsIgnoreCase("setProperties")) {
            _setProperties(request);
        } else if (option.equalsIgnoreCase("addCosigner")) {
            _addCosigner(request);
        }



        response.getWriter().write(_gson.toJson(_result));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String _getToday() {
        return _today.toString();
    }

    private SQLTable _getExternalProviders(HttpServletRequest request) {
        String providerName = (String) request.getParameter("ProviderName");
        String sta3n = (String) request.getParameter("Sta3n");
        return _dbc.getExternalProviders(providerName,sta3n);
    }

    private SQLTable _getInternalProviders(HttpServletRequest request) {
        String providerName = (String) request.getParameter("ProviderName");
        String sta3n = (String) request.getParameter("Sta3n");
        return _dbc.getInternalProviders(providerName,sta3n);
    }

    private SQLTable _getExternalClinics(HttpServletRequest request) {
        String clinicName = (String) request.getParameter("ClinicName");
        String sta3n = (String) request.getParameter("Sta3n");
        return _dbc.getExternalClinics(clinicName,sta3n);
    }

    private SQLTable _getInternalClinics(HttpServletRequest request) {
        String providerSID = (String) request.getParameter("ProviderSID");
        String sta3n = (String) request.getParameter("Sta3n");
        return _dbc.getInternalClinics(Integer.parseInt(providerSID),sta3n);
    }

    private void _addProvider(HttpServletRequest request) {
        String Sta3n = (String) request.getParameter("Sta3n");
        String ProviderIEN = (String) request.getParameter("ProviderIEN");
        String ProviderSID = (String) request.getParameter("ProviderSID");
        String ProviderName = (String) request.getParameter("ProviderName");
        int ien = Integer.parseInt(ProviderIEN);
        int sid = Integer.parseInt(ProviderSID);
        _dbc.addProvider(Sta3n, ien, sid, ProviderName);
    }

    private void _addClinic(HttpServletRequest request) {
        String Sta3n = (String) request.getParameter("Sta3n");
        String ProviderIEN = (String) request.getParameter("ProviderIEN");
        String ProviderSID = (String) request.getParameter("ProviderSID");
        String LocationIEN = (String) request.getParameter("LocationIEN");
        String LocationSID = (String) request.getParameter("LocationSID");
        String LocationName = (String) request.getParameter("LocationName");
        int pien = Integer.parseInt(ProviderIEN);
        int psid = Integer.parseInt(ProviderSID);
        int lien = Integer.parseInt(LocationIEN);
        int lsid = Integer.parseInt(LocationSID);
        _dbc.addClinic(Sta3n, pien, psid, lien, lsid, LocationName);
    }

    private void _addCosigner(HttpServletRequest request) {
        String Sta3n = (String) request.getParameter("Sta3n");
        String ProviderSID = (String) request.getParameter("ProviderSID");
        String CosignerIEN = (String) request.getParameter("CosignerIEN");
        String CosignerSID = (String) request.getParameter("CosignerSID");
        String CosignerName = (String) request.getParameter("CosignerName");
        int psid = Integer.parseInt(ProviderSID);
        int lien = Integer.parseInt(CosignerIEN);
        int lsid = Integer.parseInt(CosignerSID);
        _dbc.addCosigner(Sta3n, psid, lsid, lien, CosignerName);
    }

    private void _removeProvider(HttpServletRequest request) {
        String Sta3n = (String) request.getParameter("Sta3n");
        String ProviderSID = (String) request.getParameter("ProviderSID");
        _dbc.removeProvider(Sta3n, Integer.parseInt(ProviderSID));
    }

    private void _removeClinic(HttpServletRequest request) {
        String Sta3n = (String) request.getParameter("Sta3n");
        String ProviderSID = (String) request.getParameter("ProviderSID");
        String LocationSID = (String) request.getParameter("LocationSID");
        _dbc.removeClinic(Sta3n, Integer.parseInt(ProviderSID), Integer.parseInt(LocationSID));
    }

    private Object _getProperties() throws IOException {
        try {
            return PropertiesUtility.get("FRAMEWORK");
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    private void _setProperties(HttpServletRequest request) {
        java.util.Map<String,String[]> map = request.getParameterMap();


//        for(String key : map.keySet()) {
//            System.out.println(key + ":");
//            for(String s : map.get(key)) {
//                System.out.println("    " + s);
//            }
//        }
    }
}
