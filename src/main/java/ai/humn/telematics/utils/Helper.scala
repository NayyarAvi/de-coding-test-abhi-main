package ai.humn.telematics.utils

object Helper {

  def calculateDuration(journey: Map[String, String]): Long = {
    try {
      journey("endTime").toLong - journey("startTime").toLong
    } catch {
      case _: NumberFormatException =>
        throw new IllegalArgumentException("Invalid format for start or end time")
      case e: Throwable =>
        throw new Exception("Unexpected error occurred", e)
    }
  }

  def calculateDistance(journey: Map[String, String]): Double = {
    try {
      journey("endOdometer").toDouble - journey("startOdometer").toDouble
    } catch {
      case _: NumberFormatException =>
        throw new IllegalArgumentException("Invalid format for start or end time")
      case e: Throwable =>
        throw new Exception("Unexpected error occurred", e)
    }
  }

  def calculateAvgSpeed(journey: Map[String, String]): Double = {
    calculateDistance(journey) / (calculateDuration(journey).toFloat / (1000 * 60 * 60))
  }
}
