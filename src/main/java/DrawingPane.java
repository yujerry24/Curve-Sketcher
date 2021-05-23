import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class DrawingPane extends ScrollPane {
    public static Main model;
    public static double firstPointX = -1 ;
    public static double firstPointY = -1;
    public static double secondPointX  = -1;
    public static double secondPointY  = -1;
    public static double firstControlX = -1 ;
    public static double firstControlY = -1;
    public static double secondControlX  = -1;
    public static double secondControlY  = -1;
    public static ArrayList<Curve> listOfCurves;
    public Curve currentDrawnCurve;
    public Curve currentSelectedCurve;
    public Circle currentSegmentPoint;

    public class Curve {
        ArrayList<CubicCurve> curveSegments;
        ArrayList<Circle> controlPoints;
        ArrayList<Circle> segmentPoints;
        ArrayList<Line> anchorLines;
        ArrayList<String> typeOfConnection;
        ArrayList<Double> xDistance;
        ArrayList<Double> yDistance;
        Color colour;
        int lineThickness;
        String lineType;
    }

    public DrawingPane(Main model) {
        this.model = model;
        addEventListeners();
    }

    public void changeConnectionType() {
        if (currentSegmentPoint != null) {
            Main.saved = false;
            for (int i = 0; i < listOfCurves.size(); i++)  {
                if (listOfCurves.get(i).segmentPoints.contains(currentSegmentPoint)) {
                    String newType;
                    int index = listOfCurves.get(i).segmentPoints.indexOf(currentSegmentPoint);
                    if (listOfCurves.get(i).typeOfConnection.get(index).equals("smooth")) {
                        newType = "sharp";
                    } else {
                        newType = "smooth";
                    }
                    if (index % 2 == 0) {
                        listOfCurves.get(i).typeOfConnection.set(index, newType);
                        if (newType.equals("sharp")) {
                            listOfCurves.get(i).segmentPoints.get(index).setFill(Color.RED);
                        } else {
                            listOfCurves.get(i).segmentPoints.get(index).setFill(Color.BLACK);
                        }
                        if (index == 0) {
                            break;
                        }
                        listOfCurves.get(i).typeOfConnection.set(index-1, newType);
                        if (newType.equals("sharp")) {
                            listOfCurves.get(i).segmentPoints.get(index-1).setFill(Color.RED);
                        } else {
                            listOfCurves.get(i).segmentPoints.get(index-1).setFill(Color.BLACK);
                        }
                        if (newType.equals("smooth")) {
                            double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY()-currentSegmentPoint.getCenterY(),
                                    listOfCurves.get(i).controlPoints.get(index).getCenterX()-currentSegmentPoint.getCenterX());
                            System.out.println("angle " + angle);
                            double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index-1).getCenterY()-currentSegmentPoint.getCenterY(),2)
                                    + Math.pow(listOfCurves.get(i).controlPoints.get(index-1).getCenterX()-currentSegmentPoint.getCenterX(),2), 0.5);
                            System.out.println("distance " + distance);
                            listOfCurves.get(i).controlPoints.get(index-1).setCenterX(currentSegmentPoint.getCenterX()+Math.cos(angle+Math.PI)*distance);
                            listOfCurves.get(i).controlPoints.get(index-1).setCenterY(currentSegmentPoint.getCenterY()+Math.sin(angle+Math.PI)*distance);
                            listOfCurves.get(i).xDistance.set(index-1, Math.cos(angle+Math.PI)*distance);
                            listOfCurves.get(i).yDistance.set(index-1, Math.sin(angle+Math.PI)*distance);
                        }
                    } else {
                        listOfCurves.get(i).typeOfConnection.set(index, newType);
                        if (newType.equals("sharp")) {
                            listOfCurves.get(i).segmentPoints.get(index).setFill(Color.RED);
                        } else {
                            listOfCurves.get(i).segmentPoints.get(index).setFill(Color.BLACK);
                        }
                        if (index + 1 < listOfCurves.get(i).typeOfConnection.size()) {
                            listOfCurves.get(i).typeOfConnection.set(index+1, newType);
                        } else {
                            break;
                        }
                        if (newType.equals("sharp")) {
                            listOfCurves.get(i).segmentPoints.get(index+1).setFill(Color.RED);
                        } else {
                            listOfCurves.get(i).segmentPoints.get(index+1).setFill(Color.BLACK);
                        }
                        if (newType.equals("smooth")) {
                            double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY()-currentSegmentPoint.getCenterY(),
                                    listOfCurves.get(i).controlPoints.get(index).getCenterX()-currentSegmentPoint.getCenterX());
                            double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index+1).getCenterY()-currentSegmentPoint.getCenterY(),2)
                                    + Math.pow(listOfCurves.get(i).controlPoints.get(index+1).getCenterX()-currentSegmentPoint.getCenterX(),2), 0.5);
                            listOfCurves.get(i).controlPoints.get(index+1).setCenterX(currentSegmentPoint.getCenterX()+Math.cos(angle+Math.PI)*distance);
                            listOfCurves.get(i).controlPoints.get(index+1).setCenterY(currentSegmentPoint.getCenterX()+Math.sin(angle+Math.PI)*distance);
                            listOfCurves.get(i).xDistance.set(index+1, Math.cos(angle+Math.PI)*distance);
                            listOfCurves.get(i).yDistance.set(index+1, Math.sin(angle+Math.PI)*distance);
                        }
                    }
                }
            }
        }
    }

    public void deleteSelectedCurve() {
        if (currentSelectedCurve != null) {
            Main.saved = false;
            listOfCurves.remove(currentSelectedCurve);
            this.getChildren().removeAll(currentSelectedCurve.segmentPoints);
            this.getChildren().removeAll(currentSelectedCurve.controlPoints);
            this.getChildren().removeAll(currentSelectedCurve.curveSegments);
            this.getChildren().removeAll(currentSelectedCurve.anchorLines);
            currentSelectedCurve = null;
        }
    }

    public void hardReset() {
        this.getChildren().clear();
        currentDrawnCurve = null;
        currentSelectedCurve = null;
        currentSegmentPoint = null;
        firstPointX = -1;
        firstPointY = -1;
        secondPointX  = -1;
        secondPointY  = -1;
        firstControlX = -1 ;
        firstControlY = -1;
        secondControlX  = -1;
        secondControlY  = -1;
        listOfCurves.clear();
    }

    public void reset() {
        if (currentDrawnCurve != null) {
            System.out.println("test");
            listOfCurves.add(currentDrawnCurve);
        }
        if (currentSelectedCurve != null) {
            setCurveUnselected(currentSelectedCurve.controlPoints);
        }
        redrawPaneOnlyLines();
        currentDrawnCurve = null;
        currentSelectedCurve = null;
        currentSegmentPoint = null;
        firstPointX = -1;
        firstPointY = -1;
        secondPointX  = -1;
        secondPointY  = -1;
        firstControlX = -1 ;
        firstControlY = -1;
        secondControlX  = -1;
        secondControlY  = -1;
    }

    public void setCurveSelected(ArrayList<Circle> controlPoints) {
        for (Circle cur : controlPoints) {
            cur.setFill(Color.GREEN);
        }
    }

    public void setCurveUnselected(ArrayList<Circle> controlPoints) {
        for (Circle cur : controlPoints) {
            cur.setFill(Color.BLACK);
        }
    }

    public void redrawPaneOnlyLines() {
        this.getChildren().clear();
        for (int i = 0; i < listOfCurves.size(); i++) {
            this.getChildren().addAll(listOfCurves.get(i).curveSegments);
        }
    }

    public void processLoadedCurve(String str) {
        String temp = str;
        int segmentPointIndex = temp.indexOf("$");
        String segmentPointsString = temp.substring(0, segmentPointIndex);
        String [] segmentPointsTemp = segmentPointsString.split(" ");
        Double [] segmentPoints = new Double[segmentPointsTemp.length];
        for (int i = 0; i < segmentPoints.length; i++) {
            segmentPoints[i] = Double.parseDouble(segmentPointsTemp[i]);
        }

        temp = temp.substring(segmentPointIndex+1);
        int controlPointsIndex = temp.indexOf("$");
        String controlPointsString = temp.substring(0, controlPointsIndex);
        String [] controlPointsTemp = controlPointsString.split(" ");
        Double [] controlPoints = new Double[controlPointsTemp.length];
        for (int i = 0; i < controlPointsTemp.length; i++) {
            controlPoints[i] = Double.parseDouble(controlPointsTemp[i]);
        }

        temp = temp.substring(controlPointsIndex+1);
        int controlTypeIndex = temp.indexOf("$");
        String controlTypeString = temp.substring(0, controlTypeIndex);
        String [] controlTypes = controlTypeString.split(" ");

        temp = temp.substring(controlTypeIndex+1);
        int colorIndex = temp.indexOf("$");
        Color color = Color.valueOf(temp.substring(0, colorIndex));

        temp = temp.substring(colorIndex+1);
        int lineTypeIndex = temp.indexOf("$");
        String lineType = temp.substring(0, lineTypeIndex);

        temp = temp.substring(lineTypeIndex+1);
        int lineThickness = Integer.parseInt(temp.substring(0 , temp.indexOf("$")));

        Curve tempCurve = new Curve();
        tempCurve.curveSegments = new ArrayList<>();
        tempCurve.segmentPoints = new ArrayList<>();
        tempCurve.controlPoints = new ArrayList<>();
        tempCurve.typeOfConnection = new ArrayList<>();
        tempCurve.anchorLines = new ArrayList<>();
        tempCurve.xDistance = new ArrayList<>();
        tempCurve.yDistance = new ArrayList<>();
        for (int i = 0; i < segmentPoints.length; i += 4) {
            CubicCurve tempSegment = new CubicCurve(segmentPoints[i], segmentPoints[i+1], controlPoints[i], controlPoints[i+1], controlPoints[i+2], controlPoints[i+3], segmentPoints[i+2], segmentPoints[i+3]);
            tempSegment.setFill(null);
            tempSegment.setStroke(color);
            tempSegment.setStrokeWidth(lineThickness);

            Circle controlOne = new Circle(controlPoints[i], controlPoints[i+1], 5);
            Circle controlTwo = new Circle(controlPoints[i+2], controlPoints[i+3], 5);

            Circle pointOne = new Circle(segmentPoints[i], segmentPoints[i+1], 5);
            Circle pointTwo = new Circle(segmentPoints[i+2], segmentPoints[i+3], 5);

            Line lineOne = new Line(segmentPoints[i], segmentPoints[i+1], controlPoints[i], controlPoints[i+1]);
            Line lineTwo = new Line(segmentPoints[i+2], segmentPoints[i+3], controlPoints[i+2], controlPoints[i+3]);
            lineOne.getStrokeDashArray().addAll(2d);
            lineTwo.getStrokeDashArray().addAll(2d);
            tempSegment.startXProperty().bindBidirectional(pointOne.centerXProperty());
            tempSegment.startYProperty().bindBidirectional(pointOne.centerYProperty());
            tempSegment.endXProperty().bindBidirectional(pointTwo.centerXProperty());
            tempSegment.endYProperty().bindBidirectional(pointTwo.centerYProperty());
            tempSegment.controlX1Property().bindBidirectional(controlOne.centerXProperty());
            tempSegment.controlY1Property().bindBidirectional(controlOne.centerYProperty());
            tempSegment.controlX2Property().bindBidirectional(controlTwo.centerXProperty());
            tempSegment.controlY2Property().bindBidirectional(controlTwo.centerYProperty());
            lineOne.startXProperty().bindBidirectional(pointOne.centerXProperty());
            lineOne.startYProperty().bindBidirectional(pointOne.centerYProperty());
            lineOne.endXProperty().bindBidirectional(controlOne.centerXProperty());
            lineOne.endYProperty().bindBidirectional(controlOne.centerYProperty());
            lineTwo.startXProperty().bindBidirectional(pointTwo.centerXProperty());
            lineTwo.startYProperty().bindBidirectional(pointTwo.centerYProperty());
            lineTwo.endXProperty().bindBidirectional(controlTwo.centerXProperty());
            lineTwo.endYProperty().bindBidirectional(controlTwo.centerYProperty());
            tempSegment.setOnMouseClicked(e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).curveSegments.contains(tempSegment)) {
                                reset();
                                Main.drawingPane.getChildren().addAll(listOfCurves.get(j).segmentPoints);
                                Main.drawingPane.getChildren().addAll(listOfCurves.get(j).controlPoints);
                                setCurveSelected(listOfCurves.get(j).controlPoints);
                                Main.drawingPane.getChildren().addAll(listOfCurves.get(j).anchorLines);
                                currentSelectedCurve = listOfCurves.get(j);
                                Main.setSelectedProperties();
                                break;
                            }
                        }
                    } else if (Main.currentlySelectedTool.equals("delete")) {
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).curveSegments.contains(tempSegment)) {
                                Main.drawingPane.getChildren().removeAll(listOfCurves.get(j).segmentPoints);
                                Main.drawingPane.getChildren().removeAll(listOfCurves.get(j).controlPoints);
                                Main.drawingPane.getChildren().removeAll(listOfCurves.get(j).anchorLines);
                                Main.drawingPane.getChildren().removeAll(listOfCurves.get(j).curveSegments);
                                listOfCurves.remove(j);
                                this.reset();
                                break;
                            }
                        }
                    }
                }
            });
            controlOne.setOnMouseDragged(e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        Main.saved = false;
                        controlOne.setCenterX(e.getX());
                        controlOne.setCenterY(e.getY());
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).controlPoints.contains(controlOne)) {
                                int index = listOfCurves.get(j).controlPoints.indexOf(controlOne);
                                listOfCurves.get(j).xDistance.set(index, controlOne.getCenterX() - listOfCurves.get(j).segmentPoints.get(index).getCenterX());
                                listOfCurves.get(j).yDistance.set(index, controlOne.getCenterY() - listOfCurves.get(j).segmentPoints.get(index).getCenterY());
                                if (listOfCurves.get(j).typeOfConnection.get(index).equals("smooth")) {
                                    if (index > 0) {
                                        Circle segmentPoint = listOfCurves.get(j).segmentPoints.get(index);
                                        double angle = Math.atan2(listOfCurves.get(j).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                listOfCurves.get(j).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                        System.out.println("angle " + angle);
                                        double distance = Math.pow(Math.pow(listOfCurves.get(j).controlPoints.get(index - 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                + Math.pow(listOfCurves.get(j).controlPoints.get(index - 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                        System.out.println("distance " + distance);
                                        listOfCurves.get(j).controlPoints.get(index - 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                        listOfCurves.get(j).controlPoints.get(index - 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                        listOfCurves.get(j).xDistance.set(index - 1, Math.cos(angle + Math.PI) * distance);
                                        listOfCurves.get(j).yDistance.set(index - 1, Math.sin(angle + Math.PI) * distance);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            });
            controlTwo.setOnMouseDragged(e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        Main.saved = false;
                        controlTwo.setCenterX(e.getX());
                        controlTwo.setCenterY(e.getY());
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).controlPoints.contains(controlTwo)) {
                                int index = listOfCurves.get(j).controlPoints.indexOf(controlTwo);
                                listOfCurves.get(j).xDistance.set(index, controlTwo.getCenterX() - listOfCurves.get(j).segmentPoints.get(index).getCenterX());
                                listOfCurves.get(j).yDistance.set(index, controlTwo.getCenterY() - listOfCurves.get(j).segmentPoints.get(index).getCenterY());
                                if (listOfCurves.get(j).typeOfConnection.get(index).equals("smooth")) {
                                    if (index + 1 < listOfCurves.get(j).segmentPoints.size()) {
                                        Circle segmentPoint = listOfCurves.get(j).segmentPoints.get(index);
                                        double angle = Math.atan2(listOfCurves.get(j).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                listOfCurves.get(j).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                        System.out.println("angle " + angle);
                                        double distance = Math.pow(Math.pow(listOfCurves.get(j).controlPoints.get(index + 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                + Math.pow(listOfCurves.get(j).controlPoints.get(index + 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                        System.out.println("distance " + distance);
                                        listOfCurves.get(j).controlPoints.get(index + 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                        listOfCurves.get(j).controlPoints.get(index + 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                        listOfCurves.get(j).xDistance.set(index + 1, Math.cos(angle + Math.PI) * distance);
                                        listOfCurves.get(j).yDistance.set(index + 1, Math.sin(angle + Math.PI) * distance);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            });
            pointOne.setOnMouseClicked( e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        System.out.println("pointOneClicked");
                        currentSegmentPoint = pointOne;
                    }
                }
            });
            pointTwo.setOnMouseClicked( e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        System.out.println("pointTwoClicked");
                        currentSegmentPoint = pointTwo;
                    }
                }
            });
            pointOne.setOnMouseDragged(e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        pointOne.setCenterX(e.getX());
                        pointOne.setCenterY(e.getY());
                        Main.saved = false;
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).segmentPoints.contains(pointOne)) {
                                int index = listOfCurves.get(j).segmentPoints.indexOf(pointOne);
                                listOfCurves.get(j).controlPoints.get(index).setCenterX(pointOne.getCenterX() + listOfCurves.get(j).xDistance.get(index));
                                listOfCurves.get(j).controlPoints.get(index).setCenterY(pointOne.getCenterY() + listOfCurves.get(j).yDistance.get(index));
                                if (index > 0) {
                                    listOfCurves.get(j).controlPoints.get(index - 1).setCenterX(pointOne.getCenterX() + listOfCurves.get(j).xDistance.get(index - 1));
                                    listOfCurves.get(j).controlPoints.get(index - 1).setCenterY(pointOne.getCenterY() + listOfCurves.get(j).yDistance.get(index - 1));
                                }
                            }
                        }
                    }
                }
            });
            pointTwo.setOnMouseDragged(e -> {
                if (Main.currentlySelectedTool != null) {
                    if (Main.currentlySelectedTool.equals("edit")) {
                        pointTwo.setCenterX(e.getX());
                        pointTwo.setCenterY(e.getY());
                        Main.saved = false;
                        for (int j = 0; j < listOfCurves.size(); j++) {
                            if (listOfCurves.get(j).segmentPoints.contains(pointTwo)) {
                                int index = listOfCurves.get(j).segmentPoints.indexOf(pointTwo);
                                listOfCurves.get(j).controlPoints.get(index).setCenterX(pointTwo.getCenterX() + listOfCurves.get(j).xDistance.get(index));
                                listOfCurves.get(j).controlPoints.get(index).setCenterY(pointTwo.getCenterY() + listOfCurves.get(j).yDistance.get(index));
                                if (index < listOfCurves.get(j).segmentPoints.size() - 1) {
                                    listOfCurves.get(j).controlPoints.get(index + 1).setCenterX(pointTwo.getCenterX() + listOfCurves.get(j).xDistance.get(index + 1));
                                    listOfCurves.get(j).controlPoints.get(index + 1).setCenterY(pointTwo.getCenterY() + listOfCurves.get(j).yDistance.get(index + 1));
                                }
                            }
                        }
                    }
                }
            });
            if (i > 0) {
                tempSegment.startXProperty().bindBidirectional(tempCurve.curveSegments.get(tempCurve.curveSegments.size() -1).endXProperty());
                tempSegment.startYProperty().bindBidirectional(tempCurve.curveSegments.get(tempCurve.curveSegments.size() -1).endYProperty());
            }
            tempCurve.controlPoints.add(controlOne);
            tempCurve.controlPoints.add(controlTwo);
            tempCurve.segmentPoints.add(pointOne);
            tempCurve.segmentPoints.add(pointTwo);
            tempCurve.curveSegments.add(tempSegment);
            tempCurve.anchorLines.add(lineOne);
            tempCurve.anchorLines.add(lineTwo);
            tempCurve.typeOfConnection.add(controlTypes[i/2]);
            if (controlTypes[i/2].equals("sharp")) {
                pointOne.setFill(Color.RED);
            } else {
                pointOne.setFill(Color.BLACK);
            }
            tempCurve.typeOfConnection.add(controlTypes[i/2+1]);
            if (controlTypes[i/2+1].equals("sharp")) {
                pointTwo.setFill(Color.RED);
            } else {
                pointTwo.setFill(Color.BLACK);
            }
            tempCurve.xDistance.add(controlOne.getCenterX()-pointOne.getCenterX());
            tempCurve.yDistance.add(controlOne.getCenterY()-pointOne.getCenterY());
            tempCurve.xDistance.add(controlTwo.getCenterX()-pointTwo.getCenterX());
            tempCurve.yDistance.add(controlTwo.getCenterY()-pointTwo.getCenterY());
            tempCurve.colour = color;
            tempCurve.lineThickness = lineThickness;
            tempCurve.lineType = lineType;
            if (tempCurve.lineType .equals("dotted")) {
                tempSegment.getStrokeDashArray().clear();
                tempSegment.getStrokeDashArray().addAll(2d, 21d);
            } else if (tempCurve.lineType.equals("dashed")) {
                tempSegment.getStrokeDashArray().clear();
                tempSegment.getStrokeDashArray().addAll(25d, 10d);
            } else {
                tempSegment.getStrokeDashArray().clear();
            }

            this.getChildren().addAll(tempSegment);
        }
        listOfCurves.add(tempCurve);
    }

    public void setNewProperties() {
        if (currentSelectedCurve != null) {
            if (currentSelectedCurve.lineThickness==Main.currentLineThickness && currentSelectedCurve.colour.equals(Main.currentColor) && currentSelectedCurve.lineType.equals(Main.currentLineType)) {
                return;
            }
            Main.saved = false;
            currentSelectedCurve.lineThickness = Main.currentLineThickness;
            currentSelectedCurve.colour = Main.currentColor;
            currentSelectedCurve.lineType = Main.currentLineType;
            for (int i = 0; i < currentSelectedCurve.curveSegments.size(); i++) {
                currentSelectedCurve.curveSegments.get(i).setStrokeWidth(currentSelectedCurve.lineThickness);
                currentSelectedCurve.curveSegments.get(i).setStroke(currentSelectedCurve.colour);
                if (currentSelectedCurve.lineType.equals("dashed")) {
                    currentSelectedCurve.curveSegments.get(i).getStrokeDashArray().clear();
                    currentSelectedCurve.curveSegments.get(i).getStrokeDashArray().addAll(25d, 10d);
                } else if (currentSelectedCurve.lineType.equals("dotted")) {
                    currentSelectedCurve.curveSegments.get(i).getStrokeDashArray().clear();
                    currentSelectedCurve.curveSegments.get(i).getStrokeDashArray().addAll(2d, 21d);
                } else {
                    currentSelectedCurve.curveSegments.get(i).getStrokeDashArray().clear();
                }
            }
        }
    }

    public void redrawPaneEverything() {
        this.getChildren().clear();
        for (int i = 0; i < listOfCurves.size(); i++) {
            this.getChildren().addAll(listOfCurves.get(i).curveSegments);
            this.getChildren().addAll(listOfCurves.get(i).controlPoints);
            this.getChildren().addAll(listOfCurves.get(i).segmentPoints);
        }
    }
    public void addEventListeners() {
        listOfCurves = new ArrayList<>();

        this.setOnMouseReleased(event -> {
            if (Main.currentlySelectedTool != null && Main.currentlySelectedTool.equals("drawing")) {
                firstControlX = secondControlX;
                firstControlY = secondControlY;
                secondControlX = event.getX();
                secondControlY = event.getY();
            }
            if (firstPointX != -1) {
                if (currentDrawnCurve == null) {
                    Main.saved = false;
                    CubicCurve tempSegment = new CubicCurve(firstPointX, firstPointY, firstControlX, firstControlY, secondControlX, secondControlY, secondPointX, secondPointY);
                    tempSegment.setFill(null);
                    tempSegment.setStroke(Main.currentColor);
                    tempSegment.setStrokeWidth(Main.currentLineThickness);


                    Circle controlOne = new Circle(firstControlX, firstControlY, 5);
                    Circle controlTwo = new Circle(secondControlX, secondControlY, 5);

                    Circle pointOne = new Circle(firstPointX, firstPointY, 5);
                    Circle pointTwo = new Circle(secondPointX, secondPointY, 5);

                    Line lineOne = new Line(firstPointX, firstPointY, firstControlX, firstControlY);
                    Line lineTwo = new Line(secondPointX, secondPointY, secondControlX, secondControlY);
                    lineOne.getStrokeDashArray().addAll(2d);
                    lineTwo.getStrokeDashArray().addAll(2d);
                    currentDrawnCurve = new Curve();
                    currentDrawnCurve.segmentPoints = new ArrayList<>();
                    currentDrawnCurve.controlPoints = new ArrayList<>();
                    currentDrawnCurve.curveSegments = new ArrayList<>();
                    currentDrawnCurve.anchorLines = new ArrayList<>();
                    currentDrawnCurve.typeOfConnection = new ArrayList<>();
                    currentDrawnCurve.xDistance = new ArrayList<>();
                    currentDrawnCurve.yDistance = new ArrayList<>();
                    tempSegment.startXProperty().bindBidirectional(pointOne.centerXProperty());
                    tempSegment.startYProperty().bindBidirectional(pointOne.centerYProperty());
                    tempSegment.endXProperty().bindBidirectional(pointTwo.centerXProperty());
                    tempSegment.endYProperty().bindBidirectional(pointTwo.centerYProperty());
                    tempSegment.controlX1Property().bindBidirectional(controlOne.centerXProperty());
                    tempSegment.controlY1Property().bindBidirectional(controlOne.centerYProperty());
                    tempSegment.controlX2Property().bindBidirectional(controlTwo.centerXProperty());
                    tempSegment.controlY2Property().bindBidirectional(controlTwo.centerYProperty());
                    lineOne.startXProperty().bindBidirectional(pointOne.centerXProperty());
                    lineOne.startYProperty().bindBidirectional(pointOne.centerYProperty());
                    lineOne.endXProperty().bindBidirectional(controlOne.centerXProperty());
                    lineOne.endYProperty().bindBidirectional(controlOne.centerYProperty());
                    lineTwo.startXProperty().bindBidirectional(pointTwo.centerXProperty());
                    lineTwo.startYProperty().bindBidirectional(pointTwo.centerYProperty());
                    lineTwo.endXProperty().bindBidirectional(controlTwo.centerXProperty());
                    lineTwo.endYProperty().bindBidirectional(controlTwo.centerYProperty());
                    tempSegment.setOnMouseClicked(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).curveSegments.contains(tempSegment)) {
                                        reset();
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).segmentPoints);
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).controlPoints);
                                        setCurveSelected(listOfCurves.get(i).controlPoints);
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).anchorLines);
                                        currentSelectedCurve = listOfCurves.get(i);
                                        Main.setSelectedProperties();
                                        break;
                                    }
                                }
                            } else if (Main.currentlySelectedTool.equals("delete")) {
                                Main.saved = false;
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).curveSegments.contains(tempSegment)) {
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).segmentPoints);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).controlPoints);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).anchorLines);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).curveSegments);
                                        listOfCurves.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    controlOne.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                controlOne.setCenterX(e.getX());
                                controlOne.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).controlPoints.contains(controlOne)) {
                                        int index = listOfCurves.get(i).controlPoints.indexOf(controlOne);
                                        listOfCurves.get(i).xDistance.set(index, controlOne.getCenterX() - listOfCurves.get(i).segmentPoints.get(index).getCenterX());
                                        listOfCurves.get(i).yDistance.set(index, controlOne.getCenterY() - listOfCurves.get(i).segmentPoints.get(index).getCenterY());
                                        if (listOfCurves.get(i).typeOfConnection.get(index).equals("smooth")) {
                                            if (index > 0) {
                                                Circle segmentPoint = listOfCurves.get(i).segmentPoints.get(index);
                                                double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                        listOfCurves.get(i).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                                System.out.println("angle " + angle);
                                                double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index - 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                        + Math.pow(listOfCurves.get(i).controlPoints.get(index - 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                                System.out.println("distance " + distance);
                                                listOfCurves.get(i).controlPoints.get(index - 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).controlPoints.get(index - 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                                listOfCurves.get(i).xDistance.set(index - 1, Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).yDistance.set(index - 1, Math.sin(angle + Math.PI) * distance);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    controlTwo.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                controlTwo.setCenterX(e.getX());
                                controlTwo.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).controlPoints.contains(controlTwo)) {
                                        int index = listOfCurves.get(i).controlPoints.indexOf(controlTwo);
                                        listOfCurves.get(i).xDistance.set(index, controlTwo.getCenterX() - listOfCurves.get(i).segmentPoints.get(index).getCenterX());
                                        listOfCurves.get(i).yDistance.set(index, controlTwo.getCenterY() - listOfCurves.get(i).segmentPoints.get(index).getCenterY());
                                        if (listOfCurves.get(i).typeOfConnection.get(index).equals("smooth")) {
                                            if (index + 1 < listOfCurves.get(i).controlPoints.size()) {
                                                Circle segmentPoint = listOfCurves.get(i).segmentPoints.get(index);
                                                double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                        listOfCurves.get(i).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                                System.out.println("angle " + angle);
                                                double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index + 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                        + Math.pow(listOfCurves.get(i).controlPoints.get(index + 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                                System.out.println("distance " + distance);
                                                listOfCurves.get(i).controlPoints.get(index + 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).controlPoints.get(index + 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                                listOfCurves.get(i).xDistance.set(index + 1, Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).yDistance.set(index + 1, Math.sin(angle + Math.PI) * distance);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    pointOne.setOnMouseClicked( e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                System.out.println("pointOneClicked");
                                currentSegmentPoint = pointOne;
                            }
                        }
                    });
                    pointTwo.setOnMouseClicked( e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                System.out.println("pointTwoClicked");
                                currentSegmentPoint = pointTwo;
                            }
                        }
                    });
                    pointOne.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                pointOne.setCenterX(e.getX());
                                pointOne.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).segmentPoints.contains(pointOne)) {
                                        int index = listOfCurves.get(i).segmentPoints.indexOf(pointOne);
                                        listOfCurves.get(i).controlPoints.get(index).setCenterX(pointOne.getCenterX() + listOfCurves.get(i).xDistance.get(index));
                                        listOfCurves.get(i).controlPoints.get(index).setCenterY(pointOne.getCenterY() + listOfCurves.get(i).yDistance.get(index));
                                        if (index > 0) {
                                            listOfCurves.get(i).controlPoints.get(index - 1).setCenterX(pointOne.getCenterX() + listOfCurves.get(i).xDistance.get(index - 1));
                                            listOfCurves.get(i).controlPoints.get(index - 1).setCenterY(pointOne.getCenterY() + listOfCurves.get(i).yDistance.get(index - 1));
                                        }
                                    }
                                }
                            }
                        }
                    });
                    pointTwo.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                pointTwo.setCenterX(e.getX());
                                pointTwo.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).segmentPoints.contains(pointTwo)) {
                                        int index = listOfCurves.get(i).segmentPoints.indexOf(pointTwo);
                                        listOfCurves.get(i).controlPoints.get(index).setCenterX(pointTwo.getCenterX() + listOfCurves.get(i).xDistance.get(index));
                                        listOfCurves.get(i).controlPoints.get(index).setCenterY(pointTwo.getCenterY() + listOfCurves.get(i).yDistance.get(index));
                                        if (index < listOfCurves.get(i).segmentPoints.size() - 1) {
                                            listOfCurves.get(i).controlPoints.get(index + 1).setCenterX(pointTwo.getCenterX() + listOfCurves.get(i).xDistance.get(index + 1));
                                            listOfCurves.get(i).controlPoints.get(index + 1).setCenterY(pointTwo.getCenterY() + listOfCurves.get(i).yDistance.get(index + 1));
                                        }
                                    }
                                }
                            }
                        }
                    });
                    currentDrawnCurve.controlPoints.add(controlOne);
                    currentDrawnCurve.controlPoints.add(controlTwo);
                    currentDrawnCurve.segmentPoints.add(pointOne);
                    currentDrawnCurve.segmentPoints.add(pointTwo);
                    currentDrawnCurve.curveSegments.add(tempSegment);
                    currentDrawnCurve.anchorLines.add(lineOne);
                    currentDrawnCurve.anchorLines.add(lineTwo);
                    currentDrawnCurve.typeOfConnection.add("smooth");
                    currentDrawnCurve.typeOfConnection.add("smooth");
                    currentDrawnCurve.xDistance.add(controlOne.getCenterX()-pointOne.getCenterX());
                    currentDrawnCurve.yDistance.add(controlOne.getCenterY()-pointOne.getCenterY());
                    currentDrawnCurve.xDistance.add(controlTwo.getCenterX()-pointTwo.getCenterX());
                    currentDrawnCurve.yDistance.add(controlTwo.getCenterY()-pointTwo.getCenterY());
                    currentDrawnCurve.colour = Main.currentColor;
                    currentDrawnCurve.lineThickness = Main.currentLineThickness;
                    currentDrawnCurve.lineType = Main.currentLineType;
                    if (Main.currentLineType.equals("dotted")) {
                        tempSegment.getStrokeDashArray().clear();
                        tempSegment.getStrokeDashArray().addAll(2d, 21d);
                    } else if (Main.currentLineType.equals("dashed")) {
                        tempSegment.getStrokeDashArray().clear();
                        tempSegment.getStrokeDashArray().addAll(25d, 10d);
                    } else {
                        tempSegment.getStrokeDashArray().clear();
                    }
                    this.getChildren().addAll(tempSegment, controlOne, controlTwo, pointOne, pointTwo, lineOne, lineTwo);
                } else {
                    Main.saved = false;
                    double angleControl = Math.atan2(firstControlY-firstPointY,
                            firstControlX-firstPointX);
                    double distanceControl = Math.pow(Math.pow(firstControlY-firstPointY,2)
                            + Math.pow(firstControlX-firstPointX,2), 0.5);
                    CubicCurve tempSegment = new CubicCurve(firstPointX, firstPointY, firstPointX + Math.cos(angleControl+Math.PI)*distanceControl, firstPointY + Math.sin(angleControl+Math.PI)*distanceControl, secondControlX, secondControlY, secondPointX, secondPointY);
                    tempSegment.setFill(null);
                    tempSegment.setStroke(Main.currentColor);
                    tempSegment.setStrokeWidth(Main.currentLineThickness);


                    Circle controlOne = new Circle(tempSegment.getControlX1(), tempSegment.getControlY1(), 5);
                    Circle controlTwo = new Circle(secondControlX, secondControlY, 5);

                    Circle pointOne = new Circle(firstPointX, firstPointY, 5);
                    Circle pointTwo = new Circle(secondPointX, secondPointY, 5);

                    Line lineOne = new Line(firstPointX, firstPointY, tempSegment.getControlX1(),  tempSegment.getControlY1());
                    Line lineTwo = new Line(secondPointX, secondPointY, secondControlX, secondControlY);
                    lineOne.getStrokeDashArray().addAll(2d);
                    lineTwo.getStrokeDashArray().addAll(2d);
                    tempSegment.setOnMouseClicked(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).curveSegments.contains(tempSegment)) {
                                        reset();
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).segmentPoints);
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).controlPoints);
                                        setCurveSelected(listOfCurves.get(i).controlPoints);
                                        Main.drawingPane.getChildren().addAll(listOfCurves.get(i).anchorLines);
                                        currentSelectedCurve = listOfCurves.get(i);
                                        Main.setSelectedProperties();
                                        break;
                                    }
                                }
                            } else if (Main.currentlySelectedTool.equals("delete")) {
                                Main.saved = false;
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).curveSegments.contains(tempSegment)) {
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).segmentPoints);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).controlPoints);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).anchorLines);
                                        Main.drawingPane.getChildren().removeAll(listOfCurves.get(i).curveSegments);
                                        listOfCurves.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    controlOne.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                controlOne.setCenterX(e.getX());
                                controlOne.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).controlPoints.contains(controlOne)) {
                                        int index = listOfCurves.get(i).controlPoints.indexOf(controlOne);
                                        listOfCurves.get(i).xDistance.set(index, controlOne.getCenterX() - listOfCurves.get(i).segmentPoints.get(index).getCenterX());
                                        listOfCurves.get(i).yDistance.set(index, controlOne.getCenterY() - listOfCurves.get(i).segmentPoints.get(index).getCenterY());
                                        if (listOfCurves.get(i).typeOfConnection.get(index).equals("smooth")) {
                                            if (index > 0) {
                                                Circle segmentPoint = listOfCurves.get(i).segmentPoints.get(index);
                                                double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                        listOfCurves.get(i).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                                System.out.println("angle " + angle);
                                                double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index - 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                        + Math.pow(listOfCurves.get(i).controlPoints.get(index - 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                                System.out.println("distance " + distance);
                                                listOfCurves.get(i).controlPoints.get(index - 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).controlPoints.get(index - 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                                listOfCurves.get(i).xDistance.set(index - 1, Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).yDistance.set(index - 1, Math.sin(angle + Math.PI) * distance);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    controlTwo.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                controlTwo.setCenterX(e.getX());
                                controlTwo.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).controlPoints.contains(controlTwo)) {
                                        int index = listOfCurves.get(i).controlPoints.indexOf(controlTwo);
                                        listOfCurves.get(i).xDistance.set(index, controlTwo.getCenterX() - listOfCurves.get(i).segmentPoints.get(index).getCenterX());
                                        listOfCurves.get(i).yDistance.set(index, controlTwo.getCenterY() - listOfCurves.get(i).segmentPoints.get(index).getCenterY());
                                        if (listOfCurves.get(i).typeOfConnection.get(index).equals("smooth")) {
                                            if (index + 1 < listOfCurves.get(i).controlPoints.size()) {
                                                Circle segmentPoint = listOfCurves.get(i).segmentPoints.get(index);
                                                double angle = Math.atan2(listOfCurves.get(i).controlPoints.get(index).getCenterY() - segmentPoint.getCenterY(),
                                                        listOfCurves.get(i).controlPoints.get(index).getCenterX() - segmentPoint.getCenterX());
                                                System.out.println("angle " + angle);
                                                double distance = Math.pow(Math.pow(listOfCurves.get(i).controlPoints.get(index + 1).getCenterY() - segmentPoint.getCenterY(), 2)
                                                        + Math.pow(listOfCurves.get(i).controlPoints.get(index + 1).getCenterX() - segmentPoint.getCenterX(), 2), 0.5);
                                                System.out.println("distance " + distance);
                                                listOfCurves.get(i).controlPoints.get(index + 1).setCenterX(segmentPoint.getCenterX() + Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).controlPoints.get(index + 1).setCenterY(segmentPoint.getCenterY() + Math.sin(angle + Math.PI) * distance);
                                                listOfCurves.get(i).xDistance.set(index + 1, Math.cos(angle + Math.PI) * distance);
                                                listOfCurves.get(i).yDistance.set(index + 1, Math.sin(angle + Math.PI) * distance);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    pointOne.setOnMouseClicked( e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                System.out.println("pointOneClicked");
                                currentSegmentPoint = pointOne;
                            }
                        }
                    });
                    pointTwo.setOnMouseClicked( e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                System.out.println("pointTwoClicked");
                                currentSegmentPoint = pointTwo;
                            }
                        }
                    });
                    pointOne.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                pointOne.setCenterX(e.getX());
                                pointOne.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).segmentPoints.contains(pointOne)) {
                                        int index = listOfCurves.get(i).segmentPoints.indexOf(pointOne);
                                        listOfCurves.get(i).controlPoints.get(index).setCenterX(pointOne.getCenterX() + listOfCurves.get(i).xDistance.get(index));
                                        listOfCurves.get(i).controlPoints.get(index).setCenterY(pointOne.getCenterY() + listOfCurves.get(i).yDistance.get(index));
                                        if (index > 0) {
                                            listOfCurves.get(i).controlPoints.get(index - 1).setCenterX(pointOne.getCenterX() + listOfCurves.get(i).xDistance.get(index - 1));
                                            listOfCurves.get(i).controlPoints.get(index - 1).setCenterY(pointOne.getCenterY() + listOfCurves.get(i).yDistance.get(index - 1));
                                        }
                                    }
                                }
                            }
                        }
                    });
                    pointTwo.setOnMouseDragged(e -> {
                        if (Main.currentlySelectedTool != null) {
                            if (Main.currentlySelectedTool.equals("edit")) {
                                Main.saved = false;
                                pointTwo.setCenterX(e.getX());
                                pointTwo.setCenterY(e.getY());
                                for (int i = 0; i < listOfCurves.size(); i++) {
                                    if (listOfCurves.get(i).segmentPoints.contains(pointTwo)) {
                                        int index = listOfCurves.get(i).segmentPoints.indexOf(pointTwo);
                                        listOfCurves.get(i).controlPoints.get(index).setCenterX(pointTwo.getCenterX() + listOfCurves.get(i).xDistance.get(index));
                                        listOfCurves.get(i).controlPoints.get(index).setCenterY(pointTwo.getCenterY() + listOfCurves.get(i).yDistance.get(index));
                                        if (index < listOfCurves.get(i).segmentPoints.size() - 1) {
                                            listOfCurves.get(i).controlPoints.get(index + 1).setCenterX(pointTwo.getCenterX() + listOfCurves.get(i).xDistance.get(index + 1));
                                            listOfCurves.get(i).controlPoints.get(index + 1).setCenterY(pointTwo.getCenterY() + listOfCurves.get(i).yDistance.get(index + 1));
                                        }
                                    }
                                }
                            }
                        }
                    });
                    tempSegment.startXProperty().bindBidirectional(pointOne.centerXProperty());
                    tempSegment.startYProperty().bindBidirectional(pointOne.centerYProperty());
                    tempSegment.endXProperty().bindBidirectional(pointTwo.centerXProperty());
                    tempSegment.endYProperty().bindBidirectional(pointTwo.centerYProperty());
                    tempSegment.controlX1Property().bindBidirectional(controlOne.centerXProperty());
                    tempSegment.controlY1Property().bindBidirectional(controlOne.centerYProperty());
                    tempSegment.controlX2Property().bindBidirectional(controlTwo.centerXProperty());
                    tempSegment.controlY2Property().bindBidirectional(controlTwo.centerYProperty());
                    tempSegment.startXProperty().bindBidirectional(this.currentDrawnCurve.curveSegments.get(this.currentDrawnCurve.curveSegments.size() -1).endXProperty());
                    tempSegment.startYProperty().bindBidirectional(this.currentDrawnCurve.curveSegments.get(this.currentDrawnCurve.curveSegments.size() -1).endYProperty());
                    tempSegment.startYProperty().bindBidirectional(pointOne.centerYProperty());
                    lineOne.startXProperty().bindBidirectional(pointOne.centerXProperty());
                    lineOne.startYProperty().bindBidirectional(pointOne.centerYProperty());
                    lineOne.endXProperty().bindBidirectional(controlOne.centerXProperty());
                    lineOne.endYProperty().bindBidirectional(controlOne.centerYProperty());
                    lineTwo.startXProperty().bindBidirectional(pointTwo.centerXProperty());
                    lineTwo.startYProperty().bindBidirectional(pointTwo.centerYProperty());
                    lineTwo.endXProperty().bindBidirectional(controlTwo.centerXProperty());
                    lineTwo.endYProperty().bindBidirectional(controlTwo.centerYProperty());
                    currentDrawnCurve.controlPoints.add(controlOne);
                    currentDrawnCurve.controlPoints.add(controlTwo);
                    currentDrawnCurve.segmentPoints.add(pointOne);
                    currentDrawnCurve.segmentPoints.add(pointTwo);
                    currentDrawnCurve.curveSegments.add(tempSegment);
                    currentDrawnCurve.anchorLines.add(lineOne);
                    currentDrawnCurve.anchorLines.add(lineTwo);
                    currentDrawnCurve.typeOfConnection.add("smooth");
                    currentDrawnCurve.typeOfConnection.add("smooth");
                    currentDrawnCurve.xDistance.add(controlOne.getCenterX()-pointOne.getCenterX());
                    currentDrawnCurve.yDistance.add(controlOne.getCenterY()-pointOne.getCenterY());
                    currentDrawnCurve.xDistance.add(controlTwo.getCenterX()-pointTwo.getCenterX());
                    currentDrawnCurve.yDistance.add(controlTwo.getCenterY()-pointTwo.getCenterY());
                    currentDrawnCurve.colour = Main.currentColor;
                    currentDrawnCurve.lineThickness = Main.currentLineThickness;
                    currentDrawnCurve.lineType = Main.currentLineType;
                    if (Main.currentLineType.equals("dotted")) {
                        tempSegment.getStrokeDashArray().clear();
                        tempSegment.getStrokeDashArray().addAll(2d, 21d);
                    } else if (Main.currentLineType.equals("dashed")) {
                        tempSegment.getStrokeDashArray().clear();
                        tempSegment.getStrokeDashArray().addAll(25d, 10d);
                    } else {
                        tempSegment.getStrokeDashArray().clear();
                    }
                    this.getChildren().addAll(tempSegment, controlOne, controlTwo, pointOne, pointTwo, lineOne, lineTwo);
                }



            }
        });

        this.setOnMousePressed(event -> {
            if (Main.currentlySelectedTool != null && Main.currentlySelectedTool.equals("drawing")) {
                firstPointX = secondPointX;
                firstPointY = secondPointY;
                secondPointX = event.getX();
                secondPointY = event.getY();

            }
        });
    }
}
