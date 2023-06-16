package hhs4a.project2.c42.utils.internet;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class InternetConnection {
    private static Thread internetCheckThread;
    private static InternetConnection instance;
    private static boolean internetAvailable = true;
    private static boolean alpacaAvailable = true;
    private boolean noInternetConnectionAlertShown = false;
    private boolean alpacaNotAvailableAlertShown = false;

    public static InternetConnection getInstance() {
        if (instance == null) instance = new InternetConnection();
        return instance;
    }

    public boolean isInternetAvailable() {
        return internetAvailable;
    }

    public boolean isAlpacaAvailableBool() {
        return alpacaAvailable;
    }

    private InternetConnection() {
    }

    // Checks if the device is connected to the internet by establishing a connection to Google's DNS server
    public boolean checkInternetConnection() {
        return isNetworkAvailable();
    }

    // Checks if the device is connected to the internet by establishing a connection to Google's DNS server
    private static boolean isNetworkAvailable() {
        try {
            int timeout = 20000; //setting a timeout of 20 seconds
            Socket sock = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(socketAddress, timeout);
            sock.close();

            internetAvailable = true;
            return true;
        } catch (IOException e) {
            internetAvailable = false;
            //if an exception occurs, it means that the device is not connected to the internet (or that googles DNS server is down, but this is unlikely)
            return false;
        }
    }

    //checking to see if alpaca is online
    private static boolean isAlpacaAvailable() {
        try {
            int timeout = 20000; // Setting a timeout of 20 seconds
            Socket sock = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("138.201.83.99", 3000);

            sock.connect(socketAddress, timeout);
            sock.close();

            alpacaAvailable = true;
            return true;
        } catch (IOException e) {
            // If an exception occurs, it means that the Alpaca server is offline
            alpacaAvailable = false;
            return false;
        }
    }

    //Background thread for checking internet
    public void internetCheck() {
        if (internetCheckThread != null && internetCheckThread.isAlive()) {
            return;
        }

        internetCheckThread = new Thread(this::runInternetCheck);
        internetCheckThread.setDaemon(true);
        internetCheckThread.start();
    }

    private void runInternetCheck() {
        while (true) {

            if (MenuOverlayScreenController.chatScreenController == null) {
                try {
                    //sleeping for 5 seconds before rerunning the loop
                    //noinspection BusyWait
                    Thread.sleep(5000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Checking internet");
            showNoInternetConnectionAlert();
            System.out.println("Checking alpaca availability");
            showAlpacaOfflineAlert();

            if (ConversationHistoryHolder.getInstance().getConversationHistory() != null) {

                Model modelName = getChatmodel();
                ConversationHistoryHolder.getInstance().getConversationHistory().getChat().setModel(modelName);

                try {
                    //checking the internet every 60 seconds
                    //noinspection BusyWait
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Model getChatmodel() {
        if (isNetworkAvailable() && isAlpacaAvailable() && LoggedInAccountHolder.getInstance().getAccount().getPermissions().getCanChatWithOnlineChatbot()) {
            return Model.ALPACA13B;
        } else {
            if (LoggedInAccountHolder.getInstance().getAccount().getPermissions().getCanChatWithOfflineChatbot()) {
                String language = MenuOverlayScreenController.chatScreenController.getLanguageSelectorComboBox().getValue();
                if (language.equalsIgnoreCase("English")) {
                    return Model.AIML_ENG;
                } else if (language.equalsIgnoreCase("Nederlands")) {
                    return Model.AIML_NL;
                }
            }
        }
        return null;
    }

    private void showAlpacaOfflineAlert() {
        if (!isAlpacaAvailable() && !alpacaNotAvailableAlertShown) {
            Platform.runLater(() -> {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Alpaca server is offline", "Er is momenteel geen verbinding beschikbaar met de Alpaca server. Het Alpaca-chatmodel is niet beschikbaar voor gebruik.");
                alpacaNotAvailableAlertShown = true;
            });
        }
    }

    private void showNoInternetConnectionAlert() {
        if (!isNetworkAvailable() && !noInternetConnectionAlertShown) {
            Platform.runLater(() -> {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Geen internetverbinding", "Er is momenteel geen internetverbinding beschikbaar. Sommige functionaliteiten zijn beperkt of niet beschikbaar.");
                noInternetConnectionAlertShown = true;
            });
        }
    }
}