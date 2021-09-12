package bezier.handles;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

class BoundLine extends Line {
    public BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
      startXProperty().bind(startX);
      startYProperty().bind(startY);
      endXProperty().bind(endX);
      endYProperty().bind(endY);
      setStrokeWidth(2);
      setStroke(Color.DIMGRAY);
      setStrokeLineCap(StrokeLineCap.BUTT);
      getStrokeDashArray().setAll(10.0, 5.0);
    }

    public BoundLine(Anchor p1, Anchor p2){
        this(p1.centerXProperty(), p1.centerYProperty(), p1.centerXProperty(), p1.centerYProperty());
    }


  }
