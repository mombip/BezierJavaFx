package bezier.handles;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

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
import javafx.stage.Stage;

public class SplineWithCubic_Points extends Application {

    private Point2D basePoint = null;
    private Point2D prevPoint = null;
    private Point2D currentPoint = null;


//    private CubicCurve currentCurve = null;
private Segment prevSegment = null;
    
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
        Group root = new Group(createImage());
        root.getChildren().add(pane);
//        pane.setStyle("-fx-background-color: #" + String.format("%02x%02x%02x" , 180, 180, 180 ));
        pane.setStyle("-fx-background-color: transparent;");

       
        root.setOnMousePressed(e -> {
        	pane.requestFocus();
            currentPoint = new Point2D(e.getX(), e.getY());
            if (prevPoint != null) {
                addCurveSegmentForPoint(pane, currentPoint);
            }
        });
        root.setOnMouseDragged(e -> {
            currentPoint = new Point2D(e.getX(), e.getY());
            if(prevSegment!=null) {
            	updateCurve(basePoint, prevPoint, currentPoint, prevSegment);
            }
        });

        root.setOnMouseReleased(e -> {
            basePoint = prevPoint;
            prevPoint = currentPoint;
            prevHandle = currentHandle;
            currentPoint = null;
        });
        
        root.setOnKeyPressed(e -> {
        	if(e.getCode() == KeyCode.DELETE) {
        		pane.getChildren().clear();
        	    basePoint = null;
        	    prevPoint = null;
        	    currentPoint = null;
        	    prevSegment = null;
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
		
        Segment currentCurve = makeCurve(basePoint, prevPoint, newPoint, prevSegment);

        final CubicCurve cc = currentCurve.getCubicCurve();
        pane.getChildren().add(0, cc);

        Anchor currentStartAnchor = new Anchor(Color.PALEGREEN, cc.startXProperty(), cc.startYProperty(), radius);

        Anchor currentEndAnchor = new Anchor(Color.PALEGREEN, cc.endXProperty(), cc.endYProperty(), radius);

		pane.getChildren().add(currentStartAnchor);
		pane.getChildren().add(currentEndAnchor);

		currentHandle = new ControlHandle(prevSegment, currentCurve, radiusC);
		pane.getChildren().add(currentHandle);
		updateCurve(basePoint, prevPoint, newPoint, currentCurve);
		
		prevSegment = currentCurve;
	}



	private void printPolySplineFun(PolynomialSplineFunction psf) {
		for (PolynomialFunction pf : psf.getPolynomials()) {
		        System.out.println(pf.polynomialDerivative());  
		    }
		    System.out.println(Arrays.toString( psf.getKnots()));
	}
	
	private float pathCoordsBuffer[] = new float[6];
	

    private Segment makeCurve(Point2D basePoint, Point2D prevPoint, Point2D currentPoint, Segment prevCurve) {
        CubicCurve cc = new CubicCurve();
        cc.setStroke(Color.BLACK);
        cc.setStrokeWidth(3);
        cc.setFill(null);
        if (prevCurve != null) {
            cc.startXProperty().bindBidirectional(prevCurve.getCubicCurve().endXProperty());
            cc.startYProperty().bindBidirectional(prevCurve.getCubicCurve().endYProperty());
        }
        return new Segment(cc);
    }

    private void updateCurve(Point2D startPoint, Point2D midPoint, Point2D endPoint, Segment cc) {
    	
        if(midPoint != null && endPoint != null) {
            Point2D control1 = midPoint;
            Point2D control2 = endPoint;

            cc.setStart(midPoint);
            cc.setControl1(control1);
            cc.setControl2(control2);
            cc.setEnd(endPoint);

            if (currentHandle != null) {
                currentHandle.updateControlHandles();
            }
        }

    }

    public static void run(String[] args) {
        launch(args);
    }

}