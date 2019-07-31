import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;


public class KdTree {
    private Node root;
    private int size;
    private RectHV rootRect;
    private Point2D nearest;



    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left;        // the left/bottom subtree
        private Node right;// the right/top subtree
        private boolean isVertical;


        private Node(Point2D point,  boolean isVertical, RectHV rect){
            this.rect =rect;
            this.p = point;
            this.isVertical = isVertical;
        }
    }

    public  KdTree(){
        root = null;
        size = 0;
        rootRect = new RectHV(0,0,1,1);
    }// construct an empty set of points

    public  boolean isEmpty() {
        return root == null;
    }                     // is the set empty?

    public  int size(){
        return size;
    }// number of points in the set


    public void insert(Point2D p){
        checkNull(p);
        root = insert(root, p, true, rootRect);
    }// add the point to the set (if it is not already in the set)

    private  Node  insert(Node x, Point2D point, Boolean isVertical, RectHV rect){

        if (x == null) {
            size++;
            return new Node(point,isVertical,rect);
        }

        double cmpX = point.x()- x.p.x();
        double cmpY = point.y() - x.p.y();
        double xmin = x.rect.xmin();
        double xmax = x.rect.xmax();
        double ymin = x.rect.ymin();
        double ymax = x.rect.ymax();

        if (cmpX ==0 && cmpY== 0)
            return x;
        if (x.isVertical && cmpX < 0 || !x.isVertical && cmpY < 0){
            if (x.left != null)
                x.left = insert(x.left, point, false, x.left.rect);
            else {
                    if (x.isVertical && cmpX < 0 ){
                        RectHV newRect = new RectHV(xmin,ymin, x.p.x(), ymax);
                        x.left = insert(x.left, point, false, newRect);
                    }
                    else {
                        RectHV newRect = new RectHV(xmin, ymin, xmax, x.p.y());
                        x.left = insert(x.left, point, true, newRect);
                    }
                }
            }

        if (x.isVertical && cmpX >= 0 || !x.isVertical && cmpY >= 0) {
            if (x.right != null)
                    x.right = insert(x.right, point, false, x.right.rect);
                else {
                    if (x.isVertical && cmpX >= 0){
                        RectHV newRect = new RectHV(x.p.x(),ymin, xmax, ymax);
                        x.right = insert(x.right, point, false,newRect);
                    }
                    else {
                        RectHV newRect = new RectHV(xmin, x.p.y(), xmax, ymax);
                        x.right = insert(x.right, point, true, newRect);
                    }
                }
            }
//        else {
//            if (cmpY < 0) {
//                if (x.left != null)
//                    x.left = insert(x.left, point, false, x.left.rect);
//                else { RectHV newRect = new RectHV(xmin, ymin, xmax, x.p.y());
//                    x.left = insert(x.left, point, true, newRect);
//                }
//            }
//            else {
//                if (x.right != null)
//                    x.right = insert(x.right, point, false, x.right.rect);
//                else {
//                RectHV newRect = new RectHV(xmin, x.p.y(), xmax, ymax);
//                x.right = insert(x.right, point, true, newRect);
//                }
//            }
//        }
        return x;
    }

    public  boolean contains(Point2D p){
        checkNull(p);
        return get(p) != null;
    }// does the set contain point p?

    private Point2D get(Point2D p){
        return search(root, p);
    }
    private Point2D search(Node x, Point2D point){
        checkNull(point);
        if(x==null)
            return null;
        double cmp = compareTo(point,x,x.isVertical);
//        double cmpX = point.x()- x.p.x();
//        double cmpY = point.y() - x.p.y();
        if (x.p.equals(point))
            return x.p;
        else if (cmp <0)
            return search(x.left,point);
        else return search(x.right,point);
    }

    public void draw(){
        drawTree(root);
    }// draw all points to standard draw

    private void drawTree(Node x){
            if (x == null) return;
            x.left.p.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            x.p.draw();
            StdDraw.setPenRadius();
            if (x.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            }
            x.right.p.draw();
        }

    public Iterable<Point2D> range(RectHV rect){
        checkNull(rect);
        Queue<Point2D> queue = new Queue<>();
        rangeSearch(root, rect, queue);
        return queue;
    }// all points that are inside the rectangle (or on the boundary)

    private void rangeSearch(Node x,RectHV rect, Queue<Point2D> queue){
        if (x == null) return;
        if (!rect.intersects(rootRect)) return;
        boolean left = x.isVertical && rect.xmin() < x.p.x() ||
                    !x.isVertical && rect.ymin() < x.p.y ();

        boolean right = x.isVertical && rect.xmax() >= x.p.x() ||
                        !x.isVertical && rect.ymax() >= x.p.y();
        if (left)
            rangeSearch(x.left,rect,queue);
        if (rect.contains(x.p))
            queue.enqueue(x.p);
        if (right)
            rangeSearch(x.right,rect,queue);
        }

    public  Point2D nearest(Point2D p){
        checkNull(p);
        if (isEmpty()) return null;
        return  nearest(root,p,root.p);
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private  Point2D nearest(Node x, Point2D queryPoint, Point2D nearest){
        if (x==null)
            return nearest;
       if (x.p.equals(queryPoint))
            return queryPoint;
        double distanceToCurrent = x.p.distanceSquaredTo(queryPoint);
        if (distanceToCurrent < nearest.distanceSquaredTo(queryPoint))
            nearest = x.p;

        double distanceToRect = compareTo(queryPoint,x,x.isVertical);
        if (distanceToRect<0) {
            nearest =nearest(x.left,queryPoint,nearest);
            if (x.right!=null && nearest.distanceSquaredTo(queryPoint) >=
                    x.right.rect.distanceSquaredTo(queryPoint))
            nearest =nearest(x.right,queryPoint,nearest);
        }
        else {
            nearest = nearest(x.right,queryPoint,nearest);
            if (x.left!=null && nearest.distanceSquaredTo(queryPoint) >=
                   x.left.rect.distanceSquaredTo(queryPoint))
                nearest = nearest(x.left,queryPoint,nearest);
        }
        return nearest;
    }

    private double compareTo(Point2D p, Node x, boolean isVertical){
        if (isVertical)
            return p.x() - x.p.x();
        else
            return p.y() - x.p.y();
    }

    public static void main(String[] args){
        KdTree kt = new KdTree();
       In in = new In(args[0]);// initialize the data structures with N points from standard input
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kt.insert(p);
        }
        RectHV rect = new RectHV(0,0,1,1);
        StdOut.println(kt.range(rect));
        Point2D queryPoint = new Point2D(0.5,0.45);
        StdOut.println(kt.nearest(queryPoint));
    }
    private void checkNull(Object y){
        if (y == null)
            throw new java.lang.IllegalArgumentException();
    }
}
