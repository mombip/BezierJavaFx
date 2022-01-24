package bezier.handles;


import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class ControlHandle extends Group {

    private CubicCurve curve1;
    private CubicCurve curve2;
    private Line controlLine1;
    private Line controlLine2;
    private Anchor control1;
    Point2D control2pt;
    private Anchor control2;


    public ControlHandle(CubicCurve curve1, CubicCurve curve2, double size) {
        Text txt = new Text(""+this.hashCode());
        txt.setX(curve2.getStartX()+10);
        txt.setY(curve2.getStartY()+10);
        getChildren().add(txt);
        if(curve1 != null){
            this.curve1 = curve1;
            control1 = new Anchor(Color.GOLD,      curve1.controlX2Property(), curve1.controlY2Property(), size);
            controlLine1 = new BoundLine(curve1.controlX2Property(), curve1.controlY2Property(), curve1.endXProperty(),   curve1.endYProperty());
            getChildren().add(this.curve1);
            getChildren().add(control1);
            getChildren().add(controlLine1);

        }

        if(curve2 != null){
            this.curve2 = curve2;
            control2 = new Anchor(Color.CADETBLUE, curve2.controlX1Property(), curve2.controlY1Property(), size);
            controlLine2 = new BoundLine(curve2.controlX1Property(), curve2.controlY1Property(), curve2.startXProperty(), curve2.startYProperty());
            getChildren().add(this.curve2);
            getChildren().add(control2);
            getChildren().add(controlLine2);
        }
        
        if(control1 != null && control2 != null){
            
            control1.centerXProperty().addListener(pos -> {
                updateAnchor(control2, control1.getCenter());
            });
            control1.centerYProperty().addListener(pos -> {
                updateAnchor(control2, control1.getCenter());
            });
//            control2.centerXProperty().addListener(pos -> {
//                updateAnchor(control1, control2.getCenter());
//            });
//            control2.centerYProperty().addListener(pos -> {
//                updateAnchor(control1, control2.getCenter());
//            });
        }
        
    }


    public void updateC2PosAutosize(Point2D pos) {
        System.out.println("mod:"+this.hashCode());
        if(control1 != null){
            control2.centerXProperty().set(pos.getX());
            control2.centerYProperty().set(pos.getY());
            
            updateAnchor(this.control1, control2.getCenter() );
        }
    }

    private double getAutoC2Length(Point2D c2Point) {
    	Point2D startPoint = new Point2D(curve1.getStartX(), curve1.getStartY());
    	Point2D midPoint = new Point2D(curve2.getStartX(), curve2.getStartY());
    	Point2D endPoint = new Point2D(curve2.getEndX(), curve2.getEndY());
    	
//    	 final Point2D baseVect = endPoint.subtract(startPoint);
//         final Point2D baseVectNorm = baseVect.normalize();
//         Point2D c2Direction = baseVectNorm;
    	Point2D c2Direction = midPoint.subtract(c2Point).normalize();
    	final Point2D armVect = midPoint.subtract(startPoint);
        double armVectLength = armVect.magnitude();
    	
        double arm2ProjLength = Math.abs(c2Direction.dotProduct(armVect)); 
//        double smoothFactor = 0.2;
//        double c2Length = (arm2ProjLength+armVectLength) * smoothFactor;
        double smoothFactor = 0.4;
        double c2Length = (arm2ProjLength) * smoothFactor;
    	
		return c2Length;
    }

//    public void updateAnchor(Anchor c2Anchor, Point2D otherAnchorPos) {
//        Point2D midPoint = new Point2D(curve2.getStartX(), curve2.getStartY());
//        double c1Length = c2Anchor.getCenter().subtract(midPoint).magnitude();
//        updateAnchor(c2Anchor, otherAnchorPos, c1Length);
//    }

    private void updateAnchor(Anchor anchor, Point2D otherAnchorPos) {
        Point2D midPoint = new Point2D(curve2.getStartX(), curve2.getStartY());
        Point2D c2Direction = midPoint.subtract(otherAnchorPos).normalize();
        
        final double c2Length = getAutoC2Length(control2.getCenter());
        
        Point2D c2Pos = midPoint.add(c2Direction.multiply(c2Length));
        anchor.centerXProperty().set(c2Pos.getX());
        anchor.centerYProperty().set(c2Pos.getY());
    }
}
