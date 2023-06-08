package li.ijse.chat_application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginFormController {


    public TextField txtUserName;
    public Button btnLogin;

    public static ArrayList<String> nameList=new ArrayList<>();


    public void loginOnAction(ActionEvent actionEvent) {
        nameList.add(txtUserName.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/li/ijse/chat_application/view/chat_form.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Group Chat");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        txtUserName.clear();


    }

    public void userNameOnAction(ActionEvent actionEvent) {
        loginOnAction(actionEvent);
    }
}
