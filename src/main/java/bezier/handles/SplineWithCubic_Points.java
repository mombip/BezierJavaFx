package bezier.handles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

public class SplineWithCubic_Points extends Application {

    private Point2D basePoint = null;
    private Point2D prevPoint = null;
    private Point2D currentPoint = null;


//    private CubicCurve currentCurve = null;
    private CubicCurve prevCurve = null;
    
//    private Circle c = new Circle(100,100,3);

//    Anchor prevC2, currentC1, currentC2 = null;
    ControlHandle prevHandle, currentHandle;

    private double radius = 8f;
    private double radiusC = radius*0.6;
    
    private List<Point2D> points = new ArrayList<>();
    private List<CubicCurve> splineSegments = new ArrayList<>();

    @Override 
    public void start(Stage primaryStage) throws FileNotFoundException {
        Pane pane = new Pane();
//        Group root = new Group(pane);
//        root.getChildren().add(createImage());
        Group root = new Group(createImage());
        root.getChildren().add(pane);
//        pane.setStyle("-fx-background-color: #" + String.format("%02x%02x%02x" , 180, 180, 180 ));
        pane.setStyle("-fx-background-color: transparent;");
//        pane.getChildren().add(c);
       
        root.setOnMousePressed(e -> {
        	pane.requestFocus();
            currentPoint = new Point2D(e.getX(), e.getY());
//            points.add(currentPoint);
            if (prevPoint != null) {
                addCurveSegmentForPoint(pane, currentPoint);
            }
        });
        root.setOnMouseDragged(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
//            updateCurve(basePoint, prevPoint, currentPoint, currentCurve);
            if(prevCurve!=null) {
            	updateCurve(basePoint, prevPoint, currentPoint, prevCurve);
            }
        });

        root.setOnMouseReleased(e -> {
            basePoint = prevPoint;
            prevPoint = currentPoint;
//            prevCurve = currentCurve;
            prevHandle = currentHandle;
            currentPoint = null;
        });
        
        root.setOnKeyPressed(e -> {
        	if(e.getCode() == KeyCode.DELETE) {
        		pane.getChildren().clear();
        	    basePoint = null;
        	    prevPoint = null;
        	    currentPoint = null;
        	    prevCurve = null;
        	    prevHandle = null;
        	}
        });

        Scene scene = new Scene(root,1600, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

	private ImageView createImage() throws FileNotFoundException {
	    InputStream stream = this.getClass().getClassLoader().getResourceAsStream("spline.png");
	      Image image = new Image(stream);
	      //Creating the image view
	      ImageView imageView = new ImageView();
	      //Setting image to the image view
	      imageView.setImage(image);
	      //Setting the image view parameters
	      imageView.setX(10);
	      imageView.setY(10);
	      imageView.setFitWidth(575);
	      imageView.setPreserveRatio(true);
	      return imageView;
        
    }

    private void addCurveSegmentForPoint(Pane pane, Point2D newPoint) {
		System.out.println("line1" + prevPoint);
		System.out.println("line2" + newPoint);
		

		CubicCurve currentCurve = makeCurve(basePoint, prevPoint, newPoint, prevCurve);

		pane.getChildren().add(0, currentCurve);
		Anchor currentStartAnchor = new Anchor(Color.PALEGREEN, currentCurve.startXProperty(),
		        currentCurve.startYProperty(), radius);
		pane.getChildren().add(currentStartAnchor);
		Anchor currentEndAnchor = new Anchor(Color.PALEGREEN, currentCurve.endXProperty(),
		        currentCurve.endYProperty(), radius);
		pane.getChildren().add(currentEndAnchor);

		currentHandle = new ControlHandle(prevCurve, currentCurve, radiusC);
		pane.getChildren().add(currentHandle);
		updateCurve(basePoint, prevPoint, newPoint, currentCurve);
		
		prevCurve = currentCurve;

//		addSpline(pane);
//		updateSpline(splineSegments);
	}

	private void updateSpline(List<CubicCurve> splineSegments2) {
		
//		double[] x = splineSegments.stream()
//			.mapToDouble(curve -> curve.getStartX())
//			.toArray();
//		
//		double[] y = splineSegments.stream()
//				.mapToDouble(curve -> curve.getStartY())
//				.toArray();
//		
//		ClampedSplineInterpolator asi = new ClampedSplineInterpolator();
//		PolynomialSplineFunction psf = asi.interpolate(x, y, 0,0);
//		
////		Path2D path = new Path2D();
//////		path.
////		splineSegments.stream().forEach(c -> path.quadToSmooth((float)c.getStartX(), (float)c.getStartY()));
//		
//		 printPolySplineFun(psf);

		
	}

	private void printPolySplineFun(PolynomialSplineFunction psf) {
		for (PolynomialFunction pf : psf.getPolynomials()) {
		        System.out.println(pf.polynomialDerivative());  
		    }
		    System.out.println(Arrays.toString( psf.getKnots()));
	}
	
	private float pathCoordsBuffer[] = new float[6];
	private Path toPath( Shape s) {
		Path path = new Path();
	
//	    context.beginPath();
	    PathIterator pi = s.getPathIterator(null);
	    while (!pi.isDone()) {
	      int pitype = pi.currentSegment(pathCoordsBuffer);
	      switch (pitype) {
	        case PathIterator.SEG_MOVETO:
//	        	context.moveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
	        	path.getElements().add(new MoveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]));
	          break;
	        case PathIterator.SEG_LINETO:
//	          context.lineTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
	          break;
	        case PathIterator.SEG_QUADTO:
//	          context.quadraticCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
//	                                   pathCoordsBuffer[2], pathCoordsBuffer[3]);
	          path.getElements().add(new QuadCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
                      pathCoordsBuffer[2], pathCoordsBuffer[3]));
	          break;
	        case PathIterator.SEG_CUBICTO:
//	          context.bezierCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
//	                                pathCoordsBuffer[2], pathCoordsBuffer[3],
//	                                pathCoordsBuffer[4], pathCoordsBuffer[5]);
	          break;
	        case PathIterator.SEG_CLOSE:
	          //context.closePath();
	         
	          break;
	        default:
	          //showWarning("Unknown segment type " + pitype);
	      }
	      pi.next();
	    }
	   
//	    context.stroke();
	    return path;
	  }

//	private void addSpline(Pane pane) {
//		CubicCurve spline = new CubicCurve(currentCurve.getStartX(),
//				currentCurve.getStartY(),
//				currentCurve.getControlX1(),
//				currentCurve.getControlY1(),
//				currentCurve.getControlX2(),
//				currentCurve.getControlY2(),
//				currentCurve.getEndX(),
//				currentCurve.getEndY());
//		
//		spline.startXProperty().bind(currentCurve.startXProperty());
//		spline.startYProperty().bind(currentCurve.startYProperty());
//		spline.controlX1Property().bind(currentCurve.controlX1Property());
//		spline.controlY1Property().bind(currentCurve.controlY1Property());
//		spline.controlX2Property().bind(currentCurve.controlX2Property());
//		spline.controlY2Property().bind(currentCurve.controlY2Property());
//		spline.endXProperty().bind(currentCurve.endXProperty());
//		spline.endYProperty().bind(currentCurve.endYProperty());
//		
//		spline.setStroke(Color.RED);
//		spline.setFill(null);
//		splineSegments.add(spline);
//		
//		pane.getChildren().add(spline);
//	}

    private CubicCurve makeCurve(Point2D basePoint, Point2D prevPoint, Point2D currentPoint, CubicCurve prevCurve) {
        CubicCurve cc = new CubicCurve();
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
            Point2D control1 = midPoint;
            Point2D control2 = endPoint;
            if(startPoint != null ) {
            	
//                final Point2D baseVect = endPoint.subtract(startPoint);
//                final Point2D baseVectNorm = baseVect.normalize();
                
                final Point2D arm1Vect = midPoint.subtract(startPoint);
//                double firstArmLength = arm1Vect.magnitude();
                final Point2D arm2Vect = midPoint.subtract(endPoint);
				double arm2Length = arm2Vect.magnitude();
    				
                final Point2D arm1VectNorm = arm1Vect.normalize();
                final Point2D arm2VectNorm = arm2Vect.normalize();
                final Point2D c1Direction = arm1VectNorm.subtract(arm2VectNorm).normalize();
//                final Point2D c1Direction = baseVectNorm;
                

                double arm2ProjLength = Math.abs(c1Direction.dotProduct(arm2Vect)); 
//                double smoothFactor = 0.2;
//                double c1Length = (arm2ProjLength + arm2Length) * smoothFactor;
                double smoothFactor = 0.4;
                double c1Length = (arm2ProjLength) * smoothFactor;

                control1 = midPoint.add(c1Direction.multiply(c1Length));
                if (currentHandle != null) {
                    currentHandle.updateC2PosAutosize(control1);
                }
            }
            cc.setStartX(midPoint.getX());
            cc.setStartY(midPoint.getY());
            cc.setControlX1(control1.getX());
            cc.setControlY1(control1.getY());
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