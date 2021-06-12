/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Muhammad Fajar
 */
public class TabelLevel extends DB{
    public TabelLevel() throws Exception, SQLException{
        super();
    }
    
    public void getTLevel(){
        try {
            String query = "SELECT * from tlevel";
            createQuery(query);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void getTLevelByUsername(String username) {
        // System.out.println("get by username");
        try {
            String query = "SELECT * from tlevel WHERE username = '" + username + "'";
            // System.out.println(query);
            createQuery(query);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public void insertData(String username, int success, int fail){
        // Check for update
        int update = isUpdate(username, success, fail);
        // Untuk insert
        if(update == 0){
            // System.out.println("insert");
            try {
                String query = "INSERT INTO tlevel VALUES(NULL, '" + username + "', " + Integer.toString(success) + ", " + Integer.toString(fail) + ")";
                // System.out.println(query);
                createUpdate(query);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        // Untuk update
        else if(update == 1){
            try {
                String query = "UPDATE tlevel SET success = " + success + " WHERE username = '" + username + "'";
                createUpdate(query);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        else if(update == 2){
            try {
                String query = "UPDATE tlevel SET fail = " + fail + " WHERE username = '" + username + "'";
                createUpdate(query);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        else if(update == 3){
            try {
                String query = "UPDATE tlevel SET success = " + success + ", fail = " + fail + " WHERE username = '" + username + "'";
                createUpdate(query);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
    // Check for update function
    public int isUpdate(String username, int success, int fail){
        // System.out.println("is update");
        int result = -1;
        try {
            int c = 0;
            int ts = 0;
            int tf = 0;
            TabelLevel temp = new TabelLevel();
            temp.getTLevelByUsername(username);
            while(temp.getResult().next()){
                ts = Integer.parseInt(temp.getResult().getString(3));
                tf = Integer.parseInt(temp.getResult().getString(4));
                c++;
            }
            if(c == 0){
                result = 0;
            }
            else{
                if(success > ts){
                    result = 1;
                }
                if(fail > tf){
                    result = 2;
                }
                if(success > ts && fail > tf){
                    result = 3;
                }
            }
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return result;
    }
    
    // Set up datatable function
    public DefaultTableModel setTable(){
        
        DefaultTableModel dataTabel = null;
        try{
            Object[] column = {"Username", "Fail", "Success"};
            dataTabel = new DefaultTableModel(null, column);
            
            String query = "SELECT * from tlevel order by success DESC";
            this.createQuery(query);
            while(this.getResult().next()){
                Object[] row = new Object[3];
                row[0] = this.getResult().getString(2);
                row[1] = this.getResult().getString(4);
                row[2] = this.getResult().getString(3);
                dataTabel.addRow(row);
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        return dataTabel;
    }
}
