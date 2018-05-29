package com.vehicle.router.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VehicleRouterApp extends Application {

    public static int algorithmType = 0;
    public static String serverIp = "http://192.168.0.2:6060";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/layout/RoutingWindow.fxml"));

        primaryStage.setTitle("Vehicle Router UI");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
