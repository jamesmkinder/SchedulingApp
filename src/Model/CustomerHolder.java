package Model;

/**
 *CustomerHolder class is used as a container to pass Customer objects between windows easily.
 */
public class CustomerHolder {

    private static Customer customer;

    public CustomerHolder(Customer customer){
        this.customer = customer;
    }


    /**
     * @return The Customer to be passed.
     */
    public static Customer getCustomer(){
        return customer;
    }
}
