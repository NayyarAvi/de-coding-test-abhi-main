package ai.humn.telematics.utils

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

import java.net.URL

@RunWith(classOf[JUnitRunner])
class DataLoaderTest extends FlatSpec with Matchers {

  val testFilePath: URL = this.getClass.getClassLoader.getResource("2021-10-05_journeys.csv")

  "loadJourneys" should "process journeys correctly and return all the headers" in {
    val journeysList = DataLoader.loadJourneys(testFilePath.getPath)
    val firstJourney = journeysList.head

    // checking if all the headers are present
    firstJourney.keySet should contain allOf ("journeyId", "driverId", "startTime", "endTime", "startLat", "startLon", "endLat", "endLon", "startOdometer", "endOdometer")
  }

  it should "parse the CSV file correctly and return all data rows" in {
    val journeysList = DataLoader.loadJourneys(testFilePath.getPath)
    journeysList should have length 11
  }
}
