import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {

    public static BorderPane borderPane;
    public static MenuBar menuBar;
    public static DrawingPane drawingPane;
    public static GridPane toolPane;
    public static VBox curveEditingPane;
    public static String currentlySelectedTool = "drawing";
    public static Color currentColor = Color.BLACK;
    public static String currentLineType = "continuous";
    public static ColorPicker colorPicker;
    public static HBox lines;
    public static HBox styles;
    public static int currentLineThickness = 3;

    public static MenuItem newDrawing;
    public static MenuItem loadDrawing;
    public static MenuItem saveDrawing;
    public static MenuItem quitDrawing;

    public static Button select;
    public static Button edit;
    public static Button point;
    public static Button delete;

    public static boolean saved = true;

    public static void setBorderSizes() {
        borderPane.setMaxSize(1600,800);
        borderPane.setMinSize(640,480);
        borderPane.setPrefSize(1080,720);
    }

    public static void newDrawing() {
        if (!saved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New file confirmation");
            alert.setContentText("Your current file isn't saved. Would you like to save?");

            ButtonType buttonTypeOne = new ButtonType("Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
                if (selectedFile != null) {
                    System.out.println(selectedFile.getName());
                    saveDrawing(selectedFile);
                }
            } else {
                alert.close();
            }
        }
        saved =  true;
        drawingPane.hardReset();
    }

    public static void loadDrawing(File file) {
        drawingPane.hardReset();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                drawingPane.processLoadedCurve(line);
            }
            reader.close();
            currentlySelectedTool = "edit";
            enableTools();
            point.setDisable(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveDrawing(File file) {
        System.out.println("Save drawing");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            drawingPane.reset();
            for (int i = 0; i < DrawingPane.listOfCurves.size(); i++) {
                for (int j = 0; j < DrawingPane.listOfCurves.get(i).curveSegments.size(); j++) {
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).curveSegments.get(j).getStartX()));
                    br.write(" ");
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).curveSegments.get(j).getStartY()));
                    br.write(" ");
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).curveSegments.get(j).getEndX()));
                    br.write(" ");
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).curveSegments.get(j).getEndY()));
                    if (j ==  DrawingPane.listOfCurves.get(i).curveSegments.size()-1) {
                        break;
                    }
                    br.write(" ");
                }
                br.write("$");
                for (int j = 0; j < DrawingPane.listOfCurves.get(i).controlPoints.size(); j++) {
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).controlPoints.get(j).getCenterX()));
                    br.write(" ");
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).controlPoints.get(j).getCenterY()));
                    if (j ==  DrawingPane.listOfCurves.get(i).controlPoints.size()-1) {
                        break;
                    }
                    br.write(" ");
                }
                br.write("$");
                for (int j = 0; j < DrawingPane.listOfCurves.get(i).typeOfConnection.size(); j++) {
                    br.write( String.valueOf(DrawingPane.listOfCurves.get(i).typeOfConnection.get(j)));
                    if (j ==  DrawingPane.listOfCurves.get(i).typeOfConnection.size()-1) {
                        break;
                    }
                    br.write(" ");
                }
                br.write("$");
                br.write(DrawingPane.listOfCurves.get(i).colour.toString());
                br.write("$");
                br.write(DrawingPane.listOfCurves.get(i).lineType);
                br.write("$");
                br.write(Integer.toString(DrawingPane.listOfCurves.get(i).lineThickness));
                br.write("$");
                br.newLine();
            }
            saved = true;
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setSelectedProperties() {
        currentColor = drawingPane.currentSelectedCurve.colour;
        currentLineType = drawingPane.currentSelectedCurve.lineType;
        currentLineThickness = drawingPane.currentSelectedCurve.lineThickness;
        colorPicker.setValue(currentColor);
        if (currentLineThickness == 3) {
            RadioButton r1 = (RadioButton) lines.getChildren().get(0);
            r1.setSelected(true);
        } else if (currentLineThickness == 5) {
            RadioButton r2 = (RadioButton) lines.getChildren().get(1);
            r2.setSelected(true);
        } else {
            RadioButton r3 = (RadioButton) lines.getChildren().get(2);
            r3.setSelected(true);
        }

        if (currentLineType.equals("dashed")){
            RadioButton r1 = (RadioButton) styles.getChildren().get(0);
            r1.setSelected(true);
        } else if (currentLineType.equals("dotted")) {
            RadioButton r2 = (RadioButton) styles.getChildren().get(1);
            r2.setSelected(true);
        } else {
            RadioButton r3 = (RadioButton) styles.getChildren().get(2);
            r3.setSelected(true);
        }
    }

    public static void setUpMenuBar() {
        menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu help = new Menu("Help");
        MenuItem helpAbout = new MenuItem("About");
        help.getItems().addAll(helpAbout);
        helpAbout.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            Label label = new Label("This program was made by Jerry Yu (Software Engineering 2023). \nOther IDS are JR2YU, 20764520). \nThe program name is Bezier Curve Tool");
            label.setWrapText(true);
            alert.getDialogPane().setContent(label);
            alert.showAndWait();
        });

        newDrawing = new MenuItem("New");
        loadDrawing = new MenuItem("Load");
        saveDrawing = new MenuItem("Save");
        quitDrawing = new MenuItem("Quit");
        file.getItems().addAll(newDrawing, loadDrawing, saveDrawing, quitDrawing);
        menuBar.getMenus().addAll(file, help);
    }

    public static void enableTools() {
        colorPicker.setDisable(false);
        lines.getChildren().get(0).setDisable(false);
        lines.getChildren().get(1).setDisable(false);
        lines.getChildren().get(2).setDisable(false);
        styles.getChildren().get(0).setDisable(false);
        styles.getChildren().get(1).setDisable(false);
        styles.getChildren().get(2).setDisable(false);
    }

    public static void disableTools() {
        colorPicker.setDisable(true);
        lines.getChildren().get(0).setDisable(true);
        lines.getChildren().get(1).setDisable(true);
        lines.getChildren().get(2).setDisable(true);
        styles.getChildren().get(0).setDisable(true);
        styles.getChildren().get(1).setDisable(true);
        styles.getChildren().get(2).setDisable(true);
    }

    public static void setUpTools() {
        toolPane = new GridPane();
        toolPane.setAlignment(Pos.CENTER);
        toolPane.setVgap(10);
        toolPane.setHgap(10);
        // Select Button
        select = new Button("Select");
        ImageView cursor = new ImageView("selectionIcon.png");
        cursor.setFitWidth(40);
        cursor.setFitHeight(40);
        select.setGraphic(cursor);
        select.setPrefWidth(60);
        select.setPrefHeight(60);
        select.setContentDisplay(ContentDisplay.TOP);
        select.setOnAction( e -> {
            point.setDisable(true);
            currentlySelectedTool = "drawing";
            disableTools();
            drawingPane.reset();
        });

        // Edit Button
        edit = new Button("Edit");
        ImageView editIcon = new ImageView("editIcon.png");
        editIcon.setFitWidth(40);
        editIcon.setFitHeight(40);
        edit.setGraphic(editIcon);
        edit.setPrefWidth(60);
        edit.setPrefHeight(60);
        edit.setContentDisplay(ContentDisplay.TOP);
        edit.setOnAction( e -> {
            point.setDisable(false);
            delete.setDisable(false);
            currentlySelectedTool = "edit";
            enableTools();
            drawingPane.reset();
        });

        // Point button
        point = new Button("Point");
        ImageView pointIcon = new ImageView("pointIcon.png");
        pointIcon.setFitWidth(40);
        pointIcon.setFitHeight(40);
        point.setGraphic(pointIcon);
        point.setPrefWidth(60);
        point.setPrefHeight(60);
        point.setContentDisplay(ContentDisplay.TOP);
        point.setOnAction(e -> {
            drawingPane.changeConnectionType();
        });

        // Delete button
        delete = new Button("Delete");
        ImageView deleteIcon = new ImageView("deleteIcon.png");
        deleteIcon.setFitWidth(40);
        deleteIcon.setFitHeight(40);
        delete.setGraphic(deleteIcon);
        delete.setPrefWidth(60);
        delete.setPrefHeight(60);
        delete.setContentDisplay(ContentDisplay.TOP);
        delete.setOnAction(e -> {
            point.setDisable(true);
            disableTools();
            if (currentlySelectedTool != null && currentlySelectedTool.equals("edit")) {
                drawingPane.deleteSelectedCurve();
            }
            currentlySelectedTool = "delete";
            drawingPane.reset();
            disableTools();
        });
        toolPane.add(select, 0, 0);
        toolPane.add(point, 0, 1);
        toolPane.add(edit, 1,0);
        toolPane.add(delete, 1,1);
    }

    public static void setUpCurveEditingPane() {
        Label title = new Label("Curve Properties");
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(e -> {
            currentColor = colorPicker.getValue();
            drawingPane.setNewProperties();
        });

        Label lineThicknessLabel = new Label("Types of Line Thickness");
        lineThicknessLabel.setAlignment(Pos.CENTER);
        ToggleGroup lineThickness = new ToggleGroup();
        RadioButton r1 = new RadioButton("Thin");
        r1.setSelected(true);
        r1.setGraphic(new ImageView("thinLine.png"));
        r1.setContentDisplay(ContentDisplay.BOTTOM);
        r1.setOnMouseClicked(e -> {
            currentLineThickness = 3;
            drawingPane.setNewProperties();
        });
        RadioButton r2 = new RadioButton("Medium");
        r2.setContentDisplay(ContentDisplay.BOTTOM);
        r2.setGraphic(new ImageView("mediumLine.png"));
        r2.setOnMouseClicked(e -> {
            currentLineThickness = 5;
            drawingPane.setNewProperties();
        });
        RadioButton r3 = new RadioButton("Thick");
        r3.setContentDisplay(ContentDisplay.BOTTOM);
        r3.setGraphic(new ImageView("thickLine.png"));
        r3.setOnMouseClicked(e -> {
            currentLineThickness = 7;
            drawingPane.setNewProperties();
        });
        r1.setToggleGroup(lineThickness);
        r2.setToggleGroup(lineThickness);
        r3.setToggleGroup(lineThickness);
        lines = new HBox(r1, r2, r3);

        ToggleGroup lineStyle = new ToggleGroup();
        RadioButton s1 = new RadioButton("Dashed");
        s1.setContentDisplay(ContentDisplay.BOTTOM);
        s1.setGraphic(new ImageView("dashedLine.png"));
        s1.setOnMouseClicked(e -> {
            currentLineType = "dashed";
            drawingPane.setNewProperties();
        });
        RadioButton s2 = new RadioButton("Dotted");
        s2.setContentDisplay(ContentDisplay.BOTTOM);
        s2.setGraphic(new ImageView("dottedLine.png"));
        s2.setOnMouseClicked(e -> {
            currentLineType = "dotted";
            drawingPane.setNewProperties();
        });
        RadioButton s3 = new RadioButton("Continuous");
        s3.setContentDisplay(ContentDisplay.BOTTOM);
        s3.setGraphic(new ImageView("continuousLine.png"));
        s3.setSelected(true);
        s3.setOnMouseClicked(e -> {
            currentLineType = "continuous";
            drawingPane.setNewProperties();
        });
        s1.setToggleGroup(lineStyle);
        s2.setToggleGroup(lineStyle);
        s3.setToggleGroup(lineStyle);
        styles = new HBox(s1, s2, s3);

        curveEditingPane = new VBox(title, colorPicker,lineThicknessLabel, lines, styles);
        curveEditingPane.setSpacing(10);
        curveEditingPane.setAlignment(Pos.CENTER);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Cubic Curve Drawer - Jerry Yu - JR2YU - 20764520");
        borderPane = new BorderPane();
        setBorderSizes();
        setUpMenuBar();
        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                currentlySelectedTool = null;
                drawingPane.reset();
                enableTools();
                point.setDisable(false);
                delete.setDisable(false);
                System.out.println("ESCAPE key was pressed");
            } else if (e.getCode() == KeyCode.DELETE) {
                drawingPane.deleteSelectedCurve();
                System.out.println("DELETE key was pressed");
            }
        });

        drawingPane = new DrawingPane(this);
        borderPane.setCenter(drawingPane);

        setUpTools();
        setUpCurveEditingPane();

        Label curveTools = new Label("Curve Tools");
        VBox topLabel = new VBox(curveTools);
        topLabel.setAlignment(Pos.CENTER);
        VBox tools = new VBox(topLabel, toolPane, curveEditingPane);
        tools.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        tools.setSpacing(20);
        borderPane.setLeft(tools);

        newDrawing.setOnAction(e -> {
            newDrawing();
        });

        saveDrawing.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(primaryStage);
            if (selectedFile != null) {
                System.out.println(selectedFile.getName());
                saveDrawing(selectedFile);
            }
        });

        loadDrawing.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                System.out.println(selectedFile.getName());
                loadDrawing(selectedFile);
            }
        });

        quitDrawing.setOnAction(e -> {
            if (!saved && !DrawingPane.listOfCurves.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Quit confirmation");
                alert.setContentText("Your current file isn't saved. Would you like to save?");

                ButtonType buttonTypeOne = new ButtonType("Save");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne){
                    FileChooser fileChooser = new FileChooser();
                    File selectedFile = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
                    if (selectedFile != null) {
                        System.out.println(selectedFile.getName());
                        saveDrawing(selectedFile);
                    }
                } else {
                    alert.close();
                }
            }
            primaryStage.close();
        });

        primaryStage.setOnCloseRequest(e -> {
            if (!saved && !DrawingPane.listOfCurves.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Quit confirmation");
                alert.setContentText("Your current file isn't saved. Would you like to save?");

                ButtonType buttonTypeOne = new ButtonType("Save");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne){
                    FileChooser fileChooser = new FileChooser();
                    File selectedFile = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
                    if (selectedFile != null) {
                        System.out.println(selectedFile.getName());
                        saveDrawing(selectedFile);
                    }
                } else {
                    alert.close();
                }
            }
            primaryStage.close();
        });
        disableTools();
        point.setDisable(true);
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1600);
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.show();
    }
}
