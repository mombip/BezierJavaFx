package com.sun.javafx.geom;

import java.util.Arrays;
import java.util.stream.Stream;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

public class PathTest extends Application{


	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 Pane pane = new Pane();
		Canvas canvas = new Canvas(600,600); 
		pane.getChildren().add(canvas);
		 
		float [][] points = new float [][] {{24.0f, 13.0f},
			{35.0f, 45.0f},
			{67.0f, 8.0f},
			{78.0f, 45.0f},
			{89.0f, 23.0f}};

	
	    Path2D patha = new Path2D();
	    patha.moveTo(10, 10);
	    Stream.of(points).forEach(point -> {
	    	pane.getChildren().add(new Circle(point[0], point[1], 2));
	    	System.out.println(String.format("->pt: (%.2f, %.2f)",point[0], point[1]));
	    	//printPath("A:",path);
//	    	patha.quadToSmooth(point[0], point[1]);
	    	patha.curveToSmooth(point[0], point[1], (float)(point[0]+Math.random()*10f), (float)( point[1]+Math.random()*10f));
	    	printPath("B:",patha);
	    });
	    
	    
	    
	    PathIterator pi = patha.getPathIterator(null);
	    while (!pi.isDone()) {
	    	int pitype = pi.currentSegment(pathCoordsBuffer);
	    	System.out.println(Arrays.toString(pathCoordsBuffer));
	    	pi.next();
	    }
	    
	    
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    
	    Path shape = drawShape(gc, patha);
	    pane.getChildren().add(shape);
	    Scene scene = new Scene(pane, 600, 600);
	    primaryStage.setScene(scene);
	    primaryStage.show();
//    path.quadToSmooth(0, 0);
	}

	private void printPath(String prefix, Path2D path) {
		System.out.println(String.format("%sprev: (%.2f, %.2f) curr: (%.2f, %.2f)",prefix,
				path.prevX, path.prevY,
				path.currX, path.currY
				));
	}
	
	private float pathCoordsBuffer[] = new float[6];
	
	private Path drawShape(GraphicsContext context, Shape s) {
		Path shape = new Path();
	
	    context.beginPath();
	    PathIterator pi = s.getPathIterator(null);
	    while (!pi.isDone()) {
	      int pitype = pi.currentSegment(pathCoordsBuffer);
	      switch (pitype) {
	        case PathIterator.SEG_MOVETO:
	        	context.moveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
	        	shape.getElements().add(new MoveTo(pathCoordsBuffer[0], pathCoordsBuffer[1]));
	          break;
	        case PathIterator.SEG_LINETO:
	          context.lineTo(pathCoordsBuffer[0], pathCoordsBuffer[1]);
	          break;
	        case PathIterator.SEG_QUADTO:
	          context.quadraticCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
	                                   pathCoordsBuffer[2], pathCoordsBuffer[3]);
	          shape.getElements().add(new QuadCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
                      pathCoordsBuffer[2], pathCoordsBuffer[3]));
	          break;
	        case PathIterator.SEG_CUBICTO:
	          context.bezierCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
	                                pathCoordsBuffer[2], pathCoordsBuffer[3],
	                                pathCoordsBuffer[4], pathCoordsBuffer[5]);
	          shape.getElements().add(new CubicCurveTo(pathCoordsBuffer[0], pathCoordsBuffer[1],
	        		  pathCoordsBuffer[2], pathCoordsBuffer[3],
	        		  pathCoordsBuffer[4], pathCoordsBuffer[5]));
	          break;
	        case PathIterator.SEG_CLOSE:
	          context.closePath();
	          break;
	        default:
	          //showWarning("Unknown segment type " + pitype);
	      }
	      pi.next();
	    }
	   
//	    context.stroke();
	    return shape;
	  }

	public static void run(String[] args) {
		PathTest.launch(args);
	}

}
