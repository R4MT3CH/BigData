package org.itc.com

object Main {

  //  anonymous function
  def main(args: Array[String]): Unit = {

    print("\n--Exercise 1--\n");
    print(polynomial(2, 3));
    print("\n------\n");

    print("\n--Exercise 2--\n");
    print(sum(5));
    print("\n------\n");

    print("\n--Exercise 3--\n");
    print(cycle(1, 2, 3));
    print("\n------\n");

    println("--Exercise 4--");
    val president = "George H. W. Bush"
    val number = numFromName(president)
    println(s"$president is the ${ordinalSuffix(number)} US president")
    println("---------");

    println("--Exercise 5--");
    // Example usage
    println(favouriteColour(Red())) // Output: false
    println(favouriteColour(Blue())) // Output: false
    println(favouriteColour(Green())) // Output: false
    println(favouriteColour(Yellow())) // Output: true
    println("---------");

    println("--Exercise 6--");
    val rect = Rectangle(10, 10, 1, 2)
    val circ = Circle(3, 1, 2)
    println(boundingBox(rect)) // Output: Rectangle(10.0,10.0,1.0,2.0)
    println(boundingBox(circ)) // Output: Rectangle(-2.0,-1.0,6.0,6.0)
    println("---------");


    println("--Exercise 7--");
    val rect1 = Rectangle(10, 10, 1, 2)
    val rect2 = Rectangle(11, 11, 1, 2)
    val circ1 = Circle(3, 1, 2)
    val circ2 = Circle(3, 5, 5)

    println(mayOverlap(rect1, rect2))  // Output: false
    println(mayOverlap(rect1, circ1))  // Output: false
    println(mayOverlap(rect1, circ2))  // Output: false
    println(mayOverlap(circ1, circ2))  // Output: true
    println("---------");

    println("--Exercise 8--");
    println("---------");

    println("--Exercise 9--");
    println("---------");

    println("--Exercise 10--");
    println("---------");

    println("--Exercise 11--");
    println("---------");

    println("--Exercise 12--");
    println("---------");

    println("--Exercise 13--");
    println("---------");

    println("--Exercise 14--");
    println("---------");

    println("--Exercise 15--");
    println("---------");

    println("--Exercise 16--");
    println("---------");

    println("--Exercise 17--");
    println("---------");

    println("--Exercise 18--");
    println("---------");

    println("--Exercise 19--");
    println("---------");

    println("--Exercise 20--");
    println("---------");


  }

  //  Exercise 1. Define a function p: (Int,Int) => Int such that p(x,y) is the value of the polynomial
  //  x
  //  2 + 2xy + y
  //  3 − 1 for the given x and y.

  def polynomial(x: Int, y: Int): Int = {
    val m = 2 + (2 * x * y) + y;
    return m;
  }

  //  Exercise 2. Define a function sum: Int => Int such that sum(n) is the sum of the numbers 0, 1, . . . , n.
  //    For example, sum(10) = 55

  def sum(n: Int): Int = {
    var sum = 0;
    for (i <- n to 0 by -1) {
      sum = sum + i;
    }
    return sum;
  }

  //  Exercise 3. Write a function cycle: (Int,Int,Int) => (Int,Int,Int)
  //  that takes a triple of integers and “cycles through them”,
  //  moving the first component to the end and the other two forward, e.g.
  //    cycle((1,2,3)) = (2,3,1).

  def cycle(triple: (Int, Int, Int)): (Int, Int, Int) = {

    val (a, b, c) = triple;
    return (b, c, a)

  }

  //  Exercise 4. The above code uses the suffix th for all numbers, which is wrong for numbers like 41. The rules
  //  for suffixes of ordinal numbers in English are irregular:
  //  • for numbers ending with 11, 12, or 13 we write “10th”, “11th”, “12th”,
  //  • for numbers ending with 1, 2, or 3 we write“1st”, “2nd”, “3rd”
  //  • and for all other numbers we use the suffix “-th”.

  def ordinalSuffix(number: Int): String = {
    val suffix = number % 100 match {
      case 11 | 12 | 13 => "th"
      case _ => number % 10 match {
        case 1 => "st"
        case 2 => "nd"
        case 3 => "rd"
        case _ => "th"
      }
    }
    s"$number$suffix"
  }

  def numFromName(presidentName: String): Int = {
    presidentName match {
      case "George H. W. Bush" => 41
      case "Bill Clinton" => 42
      case "George W. Bush" => 43
      case "Barack Obama" => 44
      case "Donald J. Trump" => 45
      case _ => 0 // Handle unknown names
    }
  }

  //  Exercise 5. Fill in the definition of favouriteColour above; if your favourite colour is not one of the available case classes, add it.
  // Define the Colour trait and its case classes
  // Define the abstract class and case classes
  abstract class Colour

  case class Red() extends Colour

  case class Green() extends Colour

  case class Blue() extends Colour

  case class Yellow() extends Colour // Adding a new color

  // Define the favouriteColour function
  def favouriteColour(c: Colour): Boolean = c match {
    case Red() => false
    case Blue() => false
    case Green() => false
    case Yellow() => true
  }

  //  Exercise 6. Define a function boundingBox that takes a Shape and computes the smallest Rectangle
  //    containing it. (That is, a rectangle’s bounding box is itself; a circle’s bounding box is the smallest square that
  //    covers the circle.)
  abstract class Shape

  case class Circle(r: Double, x: Double, y: Double) extends Shape

  case class Rectangle(llx: Double, lly: Double, w: Double, h: Double) extends Shape

  // Define the boundingBox function
  def boundingBox(s: Shape): Rectangle = s match {
    case Rectangle(llx, lly, w, h) => Rectangle(llx, lly, w, h)
    case Circle(r, x, y) =>
      // The smallest square covering the circle has side length 2 * r
      Rectangle(x - r, y - r, 2 * r, 2 * r)
  }


  //  Exercise 7. Define a function mayOverlap that takes two Shapes and determines whether their bounding
  //    boxes overlap. (For fun, you might enjoy writing an exact overlap test, using mayOverlap to eliminate the
  //    easy case....

  // Define a helper function to check if two intervals overlap
  def intervalsOverlap(aStart: Double, aEnd: Double, bStart: Double, bEnd: Double): Boolean = {
    aEnd >= bStart && bEnd >= aStart
  }

  // Define the mayOverlap function
  def mayOverlap(s1: Shape, s2: Shape): Boolean = {
    val Rectangle(llx1, lly1, w1, h1) = boundingBox(s1)
    val Rectangle(llx2, lly2, w2, h2) = boundingBox(s2)

    // Check if the bounding boxes overlap
    val horizontalOverlap = intervalsOverlap(llx1, llx1 + w1, llx2, llx2 + w2)
    val verticalOverlap = intervalsOverlap(lly1, lly1 + h1, lly2, lly2 + h2)

    horizontalOverlap && verticalOverlap
  }

}