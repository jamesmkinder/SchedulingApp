package Model;

/**
 * AppointmentHolder class is used as a container to pass Appointments between windows easily.
 */
public class AppointmentHolder {

    private static Appointment appointment;

    public AppointmentHolder(Appointment appointment){
        this.appointment = appointment;
    }

    /**
     * @return The appointment to be passed.
     */
    public static Appointment getAppointment(){
        return appointment;
    }
}
