package ai.humn.telematics.utils

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DataRefinerTest extends FlatSpec with Matchers {

    "refineJourneys" should "filter out journeys with invalid distance" in {
      val inputTestData = List(
        Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1000", "endTime" -> "2000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "100", "endOdometer" -> "200"),
        Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "2000", "endTime" -> "2500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "150"),
        Map("journeyId" -> "3", "driverId" -> "C", "startTime" -> "3000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400")
      )

      val refinedJourneys = DataRefiner.refineJourneys(inputTestData)

      refinedJourneys should have length 2
      refinedJourneys.map(_("journeyId")) should contain allOf ("1", "3")
    }

    it should "filter out journeys with invalid duration" in {
      val inputTestData = List(
        Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1000", "endTime" -> "2000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "100", "endOdometer" -> "200"),
        Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "3000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400"),
        Map("journeyId" -> "3", "driverId" -> "C", "startTime" -> "5000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400")
      )

      val refinedJourneys = DataRefiner.refineJourneys(inputTestData)

      refinedJourneys should have length 2
      refinedJourneys.map(_("journeyId")) should contain allOf ("1", "2")
    }

    it should "filter out journeys with null or empty value" in {
      val inputTestData = List(
        Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1000", "endTime" -> "2000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "100", "endOdometer" -> "200"),
        Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "3000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400"),
        Map("journeyId" -> "3", "driverId" -> "C", "startTime" -> "5000", "endTime" -> null, "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400")
      )

      val refinedJourneys = DataRefiner.refineJourneys(inputTestData)

      refinedJourneys should have length 2
      refinedJourneys.map(_("journeyId")) should contain allOf ("1", "2")
    }

    it should "filter out duplicate journeys" in {
      val inputTestData = List(
        Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1000", "endTime" -> "2000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "100", "endOdometer" -> "200"),
        Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "3000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400"),
        Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "3000", "endTime" -> "3500", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "200", "endOdometer" -> "400")
      )

      val refinedJourneys = DataRefiner.refineJourneys(inputTestData)

      refinedJourneys should have length 2
      refinedJourneys.map(_("journeyId")) should contain allOf ("1", "2")
    }

}
