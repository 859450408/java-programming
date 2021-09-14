package week2;

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
import week1.IOWithoutGui;
import week1.TextFileIO;

import java.io.File;
import java.time.LocalDateTime;

public class SimpleFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnClean = new Button("清除");
    private Button btnConnect = new Button("连接");
    //待发送信息的文本框
    private TextField tfIP = new TextField("127.0.0.1");
    private TextField tfPort = new TextField("8080");
    private TextField tfSend = new TextField();
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();
    private TCPClient tcpClient;
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        //内容显示区域

        HBox topHBox = new HBox();
        topHBox.setSpacing(20);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(25, 20, 10, 20));
        topHBox.getChildren().addAll(new Label("ip地址:"), tfIP,
                new Label("端口:"), tfPort, btnConnect);
        mainPane.setTop(topHBox);

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
        hBox.getChildren().addAll(btnSend, btnClean, btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnClean.setOnAction(event -> {taDisplay.clear();});

        btnExit.setOnAction(event -> {
            if(tcpClient != null){
                //向服务器发送关闭连接的约定信息
                tcpClient.send("bye");
                tcpClient.close();
            }


            System.exit(0);
        });

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

        btnConnect.setOnAction(event -> {
            String ip = tfIP.getText().trim();
            String port = tfPort.getText().trim();

            try {
                //tcpClient不是局部变量，是本程序定义的一个TCPClient类型的成员变量
                tcpClient = new TCPClient(ip,port);
                //成功连接服务器，接收服务器发来的第一条欢迎信息
                String firstMsg = tcpClient.receive();
                taDisplay.appendText(firstMsg + "\n");
            } catch (Exception e) {
                taDisplay.appendText("服务器连接失败！" + e.getMessage() + "\n");
            }
        });
    }


    private void send(){
        String msg = tfSend.getText();
        if (msg != null && !msg.equals("")){
            if (tcpClient != null){

                tcpClient.send(msg);
                taDisplay.appendText("客户端：" + msg + "\n");
                tfSend.clear();
                String receiveMsg = tcpClient.receive();//从服务器接收一行字符
                taDisplay.appendText(receiveMsg + "\n");
            }else {
                taDisplay.appendText("服务器未链接：" + msg + "\n");
                tfSend.clear();
            }
        } else {
            taDisplay.appendText("请输入内容\n");
        }
    }

    private void shiftSend() {
        String msg = tfSend.getText();
        if (msg != null && !msg.equals("")) {
            if (tcpClient != null) {

                tcpClient.send(msg);
                taDisplay.appendText("eco：" + msg + "\n");
                tfSend.clear();
                String receiveMsg = tcpClient.receive();//从服务器接收一行字符
                taDisplay.appendText(receiveMsg + "\n");
            } else {
                taDisplay.appendText("服务器未链接：" + msg + "\n");
                tfSend.clear();
            }
        } else {
        taDisplay.appendText("请输入内容\n");
        }
    }
}
