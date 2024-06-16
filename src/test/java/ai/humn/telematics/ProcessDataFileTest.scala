package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProcessDataFileTest extends FlatSpec with Matchers {

  val testFilePath = this.getClass.getClassLoader.getResource("2021-10-05_journeys.csv")

  "main" should "process journeys data correctly" in {
    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      ProcessDataFile.main(Array(testFilePath.getPath))
    }

    val output = outputStream.toString
    println(output)

    output should include("Journeys of 90 minutes or more.")
    output should include("journeyId: 000006 driver_c distance 111.0 durationMS  7200000 avgSpeed in kph was 55.5")

    output should include("Average speeds in Kph")
    output should include("journeyId: 000001 driver_a distance 3.0 durationMS  600000 avgSpeed in kph was 18.0")
    output should include("journeyId: 000002 driver_a distance 41.0 durationMS  3600000 avgSpeed in kph was 41.0")
    output should include("journeyId: 000003 driver_b distance 3.0 durationMS  600000 avgSpeed in kph was 18.0")
    output should include("journeyId: 000004 driver_b distance 1.0 durationMS  60000 avgSpeed in kph was 60.0")
    output should include("journeyId: 000005 driver_b distance 1.0 durationMS  60000 avgSpeed in kph was 60.0")
    output should include("journeyId: 000006 driver_c distance 111.0 durationMS  7200000 avgSpeed in kph was 56.0")

    output should include("Mileage By Driver")
    output should include("driver_a drove 44 kilometers")
    output should include("driver_b drove 5 kilometers")
    output should include("driver_c drove 111 kilometers")

    output should include("Most active driver is driver_c")
  }
}
