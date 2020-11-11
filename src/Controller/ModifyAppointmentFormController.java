package Controller;

import Model.AppointmentHolder;
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
import java.time.temporal.TemporalField;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * ModifyAppointmentFormController is a controller class for the modify appointment form.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class ModifyAppointmentFormController implements Initializable {


    @FXML public Label updateAppointmentLabel;
    @FXML public TextField appointmentID;
    @FXML public Label titleLabel;
    @FXML public TextField titleText;
    @FXML public Label descriptionLabel;
    @FXML public TextField descriptionText;
    @FXML public Label locationLabel;
    @FXML public TextField locationText;
    @FXML public Label typeLabel;
    @FXML public TextField typeText;
    @FXML public Label startLabel;
    @FXML public Label contactLabel;
    @FXML public ComboBox<String> contactComboBox;
    @FXML public Label endLabel;
    @FXML public Label customerIDLabel;
    @FXML public ComboBox<String> customerIDComboBox;
    @FXML public Button save;
    @FXML public Button cancel;
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

        updateAppointmentLabel.setText(resourceBundle.getString("modifyappointment"));
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
        appointmentID.setText(Integer.toString(AppointmentHolder.getAppointment().getAppointmentID()));

        titleText.setText(AppointmentHolder.getAppointment().getTitle());
        descriptionText.setText(AppointmentHolder.getAppointment().getDescription());
        locationText.setText(AppointmentHolder.getAppointment().getLocation());
        typeText.setText(AppointmentHolder.getAppointment().getType());
        LocalDateTime appointmentStart = LocalDateTime.of(Integer.parseInt(AppointmentHolder.getAppointment().getStartYear()), Integer.parseInt(AppointmentHolder.getAppointment().getStartMonth()),
                Integer.parseInt(AppointmentHolder.getAppointment().getStartDay()), Integer.parseInt(AppointmentHolder.getAppointment().getStartHour()), Integer.parseInt(AppointmentHolder.getAppointment().getStartMinute()));
        ZonedDateTime appointmentStartZoned = ZonedDateTime.of(appointmentStart, ZoneOffset.UTC);
        ZonedDateTime appointmentStartToLocal = appointmentStartZoned.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime appointmentEnd = LocalDateTime.of(Integer.parseInt(AppointmentHolder.getAppointment().getEndYear()), Integer.parseInt(AppointmentHolder.getAppointment().getEndMonth()),
                Integer.parseInt(AppointmentHolder.getAppointment().getEndDay()), Integer.parseInt(AppointmentHolder.getAppointment().getEndHour()), Integer.parseInt(AppointmentHolder.getAppointment().getEndMinute()));
        ZonedDateTime appointmentEndZoned = ZonedDateTime.of(appointmentEnd, ZoneOffset.UTC);
        ZonedDateTime appointmentEndToLocal = appointmentEndZoned.withZoneSameInstant(ZoneId.systemDefault());
        AppointmentHolder.getAppointment().setStartTime(appointmentStartToLocal.getHour(), appointmentStartToLocal.getMinute());
        AppointmentHolder.getAppointment().setEndTime(appointmentEndToLocal.getHour(), appointmentEndToLocal.getMinute());
        startYearText.setText(Integer.toString(appointmentStartToLocal.getYear()));
        startMonthText.setText(Integer.toString(appointmentStartToLocal.getMonthValue()));
        startDayText.setText(Integer.toString(appointmentStartToLocal.getDayOfMonth()));
        startHourText.setText(AppointmentHolder.getAppointment().getStartHour());
        startMinuteText.setText(AppointmentHolder.getAppointment().getStartMinute());
        endYearText.setText(Integer.toString(appointmentEndToLocal.getYear()));
        endMonthText.setText(Integer.toString(appointmentEndToLocal.getMonthValue()));
        endDayText.setText(Integer.toString(appointmentEndToLocal.getDayOfMonth()));
        endHourText.setText(AppointmentHolder.getAppointment().getEndHour());
        endMinuteText.setText(AppointmentHolder.getAppointment().getEndMinute());


        ObservableList<String> contacts = FXCollections.observableArrayList();
        String contactQuery = "SELECT Contact_Name FROM contacts";
        String selectedContactQuery = "SELECT Contact_Name FROM appointments JOIN contacts USING (Contact_ID) WHERE appointments.Contact_ID = " + AppointmentHolder.getAppointment().getContactID();
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(contactQuery);
            while (rs.next()) {
                contacts.add(rs.getString("Contact_Name"));
            }
            contactComboBox.setItems(contacts);
            ResultSet resultSet = statement.executeQuery(selectedContactQuery);
            resultSet.next();
            contactComboBox.getSelectionModel().select(resultSet.getString("Contact_Name"));
        }catch (SQLException e) {e.printStackTrace();}






        ObservableList<String> customers = FXCollections.observableArrayList();
        String customerQuery = "SELECT Customer_ID FROM customers";
        String selectedCustomerQuery = "SELECT Customer_ID FROM appointments JOIN customers USING (Customer_ID) WHERE appointments.Customer_ID = " + AppointmentHolder.getAppointment().getCustomerID();
        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(customerQuery);
            while (rs.next()) {
                customers.add(Integer.toString(rs.getInt("Customer_ID")));
            }
            customerIDComboBox.setItems(customers);
            ResultSet resultSet = statement.executeQuery(selectedCustomerQuery);
            resultSet.next();
            customerIDComboBox.getSelectionModel().select(Integer.toString(resultSet.getInt("Customer_ID")));
        }catch (SQLException e) {e.printStackTrace();}


        /**
         * save.setOnAction lambda expression saves the entered appointment information after checking if the time entered is out of business hours.
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

                String customerScheduleQuery = "SELECT Appointment_ID FROM appointments JOIN customers USING (Customer_ID) WHERE Customer_ID = (SELECT Customer_ID FROM customers WHERE Customer_Name" +
                " = '" + customerIDComboBox.getSelectionModel().getSelectedItem() +
                        "') AND Start BETWEEN '" + start + "' AND '" + end + "' OR End BETWEEN '" + start + "' AND '" + end + "' AND Appointment_ID != " + AppointmentHolder.getAppointment().getAppointmentID();
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(customerScheduleQuery);

                if(startToUTC.getHour() < 12 || (endToUTC.getDayOfMonth() > endDateLocal.getDayOfMonth() || endToUTC.getMonthValue() > endDateLocal.getMonthValue()) && endToUTC.getHour() > 2){
                    errorLabel.setText(resourceBundle.getString("invalidtime"));
                }
                else if (rs.next()){
                    errorLabel.setText(resourceBundle.getString("conflictingappointment"));
                }
                else {

                    String insertAppointmentQuery = String.format("UPDATE appointments SET Title = '%s', Description = '%s', Location = '%s', Type = '%s', Start = '%s', End = '%s', Last_Update = CURRENT_TIMESTAMP," +
                                    "Customer_ID = '%s', User_ID = (SELECT User_ID FROM users WHERE User_Name = '%s'), " +
                                    "Contact_ID = (SELECT Contact_ID FROM contacts WHERE Contact_Name = '%s') WHERE Appointment_ID = %d", titleText.getText(), descriptionText.getText(), locationText.getText(), typeText.getText(), start, end,
                            customerIDComboBox.getSelectionModel().getSelectedItem(), User.getUsername(), contactComboBox.getSelectionModel().getSelectedItem(), AppointmentHolder.getAppointment().getAppointmentID());
                    statement.executeUpdate(insertAppointmentQuery);

                    Stage close = (Stage) updateAppointmentLabel.getScene().getWindow();
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
         * cancel.setOnAction lambda expression closes the current window and returns to the main form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */
        cancel.setOnAction(e -> {
            try {
                Stage close = (Stage) updateAppointmentLabel.getScene().getWindow();
                close.close();
                Parent root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
                Scene scene = new Scene(root);
                Stage mainMenu = new Stage();
                mainMenu.setScene(scene);
                mainMenu.show();
            } catch (IOException exception) {exception.printStackTrace();}
        });
    }
}
