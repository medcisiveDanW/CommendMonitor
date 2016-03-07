package com.medcisive.commend.monitor;

import com.medcisive.utility.sql2.*;

/**
 *
 * @author vhapalchambj
 */
public class ManagmentDBC extends com.medcisive.utility.sql2.DBCUtil {

    public ManagmentDBC() {}

    public SQLTable getExternalProviders(String providerName, String sta3n) {
        String query =
                "SELECT Sta3n \n"
                + "     ,StaffIEN as ProviderIEN \n"
                + "     ,StaffSID as ProviderSID \n"
                + "     ,StaffName as ProviderName \n"
                + "FROM VDWWork.SStaff.SStaff \n"
                + "WHERE StaffName like " + DBC.fixString("%" + providerName + "%") + " \n"
                + "  AND Sta3n = " + DBC.fixString(sta3n);
        return _src.getTable(query);
    }

    public SQLTable getInternalProviders(String providerName, String sta3n) {
        if (providerName == null) {
            providerName = "";
        }
        if (sta3n == null) {
            sta3n = "";
        }
        String query =
                "SELECT * \n"
                + "FROM dbo.CommendProviders \n"
                + "WHERE name like '%" + providerName + "%' \n"
                + "  AND sta3n like '%" + sta3n + "%'";
        return _dest.getTable(query);
    }

    public SQLTable getExternalClinics(String clinicName, String sta3n) {
        if (clinicName == null) {
            return null;
        }
        String query =
                "SELECT \n"
                + "     Sta3n \n"
                + "     ,LocationIEN \n"
                + "     ,LocationSID \n"
                + "     ,LocationName \n"
                + "FROM VDWWork.Dim.Location \n"
                + "WHERE LocationName like '%" + clinicName + "%' \n"
                + "  AND Sta3n = " + DBC.fixString(sta3n);
        return _src.getTable(query);
    }

    public SQLTable getInternalClinics(int providerSid, String sta3n) {
        String query =
                "SELECT * \n"
                + "FROM dbo.CommendProviderClinics \n"
                + "WHERE SID = " + providerSid + " \n"
                + "  AND sta3n = " + DBC.fixString(sta3n);
        return _dest.getTable(query);
    }

    public void addProvider(String sta3n, int providerIEN, int providerSID, String providerName) {
        String query =
                "SELECT * FROM dbo.CommendProviders \n"
                + "WHERE sta3n = " + DBC.fixString(sta3n) + " \n"
                + "  AND SID = " + providerSID;
        SQLTable table = _dest.getTable(query);
        if (table != null && table.size() > 0) {
            return;
        }
        query =
                "INSERT INTO dbo.CommendProviders (sta3n,SID,DUZ,name,role) \n"
                + "VALUES (" + DBC.fixString(sta3n) + "," + providerSID + "," + providerIEN + "," + DBC.fixString(providerName) + ",'provider')";
        _dest.update(query);
    }

    public void addClinic(String sta3n, int providerIEN, int providerSID, int locationIEN, int locationSID, String locationName) {
        String query =
                "SELECT * FROM dbo.CommendProviderClinics \n"
                + "WHERE Sta3n = " + DBC.fixString(sta3n) + " \n"
                + "  AND SID = " + providerSID + " \n"
                + "  AND LocationSID = " + locationSID;
        SQLTable table = _dest.getTable(query);
        if (table != null && table.size() > 0) {
            return;
        }
        //sta3n,SID,DUZ,TrtLocIEN,clinicLocName,disable,LocationSID
        query =
                "INSERT INTO dbo.CommendProviderClinics (sta3n,SID,DUZ,TrtLocIEN,clinicLocName,disable,LocationSID) \n"
                + "VALUES (" + DBC.fixString(sta3n) + ","
                + providerSID + ","
                + providerIEN + ","
                + locationIEN + ","
                + DBC.fixString(locationName) + ","
                + "'N',"
                + locationSID + ")";
        _dest.update(query);
    }

    public void addCosigner(String sta3n, int providerSID, int cosignerSID, int cosignerDUZ, String cosignerName) {
        String update =
                "UPDATE Commend.dbo.CommendProviders \n"
                + "SET cosignerSID = " + cosignerSID + ", cosignerDUZ = " + cosignerDUZ + ", cosignerName = " + DBC.fixString(cosignerName) + " \n"
                + "WHERE SID = " + providerSID + " \n"
                + "  AND sta3n = " + DBC.fixString(sta3n) + " \n";
        _dest.update(update);
    }

    public void removeProvider(String sta3n, int providerSID) {
        String query =
                "DELETE FROM dbo.CommendProviders \n"
                + "WHERE sta3n = " + DBC.fixString(sta3n) + " \n"
                + "  AND SID = " + providerSID;
        _dest.update(query);
        query =
                "DELETE FROM dbo.CommendProviderClinics \n"
                + "WHERE sta3n = " + DBC.fixString(sta3n) + " \n"
                + "  AND SID = " + providerSID;
        _dest.update(query);
    }

    public void removeClinic(String sta3n, int providerSID, int locationSID) {
        String query =
                "DELETE FROM dbo.CommendProviderClinics \n"
                + "WHERE sta3n = " + DBC.fixString(sta3n) + " \n"
                + "  AND SID = " + providerSID + " \n"
                + "  AND LocationSID = " + locationSID;
        _dest.update(query);
    }
}
