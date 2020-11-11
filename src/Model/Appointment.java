package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Appointment model class.
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    public Appointment(int appointmentID, String title, String description, String location, String type, String start, String end, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID){
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return The appointment ID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The create date.
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @return The username of the user that created the appointment.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return The time of the last update.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @return The username of the user that last updated the appointment.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @return The customer ID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return The user ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return The contact ID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Method sets the hour and minute values for the start time.
     * @param startHour Start hour to be set.
     * @param startMinute Start minute to be set.
     */
    public void setStartTime(int startHour, int startMinute){

        if(startHour > 9) this.start = start.replace(start.substring(11, 13), Integer.toString(startHour));
        else this.start = start.replace(start.substring(11, 13), "0" + startHour);

        if(startMinute > 9) this.start = start.replace(start.substring(14, 16), Integer.toString(startMinute));
        else this.start = start.replace(start.substring(14, 16), "0" + startMinute);

        System.out.println(start);
    }

    /**
     * Method sets the hour and minute values for the end time.
     * @param endHour End hour to be set.
     * @param endMinute End minute to be set.
     */
    public void setEndTime(int endHour, int endMinute){

        if(endHour > 9) this.end = end.replace(end.substring(11, 13), Integer.toString(endHour));
        else this.end = end.replace(end.substring(11, 13), "0" + endHour);

        if(endMinute > 9) this.end = end.replace(end.substring(14, 16), Integer.toString(endMinute));
        else this.end = end.replace(end.substring(14, 16), "0" + endMinute);
    }


    /**
     * Method accepts a month and year and returns all appointments in the database that are scheduled to start in that month and year.
     * @param month The month to get appointments from.
     * @param year The year of the month to get appointments from.
     * @return A list of appointments that start in the month and year provided.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointmentsForMonth(int month, int year) throws SQLException {
        DBMSConnection connection = new DBMSConnection();
        ObservableList<Appointment> allAppointmentsOL = FXCollections.observableArrayList();
        ArrayList<Appointment> allAppointments = new ArrayList<>();
        String allAppointmentsQuery = String.format("SELECT * FROM appointments WHERE MONTH(Start) = %d AND YEAR(Start) = %d", month, year);
        PreparedStatement statement = connection.getConnection().prepareStatement(allAppointmentsQuery);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Appointment newAppointment = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                     rs.getString("Start"), rs.getString("End"), rs.getString("Create_Date"), rs.getString("Created_By"), rs.getString("Last_Update"), rs.getString("Last_Updated_By"),
                     rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getInt("Contact_ID"));
            allAppointments.add(newAppointment);
        }
        allAppointmentsOL.setAll(allAppointments);
        return allAppointmentsOL;
    }

    /**
     * Method accepts a week and year and returns all appointments in the database that are scheduled to start in that week and year.
     * @param week The week to get appointments from.
     * @param year The year of the week to get appointments from.
     * @return A list of appointments that start in the week and year provided.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointmentsForWeek(int week, int year) throws SQLException {
        DBMSConnection connection = new DBMSConnection();
        ObservableList<Appointment> allAppointmentsOL = FXCollections.observableArrayList();
        ArrayList<Appointment> allAppointments = new ArrayList<>();
        String allAppointmentsQuery = String.format("SELECT * FROM appointments WHERE WEEK(Start) = %d AND YEAR(Start) = %d", week, year);
        PreparedStatement statement = connection.getConnection().prepareStatement(allAppointmentsQuery);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Appointment newAppointment = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                    rs.getString("Start"), rs.getString("End"), rs.getString("Create_Date"), rs.getString("Created_By"), rs.getString("Last_Update"), rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getInt("Contact_ID"));
            allAppointments.add(newAppointment);
        }
        allAppointmentsOL.setAll(allAppointments);
        return allAppointmentsOL;
    }

    /**
     * @return The start year.
     */
    public String getStartYear(){
        return start.substring(0, 4);
    }

    /**
     * @return The start month.
     */
    public String getStartMonth(){
        return start.substring(5, 7);
    }

    /**
     * @return The start day.
     */
    public String getStartDay(){
        return start.substring(8, 10);
    }

    /**
     * @return The start hour.
     */
    public String getStartHour(){
        return start.substring(11, 13);
    }

    /**
     * @return The start minute.
     */
    public String getStartMinute() {
        return start.substring(14, 16);
    }

    /**
     * @return The end year.
     */
    public String getEndYear(){
        return end.substring(0, 4);
    }

    /**
     * @return The end month.
     */
    public String getEndMonth(){
        return end.substring(5, 7);
    }

    /**
     * @return The end day.
     */
    public String getEndDay(){
        return end.substring(8, 10);
    }

    /**
     * @return The end hour.
     */
    public String getEndHour(){
        return end.substring(11, 13);
    }

    /**
     * @return The end minute.
     */
    public String getEndMinute(){
        return end.substring(14, 16);
    }
}
