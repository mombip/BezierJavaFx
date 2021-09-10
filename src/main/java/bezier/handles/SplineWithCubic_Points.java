package bezier.handles;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
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

    Anchor prevC2, currentC1, currentC2 = null;

    private double radius = 6f;
    private double radiusC = radius*0.6;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.getChildren().add(c);
        pane.setOnMousePressed(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            if (prevPoint != null) {
                System.out.println("line1" + prevPoint);
                System.out.println("line2" + currentPoint);
                currentCurve = makeCurve(basePoint, prevPoint, currentPoint, prevCurve);
                pane.getChildren().add(currentCurve);
                Line controlLine1 = new BoundLine(currentCurve.controlX1Property(), currentCurve.controlY1Property(), currentCurve.startXProperty(), currentCurve.startYProperty());
                Line controlLine2 = new BoundLine(currentCurve.controlX2Property(), currentCurve.controlY2Property(), currentCurve.endXProperty(),   currentCurve.endYProperty());
                pane.getChildren().add(controlLine1);
                pane.getChildren().add(controlLine2);
                currentStartAnchor = new Anchor(Color.PALEGREEN, currentCurve.startXProperty(),
                        currentCurve.startYProperty(), radius);
                pane.getChildren().add(currentStartAnchor);
                currentEndAnchor = new Anchor(Color.PALEGREEN, currentCurve.endXProperty(),
                        currentCurve.endYProperty(), radius);
                pane.getChildren().add(currentEndAnchor);
                currentC1 = new Anchor(Color.GOLD, currentCurve.controlX1Property(), currentCurve.controlY1Property(), radiusC);
                pane.getChildren().add(currentC1);
                currentC2 = new Anchor(Color.GOLDENROD, currentCurve.controlX2Property(),
                        currentCurve.controlY2Property(), radiusC);
                pane.getChildren().add(currentC2);
            }
        });
        pane.setOnMouseDragged(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            updateCurve(basePoint, prevPoint, currentPoint, currentCurve, prevCurve);
            if (prevCurve != null) {
                prevC2.setCenterX(prevCurve.getControlX2());
                prevC2.setCenterY(prevCurve.getControlY2());
                currentC1.setCenterX(currentCurve.getControlX1());
                currentC1.setCenterY(currentCurve.getControlY1());
                currentC2.setCenterX(currentCurve.getControlX2());
                currentC2.setCenterY(currentCurve.getControlY2());
            }

        });

        pane.setOnMouseReleased(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            basePoint = prevPoint;
            prevPoint = currentPoint;
            prevCurve = currentCurve;
            prevC2 = currentC2;
            currentPoint = null;

        });

        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private CubicCurve makeCurve(Point2D basePoint, Point2D prevPoint, Point2D currentPoint, CubicCurve prevCurve) {
        CubicCurve cc = new CubicCurve();
        updateCurve(basePoint, prevPoint, currentPoint, cc, prevCurve);
        cc.setStroke(Color.BLACK);
        cc.setFill(null);
        if (prevCurve != null) {
            cc.startXProperty().bindBidirectional(prevCurve.endXProperty());
            cc.startYProperty().bindBidirectional(prevCurve.endYProperty());
        }
        return cc;
    }

    private void updateCurve(Point2D startPoint, Point2D midPoint, Point2D endPoint, CubicCurve cc,
            CubicCurve prevCurve) {
        if(midPoint != null && endPoint != null) {
            Point2D currentControl1 = midPoint;
            Point2D control2 = endPoint;
            if(startPoint != null ) {
                final Point2D firstArmVect = midPoint.subtract(startPoint);
                double firstArmLength = firstArmVect.magnitude() * 0.4;
                final Point2D seconfArmVect = midPoint.subtract(endPoint);
                double secondArmLength = seconfArmVect.magnitude() * 0.4;
    
//                final Point2D normStartVect = firstArmVect.normalize();
//                final Point2D normEndVect = seconfArmVect.normalize();
//                final Point2D c1Direction = normStartVect.subtract(normEndVect).normalize();
                
                final Point2D mid = startPoint.midpoint(endPoint);
                c.setCenterX(mid.getX());
                c.setCenterY(mid.getY());
                final Point2D midToMid = mid.subtract(midPoint);
                final Point2D c1Direction = new Point2D(midToMid.getY(), -midToMid.getX()).normalize();
                
                currentControl1 = midPoint.add(c1Direction.multiply(secondArmLength));
                Point2D prevControl2 = midPoint.subtract(c1Direction.multiply(firstArmLength));
                if (prevCurve != null) {
                    prevCurve.setControlX2(prevControl2.getX());
                    prevCurve.setControlY2(prevControl2.getY());
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

    public static void main(String[] args) {
        launch(args);
    }

}