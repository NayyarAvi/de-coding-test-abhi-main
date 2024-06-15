package ai.humn.telematics

import scala.collection.mutable.ListBuffer
import scala.io.Source

object ProcessDataFile {

  def main(args: Array[String]) = {

    // read file path from args
    var x = args(0)

    // This is the file
    val y = Source.fromFile(x)

    // All of the lines in the file
    var l: Seq[String] = y.getLines().toList

    // Make a variable to hold the parsed lines from the file.
    var results = ListBuffer[ Array[String] ]()

    // parse each line as csv to a collection
    for (a <- 0 until l.length) {
          results += l(a).split(",")
    }

    // This is a collection of the journey lines
    val j = results.toList

    // 1. Find journeys that are 90 minutes or more.
    println("Journeys of 90 minutes or more.")
    var i = 0
    var ok = true
    while(ok){
      if( j(i)(2).toLong - j(i)(3).toLong >= 90 * 1000 * 60 ){
        println("This journey took longer than 90 minutes")
        println(j(i).mkString(","))
      }
      i = i + 1
      if( i >= j.size ) ok = false
    }

    // need to do the
    // 2. Find the average speed per journey in kph.

    // her eis where I will
    // 3. Find the total mileage by driver for the whole day.

    // This part is the last part of the puzzle
    // This jira was a little bit unclear.
    // I assume that most active driver means the driver who drove the most mileage
    // for all of the journeys.
    // we somehow need to
    // 4. Find the most active driver - the driver who has driven the most kilometers.

  }

}
