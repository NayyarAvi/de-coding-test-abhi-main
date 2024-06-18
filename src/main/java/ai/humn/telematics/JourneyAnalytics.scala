package ai.humn.telematics

import utils.Helper.calculateDuration
import utils.Helper.calculateDistance
import utils.Helper.calculateAvgSpeed

object JourneyAnalytics {
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

    // looping through all the driver journeys and printing the journeys where time taken is 90 minutes or more
    for (journey <- journeysList) {
      val durationMS = calculateDuration(journey)
      if (durationMS >= 90 * 1000 * 60) {
        println(s"journeyId: ${journey("journeyId")} ${journey("driverId")} distance ${calculateDistance(journey)} durationMS  $durationMS avgSpeed in kph was ${calculateAvgSpeed(journey)}")
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

    // looping through all the driver journeys and printing the average speed
    for (journey <- journeysList) {
      println(s"journeyId: ${journey("journeyId")} ${journey("driverId")} distance ${calculateDistance(journey)} durationMS  ${calculateDuration(journey)} avgSpeed in kph was ${calculateAvgSpeed(journey).ceil}")
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
      val distance = calculateDistance(journey).toInt
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
      val distance = calculateDistance(journey).toInt
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
}
