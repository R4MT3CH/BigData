package org.itc.com

import scala.collection.immutable.ListMap

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
    println(doubleThenIncrement(3))  // Output: 7
    println("---------");

    println("--Exercise 9--");
    println(doubleThenIncrement2(3))  // Output: 7
    println("---------");

    println("--Exercise 10--");
    println(composedFunction(123))  // Output: true
    println(composedFunction(0))    // Output: true
    println(composedFunction(-1))   // Output: true
    println("---------");

    println("--Exercise 11--");
    val nums = List(1, 2, 3, 4)
    val double: Int => Int = _ * 2

    val doubledNums = map(double, nums)

    println(doubledNums)  // Output: List(2, 4, 6, 8)

    println("---------");

    println("--Exercise 12--");

    val nums2 = List(1, 2, 3, 4, 5, 6)
    val isEven: Int => Boolean = _ % 2 == 0

    val evenNums = filter(isEven, nums2)

    println(evenNums)  // Output: List(2, 4, 6)

    println("---------");

    println("--Exercise 13--");
    val nums3 = List(1, 2, 3, 4, 5)
    val reversedNums = reverse(nums3)

    println(reversedNums)  // Output: List(5, 4, 3, 2, 1)

    println("---------");

    println("--Exercise 14--");
    val dictionary = List(("apple", "fruit"), ("carrot", "vegetable"), ("cat", "animal"))

    val fruit = lookup(dictionary, "apple")
    println(fruit)  // Output: fruit

    val animal = lookup(dictionary, "cat")
    println(animal)  // Output: animal

    try {
      val unknown = lookup(dictionary, "dog")
    } catch {
      case e: NoSuchElementException => println(e.getMessage)  // Output: Key not found
    }
    println("---------");

    println("--Exercise 15--");

    val updatedDictionary1 = update(dictionary, "carrot", "root vegetable")
    println(updatedDictionary1)  // Output: List((apple,fruit), (carrot,root vegetable), (cat,animal))

    val updatedDictionary2 = update(dictionary, "dog", "animal")
    println(updatedDictionary2)  // Output: List((apple,fruit), (carrot,vegetable), (cat,animal), (dog,animal))


    println("---------");

    println("--Exercise 16--");

    val keyList = keys(dictionary)
    println(keyList)  // Output: List(apple, carrot, cat)

    println("---------");

    println("--Exercise 17--");
    println(presidentListMap(44))  // Output: Barack Obama
    println(presidentListMap(41))  // Output: George H. W. Bush
    println("---------");

    println("--Exercise 18--");
    println(map12(44))  // Output: Barack Obama
    println(map12(41))  // Output: George H. W. Bush

    println("---------");

    println("--Exercise 19--");
    val list = List((41, "George H. W. Bush"), (42, "Bill Clinton"), (43, "George W. Bush"), (44, "Barack Obama"), (45, "Donald J. Trump"))

    val listMap = listToListMap(list)
    println(listMap)  // Output: ListMap(41 -> George H. W. Bush, 42 -> Bill Clinton, 43 -> George W. Bush, 44 -> Barack Obama, 45 -> Donald J. Trump)

    println("---------");

    println("--Exercise 20--");
    val votes = List("Alice", "Bob", "Alice", "Alice", "Bob", "Charlie")

    val result = election(votes)
    println(result)  // Output: ListMap(Alice -> 3, Bob -> 2, Charlie -> 1)

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

//  Exercise 8. Define the function compose1 that takes two functions and composes them:


def compose1[A, B, C](f: B => C, g: A => B): A => C = {
  a: A => f(g(a))
}

  val double: Int => Int = x => x * 2
  val increment: Int => Int = x => x + 1

  val doubleThenIncrement: Int => Int = compose1(increment, double)

// Exercise 9. Using anonymous functions, define the function compose that takes two functions and composes them:

  def compose2[A, B, C](f: A => B, g: B => C): A => C = a => g(f(a))

  val double2: Int => Int = x => x * 2
  val increment2: Int => Int = x => x + 1

  val doubleThenIncrement2: Int => Int = compose2(double2, increment2)

//  Exercise 10. Define two expressions e1, e2 such that
//  compose[Int,String,Boolean](e1,e2)

  val e1: Int => String = _.toString
  val e2: String => Boolean = _.nonEmpty

  val composedFunction: Int => Boolean = compose2(e1, e2)

//  Exercise 11. Define a function map that takes a function f: A => B and a list l: List[A] and traverses
//  the list applying f to each element.
//  def map[A,B](f: A => B, l: List[A]): List[B] = ...

def map[A, B](f: A => B, l: List[A]): List[B] = l match {
  case Nil => Nil
  case head :: tail => f(head) :: map(f, tail)
}

//  Exercise 12: Define a function filter that takes a predicate and a list and traverses the list, retaining only
//    the elements for which p is true.
//  def filter[A](p: A => Boolean, l: List[A]): List[A] = ...

  def filter[A](p: A => Boolean, l: List[A]): List[A] = l match {
    case Nil => Nil
    case head :: tail =>
      if (p(head)) head :: filter(p, tail)
      else filter(p, tail)
  }

// Exercise 13:  Write a function to reverse a list.
//  def reverse[A](l: List[A]): List[A] = ....

  def reverse[A](l: List[A]): List[A] = {
    def reverseHelper(remaining: List[A], reversed: List[A]): List[A] = remaining match {
      case Nil => reversed
      case head :: tail => reverseHelper(tail, head :: reversed)
    }
    reverseHelper(l, Nil)
  }

//  Exercise 14. Define the lookup function:
//  def lookup[K,V](m: List[(K,V)], k: K): V = ...

def lookup[K, V](m: List[(K, V)], k: K): V = m match {
  case Nil => throw new NoSuchElementException("Key not found")
  case (key, value) :: tail =>
    if (key == k) value
    else lookup(tail, k)
}


//  Exercise 15. Define the update function:
//  def update[K,V](m: List[(K,V)], k: K, v: V): List[(K,V)] = ...

  def update[K, V](m: List[(K, V)], k: K, v: V): List[(K, V)] = m match {
    case Nil => List((k, v))
    case (key, value) :: tail =>
      if (key == k) (key, v) :: tail
      else (key, value) :: update(tail, k, v)
  }


//  Exercise 16. Define the keys function.
//  def keys[K,V](m: List[K,V]): List[K] = ...

def keys[K, V](m: List[(K, V)]): List[K] = m match {
  case Nil => Nil
  case (key, _) :: tail => key :: keys(tail)
}


//  Exercise 17. Define the mapping from president numbers to names from Section 4.1 as a value
//  val presidentListMap: ListMap[Int,String] = .../

  val presidentListMap: ListMap[Int, String] = ListMap(
    41 -> "George H. W. Bush",
    42 -> "Bill Clinton",
    43 -> "George W. Bush",
    44 -> "Barack Obama",
    45 -> "Donald J. Trump"
  )

//  Exercise 18. Define map map12 using the empty map and Scala’s update function.
var map12: Map[Int, String] = Map()

  map12 += (41 -> "George H. W. Bush")
  map12 += (42 -> "Bill Clinton")
  map12 += (43 -> "George W. Bush")
  map12 += (44 -> "Barack Obama")
  map12 += (45 -> "Donald J. Trump")

//  Exercise 19. The Scala ListMap class provides a method toList that converts a ListMap[K,V] to a
//  List[(K,V)]. Define a function that converts a List[(K,V)] back into a ListMap[K,V].

  def listToListMap[K, V](list: List[(K, V)]): ListMap[K, V] = {
    ListMap(list: _*)
  }

  //  Exercise 20. The Scala ListMap class provides a method contains, testing whether a map m contains a key k
//  which can be written as a method call m.contains(k) or infix as m contains k. Using contains, write a
//    function election that takes a list of Strings and constructs a ListMap[String,Int] such that for each
//  k, the value of m(k) is the number of occurrences of k in the initial list.
//  11
//  def election(votes: List[String]): ListMap[String,Int] = ...
//  For example:
//    scala> election(List("Hillary","Donald","Hillary"))
//  res1: ListMap[String,Int] = Map(Donald -> 1, Hillary -> 2)


  def election(votes: List[String]): ListMap[String, Int] = {
    votes.foldLeft(ListMap[String, Int]()) { (acc, vote) =>
      if (acc.contains(vote)) {
        acc + (vote -> (acc(vote) + 1))
      } else {
        acc + (vote -> 1)
      }
    }
  }

}




