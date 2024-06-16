package ai.humn.telematics

import scala.io.Source
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
//import org.apache.spark.sql.SparkSession

object ProcessDataFile {

  /**
   * Refines the dataset of journeys by performing the following operations:
   * 1. Removes journeys where any value in the map is null, empty, or "null".
   * 2. Removes duplicate journeys.
   * 3. Filters out journeys where the calculated distance or duration is less than or equal to 0.
   *
   * @param journeysMap A list of maps, where each map represents a journey with key-value pairs.
   * @return A refined list of maps, containing only valid journeys.
   */
  def refineDataset(journeysMap: List[Map[String, String]]): List[Map[String, String]] = {
    // Remove journeys where any value in the row is null, empty or "null"
    var refinedJourneysMap = journeysMap.filter { journey =>
      !journey.values.exists(value => value == null || value.isEmpty || value.toLowerCase() == "null")
    }

    // filtering duplicate journeys
    refinedJourneysMap = refinedJourneysMap.toSet.toList

    // Further filtering journeys where distance or durationMS is less than 0
    refinedJourneysMap = refinedJourneysMap.filter { journey =>
      val durationMS = journey("endTime").toLong - journey("startTime").toLong
      val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
      distance > 0 && durationMS > 0
    }

    // returning refined Journeys, after filtering all the invalid data rows
    refinedJourneysMap
  }

  /**
   * Reads a CSV file and converts it into a list of maps, where each map represents a journey.
   * Each map contains key-value pairs derived from the CSV headers and corresponding row values.
   *
   * @param csvFilePath The path to the CSV file to be read.
   * @return A list of maps, where each map represents a journey with key-value pairs corresponding to the CSV headers and row values.
   */
  def getJourneysList(csvFilePath: String): List[Map[String, String]] = {
    // Reading the CSV file using OpenCSV
    val reader = Source.fromFile(csvFilePath).bufferedReader()
    val parser = new CSVParserBuilder().withSeparator(',').build()
    val csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()

    // Reading headers from the first row
    val headers = csvReader.readNext().map(_.trim)

    // Initializing a list to store maps
    var journeysList: List[Map[String, String]] = List()

    // Read remaining rows and convert them to maps
    var row: Array[String] = csvReader.readNext()
    while (row != null) {
      val rowData = headers.zip(row).toMap
      journeysList = journeysList :+ rowData
      row = csvReader.readNext()
    }

    // Close resources
    reader.close()
    csvReader.close()

    // returning journeys list
    journeysList
  }

  /**
   * Use Case 1: Finds journeys that are 90 minutes or more.
   *
   * This function iterates through the list of journeys and calculates the duration for each journey.
   * It then checks if the journey duration is 90 minutes (5400000 milliseconds) or more.
   *
   * @param journeysList A list of maps, where each map represents a journey with key-value pairs.
   */
  def printLongJourneys(journeysList: List[Map[String, String]]): Unit = {
    println("Journeys of 90 minutes or more.")

    // looping through all the driver journeys
    for (journey <- journeysList) {
      val durationMS = journey("endTime").toLong - journey("startTime").toLong

      // finding the journeys where time taken is 90 minutes or more
      if (durationMS >= 90 * 1000 * 60) {
        val journeyId = journey("journeyId")
        val driverId = journey("driverId")
        val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
        val avgSpeed = distance / (durationMS.toFloat / (1000 * 60 * 60))

        // printing journeys that are 90 minutes or more.
        println(s"journeyId: $journeyId $driverId distance $distance durationMS  $durationMS avgSpeed in kph was $avgSpeed")
      }
    }
  }

  /**
   * Use Case 2: Find the average speed per journey in kph.
   *
   * This function iterates through the list of journeys and calculates the average speed for each journey.
   * The average speed is calculated based on the distance covered and the duration of the journey.
   * It then prints the journey details along with the calculated average speed.
   *
   * @param journeysList A list of maps, where each map represents a journey with key-value pairs.
   */
  def printAverageSpeed(journeysList: List[Map[String, String]]): Unit = {
    println("Average speeds in Kph")

    // looping through all the driver journeys and calculation the average speed
    for (journey <- journeysList) {
      val journeyId = journey("journeyId")
      val driverId = journey("driverId")
      val durationMS = journey("endTime").toLong - journey("startTime").toLong
      val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
      val avgSpeed = (distance / (durationMS.toFloat / (1000 * 60 * 60))).ceil

      // printing the average speed per journey in kph
      println(s"journeyId: $journeyId $driverId distance $distance durationMS  $durationMS avgSpeed in kph was $avgSpeed")
    }
  }

  /**
   * Use Case 3: Find the total mileage by driver for the whole day.
   *
   * This function iterates through the list of journeys, collates the distances covered by each driver,
   * and then calculates and print the total mileage for each driver.
   *
   * @param journeysList A list of maps, where each map represents a journey with key-value pairs.
   */
  def printTotalMileage(journeysList: List[Map[String, String]]): Unit = {
    println("Mileage By Driver")

    // declaring list, to store driverId as KEY, and list of distance covered as VALUE
    var mileageList: Map[String, List[Int]] = Map()

    /**
     *  looping through all the driver journeys, and collating all the distances covered by each driver in a list.
     *  where Key --> driverId, and Value --> list of distances covered by the driver
     *  for example: if 'driver_a' covered distance '10km' and '20km'.
     *  Result: key --> driver_a, and value --> (10, 20)
    */
    for (journey <- journeysList) {
      val driverId = journey("driverId")
      val distance = journey("endOdometer").toInt - journey("startOdometer").toInt
      mileageList = mileageList + (driverId -> (mileageList.getOrElse(driverId, List()) :+ distance))
    }

    // Loop through all the driverId and adding the distance to find total Mileage covered by a driver
    mileageList.foreach { case (key, values) =>
      val totalMileage = values.sum

      // printing the total mileage by driver for the whole day.
      println(s"$key drove $totalMileage kilometers")
    }
  }

  /**
   * Use Case 4: Find the most active driver - the driver who has driven the most kilometers.
   *
   * This function iterates through the list of journeys, collates the distances covered by each driver,
   * calculates the total mileage for each driver, and identifies the driver with the maximum mileage.
   * It then prints the details of the most active driver.
   *
   * @param journeysList A list of maps, where each map represents a journey with key-value pairs.
   */
  def printActiveDriver(journeysList: List[Map[String, String]]): Unit = {
    // declaring variables
    var mileageMap: Map[String, List[Int]] = Map()
    var totalMileageMap: Map[String, Int] = Map()

    // looping through all the driver journeys, and collating all the distances covered by each driver in a list.
    for (journey <- journeysList) {
      val driverId = journey("driverId")
      val distance = journey("endOdometer").toInt - journey("startOdometer").toInt
      mileageMap = mileageMap + (driverId -> (mileageMap.getOrElse(driverId, List()) :+ distance))
    }

    // Looping through all the driverId and adding the distance to find total Mileage covered by a driver
    mileageMap.foreach { case (key, values) =>
      totalMileageMap = totalMileageMap + (key -> values.sum)
    }

    // finding the entry, where the driver has covered the maximum mileage
    val maxMileage = totalMileageMap.maxBy(_._2)

    // printing the most active driver - the driver who has driven the most kilometers.
    println(s"Most active driver is ${maxMileage._1}")
  }

  def main(args: Array[String]): Unit = {
    // storing file path from the arguments in the cmd
    val filePath = args(0)

    // refining and cleaning the input csv dataset and storing in a list
    val refinedJourneysList: List[Map[String, String]] = refineDataset(getJourneysList(filePath))

    /* Use Case 1: Find journeys that are 90 minutes or more. */
    printLongJourneys(refinedJourneysList)
    println()

    /* Use Case 2: Find the average speed per journey in kph. */
    printAverageSpeed(refinedJourneysList)
    println()

    /* Use Case 3: Find the total mileage by driver for the whole day. */
    printTotalMileage(refinedJourneysList)
    println()

    /* Use Case 4: Find the most active driver - the driver who has driven the most kilometers. */
    printActiveDriver(refinedJourneysList)
  }
}
