package bezier.handles;


import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

public class ControlHandle extends Group {

    private Segment segment1;
    private Segment segment2;
    private Line controlLine1;
    private Line controlLine2;
    private Anchor control1;
    Point2D control2pt;
    private Anchor control2;


    public ControlHandle(Segment curve1, Segment curve2, double size) {
        if(curve1 != null){
            this.segment1 = curve1;
            CubicCurve c1 = curve1.getCubicCurve();
            control1 = new Anchor(Color.GOLD, c1.controlX2Property(), c1.controlY2Property(), size);
            controlLine1 = new BoundLine(c1.controlX2Property(), c1.controlY2Property(), c1.endXProperty(),  c1.endYProperty());
            getChildren().add(c1);
            getChildren().add(control1);
            getChildren().add(controlLine1);

            curve1.addEndPropertyListener(pos -> updateControlHandles());
            c1.endYProperty().addListener(pos -> updateControlHandles());
            curve1.setEndHandle(this);
        }

        if(curve2 != null){
            this.segment2 = curve2;
            CubicCurve c2 = curve2.getCubicCurve();
            control2 = new Anchor(Color.CADETBLUE, c2.controlX1Property(), c2.controlY1Property(), size);
            controlLine2 = new BoundLine(c2.controlX1Property(), c2.controlY1Property(), c2.startXProperty(),
                    c2.startYProperty());
            getChildren().add(c2);
            getChildren().add(control2);
            getChildren().add(controlLine2);
            curve2.setStartHandle(this);
        }
        
        if(control1 != null && control2 != null){
            
            control1.centerXProperty().addListener(pos -> {
                updateControl(control1, control2);
            });
            control1.centerYProperty().addListener(pos -> {
                updateControl(control1, control2);
            });
            //            control2.centerXProperty().addListener(pos -> {
            //                updateControl(control2, control1);
            //            });
            //            control2.centerYProperty().addListener(pos -> {
            //                updateControl(control2, control1);
            //            });
        }
        

    }

    private void updateControl(Anchor sourceAnchor, Anchor destinationAnchor) {
        if (segment2 != null) {
            Point2D midPoint = segment2.getStartPoint();

            final Point2D handleDirection = sourceAnchor.getCenter().subtract(midPoint).normalize();
            double destinationLength = Math.abs(destinationAnchor.getCenter().distance(midPoint));
            final Point2D destPoint = midPoint.subtract(handleDirection.multiply(destinationLength));
            destinationAnchor.setCenterX(destPoint.getX());
            destinationAnchor.setCenterY(destPoint.getY());

        }

    }

    public void updateControlHandles() {

        if (segment1 != null) {
            segment1.startHandle.update();
        }
        update();

        if (segment2 != null && segment2.endHandle != null) {
            segment2.endHandle.update();
        }
    }

    private void update() {
        if (segment1 != null && segment2 != null) {
            Point2D startPoint = segment1.getStartPoint();
            Point2D midPoint = segment2.getStartPoint();
            Point2D endPoint = segment2.getEndPoint();

            System.out.println(String.format("startPoint: %s", startPoint));

            final Point2D arm1Vect = midPoint.subtract(startPoint);
            final Point2D arm2Vect = midPoint.subtract(endPoint);

            final Point2D handleDirection = calcHandleDirection(arm1Vect, arm2Vect);

            double c1Length = calcControlArmLength(arm1Vect, handleDirection);
            double c2Length = calcControlArmLength(arm2Vect, handleDirection);

            System.out.println(String.format("arm1Vect: %s, arm2Vect: %s", arm1Vect, arm2Vect));
            System.out.println(String.format("c1Len: %.2f, c2Len: %.2f", c1Length, c2Length));

            Point2D c1Pos = midPoint.subtract(handleDirection.multiply(c1Length));
            Point2D c2Pos = midPoint.add(handleDirection.multiply(c2Length));

            segment1.setControl2(c1Pos);
            segment2.setControl1(c2Pos);
        }
    }

    private Point2D calcHandleDirection(final Point2D arm1Vect, final Point2D arm2Vect) {
        final Point2D arm1VectNorm = arm1Vect.normalize();
        final Point2D arm2VectNorm = arm2Vect.normalize();
        final Point2D handleDirection = arm1VectNorm.subtract(arm2VectNorm).normalize();
        return handleDirection;
        //        return arm1Vect.subtract(arm2Vect).normalize();
    }

    private double calcControlArmLength(final Point2D armVect, final Point2D handleDirection) {
        double arm2ProjLength = Math.abs(handleDirection.dotProduct(armVect));
        //      double arm1Length = arm1Vect.magnitude();
        //      double arm2Length = arm2Vect.magnitude();
        //      double smoothFactor = 0.2;
        //      double c2Length = (arm2ProjLength+armVectLength) * smoothFactor;
        double smoothFactor = 0.4;
        double c2Length = (arm2ProjLength) * smoothFactor;
        return c2Length;
    }





}
