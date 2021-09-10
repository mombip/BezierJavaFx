package bezier.handles;


import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

/** Example of how a cubic curve works, drag the anchors around to change the curve. */
public class PathManipulator extends Application {
  public static void main(String[] args) throws Exception { launch(args); }
  @Override public void start(final Stage stage) throws Exception {
    CubicCurve curve = createStartingCurve();

    Line controlLine1 = new BoundLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
    Line controlLine2 = new BoundLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(),   curve.endYProperty());

    Anchor start    = new Anchor(Color.PALEGREEN, curve.startXProperty(),    curve.startYProperty());
    Anchor control1 = new Anchor(Color.GOLD,      curve.controlX1Property(), curve.controlY1Property());
    Anchor control2 = new Anchor(Color.GOLDENROD, curve.controlX2Property(), curve.controlY2Property());
    Anchor end      = new Anchor(Color.TOMATO,    curve.endXProperty(),      curve.endYProperty());

    stage.setTitle("Cubic Curve Manipulation Sample");
    stage.setScene(new Scene(new Group(controlLine1, controlLine2, curve, start, control1, control2, end), 400, 400, Color.ALICEBLUE));
    stage.show();
  }

  List<Point2D> list = new ArrayList<>();
  
  
  private CubicCurve createStartingCurve() {
    final int sx = 100;
    final int sy = 101;
    final int c1x = 150;
    final int c1y = 50;
    final int c2x = 250;
    final int c2y = 151;
    final int ex = 300;
    final int ey = 102;
    return createCurve(sx, sy, c1x, c1y, c2x, c2y, ex, ey);
  }
private CubicCurve createCurve(final int sx, final int sy, final int c1x, final int c1y, final int c2x, final int c2y,
        final int ex, final int ey) {
    CubicCurve curve = new CubicCurve();
    curve.setStartX(sx);
    curve.setStartY(sy);
    curve.setControlX1(c1x);
    curve.setControlY1(c1y);
    curve.setControlX2(c2x);
    curve.setControlY2(c2y);
    curve.setEndX(ex);
    curve.setEndY(ey);
    curve.setStroke(Color.FORESTGREEN);
    curve.setStrokeWidth(4);
    curve.setStrokeLineCap(StrokeLineCap.ROUND);
    curve.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));
    return curve;
}

  class BoundLine extends Line {
    BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
      startXProperty().bind(startX);
      startYProperty().bind(startY);
      endXProperty().bind(endX);
      endYProperty().bind(endY);
      setStrokeWidth(2);
      setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
      setStrokeLineCap(StrokeLineCap.BUTT);
      getStrokeDashArray().setAll(10.0, 5.0);
    }
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle { 
    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }  
          double newY = mouseEvent.getY() + dragDelta.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }  
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }  
}

