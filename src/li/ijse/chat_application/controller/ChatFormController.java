package li.ijse.chat_application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import li.ijse.chat_application.service.client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ChatFormController {

    public ScrollPane sp_main;

    public TextField txt_message;
    public JFXButton btn_sent;
    public JFXButton btn_sent_V;
    public TextField txtmessage;
    public VBox message;
    public Button btnSent;
    public JFXTextArea txtmessagearea;
    public TextArea txtarea;
    private client client;
    private String clientUserName;
    private final FileChooser fileChooser=new FileChooser();
    private File file;
    private Image image;
    public ImageView imgViewer;

    public void initialize(){
        this.clientUserName=LoginFormController.nameList.get(LoginFormController.nameList.size()-1);

        try {
            System.out.println("name"+" "+LoginFormController.nameList.get(LoginFormController.nameList.size()-1));

            this.client=new client(new Socket("localhost",4000),clientUserName);
            System.out.println("New Client connected to group!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

      /*  message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double)newValue);
            }
        });*/
        client.receiveMessage(message);

    }

    public void setClientUserName(String clientUserName){
        this.clientUserName=clientUserName;
    }


    public static void receiveMessage(String receivedMessage,VBox vbox){
        HBox hBox=new HBox();
        hBox.setPadding(new Insets(5,5,5,10));
        Text text=new Text(receivedMessage);
        TextFlow textFlow=new TextFlow(text);

        if(receivedMessage.startsWith("*")){
            hBox.setAlignment(Pos.CENTER);
            textFlow.setStyle("-fx-background-color: rgb(243,172,157);" +
                    "-fx-background-radius: 20px;");
        }else {
            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                    "-fx-background-radius: 20px;");

        }

        textFlow.setPadding(new Insets(5,10,5,10));
        //text.setFill(Color.color(0.934,0.945,0.996));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }


    public void sentMessageOnAction(ActionEvent actionEvent) {

        String messageToSent=txtmessage.getText();

        if(!messageToSent.isEmpty()){

            HBox hBox=new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text=new Text(messageToSent);
            TextFlow textFlow=new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(0,0,0);"+
                    "-fx-background-color: rgb(144,238,144);" +
                    "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            message.getChildren().add(hBox);

            client.sentMessage(clientUserName+": "+messageToSent);
            txtmessage.clear();
        }
    }



    public void txtMassageOnAction(TouchEvent actionEvent) {

    }

    public void SendOnAction(ActionEvent actionEvent) {
        sentMessageOnAction(actionEvent);
    }

    public void chooseFileOnAction(ActionEvent actionEvent) {
        fileChooser.setTitle("Group Chat File Chooser");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        Stage window = (Stage) btnSent.getScene().getWindow();
        file=fileChooser.showOpenDialog(window);

        if(file!=null){
            image=new Image(file.toURI().toString());
            imgViewer.setImage(image);

            btnSent.setDisable(false);
            //btn_Cancel.setVisible(true);
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Please select image").show();
        }

    }
}
