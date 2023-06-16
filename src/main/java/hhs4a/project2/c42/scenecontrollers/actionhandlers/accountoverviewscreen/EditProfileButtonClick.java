package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountoverviewscreen;

import hhs4a.project2.c42.scenecontrollers.AccountOverviewScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;

public class EditProfileButtonClick implements Actionhandler {

    private AccountOverviewScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountOverviewScreenController)) return;
        controller = (AccountOverviewScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Stage imageSelectionStage = controller.createImageSelectionStage();
        // Create a flow pane to display the images
        FlowPane flowPane = controller.createFlowPane();

        // Load images from the resources folder
        File[] imageFiles = controller.loadImagesFromResources();
        controller.displayImagesInFlowPane(imageFiles, flowPane, imageSelectionStage);

        // Create a scroll pane to contain the flow pane
        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Set the scroll pane as the root of the image selection stage
        Scene scene = new Scene(scrollPane, 500, 400);
        imageSelectionStage.setScene(scene);

        // Set the resizability of the window
        imageSelectionStage.setResizable(true);
        imageSelectionStage.show();
    }
}