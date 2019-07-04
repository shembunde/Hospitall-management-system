/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author s.obura
 */
public class DoctorDetails
{
    /**
     * initializes object dbManager
     */
    DatabaseManager dbManager = null;
    /**
     * calls
     * @param dbManager 
     */
    DoctorDetails(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    /**
     * displays doctor menu when the user selects doctors option
     * asks the user to select a choice
     */
    public void showMainMenu() {
        System.out.println("\t 1. Add Doctor");
        System.out.println("\t 2. List Doctor");
        System.out.println("\t Please select an option from the menu: ");
        /**
         * captures the user's input
         */    
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        /**
         * a switch in which every case a method is called
         */
        switch (choice) {
            case "1":
                /**
                 * adds a new doctor to the database
                 */
                this.register();
                break;
                
            case "2":
                /**
                 * displays a list of doctors available in the database
                 */
                this.list();
                break;
                
            default:
                /**
                 * message displayed after a user enters an invalid choice
                 */
                System.out.println("Wrong choice...");
                break;
        }
        
    }
    /**
     * method used for adding a new doctor to the database
     */
    private void register(){
        Scanner scanner = new Scanner(System.in);
        /**
         * prompts a user to enter a name and captures it
         */
        System.out.print("Enter the Doctor name: ");
        String DocName = scanner.nextLine();
        
        /**
         *  prompts a user to enter the contacts and captures it
         */
        System.out.print("Enter the Doctor phone number: ");
        String phoneNumber = scanner.nextLine();
        
        /**
         * statement for inserting specified details into database table DoctorsDetails  
         */
        String query = "INSERT INTO DoctorsDetails(DocName, PhoneNumber) VALUES ('"+DocName+"','"+phoneNumber+"');";
        String succeseMessage = "Doctor " + DocName + " was added successfully.";
        String failureMessage = "Doctor could not be added";
        /**
         * executes the statement
         */
        this.dbManager.execute(query, succeseMessage, failureMessage);
    }
    
    /**
     * method used for deleting a doctor from the database
     * @param id - specified
     */
    private void delete(String id) {
         
        try{
            //String query="select DocID From Treatments Where DocID LIKE '%"+id+"%' ";
             //ResultSet resultSet = this.dbManager.executeQuery(query); 
        /**
         * Asks the user if they want to delete a certain doctor or not
         * and captures their choice.
         */
        System.out.print("\n Are you sure you want to delete the selected Doctor?(Y/N) ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        /**
         * The choice is either Yes or No
         * Any other choice is invalid
         */
        String sql="";
        if ("y".equals(input.toLowerCase())) {
            /**
             * Statement for deleting a specified doctor from the database table DoctorsDetails 
             */
             sql = "DELETE FROM DoctorsDetails WHERE DocID = " + id;
            
            /**
             * executes the statement
             */
            this.dbManager.execute(sql,"Doctor deleted successfully","You cant delete a doctor whose details  exists in another table");
            
          }
            
        
        }catch(Exception ex){
            System.err.println("You cant delete a doctor whose details  exists in another table");
        }
            
    }
    /**
     * method used for changing the details of a doctor in a database
     * @param id - specified
     */
    private void modify(String id) {
        
        /**
         * asks the user if they want to edit a certain doctor's name or not
         * and captures their input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the doctor name (to skip press enter): ");
        String DocName = scanner.nextLine();
        
        /**
         * asks the user if they want to edit a certain doctor's contacts or not
         * and captures their input.
         */
        System.out.print("Enter the doctor phone number (to skip press enter): ");
        String phoneNumber = scanner.nextLine();
        
        /**
         * displays the message if the user didn't enter anything
         */
        
        if (DocName.equals("") && phoneNumber.equals("")) {
            System.out.println("No edits were entered, nothing to update");
        } else {
            
            String[] columns = {"DocName", "phoneNumber"};
            String[] values = {DocName, phoneNumber};
            
            /**
             * executes the update statement
             */
            String sql = this.dbManager.generateUpdateQuery("DoctorsDetails", columns, values);
            
            sql += " WHERE DocID = " + id;
            this.dbManager.execute(sql, "doctor updated successfully", "doctor could not be updated.");
        }
    }
    
    /**
     * asks the user if they want to view a report on patients treated by a certain doctor
     * @param id of the doctor selected by the user
     */
    private void reports(String id) {
        /**
         * statement for selecting a specified doctor's report on patients treated from the database table Treatments
         */
        
        String sql = "SELECT  d.DocName,p.PatientName, t.Diagnosis, t.DateOfTreatment " +
                     "FROM Treatments t " +
                     "INNER JOIN DoctorsDetails d ON d.DocId = t.DocID " +
                     "INNER JOIN PatientDetails p ON p.PatientId = t.PatientId " +
                     "WHERE d.DocId = " + id;
        
        /**
         * Executes the select statement which produces a result set.
         */
        ResultSet resultSet = this.dbManager.executeQuery(sql);
        
        try {
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
        
            System.out.println("\n\t REPORT ON PATIENTS TREATED BY A CERTAIN DOCTOR ");
            
            while(resultSet.next()) {

                String date = resultSet.getString("DateOfTreatment");
                String PatientName = resultSet.getString("PatientName");
                String diagnosis = resultSet.getString("Diagnosis");
                
                System.out.println("\t\t" +date + ":" + PatientName + ":" + diagnosis);   
            }
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    /**
     * displays a list of doctors from the database table DoctorsDetails
     */
    public void list() {
        String query = "SELECT * FROM DoctorsDetails;";
        /**
         * executes the select statement
         */
        ResultSet resultSet = this.dbManager.executeQuery(query);
        
        try {
        /**
         * displays the result of the statements when execution occurs
         */
            
            String DocNumbers = "";
            System.out.println("\n\t\t DOCTORS LIST");
            
            while(resultSet.next()) {

                String DocID = resultSet.getString("DocID");
                DocNumbers += DocID + ",";
                String DocName = resultSet.getString("DocName");
                String phoneNumber = resultSet.getString("PhoneNumber");
                
                System.out.println("\t" +DocID + ":\t" + DocName + "------------" + phoneNumber);
                
            }
            
            DocNumbers = DocNumbers.substring(0, DocNumbers.length() - 1);
            String[] arr = DocNumbers.split(",");
            
            /**
             * asks the user to enter a specific doctor's number
             * and outputs error message if the number does not exist
             */
            System.out.print("\nPlease enter the doctors number to continue: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            
            /**
             * captures the user's number and checks whether it is valid or not
             */
            
            boolean valid = this.validateId(choice, arr);
            
            if (valid) {
                this.showDoctorsMenu(choice);
            } else {
                System.out.println("Wrong choice...");
            }
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    /**
     * displays a menu with options whereby a user can select whichever option they desire 
     * @param id - specified
     */
     private void showDoctorsMenu(String id) {
        System.out.println("\t \t \t 1. Modify doctor");
        System.out.println("\t \t \t 2. Delete doctor");
        System.out.println("\t \t \t 3. Generate doctor Report");
        System.out.print("Please enter an option from the menu: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        
        switch(option) {
            
            /** a switch where in every case a method is called
            **/
            case "1":
                /**
                 * updates an existing doctor in the database
                 */
                this.modify(id);
                break;
            case "2":
                /**
                 * deletes a certain doctor from the database
                 */
                this.delete(id);
                break;
            case "3":
                /**
                 * generates a specific doctor's report from the database
                 */
                this.reports(id);
                break;
                /**
                 * displays error message when a user enters wrong input
                 */
            default:
                System.err.println("Please select a valid option.");
        }
        
    }
     /**
      * 
      * @param id- specified 
      * @param validId-From the specified table
      * @return boolean
      */
     private boolean validateId(String id, String[] validId) {
        
        boolean valid = false;
        
        for (String validNumber : validId) {
            if (id.equals(validNumber)) {
                valid = true;
                break;
            }
        }
        
        return valid;
        
    } 
}
