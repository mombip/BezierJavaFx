package bezier.handles;

import javafx.beans.InvalidationListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;

public class Segment {

    CubicCurve cubicCurve;
    ControlHandle startHandle, endHandle;

    public Segment(CubicCurve cc) {
        this.cubicCurve = cc;
    }

    public void setStart(Point2D midPoint) {
        cubicCurve.setStartX(midPoint.getX());
        cubicCurve.setStartY(midPoint.getY());
    }

    public void setControl1(Point2D control1) {
        cubicCurve.setControlX1(control1.getX());
        cubicCurve.setControlY1(control1.getY());
    }

    public void setControl2(Point2D control2) {
        cubicCurve.setControlX2(control2.getX());
        cubicCurve.setControlY2(control2.getY());
    }

    public void setEnd(Point2D endPoint) {
        cubicCurve.setEndX(endPoint.getX());
        cubicCurve.setEndY(endPoint.getY());
    }

    public CubicCurve getCubicCurve() {
        return cubicCurve;
    }

    public ControlHandle getEndHandle() {
        return endHandle;
    }

    public ControlHandle getStartHandle() {
        return startHandle;
    }

    public void setEndHandle(ControlHandle endHandle) {
        this.endHandle = endHandle;
    }

    public void setStartHandle(ControlHandle startHandle) {
        this.startHandle = startHandle;
    }

    public void addEndPropertyListener(InvalidationListener listener) {
        cubicCurve.endXProperty().addListener(listener);
        cubicCurve.endYProperty().addListener(listener);
    }

    public void addStartPropertyListener(InvalidationListener listener) {
        cubicCurve.startXProperty().addListener(listener);
        cubicCurve.startYProperty().addListener(listener);
    }

    public Point2D getStartPoint() {
        return new Point2D(cubicCurve.getStartX(), cubicCurve.getStartY());
    }

    public Point2D getEndPoint() {
        return new Point2D(cubicCurve.getEndX(), cubicCurve.getEndY());
    }

}
