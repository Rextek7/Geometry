import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Point {
  public double x;
  public double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double x() {
    return this.x;
  }

  public double y() {
    return this.y;
  }

  public void rotate(Point a, double angle) {
    double deltX = x;
    double deltY = y;
    x = a.getX() + ((deltX - a.getX()) * Math.cos(angle)) - (deltY - a.getY()) * Math.sin(angle);
    y = a.getY() + ((deltX - a.getX()) * Math.sin(angle)) + (deltY - a.getY()) * Math.cos(angle);
  }

  public static double length(Point a, Point b) {
    return Math.sqrt(
        (a.getX() - b.getX()) * (a.getX() - b.getX())
            + (a.getY() - b.getY()) * (a.getY() - b.getY()));
  }
}

abstract class Shape {

  public abstract Point center();

  public abstract double perimeter();

  public abstract double area();

  public abstract void translate(final Point newCenter);

  public abstract void rotate(final double angle);

  public abstract void scale(final double coefficient);
}

class Ellipse extends Shape {
  public Point A;
  public Point B;
  public double perifocalDistance;

  public Ellipse(Point A, Point B, double perifocalDistance) {
    this.A = A;
    this.B = B;
    this.perifocalDistance = perifocalDistance;
  }

  public List<Point> focuses() {
    List<Point> focuses = new ArrayList<>();
    focuses.add(A);
    focuses.add(B);
    return focuses;
  }

  public double focalDistance() {
    return (Point.length(A, B) / 2);
  }

  public double majorSemiAxis() {
    return focalDistance() + perifocalDistance;
  }

  public double minorSemiAxis() {
    return Math.sqrt(Math.pow(majorSemiAxis(), 2) - Math.pow(focalDistance(), 2));
  }

  public double eccentricity() {
    return (focalDistance() / majorSemiAxis());
  }

  @Override
  public Point center() {
    return new Point((A.getX() + B.getX()) / 2, (A.getY() + B.getY()) / 2);
  }

  @Override
  public double perimeter() {
    return ((4 * Math.PI * this.majorSemiAxis() * this.minorSemiAxis()
            + 4
                * (this.majorSemiAxis() - this.minorSemiAxis())
                * (this.majorSemiAxis() - this.minorSemiAxis()))
        / (this.majorSemiAxis() + this.minorSemiAxis()));
  }

  @Override
  public double area() {
    return Math.PI * this.minorSemiAxis() * this.majorSemiAxis();
  }

  @Override
  public void translate(final Point newCenter) {
    double moved_X = newCenter.getX() - center().getX();
    double moved_Y = newCenter.getY() - center().getY();
    A = new Point(A.getX() + moved_X, A.getY() + moved_Y);
    B = new Point(B.getX() + moved_X, B.getY() + moved_Y);
  }

  @Override
  public void rotate(final double angle) {
    Point predCent = center();
    this.B.rotate(predCent, angle);
    this.A.rotate(predCent, angle);
  }

  @Override
  public void scale(final double coefficient) {
    Point Pre_Centre = this.center();
    A =
        new Point(
            Pre_Centre.getX() + coefficient * (A.getX() - Pre_Centre.getX()),
            Pre_Centre.getY() + coefficient * (A.getY() - Pre_Centre.getY()));
    B =
        new Point(
            Pre_Centre.getX() + coefficient * (B.getX() - Pre_Centre.getX()),
            Pre_Centre.getY() + coefficient * (B.getY() - Pre_Centre.getY()));
    perifocalDistance *= Math.abs(coefficient);
  }
}

class Circle extends Ellipse {
  public Circle(Point cent, double rad) {
    super(cent, cent, rad);
  }

  public double radius() {
    return perifocalDistance;
  }

  @Override
  public Point center() {
    return A;
  }

  @Override
  public void translate(final Point newCenter) {
    A = new Point(newCenter.x, newCenter.y);
    B = new Point(newCenter.x, newCenter.y);
  }

  @Override
  public void scale(final double coefficient) {
    perifocalDistance *= Math.abs(coefficient);
  }
}

class Rectangle extends Shape {
  public Point a;
  public Point b;
  public double side;

  public Rectangle(Point a, Point b, double side) {
    this.a = a;
    this.b = b;
    this.side = side;
  }

  public double firstSide() {
    return Point.length(this.a, this.b);
  }

  public double secondSide() {
    return this.side;
  }

  public List<Point> vertices() {
    double half_side = side / 2;
    if (a.getX() > b.getX() && a.getY() > b.getY()) {
      double offset_X = a.getX() - b.getX();
      double offset_Y = a.getY() - b.getY();
      double angle = Math.atan(offset_Y / offset_X);
      double delX = half_side * Math.sin(angle);
      double delY = half_side * Math.cos(angle);
      return new ArrayList<>(
          Arrays.asList(
              new Point(a.getX() - delX, a.getY() + delY),
              new Point(b.getX() - delX, b.getY() + delY),
              new Point(b.getX() + delX, b.getY() - delY),
              new Point(a.getX() + delX, a.getY() - delY)));
    } else if (a.getX() > b.getX() && a.getY() < b.getY()) {
      double offset_X = b.getX() - a.getX();
      double offset_Y = b.getY() - a.getY();
      double angle = Math.atan(Math.abs(offset_Y / offset_X));
      double delX = half_side * Math.sin(angle);
      double delY = half_side * Math.cos(angle);
      return new ArrayList<>(
          Arrays.asList(
              new Point(a.getX() + delX, a.getY() + delY),
              new Point(b.getX() + delX, b.getY() + delY),
              new Point(b.getX() - delX, b.getY() - delY),
              new Point(a.getX() - delX, a.getY() - delY)));
    } else if (a.getX() < b.getX() && a.getY() > b.getY()) {
      double offset_X = a.getX() - b.getX();
      double offset_Y = a.getY() - b.getY();
      double angle = Math.atan(Math.abs(offset_Y / offset_X));
      double delX = half_side * Math.sin(angle);
      double delY = half_side * Math.cos(angle);
      return new ArrayList<>(
          Arrays.asList(
              new Point(a.getX() - delX, a.getY() - delY),
              new Point(b.getX() - delX, b.getY() - delY),
              new Point(b.getX() + delX, b.getY() + delY),
              new Point(a.getX() + delX, a.getY() + delY)));
    } else {
      double offset_X = b.getX() - a.getX();
      double offset_Y = b.getY() - a.getY();
      double angle = Math.atan((offset_Y / offset_X));
      double delX = half_side * Math.sin(angle);
      double delY = half_side * Math.cos(angle);
      return new ArrayList<>(
          Arrays.asList(
              new Point(a.getX() + delX, a.getY() - delY),
              new Point(b.getX() + delX, b.getY() - delY),
              new Point(b.getX() - delX, b.getY() + delY),
              new Point(a.getX() - delX, a.getY() + delY)));
    }
  }

  public double diagonal() {
    return Math.sqrt(Math.pow(firstSide(), 2) + Math.pow(secondSide(), 2));
  }

  @Override
  public Point center() {
    return new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
  }

  @Override
  public double perimeter() {
    return Point.length(a, b) * 2 + side * 2;
  }

  @Override
  public double area() {
    return Point.length(a, b) * side;
  }

  @Override
  public void translate(final Point newCenter) {
    double pereX = newCenter.getX() - center().getX();
    double pereY = newCenter.getY() - center().getY();
    a = new Point(a.getX() + pereX, a.getY() + pereY);
    b = new Point(b.getX() + pereX, b.getY() + pereY);
  }

  @Override
  public void rotate(final double angle) {
    Point Pre_Centre = this.center();
    this.b.rotate(Pre_Centre, angle);
    this.a.rotate(Pre_Centre, angle);
  }

  @Override
  public void scale(final double coefficient) {
    Point Pre_Centre = this.center();
    a =
        new Point(
            Pre_Centre.getX() + coefficient * (a.getX() - Pre_Centre.getX()),
            Pre_Centre.getY() + coefficient * (a.getY() - Pre_Centre.getY()));
    b =
        new Point(
            Pre_Centre.getX() + coefficient * (b.getX() - Pre_Centre.getX()),
            Pre_Centre.getY() + coefficient * (b.getY() - Pre_Centre.getY()));
    side *= Math.abs(coefficient);
  }
}

class Square extends Rectangle {
  public Square(Point a, Point b) {
    super(a, b, Point.length(a, b));
  }

  public double side() {
    return Point.length(a, b);
  }

  public Circle circumscribedCircle() {
    double radius = side() * Math.sqrt(2) / 2;
    Point center = new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    return new Circle(center, radius);
  }

  public Circle inscribedCircle() {
    double radius = side() / 2;
    Point center = new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    return new Circle(center, radius);
  }
}

class Triangle extends Shape {
  public Point a;
  public Point b;
  public Point c;

  public Triangle(Point a, Point b, Point c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public List<Point> vertices() {
    List<Point> vertices = new ArrayList<>();
    vertices.add(a);
    vertices.add(b);
    vertices.add(c);
    return vertices;
  }

  public Circle circumscribedCircle() {
    double aX = a.getX();
    double aY = a.getY();
    double bX = b.getX();
    double bY = b.getY();
    double cX = c.getX();
    double cY = c.getY();

    double numerator_X =
        -1
            * ((aY * (bX * bX + bY * bY - cX * cX - cY * cY))
                + (bY * (cX * cX + cY * cY - aX * aX - aY * aY))
                + (cY * (aX * aX + aY * aY - bX * bX - bY * bY)));

    double denominator = (aX * (bY - cY) + bX * (cY - aY) + cX * (aY - bY));

    double center_X = numerator_X / (2 * denominator);

    double numerator_Y =
        ((aX * (bX * bX + bY * bY - cX * cX - cY * cY))
            + (bX * (cX * cX + cY * cY - aX * aX - aY * aY))
            + (cX * (aX * aX + aY * aY - bX * bX - bY * bY)));

    double center_Y = numerator_Y / (2 * denominator);

    double radius = (Point.length(a, b) * Point.length(b, c) * Point.length(a, c)) / (4 * area());

    return new Circle(new Point(center_X, center_Y), radius);
  }

  public Circle inscribedCircle() {
    double AB = Point.length(this.a, this.b);
    double BC = Point.length(this.b, this.c);
    double AC = Point.length(this.c, this.a);
    Point CA_bis =
        new Point(
            (this.c.x + (BC / AB) * this.a.x) / (1 + (BC / AB)),
            (this.c.y + (BC / AB) * this.a.y) / (1 + (BC / AB)));
    Point CB_bis =
        new Point(
            (this.c.x + (AC / AB) * this.b.x) / (1 + (AC / AB)),
            (this.c.y + (AC / AB) * this.b.y) / (1 + (AC / AB)));
    double pX = this.a.x, pY;
    if (this.a.x == CB_bis.x) {
      pY =
          ((CA_bis.y - this.b.y) / (CA_bis.x - this.b.x)) * (this.a.x)
              + ((CA_bis.x * this.b.y - this.b.x * CA_bis.y) / (CA_bis.x - this.b.x));
    } else if (this.b.x == CA_bis.x) {
      pY =
          ((CB_bis.y - this.a.y) / (CB_bis.x - this.a.x)) * (this.b.x)
              + ((CB_bis.x * this.a.y - this.a.x * CB_bis.y) / (CB_bis.x - this.a.x));
    } else {
      pX =
          (((CB_bis.x * this.a.y - this.a.x * CB_bis.y) / (CB_bis.x - this.a.x))
                  - ((CA_bis.x * this.b.y - this.b.x * CA_bis.y) / (CA_bis.x - this.b.x)))
              / (((CA_bis.y - this.b.y) / (CA_bis.x - this.b.x))
                  - ((CB_bis.y - this.a.y) / (CB_bis.x - this.a.x)));
      pY =
          (((CA_bis.y - this.b.y) / (CA_bis.x - this.b.x))
                      * ((CB_bis.x * this.a.y - this.a.x * CB_bis.y) / (CB_bis.x - this.a.x))
                  - ((CB_bis.y - this.a.y) / (CB_bis.x - this.a.x))
                      * ((CA_bis.x * this.b.y - this.b.x * CA_bis.y) / (CA_bis.x - this.b.x)))
              / ((CA_bis.y - this.b.y) / (CA_bis.x - this.b.x)
                  - (CB_bis.y - this.a.y) / (CB_bis.x - this.a.x));
    }
    return new Circle(new Point(pX, pY), this.area() / (this.perimeter() / 2));
  }

  public Point orthocenter() {
    double coefficient = (c.x - b.x) * (c.y - a.y) - (c.x - a.x) * (c.y - b.y);

    double x =
        ((a.x * (c.x - b.x) + a.y * (c.y - b.y)) * (c.y - a.y)
                - (b.x * (c.x - a.x) + b.y * (c.y - a.y)) * (c.y - b.y))
            / coefficient;

    double y =
        ((c.x - b.x) * (b.x * (c.x - a.x) + b.y * (c.y - a.y))
                - (c.x - a.x) * (a.x * (c.x - b.x) + a.y * (c.y - b.y)))
            / coefficient;

    return new Point(x, y);
  }

  public Circle ninePointsCircle() {
    return new Circle(
        new Point(
            (circumscribedCircle().A.x + orthocenter().x) / 2,
            (orthocenter().y + circumscribedCircle().A.y) / 2),
        circumscribedCircle().radius() / 2);
  }

  @Override
  public void translate(Point newCenter) {
    double pereX = newCenter.getX() - center().getX();
    double pereY = newCenter.getY() - center().getY();
    a = new Point(a.getX() + pereX, a.getY() + pereY);
    b = new Point(b.getX() + pereX, b.getY() + pereY);
    c = new Point(c.getX() + pereX, c.getY() + pereY);
  }

  @Override
  public void rotate(double angle) {
    Point predCent = this.center();
    this.a.rotate(predCent, angle);
    this.b.rotate(predCent, angle);
    this.c.rotate(predCent, angle);
  }

  @Override
  public void scale(double coeff) {
    Point predCent = this.center();
    a =
        new Point(
            predCent.getX() + coeff * (a.getX() - predCent.getX()),
            predCent.getY() + coeff * (a.getY() - predCent.getY()));
    b =
        new Point(
            predCent.getX() + coeff * (b.getX() - predCent.getX()),
            predCent.getY() + coeff * (b.getY() - predCent.getY()));
    c =
        new Point(
            predCent.getX() + coeff * (c.getX() - predCent.getX()),
            predCent.getY() + coeff * (c.getY() - predCent.getY()));
  }

  @Override
  public Point center() {
    return new Point((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3);
  }

  @Override
  public double perimeter() {
    return Point.length(a, b) + Point.length(b, c) + Point.length(c, a);
  }

  @Override
  public double area() {
    return Math.sqrt(
        perimeter()
            / 2
            * (perimeter() / 2 - Point.length(a, b))
            * (perimeter() / 2 - Point.length(b, c))
            * (perimeter() / 2 - Point.length(a, c)));
  }
}
