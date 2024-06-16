package ai.humn.telematics

import scala.io.Source
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import scala.math.BigDecimal
//import org.apache.spark.sql.SparkSession

object ProcessDataFile {

  def refineDataset(journeysMap: List[Map[String, String]]): List[Map[String, String]] = {
    var refinedJourneyMap: List[Map[String, String]] = List()

    // filtering duplicate journeys
    val distinctJourneyMap: List[Map[String, String]] = journeysMap.toSet.toList

    // filtering journeys where distance or durationMS is less than 0
    for (journey <- distinctJourneyMap) {
      val durationMS = journey("endTime").toLong - journey("startTime").toLong
      val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
      if (distance > 0 && durationMS > 0) {
        refinedJourneyMap  = refinedJourneyMap :+ journey
      }
    }
    refinedJourneyMap
  }

  def getJourneysMap(csvFilePath: String): List[Map[String, String]] = {
    // Read the CSV file using OpenCSV
    val reader = Source.fromFile(csvFilePath).bufferedReader()
    val parser = new CSVParserBuilder().withSeparator(',').build()
    val csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()

    // Read headers from the first row
    val headers = csvReader.readNext().map(_.trim)

    // Initialize a list to store maps
    var dataList: List[Map[String, String]] = List()

    // Read remaining rows and convert them to maps
    var row: Array[String] = csvReader.readNext()
    while (row != null) {
      val rowData = headers.zip(row).toMap
      dataList = dataList :+ rowData
      row = csvReader.readNext()
    }

    // Close resources
    reader.close()
    csvReader.close()

    // returning refined and filtered value
    refineDataset(dataList)
  }

  // Use Case 1: Find journeys that are 90 minutes or more.
  def printLongJourneys(journeysMap: List[Map[String, String]]): Unit = {
    println("Journeys of 90 minutes or more.")
    for (journey <- journeysMap) {
      val durationMS = journey("endTime").toLong - journey("startTime").toLong
      if (durationMS >= 90 * 1000 * 60) {
        val journeyId = journey("journeyId")
        val driverId = journey("driverId")
        val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
        val avgSpeed = distance / (durationMS.toFloat / (1000 * 60 * 60))

        println(s"journeyId: ${journeyId} ${driverId} distance ${distance} durationMS  ${durationMS} avgSpeed in kph was ${avgSpeed}")
      }
    }
  }

  // Use Case 2: Find the average speed per journey in kph.
  def printAverageSpeed(journeysMap: List[Map[String, String]]): Unit = {
    println("Average speeds in Kph")
    for (journey <- journeysMap) {
      val journeyId = journey("journeyId")
      val driverId = journey("driverId")
      val durationMS = journey("endTime").toLong - journey("startTime").toLong
      val distance = journey("endOdometer").toDouble - journey("startOdometer").toDouble
      val avgSpeed = (distance / (durationMS.toFloat / (1000 * 60 * 60))).ceil

      println(s"journeyId: ${journeyId} ${driverId} distance ${distance} durationMS  ${durationMS} avgSpeed in kph was ${avgSpeed}")
    }
  }

  // Use Case 3: Find the total mileage by driver for the whole day.
  def printTotalMileage(journeysMap: List[Map[String, String]]): Unit = {
    var mileageList: Map[String, List[Int]] = Map()
    println("Mileage By Driver")
    for (journey <- journeysMap) {
      val driverId = journey("driverId")
      val distance = journey("endOdometer").toInt - journey("startOdometer").toInt
      mileageList = mileageList + (driverId -> (mileageList.getOrElse(driverId, List()) :+ distance))
    }

    // Loop through all keys and sum the values in each list
    mileageList.foreach { case (key, values) =>
      val totalMileage = values.sum
      println(s"$key drove $totalMileage kilometers")
    }
  }

  // Use Case 3: Find the most active driver - the driver who has driven the most kilometers.
  def printActiveDriver(journeysMap: List[Map[String, String]]): Unit = {
    // declaring variables
    var mileageMap: Map[String, List[Int]] = Map()
    var totalMileageMap: Map[String, Int] = Map()

    // looping through each journey, to find list of distance covered by each driver in a day
    for (journey <- journeysMap) {
      val driverId = journey("driverId")
      val distance = journey("endOdometer").toInt - journey("startOdometer").toInt
      mileageMap = mileageMap + (driverId -> (mileageMap.getOrElse(driverId, List()) :+ distance))
    }

    // Looping through all journeys and adding the distance to find total Mileage covered by a driver
    mileageMap.foreach { case (key, values) =>
      totalMileageMap = totalMileageMap + (key -> values.sum)
    }

    val maxEntry = totalMileageMap.maxBy(_._2)
    println(s"Most active driver is ${maxEntry._1}")
  }

  def main(args: Array[String]) = {
    val filePath = args(0)
    val journeysMap: List[Map[String, String]] = getJourneysMap(filePath)

    printLongJourneys(journeysMap)
    println("\n")
    printAverageSpeed(journeysMap)
    println("\n")
    printTotalMileage(journeysMap)
    println("\n")
    printActiveDriver(journeysMap)
  }
}
