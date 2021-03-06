package com.vehicle.router.controller;

import com.vehicle.router.http.consumer.DeviceInfoServiceConsumer;
import com.vehicle.router.main.VehicleRouterApp;
import com.vehicle.router.model.DeviceInfo;
import com.vehicle.router.plotting.DelegateTask;
import com.vehicle.router.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.util.List;

public class OptionsController {

    @FXML
    private TextField serverIp;
    @FXML
    private ComboBox<String> algorithmType;

    @FXML
    public void initialize() {
        algorithmType.getItems().addAll("Sequential", "Parallel");
        algorithmType.getSelectionModel().select(VehicleRouterApp.algorithmType);
        serverIp.setText(VehicleRouterApp.serverIp);
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void cancel(ActionEvent actionEvent) throws IOException {
        serverIp.getScene().setRoot(FXMLLoader.load(getClass().getResource("/layout/RoutingWindow.fxml")));
    }

    @FXML
    public void save(ActionEvent actionEvent) throws IOException {
        VehicleRouterApp.algorithmType = algorithmType.getSelectionModel().getSelectedIndex();
        VehicleRouterApp.serverIp = serverIp.getText();

        serverIp.getScene().setRoot(FXMLLoader.load(getClass().getResource("/layout/RoutingWindow.fxml")));
    }

    @FXML
    public void testConnectionWithServer(ActionEvent actionEvent) {
        new Thread(new DelegateTask<>(() -> {
            testConnection();
            return true;
        })).start();
    }

    private void testConnection() {
        try {
            List<DeviceInfo> devices = DeviceInfoServiceConsumer.consumeDeviceInfoService(serverIp.getText());

            if (devices.size() > 0) {
                DeviceInfo d = devices.get(0);
                AlertUtil.displayAlertLater(Alert.AlertType.INFORMATION, "Success", "Server has a parallel device",
                        String.format("Name: %s, compute capability: %s, threads per block: %d, memory in MB: %d",
                                d.getDeviceName(), d.getComputeCapability(), d.getMaxThreadsPerBlock(), d.getGlobalMemoryMegabytes()));
            } else {
                AlertUtil.displayAlertLater(Alert.AlertType.WARNING, "No Parallel Devices", "Server does not have parallel devices");
            }

        } catch (ProcessingException e) {
            AlertUtil.displayExceptionAlertLater(e, "Failure to Process Request", "Failed to test connection with a server");
        }
    }
}
