package Controller;

import Model.DBMSConnection;
import Model.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *LoginController class is a controller class for the login form.
 * Lambda expressions are used to handle button and selection change action events throughout the class because it
 * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
 * Note that lambda expressions are anonymous methods and therefore can not have dedicated javadoc comments, so their justification
 * is mentioned in their parent class here.
 */
public class LoginController implements Initializable {

    public TextField username;
    public PasswordField password;
    public Button login;
    public Label errorMessage;
    public AnchorPane ap;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label location;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        DBMSConnection connection = new DBMSConnection();
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        location.setText(Locale.getDefault().getDisplayCountry());
        login.setText(resourceBundle.getString("signin"));

        Logger log = Logger.getLogger("login_activity.txt");
        FileHandler file = null;
        try {
            file = new FileHandler("login_activity.txt", true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SimpleFormatter sf = new SimpleFormatter();
        file.setFormatter(sf);
        log.addHandler(file);
        log.setLevel(Level.INFO);

        /**
         *login.setOnAction lambda expression checks the entered username and password against the database to find a valid match, and opens the main form if there is a match.
         * Lambda expressions are used to handle button and selection change action events throughout the software because it
         * is in the developer's opinion that this makes the program easier to read and understand what the application is trying to do.
         */

        login.setOnAction(e ->{
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String usernameText = username.getText();
            String passwordText = password.getText();
            String query = "SELECT User_Name, Password FROM users";
            Boolean pass = false;
            try (Statement statement = connection.getConnection().createStatement()){
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    String testUserName = rs.getString("User_Name");
                    String testPassword = rs.getString("Password");
                    if (testUserName.equals(usernameText)){
                        if (testPassword.equals(passwordText)){
                            User user = new User(testUserName);
                            pass = true;
                            Stage close = (Stage) login.getScene().getWindow();
                            close.close();
                            Parent root = FXMLLoader.load(getClass().getResource("../View/MainForm.fxml"));
                            Scene scene = new Scene(root);
                            Stage mainMenu = new Stage();
                            mainMenu.setScene(scene);
                            mainMenu.show();
                        }
                    }
                }
            }catch(SQLException | IOException exception){
                errorMessage.setText(resourceBundle.getString("connectionerror"));
            }
            if (pass) log.info(String.format("\nLogin attempted on %s: \nUsername entered: %s\nResult: Success", time, username.getText()));
            else log.info(String.format("\nLogin attempted on %s: \nUsername entered: %s\nResult: Failure", time, username.getText()));
            errorMessage.setText(resourceBundle.getString("error"));

        });



    }
}
