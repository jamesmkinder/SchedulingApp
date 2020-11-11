package Controller;

import Model.DBMSConnection;
import Model.User;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * AddAppointmentFormController is a controller class for the add appointment form.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class AddAppointmentFormController implements Initializable {

    @FXML public Label newAppointmentLabel;
    @FXML public Label titleLabel;
    @FXML public TextField titleText;
    @FXML public Label descriptionLabel;
    @FXML public TextField descriptionText;
    @FXML public Label locationLabel;
    @FXML public TextField locationText;
    @FXML public Label typeLabel;
    @FXML public TextField typeText;
    @FXML public Label contactLabel;
    @FXML public ComboBox<String> contactComboBox;
    @FXML public Button save;
    @FXML public Button cancel;
    @FXML public Label startLabel;
    @FXML public Label endLabel;
    @FXML public Label customerIDLabel;
    @FXML public ComboBox<String> customerIDComboBox;
    @FXML public TextField appointmentID;
    @FXML public TextField startMonthText;
    @FXML public TextField startDayText;
    @FXML public TextField startYearText;
    @FXML public TextField startHourText;
    @FXML public TextField startMinuteText;
    @FXML public TextField endMonthText;
    @FXML public TextField endDayText;
    @FXML public TextField endYearText;
    @FXML public TextField endHourText;
    @FXML public TextField endMinuteText;
    @FXML public Label errorLabel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBMSConnection connection = new DBMSConnection();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        newAppointmentLabel.setText(resourceBundle.getString("newappointment"));
        titleLabel.setText(resourceBundle.getString("title"));
        descriptionLabel.setText("Description");
        locationLabel.setText(resourceBundle.getString("locale"));
        typeLabel.setText("Type");
        customerIDLabel.setText(resourceBundle.getString("customerid"));
        startLabel.setText(resourceBundle.getString("start"));
        endLabel.setText(resourceBundle.getString("end"));
        contactLabel.setText(resourceBundle.getString("contact"));
        save.setText(resourceBundle.getString("save"));
        cancel.setText(resourceBundle.getString("cancel"));

        String appointmentIDQuery = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'WJ07XGR' AND TABLE_NAME = 'appointments'";

        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(appointmentIDQuery);
            rs.next();
            appointmentID.setText(Integer.toString(rs.getInt("AUTO_INCREMENT")));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        ObservableList<String> contacts = FXCollections.observableArrayList();
        String contactQuery = "SELECT Contact_Name FROM contacts";
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(contactQuery);
            while (rs.next()) {
                contacts.add(rs.getString("Contact_Name"));
            }
        }catch (SQLException e) {e.printStackTrace();}
        contactComboBox.setItems(contacts);


        ObservableList<String> customers = FXCollections.observableArrayList();
        String customerQuery = "SELECT Customer_ID FROM customers";
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(customerQuery);
            while (rs.next()) {
                customers.add(Integer.toString(rs.getInt("Customer_ID")));
            }
        }catch (SQLException e) {e.printStackTrace();}
        customerIDComboBox.setItems(customers);

        /**
         * save.setOnAction lambda expression saves a new appointment to the database in UTC using the values entered by the user.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        save.setOnAction(e -> {

            try {

                LocalDateTime startDate = LocalDateTime.parse(startYearText.getText() + "-" + startMonthText.getText() + "-" + startDayText.getText() + " " + startHourText.getText() + ":" +
                                startMinuteText.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                ZonedDateTime startDateLocal = ZonedDateTime.of(startDate, ZoneId.systemDefault());
                ZonedDateTime startToUTC = startDateLocal.withZoneSameInstant(ZoneOffset.UTC);
                String start = startToUTC.getYear() + "-" + startToUTC.getMonthValue() + "-" + startToUTC.getDayOfMonth() + " " + startToUTC.getHour() + ":" + startToUTC.getMinute();

                LocalDateTime endDate = LocalDateTime.parse(endYearText.getText() + "-" + endMonthText.getText() + "-" + endDayText.getText() + " " + endHourText.getText() + ":" +
                        endMinuteText.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                ZonedDateTime endDateLocal = ZonedDateTime.of(endDate, ZoneId.systemDefault());
                ZonedDateTime endToUTC = endDateLocal.withZoneSameInstant(ZoneOffset.UTC);
                String end = endToUTC.getYear() + "-" + endToUTC.getMonthValue() + "-" + endToUTC.getDayOfMonth() + " " + endToUTC.getHour() + ":" + endToUTC.getMinute();

                String customerScheduleQuery = "SELECT Appointment_ID FROM appointments JOIN customers USING (Customer_ID) WHERE Customer_ID = " + customerIDComboBox.getSelectionModel().getSelectedItem() +
                        " AND Start BETWEEN '" + start + "' AND '" + end + "' OR End BETWEEN '" + start + "' AND '" + end + "'";
                System.out.println(customerScheduleQuery);
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(customerScheduleQuery);

                if(startToUTC.getHour() < 12 || (endToUTC.getDayOfMonth() > endDateLocal.getDayOfMonth() || endToUTC.getMonthValue() > endDateLocal.getMonthValue()) && endToUTC.getHour() > 2){
                    errorLabel.setText(resourceBundle.getString("invalidtime"));
                }
                else if (rs.next()){
                    errorLabel.setText(resourceBundle.getString("conflictingappointment"));
                }
                else {
                    String insertAppointmentQuery = String.format("INSERT INTO appointments (Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', (SELECT User_ID FROM users WHERE User_Name = '%s'), " +
                                    "(SELECT Contact_ID FROM contacts WHERE Contact_Name = '%s'))", titleText.getText(), descriptionText.getText(), locationText.getText(), typeText.getText(), start, end,
                            User.getUsername(), User.getUsername(), customerIDComboBox.getSelectionModel().getSelectedItem(), User.getUsername(), contactComboBox.getSelectionModel().getSelectedItem());
                    statement.executeUpdate(insertAppointmentQuery);
                    Stage close = (Stage) newAppointmentLabel.getScene().getWindow();
                    close.close();
                    Parent root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
                    Scene scene = new Scene(root);
                    Stage mainMenu = new Stage();
                    mainMenu.setScene(scene);
                    mainMenu.show();
                }
            } catch (SQLException | IOException exception) {exception.printStackTrace();}
        });

        /**
         * cancel.setOnAction lambda expression closes the form and opens the main form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        cancel.setOnAction(e -> {
            Stage close = (Stage) newAppointmentLabel.getScene().getWindow();
            close.close();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
                Scene scene = new Scene(root);
                Stage mainMenu = new Stage();
                mainMenu.setScene(scene);
                mainMenu.show();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
