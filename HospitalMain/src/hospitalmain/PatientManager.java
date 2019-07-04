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
public class PatientManager {
    /**
     * initializes object dbManager
     */

    DatabaseManager dbManager = null;
    SuppliesManager suppliesManager = null;
    /**
     * calls
     * @param dbManager 
     */

    PatientManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.suppliesManager = new SuppliesManager(dbManager);
    }
    
    /**
     * displays Patient menu when the user selects patients option
     * asks the user to select a choice
     */

    public void showMainMenu() {
        System.out.println("\t \t \t 1. Add Patient");
        System.out.println("\t \t \t 2. List Patients");
        System.out.print("\t \t \t Please select an option from the menu: ");
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
                 * adds a new patient to the database
                 */
                this.register();
                break;
            case "2":
                /**
                 * displays a list of patients available in the database
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
    public void register() {
        Scanner scanner = new Scanner(System.in);
        /**
         * prompts a user to enter a name and captures it
         */
        System.out.print("Enter the patient name: ");
        String patientName = scanner.nextLine();
        /**
         *  prompts a user to enter the contacts and captures it
         */
        System.out.print("Enter the patient phone number: ");
        String phoneNumber = scanner.nextLine();
        
        /**
         * statement for inserting specified details into database table PatientDetails  
         */
        String query = "INSERT INTO PatientDetails(PatientName, PhoneNumber) VALUES ('" + patientName + "','" + phoneNumber + "');";
        String succeseMessage = "Patient " + patientName + " was added successfully.";
        String failureMessage = "Patient could not be added";
        /**
         * executes the statement
         */
        this.dbManager.execute(query, succeseMessage, failureMessage);
    }
    
    /**
     * method used for deleting a patient from the database
     * @param id - specified
     */
    private void delete(String id) {
        System.out.print("Are you sure you want to delete the selected patient?(Y/N) ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        /**
         * The choice is either Yes or No
         * Any other choice is invalid
         */
        if ("y".equals(input.toLowerCase())) {
            
            /**
             * statement for deleting a specified patient from the database table PatientDetails 
             */

            String sql = "DELETE FROM PatientDetails WHERE PatientID = " + id;
            
            /**
             * executes the statement
             */
            this.dbManager.execute(sql, "Patient deleted successfully", "Patient could not be deleted.");

        }

    }
    
    /**
     * method used for changing the details of a patient in a database
     * @param id- specified 
     */
    private void modify(String id) {
        /**
         * asks the user if they want to edit a certain patient's name or not
         * and captures their input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the patient name (to skip press enter): ");
        String patientName = scanner.nextLine();
        
        /**
         * asks the user if they want to edit a certain patient's contacts or not
         * and captures their input.
         */
        System.out.print("Enter the patient phone number (to skip press enter): ");
        String phoneNumber = scanner.nextLine();
        
        /**
         * displays the message if the user didn't enter anything
         */
        if (patientName.equals("") && phoneNumber.equals("")) {
            System.out.println("No edits were entered, nothing to update");
        } else {

            String[] columns = {"PatientName", "phoneNumber"};
            String[] values = {patientName, phoneNumber};
            
            /**
             * executes the update statement
             */
            String sql = this.dbManager.generateUpdateQuery("PatientDetails", columns, values);

            sql += " WHERE PatientID = " + id;
            this.dbManager.execute(sql, "Patient updated successfully", "Patient could not be updated.");
        }
    }
    
    /**
     * asks the user if they want to view a report on patients treatment report
     * @param id of the patient selected by the user
     */
    private void reports(String id) {
        /**
         * statement for selecting a specified patient's report from the database table Treatments
         */
        String sql = "SELECT p.PatientName, d.DocName, t.Diagnosis, t.DateOfTreatment "
                + "FROM Treatments t "
                + "INNER JOIN DoctorsDetails d ON d.DocId = t.DocID "
                + "INNER JOIN PatientDetails p ON p.PatientId = t.PatientId "
                + "WHERE p.PatientId = " + id;
        
        /**
         * executes the select statement
         */
        ResultSet resultSet = this.dbManager.executeQuery(sql);

        try {

            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t PATIENT TREATMENT REPORT");

            while (resultSet.next()) {

                String date = resultSet.getString("DateOfTreatment");
                String docName = resultSet.getString("DocName");
                String diagnosis = resultSet.getString("Diagnosis");

                System.out.println("\t\t"+date + ":" + docName + ":" + diagnosis);
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    /**
     * method used for treating the patient
     * @param id- specified 
     */
    private void treat(String id) {
        String docId = this.selectDoctor();
        
        /**
         * asks the user to enter  the patient's date of treatment
         * and captures their input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date of treatment (YYYY-MM-DD): ");
        String treatmentDate = scanner.nextLine();
        
        /**
         * asks the user to enter  the patient's return date
         * and captures their input
         */
        System.out.print("Enter return date (YYYY-MM-DD): ");
        String returnDate = scanner.nextLine();
        
        /**
         * asks the user to enter  the patient's sickness
         * and captures their input
         */
        System.out.print("Enter the diagnosis: ");
        String diagnosis = scanner.nextLine();
        
        /**
         * statement for inserting the specified patient treatment details into the database table Treatments
         */
        String query = "INSERT INTO Treatments(DocID, PatientId, DateOfTreatment, ReturnDate, Diagnosis) VALUES ('" + docId + "','" + id + "', '" + treatmentDate + "', '" + returnDate + "', '" + diagnosis + "');";
        
        /**
         * executes the insert statement
         */
        this.dbManager.execute(query, "Treatment added successfully", "Treatment could not be added");
        
        /**
         * asks the user if there were any supplies given,captures input
         * and displays the number of supplies issued
         */
        Scanner sca = new Scanner(System.in);
        System.out.print("Are there  any supplies given?(Y/N)");
        String choice = sca.nextLine().toLowerCase();

        if (choice.equals("y")) {
            String sql = "SELECT MAX(TreatmentID) as TreatmentID from treatments;";
            ResultSet rs = this.dbManager.executeQuery(sql);
            
        
            try {
                rs.next();
                String treatmentId = rs.getString("TreatmentID");
                this.issueItems(treatmentId);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

        }
    }
    
/**
 * method used for issuing supplies
 * @param treatmentId 
 */
    private void issueItems(String treatmentId) {

        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            
            /**
             * asks the user if they want to issue another supply
             */
            issueItem(treatmentId);
            System.out.print("Do you want to issue another item?(Y/N): ");
            choice = scanner.nextLine();
        } while ("y".equals(choice.toLowerCase()));

    }
    /**
     * method used for inserting supplies into a database
     * @param treatmentId 
     */

    private void issueItem(String treatmentId) {
        /**
         * displays a list of used supplies
         */
        String[] supplyIds = this.suppliesManager.list();
        
        /**
         * asks the user to select any of the used supplies displayed
         */
        System.out.print("\nPlease select the item that was used: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        boolean valid = this.validateId(choice, supplyIds);

        if (valid) {

        /**
        * asks the user to enter the amount used
        * and captures the input
        */     
            System.out.print("\nPlease enter the amount used: ");
            String qty = scanner.next(); // Validate quantity
            
        /**
        * asks the user to enter the date issued
        * and captures the input
        */
            System.out.print("\nPlese enter the date issued (YYYY/MM-DD): ");
            String dateIssued = scanner.next();
            
        /**
        * statement for inserting the supplies into the database table UsedSupplies
        * 
        */
            String sql = "INSERT INTO UsedSupplies (TreatmentID, SupplyID, DateIssued, Quantity) VALUES('" + treatmentId + "', '" + choice + "', '" + dateIssued + "', '" + qty + "');";
            this.dbManager.execute(sql, "Item issued successfully", "Item could not be issued");
            
        /**
        * statement used for updating the supplies in the database table SupplyDetails
        * and displays error message if the statement are not successful
        * 
        */
            String updateSql = "UPDATE SuppliesDetails SET Quantity = Quantity - " + qty + " WHERE SupplyID = " + choice + ";";
            this.dbManager.execute(updateSql, "", "");
            
        } else {
            System.out.println("Wrong choice...");
        }

    }
    
    /**
     * method used for selecting  all doctors in a database
     * @return String DocID and DocName
     */
    private String selectDoctor() {
        
    /**
    * displays a list of doctors from the database table DoctorsDetails
    */
        String query = "SELECT * FROM DoctorsDetails;";
        ResultSet resultSet = this.dbManager.executeQuery(query);

        try {

            String docNumbers = "";
            System.out.println("Doctor List");

            while (resultSet.next()) {

                String docID = resultSet.getString("DocID");
                docNumbers += docID + ",";
                String docName = resultSet.getString("DocName");

                System.out.println(docID + "\t" + docName);

            }

            docNumbers = docNumbers.substring(0, docNumbers.length() - 1);
            String[] arr = docNumbers.split(",");
            
            /**
             * asks the user to choose a specific doctor from the list displayed
             * and captures the user's input
             */
            System.out.print("\nPlease select the doctor from the list?: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            boolean valid = this.validateId(choice, arr);

            if (valid) {
                return choice;
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return "0";
    }

    /**
     * displays a list of patients from the database table PatientDetails
     */
    public void list() {
        String query = "SELECT * FROM PatientDetails;";
        /**
         * executes the select statement
         */
        ResultSet resultSet = this.dbManager.executeQuery(query);

        try {

        /**
         * displays the result of the statements when execution occurs
        */
            String patientNumbers = "";
            System.out.println("\n\t\t PATIENTS LIST");

            while (resultSet.next()) {

                String patientID = resultSet.getString("PatientID");
                patientNumbers += patientID + ",";
                String patientName = resultSet.getString("PatientName");
                String phoneNumber = resultSet.getString("PhoneNumber");

                System.out.println("\t" +patientID + ":\t" + patientName + "\t" + phoneNumber);

            }

            patientNumbers = patientNumbers.substring(0, patientNumbers.length() - 1);
            String[] arr = patientNumbers.split(",");
            
            /**
             * asks the user to enter a specific patient's number
             * and outputs error message if the number does not exist
             */
            System.out.print("\nPlease enter the patient number to continue: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            /**
             * captures the user's number and checks whether it is valid or not
             */
            boolean valid = this.validateId(choice, arr);

            if (valid) {
                this.showPatientMenu(choice);
            } else {
                System.out.println("Wrong choice...");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    /**
     * displays a menu with options whereby a user can select whichever option they desire 
     * @param id 
     */
    private void showPatientMenu(String id) {
        System.out.println("\t \t \t 1. Modify Patient");
        System.out.println("\t \t \t 2. Treat Patient");
        System.out.println("\t \t \t 3. Generate Patient Report");
        System.out.println("\t \t \t 4. Delete Patient");
        System.out.print("Please enter an option from the menu: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();

        switch (option) {
            /** a switch where in every case a method is called
            **/
            case "1":
                 /**
                 * updates an existing patient in the database
                 */
                this.modify(id);
                break;
                
            case "2":
                this.treat(id);
                break;
                
            case "3":
                /**
                 * generates a specific patient's report from the database
                 */
                this.reports(id);
                break;
                
            case "4":
                 /**
                 * deletes a certain patient's from the database
                 */
                this.delete(id);
                break;
                
            default:
                System.err.println("Please select a valid option.");
        }

    }

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
