package ai.humn.telematics.utils

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HelperTest extends FlatSpec with Matchers {

  "calculateDuration" should "return the correct duration in milliseconds" in {
    val journey = Map("startTime" -> "1633429702000", "endTime" -> "1633430302000")
    val duration = Helper.calculateDuration(journey)
    duration shouldEqual 600000
  }

  it should "handle negative duration gracefully" in {
    val journey = Map("startTime" -> "1633430302000", "endTime" -> "1633429702000")
    val duration = Helper.calculateDuration(journey)
    duration shouldEqual -600000
  }

  it should "handle zero duration gracefully" in {
    val journey = Map("startTime" -> "1633430302000", "endTime" -> "1633430302000")
    val duration = Helper.calculateDuration(journey)
    duration shouldEqual 0
  }

  it should "throw IllegalArgumentException for non-numeric endTime" in {
    val journey = Map("startTime" -> "1633429702000", "endTime" -> "invalidTimeFormat")
    an [IllegalArgumentException] should be thrownBy {
      Helper.calculateDuration(journey)
    }
  }

  "calculateDistance" should "return the correct distance in kilometers" in {
    val journey = Map("startOdometer" -> "100", "endOdometer" -> "200")
    val distance = Helper.calculateDistance(journey)
    distance shouldEqual 100.0
  }

  it should "handle negative distance gracefully" in {
    val journey = Map("startOdometer" -> "300", "endOdometer" -> "200")
    val distance = Helper.calculateDistance(journey)
    distance shouldEqual -100.0
  }

  it should "handle zero distance gracefully" in {
    val journey = Map("startOdometer" -> "200", "endOdometer" -> "200")
    val distance = Helper.calculateDistance(journey)
    distance shouldEqual 0.0
  }

  it should "throw IllegalArgumentException for non-numeric endOdometer" in {
    val journey = Map("startOdometer" -> "100", "endOdometer" -> "invalidOdometer")
    an [IllegalArgumentException] should be thrownBy {
      Helper.calculateDistance(journey)
    }
  }

  "calculateAvgSpeed" should "return the correct average speed in kilometers per hour" in {
    val journey = Map(
      "startTime" -> "1633429702000", "endTime" -> "1633430302000",
      "startOdometer" -> "100", "endOdometer" -> "200"
    )
    val avgSpeed = Helper.calculateAvgSpeed(journey).ceil
    avgSpeed shouldEqual 600.0
  }

  it should "throw IllegalArgumentException for non-numeric endOdometer" in {
    val journey = Map(
      "startTime" -> "1633429702000", "endTime" -> "1633430302000",
      "startOdometer" -> "100", "endOdometer" -> "invalidOdometer"
    )
    an [IllegalArgumentException] should be thrownBy {
      Helper.calculateAvgSpeed(journey)
    }
  }

}
