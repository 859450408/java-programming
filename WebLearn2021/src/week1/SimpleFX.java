package week1;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;

public class SimpleFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnOpen = new Button("加载");
    private Button btnClean = new Button("清除");
    private Button btnSave = new Button("保存");
    //待发送信息的文本框
    private TextField tfSend = new TextField();
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();
    TextFileIO textFileIO = new TextFileIO();
    IOWithoutGui IOWithoutGui = new IOWithoutGui();
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        //内容显示区域
        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10, 20, 10, 20));
        taDisplay.setEditable(false);
        taDisplay.setWrapText(true);
        vBox.getChildren().addAll(new Label("信息显示区："),
                taDisplay, new Label("信息输入区："), tfSend);
        //设置显示信息区的文本区域可以纵向自动扩充范围
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend, btnSave, btnOpen, btnClean, btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnClean.setOnAction(event -> {taDisplay.clear();});

        btnExit.setOnAction(event -> {System.exit(0);});

        btnSend.setOnAction(event -> {
            send();
        });

        tfSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && event.isShiftDown()){
                    shiftSend();
                }else if (event.getCode() == KeyCode.ENTER){
                    send();
                }
            }
        });



        btnSave.setOnAction(event -> {
            //添加当前时间信息进行保存
//            textFileIO.append(
//                    LocalDateTime.now().withNano(0) +" "+ taDisplay.getText());
            String msg =  LocalDateTime.now().withNano(0) +" "+ taDisplay.getText();
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(null);
            if(file == null) //用户放弃操作则返回
                return;
            IOWithoutGui.appendTest(file, msg);
        });

        btnOpen.setOnAction(event -> {

//            String msg = textFileIO.load();
//            if(msg != null){
//                taDisplay.clear();
//                taDisplay.setText(msg);
//            }

            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if(file == null) //用户放弃操作则返回
                return;
            String msg = IOWithoutGui.loadTest(file);
            taDisplay.appendText(msg + "\n");

        });
    }


    public void send(){
        String msg = tfSend.getText();
        taDisplay.appendText(msg + "\n");
        tfSend.clear();
    }
    public void shiftSend(){
        String msg = tfSend.getText();
        taDisplay.appendText("echo: " + msg + "\n");
        tfSend.clear();
    }
}
