package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 * ModifyCustomerFormController is a controller class for the modify customer form window.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class ModifyCustomerFormController implements Initializable {

    @FXML public Label modifyCustomerLabel;
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
    @FXML public Label customerID;
    @FXML public TextField customerIDTextField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBMSConnection connection = new DBMSConnection();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        modifyCustomerLabel.setText(resourceBundle.getString("modifycustomer"));
        nameLabel.setText(resourceBundle.getString("name"));
        phoneLabel.setText(resourceBundle.getString("phone"));
        addressLabel.setText(resourceBundle.getString("address"));
        zipLabel.setText(resourceBundle.getString("zip"));
        countryLabel.setText(resourceBundle.getString("country"));
        stateProvinceLabel.setText(resourceBundle.getString("state"));
        saveCustomer.setText(resourceBundle.getString("save"));
        cancelChanges.setText(resourceBundle.getString("cancel"));
        customerID.setText(resourceBundle.getString("customerid"));
        customerIDTextField.setText(Integer.toString(CustomerHolder.getCustomer().getCustomerID()));

        String customerToModifyQuery = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Create_Date, customers.Created_By, " +
                "customers.Last_Update, customers.Last_Updated_By, first_level_divisions.Division, countries.Country FROM customers JOIN first_level_divisions USING (Division_ID) JOIN countries USING (Country_ID) " +
                "WHERE Customer_ID = " + CustomerHolder.getCustomer().getCustomerID();

        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(customerToModifyQuery);
            rs.next();
            nameText.setText(rs.getString("Customer_Name"));
            addressText.setText(rs.getString("Address"));
            zipText.setText(rs.getString("Postal_Code"));
            phoneText.setText(rs.getString("Phone"));
            countryComboBox.getSelectionModel().select(rs.getString("Country"));
            stateProvinceComboBox.getSelectionModel().select(rs.getString("Division"));
        } catch (SQLException e) {e.printStackTrace();}

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

        EventHandler<ActionEvent> populateStateProvinces = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
            }
        };
        countryComboBox.setOnAction(populateStateProvinces);
        countryComboBox.fireEvent(new ActionEvent());

        /**
         * saveCustomer.setOnAction lambda expression is used to save the customer information that was entered in the form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it is in
         * the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        saveCustomer.setOnAction(e ->{
            if(nameText.textProperty().isEmpty().get() || addressText.textProperty().isEmpty().get() || zipText.textProperty().isEmpty().get() || phoneText.textProperty().isEmpty().get() || stateProvinceComboBox.getSelectionModel().isEmpty()){}
            else{
                String insertQuery = String.format("UPDATE customers SET Customer_Name = '%s', Address = '%s', Postal_Code = '%s', Phone = '%s', Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = '%s', Division_ID = " +
                        "(SELECT Division_ID FROM first_level_divisions WHERE Division = '%s') WHERE Customer_ID = %d", nameText.getText(), addressText.getText(), zipText.getText(), phoneText.getText(), User.getUsername(), stateProvinceComboBox.getSelectionModel().getSelectedItem(), CustomerHolder.getCustomer().getCustomerID());
                try{
                    Statement addNewCustomer = connection.getConnection().createStatement();
                    addNewCustomer.executeUpdate(insertQuery);
                }catch (SQLException exception) {exception.printStackTrace();}
                try {
                    Stage close = (Stage) modifyCustomerLabel.getScene().getWindow();
                    close.close();
                    Parent root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
                    Scene scene = new Scene(root);
                    Stage mainMenu = new Stage();
                    mainMenu.setScene(scene);
                    mainMenu.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }});
        /**
         * cancelChanges.setOnAction lambda expression will close the current window and return to the main form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it is in the developer's opinion that
         * this makes the program easier to read and understand what the application is trying to do.
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


