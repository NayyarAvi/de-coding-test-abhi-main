package ai.humn.telematics

import utils.DataLoader.loadJourneys
import utils.DataRefiner.refineJourneys

object ProcessDataFile {

  def main(args: Array[String]): Unit = {
    // storing file path from the arguments in the cmd
    val filePath = args(0)

    // refining and cleaning the input csv dataset and storing in a list
    val refinedJourneysList: List[Map[String, String]] = refineJourneys(loadJourneys(filePath))

    /* Use Case 1: Find journeys that are 90 minutes or more. */
    JourneyAnalytics.printLongJourneys(refinedJourneysList)
    println()

    /* Use Case 2: Find the average speed per journey in kph. */
    JourneyAnalytics.printAverageSpeed(refinedJourneysList)
    println()

    /* Use Case 3: Find the total mileage by driver for the whole day. */
    JourneyAnalytics.printTotalMileage(refinedJourneysList)
    println()

    /* Use Case 4: Find the most active driver - the driver who has driven the most kilometers. */
    JourneyAnalytics.printActiveDriver(refinedJourneysList)
  }
}
