package Controller;

import Model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

/**
 * MainFormController is a controller class for the main form of the application.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class MainFormController implements Initializable {


    @FXML public Button exit;
    @FXML public TableView<Customer> allCustomersList;
    @FXML public TableColumn<Customer, String> allCustomersName;
    @FXML public TableColumn<Customer, String> allCustomersAddress;
    @FXML public TableColumn<Customer, String> allCustomersPostalCode;
    @FXML public TableColumn<Customer, String> allCustomersPhone;
    @FXML public TableColumn<Customer, String> allCustomersCreateDate;
    @FXML public TableColumn<Customer, String> allCustomersCreatedBy;
    @FXML public TableColumn<Customer, String> allCustomersLastUpdate;
    @FXML public TableColumn<Customer, String> allCustomersLastUpdatedBy;
    @FXML public TableColumn<Customer, String> allCustomersDivision;
    @FXML public TableColumn<Customer,String> allCustomersCustomerIDColumn;
    @FXML public TableView<Appointment> customerAppointments;
    @FXML public TableColumn<Appointment, Number> appointmentIDColumn;
    @FXML public TableColumn<Appointment, String> titleColumn;
    @FXML public TableColumn<Appointment, String> descriptionColumn;
    @FXML public TableColumn<Appointment, String> locationColumn;
    @FXML public TableColumn<Appointment, String> typeColumn;
    @FXML public TableColumn<Appointment, String> startColumn;
    @FXML public TableColumn<Appointment, String> endColumn;
    @FXML public TableColumn<Appointment, String> createDateColumn;
    @FXML public TableColumn<Appointment, String> createdByColumn;
    @FXML public TableColumn<Appointment, String> lastUpdateColumn;
    @FXML public TableColumn<Appointment, String> lastUpdatedByColumn;
    @FXML public TableColumn<Appointment, String> customerIDColumn;
    @FXML public TableColumn<Appointment, String> userColumn;
    @FXML public TableColumn<Appointment, String> contactColumn;
    @FXML public Button newCustomer;
    @FXML public Button editCustomer;
    @FXML public Button deleteCustomer;
    @FXML public Button newAppointment;
    @FXML public Button editAppointment;
    @FXML public Button deleteAppointment;
    @FXML public TextArea userInterfaceMessages;
    @FXML public RadioButton monthRadio;
    @FXML public RadioButton weekRadio;
    @FXML public ComboBox<String> monthWeekComboBox;
    @FXML public Label yearLabel;
    @FXML public Button previousYear;
    @FXML public Button nextYear;
    @FXML public Button customerReport;
    @FXML public Button contactReport;
    @FXML public Button userReport;
    @FXML public Label customerLabel;
    @FXML public Label appointmentLabel;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        DBMSConnection connection = new DBMSConnection();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        LocalDateTime localDateTime = LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        ObservableList<String> monthsList = FXCollections.observableArrayList();
        ObservableList<String> weeksList = FXCollections.observableArrayList();
        ToggleGroup monthOrWeek = new ToggleGroup();
        monthRadio.setToggleGroup(monthOrWeek);
        weekRadio.setToggleGroup(monthOrWeek);
        yearLabel.setText(Integer.toString(calendar.get(Calendar.YEAR)));

        customerLabel.setText(resourceBundle.getString("allcustomers"));
        allCustomersName.setText(resourceBundle.getString("name"));
        allCustomersAddress.setText(resourceBundle.getString("address"));
        allCustomersPostalCode.setText(resourceBundle.getString("zip"));
        allCustomersPhone.setText(resourceBundle.getString("phone"));
        allCustomersCreateDate.setText(resourceBundle.getString("createdate"));
        allCustomersCreatedBy.setText(resourceBundle.getString("createdby"));
        allCustomersLastUpdate.setText(resourceBundle.getString("lastupdate"));
        allCustomersLastUpdatedBy.setText(resourceBundle.getString("lastupdatedby"));
        allCustomersDivision.setText(resourceBundle.getString("division"));
        newCustomer.setText(resourceBundle.getString("newcustomer"));
        editCustomer.setText(resourceBundle.getString("modifycustomer"));
        deleteCustomer.setText(resourceBundle.getString("deletecustomer"));
        customerReport.setText(resourceBundle.getString("customerreport"));
        contactReport.setText(resourceBundle.getString("contactreport"));
        userReport.setText(resourceBundle.getString("userreportbutton"));
        appointmentLabel.setText(resourceBundle.getString("appointments"));
        monthRadio.setText(resourceBundle.getString("viewbymonth"));
        weekRadio.setText(resourceBundle.getString("viewbyweek"));
        previousYear.setText(resourceBundle.getString("previous"));
        nextYear.setText(resourceBundle.getString("next"));
        appointmentIDColumn.setText(resourceBundle.getString("appointmentid"));
        titleColumn.setText(resourceBundle.getString("title"));
        locationColumn.setText(resourceBundle.getString("locale"));
        startColumn.setText(resourceBundle.getString("start"));
        endColumn.setText(resourceBundle.getString("end"));
        createDateColumn.setText(resourceBundle.getString("createdate"));
        createdByColumn.setText(resourceBundle.getString("createdby"));
        lastUpdateColumn.setText(resourceBundle.getString("lastupdate"));
        lastUpdatedByColumn.setText(resourceBundle.getString("lastupdatedby"));
        customerIDColumn.setText(resourceBundle.getString("customerid"));
        userColumn.setText(resourceBundle.getString("user"));
        newAppointment.setText(resourceBundle.getString("newappointment"));
        editAppointment.setText(resourceBundle.getString("modifyappointment"));
        deleteAppointment.setText(resourceBundle.getString("deleteappointment"));
        exit.setText(resourceBundle.getString("exit"));
        allCustomersCustomerIDColumn.setText(resourceBundle.getString("customerid"));



        String userAppointmentsQuery = "SELECT Appointment_ID, Start FROM appointments JOIN users USING (User_ID) WHERE User_Name = '" + User.getUsername() +
                "' AND TIMEDIFF(TIME(Start), TIME(NOW())) BETWEEN '00:00' AND '00:15' AND DATE(Start) = DATE(NOW())";

        try {
            Statement statement = connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(userAppointmentsQuery);

            if (rs.next()){
                String start = rs.getString("Start");
                LocalDateTime utc = LocalDateTime.of(Integer.parseInt(start.substring(0, 4)), Integer.parseInt(start.substring(5, 7)), Integer.parseInt(start.substring(8, 10)),
                        Integer.parseInt(start.substring(11, 13)), Integer.parseInt(start.substring(14, 16)));
                ZonedDateTime zdt = ZonedDateTime.of(utc, ZoneOffset.UTC);
                ZonedDateTime local = zdt.withZoneSameInstant(ZoneId.systemDefault());
                local.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a"));
                userInterfaceMessages.setText(resourceBundle.getString("appointmentcoming") + "\nID: " + rs.getInt("Appointment_ID") + "\n" + resourceBundle.getString("start") +
                        ": " + local.toString());
            }
            else {
                userInterfaceMessages.setText(resourceBundle.getString("noappointmentcoming"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        //Sets up the customer list by binding each Customer object value to its respective column's cell value factory.
        try {
            allCustomersList.getItems().setAll(Customer.getAllCustomers());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        allCustomersName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getCustomerName());
            }
        });

        allCustomersCustomerIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(Integer.toString(p.getValue().getCustomerID()));
            }
        });

        allCustomersAddress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getAddress());
            }
        });
        allCustomersPostalCode.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getPostalCode());
            }
        });
        allCustomersPhone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getPhone());
            }
        });
        allCustomersCreateDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getCreateDate());
            }
        });
        allCustomersCreatedBy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getCreatedBy());
            }
        });
        allCustomersLastUpdate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getLastUpdate());
            }
        });
        allCustomersLastUpdatedBy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getLastUpdatedBy());
            }
        });
        allCustomersDivision.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                String getDivisionQuery = String.format("SELECT Division FROM first_level_divisions WHERE Division_ID = '%s'", p.getValue().getDivisionID());
                try {
                    PreparedStatement getDivision = connection.getConnection().prepareStatement(getDivisionQuery);
                    ResultSet rs = getDivision.executeQuery();
                    rs.next();
                    return new SimpleStringProperty(rs.getString("Division"));
                } catch (SQLException e) {e.printStackTrace();}
                return null;
            }
        });


        //Sets up the appointment list by month by binding each Appointment object value to its respective column's cell value factory.

        try {
            customerAppointments.getItems().setAll(Appointment.getAllAppointmentsForMonth(localDateTime.getMonthValue(), localDateTime.getYear()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        ArrayList<String> months = new ArrayList<>();
        int i;

        for (i = 0; i < 12; i++){
            months.add(resourceBundle.getString(Integer.toString(i + 1)));
        }

        

        monthsList.setAll(months);
        monthWeekComboBox.setItems(monthsList);

        /**
         * monthRadio.setOnAction lambda expression will cause the appointment table view to be filtered by month. It also populates the monthWeekComboBox with the names of the months for filtering.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        monthRadio.setOnAction(e ->{
            try {
                customerAppointments.getItems().setAll(Appointment.getAllAppointmentsForMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
                months.clear();
                int j;
                for (j = 0; j < 12; j++){
                    months.add(resourceBundle.getString(Integer.toString(j + 1)));
                }
                monthsList.setAll(months);
                monthWeekComboBox.setItems(monthsList);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        /**
         * weekRadio.setOnAction lambda expression will cause the appointment table view to be filtered by week. It also populates the monthWeekComboBox with the week numbers for filtering.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */
        weekRadio.setOnAction(e ->{
            try {
                customerAppointments.getItems().setAll(Appointment.getAllAppointmentsForWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR)));
                ArrayList<String> weeks = new ArrayList<>();
                int j;
                for (j = 0; j < 52; j++){
                    weeks.add(resourceBundle.getString("week") + " " + Integer.toString(j + 1));
                }
                weeksList.setAll(weeks);
                monthWeekComboBox.setItems(weeksList);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        /**
         * monthWeekComboBox.setOnAction lambda expression will refresh the appointment table view to filter for the month or week that was selected.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        monthWeekComboBox.setOnAction(e ->{
            if(monthRadio.isSelected()){
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForMonth(monthWeekComboBox.getSelectionModel().getSelectedIndex() + 1, Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            else {
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForWeek(monthWeekComboBox.getSelectionModel().getSelectedIndex(), Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        /**
         * previousYear.setOnAction lambda expression decrements the year label and filter.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        previousYear.setOnAction(e -> {
            yearLabel.setText(Integer.toString(Integer.parseInt(yearLabel.getText()) - 1));
            if(monthRadio.isSelected()){
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForMonth(monthWeekComboBox.getSelectionModel().getSelectedIndex() + 1, Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            else {
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForWeek(monthWeekComboBox.getSelectionModel().getSelectedIndex(), Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        /**
         * nextYear.setOnAction lambda expression increments the year label and filter.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        nextYear.setOnAction(e -> {
            yearLabel.setText(Integer.toString(Integer.parseInt(yearLabel.getText()) + 1));
            if(monthRadio.isSelected()){
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForMonth(monthWeekComboBox.getSelectionModel().getSelectedIndex() + 1, Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            else {
                try {
                    customerAppointments.setItems(Appointment.getAllAppointmentsForWeek(monthWeekComboBox.getSelectionModel().getSelectedIndex(), Integer.parseInt(yearLabel.getText())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });


        appointmentIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, Number>, ObservableValue<Number>>() {
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Appointment, Number> p) {
                return new SimpleIntegerProperty(p.getValue().getAppointmentID());
            }
        });
        titleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getTitle());
            }
        });
        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getDescription());
            }
        });
        locationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getLocation());
            }
        });
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getType());
            }
        });
        startColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                LocalDateTime utc = LocalDateTime.of(Integer.parseInt(p.getValue().getStartYear()), Integer.parseInt(p.getValue().getStartMonth()), Integer.parseInt(p.getValue().getStartDay()),
                        Integer.parseInt(p.getValue().getStartHour()), Integer.parseInt(p.getValue().getStartMinute()));
                ZonedDateTime zdt = ZonedDateTime.of(utc, ZoneOffset.UTC);
                ZonedDateTime local = zdt.withZoneSameInstant(ZoneId.systemDefault());
                local.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a"));
                return new SimpleStringProperty(local.getYear() + "-" + local.getMonthValue() + "-" + local.getDayOfMonth() + " " + local.getHour() + ":" + p.getValue().getStartMinute());
            }
        });
        endColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                LocalDateTime utc = LocalDateTime.of(Integer.parseInt(p.getValue().getEndYear()), Integer.parseInt(p.getValue().getEndMonth()), Integer.parseInt(p.getValue().getEndDay()),
                        Integer.parseInt(p.getValue().getEndHour()), Integer.parseInt(p.getValue().getEndMinute()));
                ZonedDateTime zdt = ZonedDateTime.of(utc, ZoneOffset.UTC);
                ZonedDateTime local = zdt.withZoneSameInstant(ZoneId.systemDefault());
                local.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a"));
                return new SimpleStringProperty(local.getYear() + "-" + local.getMonthValue() + "-" + local.getDayOfMonth() + " " + local.getHour() + ":" + p.getValue().getEndMinute());
            }
        });
        createDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getCreateDate());
            }
        });
        createdByColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getCreatedBy());
            }
        });
        lastUpdateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getLastUpdate());
            }
        });
        lastUpdatedByColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(p.getValue().getLastUpdatedBy());
            }
        });
        customerIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                return new SimpleStringProperty(Integer.toString(p.getValue().getCustomerID()));
            }
        });
        userColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                String getUserQuery = String.format("SELECT User_Name FROM users WHERE User_ID = %d", p.getValue().getUserID());
                try {
                    PreparedStatement getUser = connection.getConnection().prepareStatement(getUserQuery);
                    ResultSet rs = getUser.executeQuery();
                    rs.next();
                    return new SimpleStringProperty(rs.getString("User_Name"));
                } catch (SQLException e) {e.printStackTrace();}
                return null;
            }
        });
        contactColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                String getContactQuery = String.format("SELECT Contact_Name FROM contacts WHERE Contact_ID = %d", p.getValue().getContactID());
                try {
                    PreparedStatement getContact = connection.getConnection().prepareStatement(getContactQuery);
                    ResultSet rs = getContact.executeQuery();
                    rs.next();
                    return new SimpleStringProperty(rs.getString("Contact_Name"));
                } catch (SQLException e) {e.printStackTrace();}
                return null;
            }
        });

        /**
         * newCustomer.setOnAction lambda expression opens the new customer form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        newCustomer.setOnAction(e ->{
            try {
                Stage close = (Stage) exit.getScene().getWindow();
                close.close();
                Parent root = FXMLLoader.load(getClass().getResource("../View/AddCustomerForm.fxml"));
                Stage newCustomerForm = new Stage();
                Scene scene = new Scene(root);
                newCustomerForm.setScene(scene);
                newCustomerForm.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        /**
         * editCustomer.setOnAction lambda expression opens the modify customer form and passes an instance of the selected customer using the CustomerHolder class.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        editCustomer.setOnAction(e ->{
            boolean selection = true;
            Customer customerToModify = (Customer) allCustomersList.getSelectionModel().getSelectedItem();
            CustomerHolder passCustomer = new CustomerHolder(customerToModify);
            try {
                CustomerHolder.getCustomer().getCustomerID();
            } catch (NullPointerException exception) {userInterfaceMessages.setText(resourceBundle.getString("noselection")); selection = false;}
            if (selection) {
                try {
                    Stage close = (Stage) exit.getScene().getWindow();
                    close.close();
                    Stage modifyCustomerForm = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../View/ModifyCustomerForm.fxml"));
                    Scene scene = new Scene(root);
                    modifyCustomerForm.setScene(scene);
                    modifyCustomerForm.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        /**
         * deleteCustomer.setOnAction lambda expression checks that the selected customer has no appointments associated, and if not, deletes the customer.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        deleteCustomer.setOnAction(e ->{
            Customer customerToDelete = (Customer) allCustomersList.getSelectionModel().getSelectedItem();
            String query = "SELECT * FROM appointments WHERE Customer_ID = " + customerToDelete.getCustomerID();
            try {
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()){
                    userInterfaceMessages.setText(resourceBundle.getString("cannotdelete"));
                }
                else {
                    String deleteQuery = "DELETE FROM customers WHERE Customer_ID = " + customerToDelete.getCustomerID();
                    statement.executeUpdate(deleteQuery);
                    userInterfaceMessages.setText(resourceBundle.getString("customerdeleted"));
                }
                allCustomersList.getItems().setAll(Customer.getAllCustomers());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        /**
         * newAppointment.setOnAction lambda expression closes the window and opens the new appointment form.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        newAppointment.setOnAction(e ->{
            try {
               Parent root = FXMLLoader.load(getClass().getResource("../View/AddAppointmentForm.fxml"));
                Stage close = (Stage) exit.getScene().getWindow();
                close.close();
                Scene scene = new Scene(root);
                Stage mainMenu = new Stage();
                mainMenu.setScene(scene);
                mainMenu.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        /**
         *editAppointment.setOnAction lambda expression opens the modify appointment form and passes an instance of the selected appointment using the AppointmentHolder class.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        editAppointment.setOnAction(e ->{
            boolean selection = true;
            try {
                    Appointment appointmentToModify = (Appointment) customerAppointments.getSelectionModel().getSelectedItem();
                    AppointmentHolder passAppointment = new AppointmentHolder(appointmentToModify);
                    try {
                        AppointmentHolder.getAppointment().getAppointmentID();
                    } catch (NullPointerException exception) {userInterfaceMessages.setText(resourceBundle.getString("noselection")); selection = false;}
                    if(selection) {
                        Stage close = (Stage) exit.getScene().getWindow();
                        close.close();
                        Stage modifyCustomerForm = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("../View/ModifyAppointmentForm.fxml"));
                        Scene scene = new Scene(root);
                        modifyCustomerForm.setScene(scene);
                        modifyCustomerForm.show();
                    }
            } catch (IOException ioException) {ioException.printStackTrace();}
        });

        /**
         *deleteAppointment.setOnAction lambda expression deletes the selected appointment.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        deleteAppointment.setOnAction(e -> {
            Appointment appointmentToDelete = (Appointment) customerAppointments.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentToDelete.getAppointmentID();
            try {
                    Statement statement = connection.getConnection().createStatement();
                    statement.executeUpdate(deleteQuery);
                    customerAppointments.getItems().setAll(Appointment.getAllAppointmentsForMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
                    userInterfaceMessages.setText(resourceBundle.getString("appointmentdeleted"));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            userInterfaceMessages.setText(resourceBundle.getString("appointmentdeleted") + " " + appointmentToDelete.getAppointmentID() + " type: " + appointmentToDelete.getType());
        });

        /**
         * customerReport.setOnAction lambda expression produces a report of the total number of customer appointments by type and month and displays it in the user interface message dialogue box.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        customerReport.setOnAction(e ->{
            String customerReportQuery = "SELECT MONTH(Start), COUNT(*) FROM appointments GROUP BY MONTH(Start)";
            String customerReportTypeQuery = "SELECT Type, COUNT(*) FROM appointments GROUP BY Type";
            ArrayList<String> monthsInReport = new ArrayList<>();
            try {
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(customerReportQuery);
                while(rs.next()){
                    monthsInReport.add(resourceBundle.getString(rs.getString("MONTH(Start)")) + ": " + rs.getString("COUNT(*)") + "\n");
                }
                int j;
                userInterfaceMessages.setText(resourceBundle.getString("appointmentreport") + "\n");
                for (j = 0; j < 12; j++){
                    try {
                        userInterfaceMessages.setText(userInterfaceMessages.getText() + monthsInReport.get(j));
                    } catch (IndexOutOfBoundsException exception) {break;}
                }
                ResultSet resultSet = statement.executeQuery(customerReportTypeQuery);
                ArrayList<String> typesInReport = new ArrayList<>();
                while (resultSet.next()){
                    typesInReport.add(resultSet.getString("Type") + ": " + resultSet.getString("COUNT(*)") + "\n");
                }
                j = 0;
                do{
                    try {
                        userInterfaceMessages.setText(userInterfaceMessages.getText() + typesInReport.get(j));
                    } catch (IndexOutOfBoundsException exception) {break;}
                  j++;
                } while (true);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        /**
         *contactReport.setOnAction lambda expression produces a schedule for each contact in the organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID
         * and puts the results in the user interface messages dialogue box.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        contactReport.setOnAction(e ->{
            int g = 1;
            userInterfaceMessages.setText("");
            boolean appending = true;
            do {
                try {
                    String contactScheduleQuery = "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID, Contact_Name FROM appointments JOIN contacts USING (Contact_ID) WHERE Contact_ID = " + g;
                    Statement statement = connection.getConnection().createStatement();
                    ResultSet rs = statement.executeQuery(contactScheduleQuery);
                    ArrayList<String> contactReportList = new ArrayList<>();
                    if (!rs.next()) {
                        appending = false;
                        break;
                    }
                    userInterfaceMessages.setText(userInterfaceMessages.getText() + rs.getString("Contact_Name") + ":");
                    do {
                        contactReportList.add("\n" + resourceBundle.getString("appointmentid") + ": " + rs.getString("Appointment_ID") + "\n" + resourceBundle.getString("title") + ": " + rs.getString("Title") +
                                "\n" + "Description: " + rs.getString("Description") + "\n" + resourceBundle.getString("start") + ": " + rs.getString("Start") + "\n" + resourceBundle.getString("end") +
                                ": " + rs.getString("End") + "\n" + resourceBundle.getString("customerid") + ": " + rs.getString("Customer_ID") + "\n");
                    } while (rs.next());
                    int j = 0;
                    do {
                        try {
                            userInterfaceMessages.setText(userInterfaceMessages.getText() + contactReportList.get(j));
                        } catch (IndexOutOfBoundsException exception) {break;}
                        j++;
                    } while (appending);
                    userInterfaceMessages.setText(userInterfaceMessages.getText() + "\n");
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                g++;
            } while (appending);
        });

        /**
         *userReport.setOnAction lambda expression produces a report of every appointment that was created by the user and puts the results in the user interface messages dialogue box.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        userReport.setOnAction(e ->{
            userInterfaceMessages.setText(resourceBundle.getString("userreport") + "\n");
            String userReportQuery = "SELECT Appointment_ID, Title FROM appointments JOIN users USING (User_ID) WHERE User_Name = '" + User.getUsername() + "'";
            try {
                Statement statement = connection.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(userReportQuery);
                ArrayList<String> userAppointments = new ArrayList<>();
                while (rs.next()) {
                    userAppointments.add(resourceBundle.getString("appointmentid") + ": " + rs.getString("Appointment_ID") + "\n" + resourceBundle.getString("title") + ": " + rs.getString("Title") +
                            "\n");
                }
                int j;
                for (j = 0; ; j++) {
                    try {
                        userInterfaceMessages.setText(userInterfaceMessages.getText() + userAppointments.get(j));
                    } catch (IndexOutOfBoundsException exception) {
                        break;
                    }
                }
            } catch (SQLException exception) {exception.printStackTrace();}
        });

        /**
         *exit.setOnAction lambda expression exits the application.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        exit.setOnAction(e -> {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close(); });
    }
}

