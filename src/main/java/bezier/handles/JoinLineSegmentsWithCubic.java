package bezier.handles;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class JoinLineSegmentsWithCubic extends Application {

    private Line unconnectedLine = null ;
    private Line currentDraggingLine = null ;
    private CubicCurve currentCurve = null;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.setOnDragDetected(e -> {
            System.out.println(e);
            System.out.println(e);
            currentDraggingLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
            pane.getChildren().add(currentDraggingLine);
            if (unconnectedLine != null) {
                System.out.println("line1"+unconnectedLine);
                System.out.println("line2"+currentDraggingLine);
                currentCurve = makeCurve(unconnectedLine, currentDraggingLine, pane);
            }
        });
        
//        pane.setOnMousePressed(e -> {
//            System.out.println(e);
//            pane.getOnMouseReleased();      
//        });
        
        pane.setOnMouseDragged(e -> {
            if (currentDraggingLine != null) {
                currentDraggingLine.setEndX(e.getX());
                currentDraggingLine.setEndY(e.getY());
                updateCurve(unconnectedLine, currentDraggingLine, currentCurve);
            }
        });
        
        pane.setOnMouseReleased(e -> {
            if (currentDraggingLine != null) {
                currentDraggingLine.setEndX(e.getX());
                currentDraggingLine.setEndY(e.getY());

//                if (unconnectedLine != null) {
//                    updateCurve(unconnectedLine, currentDraggingLine, pane);
//                }
                unconnectedLine = currentDraggingLine ;
                currentDraggingLine = null ;
            }
        });

        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private CubicCurve makeCurve(Line line1, Line line2, Pane parent) {
        CubicCurve cc = new CubicCurve();
        updateCurve(line1, line2, cc);

        cc.setStroke(Color.BLACK);
        cc.setFill(null);
        parent.getChildren().add(cc);
        return cc;
    }

    private void updateCurve(Line line1, Line line2, CubicCurve cc) {
        Point2D line1Start = new Point2D(line1.getStartX(), line1.getStartY());
        Point2D line1End = new Point2D(line1.getEndX(), line1.getEndY());
        Point2D line2Start = new Point2D(line2.getStartX(), line2.getStartY());
        Point2D line2End = new Point2D(line2.getEndX(), line2.getEndY());

        double line1Length = line2Start.subtract(line1End).magnitude();
//        double line2Length = line2End.subtract(line2Start).magnitude();

        // average length:
        double aveLength = (line1Length) / 2 ;

        // extend line1 in direction of line1 for aveLength:
        Point2D control1 = line1End.add(line1End.subtract(line1Start).normalize().multiply(aveLength));

        // extend line2 in (reverse) direction of line2 for aveLength:
        Point2D control2 = line2Start.add(line2Start.subtract(line2End).normalize().multiply(aveLength));

        
        cc.setStartX(line1End.getX());
        cc.setStartY(line1End.getY());
        cc.setControlX1(control1.getX());
        cc.setControlY1(control1.getY());
        cc.setControlX2(control2.getX());
        cc.setControlY2(control2.getY());
        cc.setEndX(line2Start.getX());
        cc.setEndY(line2Start.getY());
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}   