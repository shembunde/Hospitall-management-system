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
public class SuppliesManager {

    /**
     * initializes object dbManager
     */
    DatabaseManager dbManager = null;
    
    /**
     * calls
     * @param dbManager 
     */

    SuppliesManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    /**
     * Displays the supplies menu when the user selects supplies option
     * asks the user to select a choice
     */

    public void showMainMenu() {
        System.out.println("\t \t \t 1. Add Supply");
        System.out.println("\t \t \t 2. List all supplies used ");
        System.out.println("\t \t \t 3. List Supplies");
        System.out.println("\t \t \t 4. Generate Report NO of supplies used by category ");
        System.out.println("\t \t \t 5. Generate Report NO of supplies available by category ");
        System.out.println("\t \t \t 6. Generate Report NO of staff who used supplies ");
        System.out.println("\t \t \t 7. Search for a supply from the store ");
        System.out.println("\t \t \t 8. Sickness trends in the hospital ");
        System.out.print("\t \t \t Please select an option from the menu: ");

        /**
         * Captures the user's input
         */
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();

        /**
         * a switch in which every case a method is called
         */
        switch (choice) {
            case "1":
                /**
                 * adds a new supply to the database
                 */
                this.addSupplyToTheStore();
                break;

            case "2":               
                /**
                 * displays a list of used supplies in the database
                 */
                this.listUsedSupplies();
                break;

            case "3":                
                /**
                 * selects a specific supply
                 */
                this.selectSupplyItem();
                break;
                
            case "4":
                /**
                 * generates report on supplies used by category
                 */
                this.reportOnSupppliesUsedByCategory();
                break;
                
            case "5":
                /**
                 * generates report on supplies available by category
                 */
                this.reportOnSupppliesAvailableByCategory();
                break;
                
            case"6":
                /**
                 * generates report on staff who used the supplies
                 */
                this.reportOnStaffWhoUsedSupplies();
                break;
                
            case"7":
                /**
                 * searches for supplies
                 */
                this.SearchForSupplyInStore();
                break;
                
            case"8":
                /**
                 * generates report on sickness trends
                 */
                this.reportOnSicknessTrends();
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
 * method used for inserting supplies
 */
    private void addSupplyToTheStore() {
        /**
         * asks the user to enter the supply name
         * and captures the input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Supply name: ");
        String SupplyName = scanner.nextLine();
        
        /**
         * asks the user to enter the supply category
         * and captures the input
         */
        System.out.print("Enter the Supply category: ");
        String Category = scanner.nextLine();

        /**
         * asks the user to enter the date of registry
         * and captures the input
         */
        System.out.print("Enter the Date of Registry (YYYY-MM-DD): ");
        String DateOfRegistry = scanner.nextLine();
        
        /**
         * asks the user to enter the amount
         * and captures the input
         */
        System.out.print("Enter the amount: ");
        String amount = scanner.nextLine();
        
        /**
         * statement for inserting the specified details into the database SuppliesDetails
         */

        String query = "INSERT INTO SuppliesDetails(SupplyName,Category, DateOfRegistry, Quantity) VALUES ('" + SupplyName + "','" + Category + "','" + DateOfRegistry + "', '" + amount + "');";
        String succeseMessage = "Supply " + SupplyName + " was added successfully.";
        String failureMessage = "Supply could not be added";
        this.dbManager.execute(query, succeseMessage, failureMessage);
    }
    /**
     * Deletes supply from the store.
     * @param id - specified from the supplies details
     */
    private void delete(String id) {
        /**
         * asks the user if they want to delete a certain supply or not
         * and captures their choice.
         */
        System.out.print("\n Are you sure you want to delete the selected Supply?(Y/N) ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        /**
         * The choice is either Yes or No
         * Any other choice is invalid
         */
        if ("y".equals(input.toLowerCase())) {
            
            /**
             * statement for deleting a specified supply from the database table SuppliesDetails 
             */
            String sql = "DELETE FROM SuppliesDetails WHERE SupplyID = " + id;
            
            /**
             * executes the statement
             */
            this.dbManager.execute(sql, "Supply deleted successfully", "Supply could not be deleted.");

        }

    }
    
    /**
     * method used for changing the details of a supply in a database
     * @param id- specified from the supplies details 
     */
    private void modify(String id) {

        /**
         * asks the user if they want to edit a certain supply name or not
         * and captures their input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Supply name (to skip press enter): ");
        String SupplyName = scanner.nextLine();
        
        /**
         * asks the user if they want to edit a certain supply category or not
         * and captures their input
         */
        System.out.print("Enter the Supply category(to skip press enter): ");
        String SupplyCategory = scanner.nextLine();
        
        /**
         * displays the message if the user didn't enter anything
         */

        if (SupplyName.equals("") && SupplyCategory.equals("")) {
            System.out.println("No edits were entered, nothing to update");
        } else {

            String[] columns = {"SupplyName", "SupplyCategory"};
            String[] values = {SupplyName, SupplyCategory};
            
            /**
             * executes the update statement
             */
            String sql = this.dbManager.generateUpdateQuery("SuppliesDetails", columns, values);

            sql += " WHERE SupplyID = " + id;
            this.dbManager.execute(sql, "supply updated successfully", "supply could not be updated.");
        }
    }
    
    /**
     * displays a list of used supplies from the database table UsedSupplies
     */
    private void listUsedSupplies() {
       
        String sql = "SELECT u.UsedID, t.TreatmentID "
                + " FROM UsedSupplies u "
                + " INNER JOIN Treatments t ON t.TreatmentID = u.TreatmentID " 
                ;

        ResultSet resultSet = this.dbManager.executeQuery(sql);

        try {
            
        /**
         * displays the result of the statements when execution occurs
         * and error message when execution fails
         */

            System.out.println(" LIST OF SUPPLIES USED FROM THE STORE ");

            while (resultSet.next()) {

                String UsedID = resultSet.getString("UsedID");
                String TreatmentID = resultSet.getString("TreatmentID");
                //String supplyName = resultSet.getString("supplyName");

                System.out.println(UsedID + "\t" + TreatmentID);
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    /**
     * method used for viewing report on supplies used by category
     */
    public void reportOnSupppliesUsedByCategory() {
        
        /**
         * statement for selecting a report on supplies used by category from the database table UsedSupplies
         */
        String sql = "Select Sum(us.Quantity) as Quantity, s.Category "
                + " FROM UsedSupplies us "
                + " Inner Join SuppliesDetails s On  s.SupplyId = us.SupplyId "
                + " Group by s.Category ";
        
        /**
         * executes the select statement
         */
        ResultSet resultSet = this.dbManager.executeQuery(sql);
        try {
            
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t USED SUPPLIES REPORT");

            while (resultSet.next()) {

                String Quantity = resultSet.getString("Quantity");
                String Category = resultSet.getString("Category");

                System.out.println("\t" +Quantity + ":" +Category);
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * method used for viewing report on supplies available by category
     */
    public void reportOnSupppliesAvailableByCategory() {
        
        /**
         * statement for selecting a report on supplies available by category from the database table SuppliesDetails
         */
        String sql = " Select Quantity as Available, Category "
                 + " FROM SuppliesDetails  "
               ;
        
        /**
         * executes the select statement
         */
        ResultSet resultSet = this.dbManager.executeQuery(sql);
        try {
            
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t AVAILABLE SUPPLIES  REPORT");

            while (resultSet.next()) {

                String Available = resultSet.getString("Available");
                String SupplyCategory = resultSet.getString("Category");

                System.out.println("\t\t" +Available + ":" + SupplyCategory);
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    /**
     * method used for viewing report on sickness trends
     */
    public  void reportOnSicknessTrends(){
        
         /**
         * statement for selecting a report on sickness trends from the database table Treatments
         */
       String query=" select Diagnosis, count(*) as NoOfTimes "
               + " FROM Treatments "
               + " GROUP BY Diagnosis "
               + "ORDER BY NoOfTimes DESC ";
       
        /**
         * executes the select statement
         */
       ResultSet resultSet = this.dbManager.executeQuery(query);
        try {
            
             /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t REPORT ON SICKNESS TRENDS IN HOSPITAL");

            while (resultSet.next()) {

                String Diagnosis = resultSet.getString("Diagnosis");
                String NoOfTimes = resultSet.getString("NoOfTimes");

                System.out.println("\t\t" +Diagnosis + ":" + NoOfTimes);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        };
    }

    /**
     * method used for viewing report on staff who used supplies
     */
    public void reportOnStaffWhoUsedSupplies() {       
        /**
         * statement for selecting a report on staff who used supplies from the database table UsedSupplies
         */
      String sql=  "SELECT us.UsedID, us.Quantity, s.SupplyName, d.DocName"
                + " From UsedSupplies us"
                + " INNER join Treatments t on t.TreatmentID = us.TreatmentID"
                + " INNER join SuppliesDetails s on s.SupplyID = us.SupplyID"
                + " INNER join DoctorsDetails d on d.DocID = t.DocID";
      
        /**
         * executes the select statement
         */
        
        ResultSet resultSet = this.dbManager.executeQuery(sql);
        try {
            
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t REPORT ON SUPPLIES USED BY DOCTORS");

            while (resultSet.next()) {

                String DocName = resultSet.getString("DocName");
                String SupplyName = resultSet.getString("SupplyName");

                System.out.println("\t\t" +DocName + ":" + SupplyName);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        };
    }
    
    /**
     * method used for searching for supplies
     */
    public void SearchForSupplyInStore(){    
        /**
         * prompts a user to search for a supply and captures it
         */
        Scanner sc = new Scanner(System.in);
        System.out.print("\n Enter the search string to use for the search: ");
        String searchString = sc.next();
        
        /**
         * displays the result of the search based on user's input
         */
        String query="SELECT * FROM suppliesDetails WHERE SupplyName LIKE '%"+searchString+"%' ";
        ResultSet resultSet = this.dbManager.executeQuery(query);
        try {
            
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            System.out.println("\n\t SEARCH PRODUCED THE FOLLOWING");

            while (resultSet.next()) {
 
                
                int SupplyID = resultSet.getInt("SupplyID");
                String SupplyName=resultSet.getString("SupplyName");
                String Category=resultSet.getString("Category");
                String Quantity=resultSet.getString("Quantity");
                
                
                System.out.println("\n\t Supply ID: " +SupplyID );
                System.out.println("\n\t Supply Name: " + SupplyName);
                System.out.println("\n\t Supply Category: " + Category );
                System.out.println("\n\t Quantity: " + Quantity);

            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        };
    }
    
    /**
     * method for selecting and displaying a list of supplies
     * @return 
     */
    public String selectSupplyItem() {
        /**
         * asks the user to enter the supply number
         * and captures the input
         * displays error message when the user inputs an invalid choice
         */
        String[] arr = this.list();

        System.out.print("\nPlease enter the supply number to continue: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        boolean valid = this.validateId(choice, arr);

        if (valid) {
            this.showSuppliesMenu(choice);
        } else {
            System.out.println("Wrong choice...");
        }

        return choice;
    }
    
    /**
     * method for displaying a list of supplies
     * @return a String array
     */
    public String[] list() {
        
        /**
         * displays a list from the SuppliesDetails table in the database
         */
        String query = "SELECT * FROM SuppliesDetails;";
        ResultSet resultSet = this.dbManager.executeQuery(query);

        try {
            
            /**
             * displays the result of the statement if the execution is successful
             * also displays an error message if the execution is not successful
             */
            String supplyIds = "";
            System.out.println("\n\t\t\t SUPPLIES LIST");

            while (resultSet.next()) {

                String SupplyID = resultSet.getString("SupplyID");
                supplyIds += SupplyID + ",";
                String SupplyName = resultSet.getString("SupplyName");
                String Category = resultSet.getString("Category");
                String Quantity = resultSet.getString("Quantity");

                System.out.println("\t" +SupplyID + ":" + SupplyName + ":" + Category + ":" + Quantity);

            }

            supplyIds = supplyIds.substring(0, supplyIds.length() - 1);
            return supplyIds.split(",");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return new String[0];

    }
    
    /**
     * Method for displaying supplies menu when the user selects the list supplies option
     * @param id-supplies ID selected by the user.
     */
    private void showSuppliesMenu(String id) {
        System.out.println("\t 1. Modify supply");
        System.out.println("\t 2. Delete supply");
        
        /**
         * Asks the user to select any of the options
         * and accepts the input
         */
        System.out.print("\n Please enter an option from the menu: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();

        switch (option) {
            case "1":
                /**
                 * updates an existing supply in the database
                 */
                this.modify(id);
                break;
                
            case "2":
                /**
                 * deletes an existing supply in the database
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
