package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * AddCustomerFormController is a controller class for the add customer form.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class AddCustomerFormController implements Initializable {

    @FXML public Label nameLabel;
    @FXML public TextField nameText;
    @FXML public Label phoneLabel;
    @FXML public TextField phoneText;
    @FXML public Label addressLabel;
    @FXML public TextField addressText;
    @FXML public Label zipLabel;
    @FXML public TextField zipText;
    @FXML public Label countryLabel;
    @FXML public ComboBox<String> countryComboBox;
    @FXML public Label stateProvinceLabel;
    @FXML public ComboBox<String> stateProvinceComboBox;
    @FXML public Button saveCustomer;
    @FXML public Button cancelChanges;
    @FXML public Label addCustomerLabel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBMSConnection connection = new DBMSConnection();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        addCustomerLabel.setText(resourceBundle.getString("addcustomer"));
        nameLabel.setText(resourceBundle.getString("name"));
        phoneLabel.setText(resourceBundle.getString("phone"));
        addressLabel.setText(resourceBundle.getString("address"));
        zipLabel.setText(resourceBundle.getString("zip"));
        countryLabel.setText(resourceBundle.getString("country"));
        stateProvinceLabel.setText(resourceBundle.getString("state"));
        saveCustomer.setText(resourceBundle.getString("save"));
        cancelChanges.setText(resourceBundle.getString("cancel"));

        String countryQuery = "SELECT * FROM countries";
        try {
            Statement getCountries = connection.getConnection().createStatement();
            ResultSet rs = getCountries.executeQuery(countryQuery);
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            while (rs.next()) {
                Country country = new Country(rs.getString("Country"));
                countryNames.add(country.getCountry());
            }
            countryComboBox.setItems(countryNames);
        }catch (SQLException e) {e.printStackTrace();}

        /**
         * countryComboBox.setOnAction lambda expression populates the state province combo box when a new country is selected.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        countryComboBox.setOnAction(e -> {
            String stateProvinceQuery = "SELECT first_level_divisions.division_id, first_level_divisions.division, first_level_divisions.create_date, first_level_divisions.created_by, first_level_divisions.last_update, first_level_divisions.last_updated_by, first_level_divisions.COUNTRY_ID FROM first_level_divisions INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID\n" +
                    "WHERE countries.Country = '" + countryComboBox.getSelectionModel().getSelectedItem() + "'";
            try {
                Statement getStatesProvinces = connection.getConnection().createStatement();
                ResultSet rs = getStatesProvinces.executeQuery(stateProvinceQuery);
                ObservableList<String> statesProvinces = FXCollections.observableArrayList();
                while (rs.next()) {
                    FirstLevelDivision firstLevelDivision = new FirstLevelDivision(rs.getString("Division"));
                    statesProvinces.add(firstLevelDivision.getDivision());
                }
                stateProvinceComboBox.setItems(statesProvinces);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        /**
         * saveCustomer.setOnAction lambda expression will save a new customer to the database using the values entered into the form by the user.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        saveCustomer.setOnAction(e ->{
            if(nameText.textProperty().isEmpty().get() || addressText.textProperty().isEmpty().get() || zipText.textProperty().isEmpty().get() || phoneText.textProperty().isEmpty().get() || stateProvinceComboBox.getSelectionModel().isEmpty()){}
            else{
            String insertQuery = String.format("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', " +
                    "(SELECT Division_ID FROM first_level_divisions WHERE Division = '%s'))", nameText.getText(), addressText.getText(), zipText.getText(), phoneText.getText(), User.getUsername(), User.getUsername(), stateProvinceComboBox.getSelectionModel().getSelectedItem());
            try{
                Statement addNewCustomer = connection.getConnection().createStatement();
                addNewCustomer.executeUpdate(insertQuery);
            }catch (SQLException exception) {exception.printStackTrace();}
            Stage close = (Stage) saveCustomer.getScene().getWindow();
            close.close();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage mainMenu = new Stage();
            mainMenu.setScene(scene);
            mainMenu.show();
        }});

        /**
         * cancelChanges.setOnAction lambda expression closes the form and returns to the main form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it is in the developer's
         * opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        cancelChanges.setOnAction(e -> {
            Stage close = (Stage) cancelChanges.getScene().getWindow();
            close.close();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage mainMenu = new Stage();
            mainMenu.setScene(scene);
            mainMenu.show();
        });
    }
}
