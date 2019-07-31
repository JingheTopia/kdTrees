import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointsTree;
    public   PointSET(){
        pointsTree = new TreeSet<Point2D>();
    }                               // construct an empty set of points
    public   boolean isEmpty(){
        return pointsTree.isEmpty();
    }                      // is the set empty?
    public   int size(){
        return pointsTree.size();
    }// number of points in the set

    public void insert(Point2D p){
        checkNull(p);
        if (!pointsTree.contains(p))
            pointsTree.add(p);
    }       // add the point to the set (if it is not already in the set)

    public  boolean contains(Point2D p){
        checkNull(p);
        return pointsTree.contains(p);
    }            // does the set contain point p// ?

    public  void draw(){
        for (Point2D p : pointsTree) {
            p.draw();
        }
    }                         // draw all points to standard draw

    private void checkNull(Object y){
        if (y == null)
            throw new java.lang.IllegalArgumentException();
    }

    public Iterable<Point2D> range(RectHV rect){
        checkNull(rect);
        LinkedList<Point2D> list = new LinkedList<>();
        Point2D pointMin = new Point2D(rect.xmin(), rect.ymin());
        Point2D pointMax = new Point2D(rect.xmax(), rect.ymax());
        for (Point2D p: pointsTree.subSet(pointMin,true,pointMax,true))
           if (rect.contains(p)){
               list.add(p);
           }
        return list;
    }// all points that are inside the rectangle (or on the boundary)

    public  Point2D nearest(Point2D p){
        checkNull(p);
        if (isEmpty())
            return null;
        Point2D nearest;

        if (pointsTree.ceiling(p) == null && pointsTree.floor(p) == null)
            return null;
        else if (pointsTree.ceiling(p)!=null)
             nearest = pointsTree.ceiling(p);
        else nearest = pointsTree.floor(p);

        for (Point2D candidate : pointsTree){
            if (p.distanceTo(candidate) < p.distanceTo(nearest))
            nearest = candidate;
        }
        return  nearest;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        // initialize the two data structures with point from file
//        In in = new In(args[0]);// initialize the data structures with N points from standard input
//        PointSET brute = new PointSET();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            brute.insert(p);
//        }
//
//        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
//        double x1 = 0.0, y1 = 0.0;      // current location of mouse
//        boolean isDragging = false;     // is the user dragging a rectangle
//
//        // draw the points
//        StdDraw.clear();
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(.01);
//        brute.draw();
//
//        while (true) {
//            StdDraw.show();
//
//            // user starts to drag a rectangle
//            if (StdDraw.isMousePressed() && !isDragging) {
//                x0 = StdDraw.mouseX();
//                y0 = StdDraw.mouseY();
//                isDragging = true;
//                continue;
//            }
//
//            // user is dragging a rectangle
//            else if (StdDraw.isMousePressed() && isDragging) {
//                x1 = StdDraw.mouseX();
//                y1 = StdDraw.mouseY();
//                continue;
//            }
//
//            // mouse no longer pressed
//            else if (!StdDraw.isMousePressed() && isDragging) {
//                isDragging = false;
//            }
//
//
//            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
//                    Math.max(x0, x1), Math.max(y0, y1));
//            // draw the points
//            StdDraw.clear();
//            StdDraw.setPenColor(StdDraw.BLACK);
//            StdDraw.setPenRadius(.01);
//            brute.draw();
//
//            // draw the rectangle
//            StdDraw.setPenColor(StdDraw.BLACK);
//            StdDraw.setPenRadius();
//            rect.draw();
//
//            // draw the range search results for brute-force data structure in red
//            StdDraw.setPenRadius(.03);
//            StdDraw.setPenColor(StdDraw.RED);
//            for (Point2D p : brute.range(rect))
//                p.draw();
//
//            // draw the range search results for kd-tree in blue
//            StdDraw.setPenRadius(.02);
//            StdDraw.setPenColor(StdDraw.BLUE);
//            //for (Point2D p : kdtree.range(rect))
//            //  p.draw();
//
//            StdDraw.show();
//        }
        Point2D[] points = new Point2D[9];
        points[0] = new Point2D(0.1,0.2);
        points[1] = new Point2D(0.1,0.3);
        points[2] = new Point2D(0.3,0.2);
        points[3] = new Point2D(0.4,0.5);
        points[4] = new Point2D(0.3,0.5);
        points[5] = new Point2D(0.6,0.2);
        points[6] = new Point2D(0.1,0.8);
        points[7] = new Point2D(0.6,0.8);
        points[8] = new Point2D(0.5,0.5);

        PointSET brute = new PointSET();
        for (Point2D p : points)
            brute.insert(p);

        RectHV rect = new RectHV(0.2,0.1,0.3,0.5);
        StdOut.println(brute.range(rect));
        Point2D point = new Point2D(0.45,0.45);
        StdOut.println(brute.nearest(point));

    }// unit testing of the methods (optional)
}
