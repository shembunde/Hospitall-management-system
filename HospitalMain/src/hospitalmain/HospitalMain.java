/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import java.util.Scanner;
import java.sql.SQLException;
/**
 *
 * @author p.kitheka
 */
public class HospitalMain {

    /**
     * @param args the command line arguments
     * 
     * 
     */
    public static void main(String[] args) {
      
        try{
       /**
       * Creates an object of DatabaseManager
       */
        DatabaseManager dbManager = new DatabaseManager();
        
        Scanner userChoice = new Scanner(System.in);
        /**
         * A variable to hold the choice of the user
         */
        String choice = "No choice yet!"; 
        /**
         * A  loop which breaks only when the 
         * user wants to exit the program.
         */
        do {
            if (!choice.equals("No choice yet!")) {
                /**
                 * Asks the user whether to continue or not
                 */
                boolean wantToContinue = shouldContinue();
                if (!wantToContinue) {
                    break;
                }

            }
            /**
             * Displays a menu
             */
            showMenu();
            /**
             * captures the users input
             */
            choice = userChoice.next();
            
             /**
              * A switch where every case creates a new object of every class 
              * and calls the methods of the specified classes.
              */
            switch (choice) {

                case "1": {
                    PatientManager patientManager = new PatientManager(dbManager);
                    patientManager.showMainMenu();
                    break;

                }
                case "2": {
                    DoctorDetails doctorDetails = new DoctorDetails(dbManager);
                    doctorDetails.showMainMenu();
                    break;
                }
                case "3": {
                    SuppliesManager suppliesDetails = new SuppliesManager(dbManager);
                    suppliesDetails.showMainMenu();
                    break;

                }
                case "4": {
                    
                    System.out.println("You chose to exit.......BYE!!");
                    break;
                }
                default: {
                    System.out.println("Invalid Choice...Please enter the correct Input and try Again");
                }

            }
        } while (!choice.equals("4"));
         }catch(SQLException ex) {
            System.err.println("Database connection could not be estsblished");
        }
    }
    /**
     * Displays the first menu to the user
     */
    public static void showMenu() {
        System.out.println("............................................");
        System.out.println("..............MALIZIA HOSPITAL..............");
        System.out.println("1. Patients");
        System.out.println("2. Doctors");
        System.out.println("3. Supplies");
        System.out.println("4. Exit the program");
        System.out.print("Please select a number from the menu: ");
    }
   /**
    * Asks the user whether to continue or not
    * @return boolean
    * Only has two valid choices-(Y or N).
    * Any other choice is invalid.
    */
    private static boolean shouldContinue() {
        System.out.print("Do you want to continue? (Y or N)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        while (!("n".equalsIgnoreCase(input) || "y".equalsIgnoreCase(input))) {
            System.out.print("Please enter valid input. Y/N: ");
            input = scanner.next();
        }
        
        return "y".equalsIgnoreCase(input);

    }

}
