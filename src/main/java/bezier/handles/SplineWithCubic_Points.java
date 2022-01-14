package bezier.handles;

import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;

public class SplineWithCubic_Points extends Application {

    private Point2D basePoint = null;
    private Point2D prevPoint = null;
    private Point2D currentPoint = null;

    private Anchor currentStartAnchor = null;
    private Anchor prevEndAnchor = null;
    private Anchor currentEndAnchor = null;
    private CubicCurve currentCurve = null;
    private CubicCurve prevCurve = null;
    
    private Circle c = new Circle(100,100,3);

//    Anchor prevC2, currentC1, currentC2 = null;
    ControlHandle prevHandle, currentHandle;

    private double radius = 6f;
    private double radiusC = radius*0.6;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #" + String.format("%02x%02x%02x" , 180, 180, 180 ));
        pane.getChildren().add(c);
        pane.setOnMousePressed(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            if (prevPoint != null) {
                System.out.println("line1" + prevPoint);
                System.out.println("line2" + currentPoint);
                currentCurve = makeCurve(basePoint, prevPoint, currentPoint, prevCurve);
                pane.getChildren().add(currentCurve);
                currentStartAnchor = new Anchor(Color.PALEGREEN, currentCurve.startXProperty(),
                        currentCurve.startYProperty(), radius);
                pane.getChildren().add(currentStartAnchor);
                currentEndAnchor = new Anchor(Color.PALEGREEN, currentCurve.endXProperty(),
                        currentCurve.endYProperty(), radius);
                pane.getChildren().add(currentEndAnchor);

                currentHandle = new ControlHandle(prevCurve, currentCurve, radiusC);
                pane.getChildren().add(currentHandle);
                updateCurve(basePoint, prevPoint, currentPoint, currentCurve);

//                Line controlLine1 = new BoundLine(currentCurve.controlX1Property(), currentCurve.controlY1Property(), currentCurve.startXProperty(), currentCurve.startYProperty());
//                Line controlLine2 = new BoundLine(currentCurve.controlX2Property(), currentCurve.controlY2Property(), currentCurve.endXProperty(),   currentCurve.endYProperty());
//                pane.getChildren().add(controlLine1);
//                pane.getChildren().add(controlLine2);
//                currentC1 = new Anchor(Color.GOLD, currentCurve.controlX1Property(), currentCurve.controlY1Property(), radiusC);
//                pane.getChildren().add(currentC1);
//                currentC2 = new Anchor(Color.GOLDENROD, currentCurve.controlX2Property(),
//                        currentCurve.controlY2Property(), radiusC);
//                pane.getChildren().add(currentC2);
                Interpolator.SPLINE(radius, radius, radiusC, radius);
                
//                pane.getChildren().add();
                
            }
        });
        pane.setOnMouseDragged(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            updateCurve(basePoint, prevPoint, currentPoint, currentCurve);
        });

        pane.setOnMouseReleased(e -> {
            basePoint = prevPoint;
            prevPoint = currentPoint;
            prevCurve = currentCurve;
            prevHandle = currentHandle;
            currentPoint = null;
        });

        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private CubicCurve makeCurve(Point2D basePoint, Point2D prevPoint, Point2D currentPoint, CubicCurve prevCurve) {
        CubicCurve cc = new CubicCurve();
//        updateCurve(basePoint, prevPoint, currentPoint, cc);
        cc.setStroke(Color.BLACK);
        cc.setStrokeWidth(3);
        cc.setFill(null);
        if (prevCurve != null) {
            cc.startXProperty().bindBidirectional(prevCurve.endXProperty());
            cc.startYProperty().bindBidirectional(prevCurve.endYProperty());
        }
        return cc;
    }

    private void updateCurve(Point2D startPoint, Point2D midPoint, Point2D endPoint, CubicCurve cc    ) {
        if(midPoint != null && endPoint != null) {
            Point2D currentControl1 = midPoint;
            Point2D control2 = endPoint;
            if(startPoint != null ) {
                final Point2D firstArmVect = midPoint.subtract(startPoint);
                double firstArmLength = firstArmVect.magnitude() * 0.4;
                final Point2D seconfArmVect = midPoint.subtract(endPoint);
                double secondArmLength = seconfArmVect.magnitude() * 0.4;
    
                final Point2D normStartVect = firstArmVect.normalize();
                final Point2D normEndVect = seconfArmVect.normalize();
                final Point2D c1Direction = normStartVect.subtract(normEndVect).normalize();
                
                currentControl1 = midPoint.add(c1Direction.multiply(secondArmLength));
                if (currentHandle != null) {
                    currentHandle.updateC2PosAutosize(currentControl1);
                }
            }
            cc.setStartX(midPoint.getX());
            cc.setStartY(midPoint.getY());
            cc.setControlX1(currentControl1.getX());
            cc.setControlY1(currentControl1.getY());
            cc.setControlX2(control2.getX());
            cc.setControlY2(control2.getY());
            cc.setEndX(endPoint.getX());
            cc.setEndY(endPoint.getY());
        }

    }

    public static void run(String[] args) {
        launch(args);
    }

}