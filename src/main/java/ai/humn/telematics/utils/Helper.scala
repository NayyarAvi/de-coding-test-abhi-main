package ai.humn.telematics.utils

object Helper {

  /**
   * Calculates the duration of a journey based on start and end times.
   *
   * @param journey A map containing "startTime" and "endTime" as keys with values representing timestamps.
   * @return The duration of the journey in milliseconds.
   * @throws IllegalArgumentException If either "startTime" or "endTime" is not in a valid numeric format.
   * @throws Exception If an unexpected error occurs during calculation.
   */
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

  /**
   * Calculates the distance traveled during a journey based on start and end odometer.
   *
   * @param journey A map containing "startOdometer" and "endOdometer" as keys with values representing distances.
   * @return The distance traveled during the journey.
   * @throws IllegalArgumentException If either "startOdometer" or "endOdometer" is not in a valid numeric format.
   * @throws Exception If an unexpected error occurs during calculation.
   */
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

  /**
   * Calculates the average speed of a journey based on distance traveled and duration.
   *
   * @param journey A map containing "startTime", "endTime", "startOdometer", and "endOdometer".
   * @return The average speed in kilometers per hour (kph) for the journey.
   */
  def calculateAvgSpeed(journey: Map[String, String]): Double = {
    calculateDistance(journey) / (calculateDuration(journey).toFloat / (1000 * 60 * 60))
  }
}
