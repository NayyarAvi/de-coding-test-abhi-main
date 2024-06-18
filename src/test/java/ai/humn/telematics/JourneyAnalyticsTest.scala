package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JourneyAnalyticsTest extends FlatSpec with Matchers {

  "printLongJourneys" should "print journeys of 90 minutes or more" in {
    val inputTestData = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "1633436902000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "350"),
      Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "1633429702000", "endTime" -> "1633429708000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "320")
    )

    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      JourneyAnalytics.printLongJourneys(inputTestData)
    }

    val output = outputStream.toString
    println(output)
    output should include("Journeys of 90 minutes or more.")
    output should include("journeyId: 1 A distance 50.0 durationMS  7200000 avgSpeed in kph was 25.0")
  }

  it should "throw IllegalArgumentException for non-numeric endTime" in {
    val inputTestData = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "invalid", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "350"))
    an [IllegalArgumentException] should be thrownBy {
      JourneyAnalytics.printLongJourneys(inputTestData)
    }
  }

  "printAverageSpeed" should "calculate and print average speeds" in {
    val inputTestData = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "1633430302000", "startOdometer" -> "100", "endOdometer" -> "120"),
      Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "1633429702000", "endTime" -> "1633430303000", "startOdometer" -> "300", "endOdometer" -> "350")
    )

    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      JourneyAnalytics.printAverageSpeed(inputTestData)
    }

    val output = outputStream.toString
    println(output)
    output should include("Average speeds in Kph")
    output should include("journeyId: 1 A distance 20.0 durationMS  600000 avgSpeed in kph was 120.0")
    output should include("journeyId: 2 B distance 50.0 durationMS  601000 avgSpeed in kph was 300.0")
  }

  it should "throw IllegalArgumentException for non-numeric endTime" in {
    val inputTestData = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "invalid", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "350"))
    an [IllegalArgumentException] should be thrownBy {
      JourneyAnalytics.printAverageSpeed(inputTestData)
    }
  }

  "printTotalMileage" should "calculate and print total mileage by driver" in {
    val inputJourneys = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "1633430302000", "startOdometer" -> "100", "endOdometer" -> "200"),
      Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "1733429702000", "endTime" -> "1733430302000", "startOdometer" -> "300", "endOdometer" -> "500"),
      Map("journeyId" -> "3", "driverId" -> "A", "startTime" -> "1633430302000", "endTime" -> "1633440302000", "startOdometer" -> "200", "endOdometer" -> "400")
    )

    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      JourneyAnalytics.printTotalMileage(inputJourneys)
    }

    val output = outputStream.toString
    println(output)
    output should include("Mileage By Driver")
    output should include("A drove 300 kilometers")
    output should include("B drove 200 kilometers")
  }

  it should "throw IllegalArgumentException for non-numeric endOdometer" in {
    val inputTestData = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "1633439702000", "startLat" -> "0.456", "startLon" -> "0.456", "endLat" -> "0.124", "endLon" -> "0.457", "startOdometer" -> "300", "endOdometer" -> "invalid"))
    an [IllegalArgumentException] should be thrownBy {
      JourneyAnalytics.printTotalMileage(inputTestData)
    }
  }

  "printActiveDriver" should "identify and print the most active driver" in {
    val inputJourneys = List(
      Map("journeyId" -> "1", "driverId" -> "A", "startTime" -> "1633429702000", "endTime" -> "1633430302000", "startOdometer" -> "100", "endOdometer" -> "200"),
      Map("journeyId" -> "2", "driverId" -> "B", "startTime" -> "1733429702000", "endTime" -> "1733430302000", "startOdometer" -> "300", "endOdometer" -> "500"),
      Map("journeyId" -> "3", "driverId" -> "A", "startTime" -> "1633430302000", "endTime" -> "1633440302000", "startOdometer" -> "200", "endOdometer" -> "400")
    )

    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      JourneyAnalytics.printActiveDriver(inputJourneys)
    }

    val output = outputStream.toString
    println(output)
    output should include("Most active driver is A")
  }
}
