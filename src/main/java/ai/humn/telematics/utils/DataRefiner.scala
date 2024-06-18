package ai.humn.telematics.utils

import Helper.calculateDuration
import Helper.calculateDistance

object DataRefiner {

  /**
   * Refines the dataset of journeys by performing the following operations:
   * 1. Removes journeys where any value in the map is null, empty, or "null".
   * 2. Removes duplicate journeys.
   * 3. Filters out journeys where the calculated distance or duration is less than or equal to 0.
   *
   * @param journeysMap A list of maps, where each map represents a journey with key-value pairs.
   * @return A refined list of maps, containing only valid journeys.
   */
  def refineJourneys(journeysMap: List[Map[String, String]]): List[Map[String, String]] = {

    // Remove journeys where any value in the row is null, empty or "null"
    var refinedJourneysMap = journeysMap.filter { journey =>
      !journey.values.exists(value => value == null || value.isEmpty || value.toLowerCase() == "null")
    }

    // filtering duplicate journeys
    refinedJourneysMap = refinedJourneysMap.toSet.toList

    // Further filtering journeys where distance or durationMS is less than 0
    refinedJourneysMap = refinedJourneysMap.filter { journey =>
      calculateDistance(journey) > 0 && calculateDuration(journey) > 0
    }

    // returning refined Journeys, after filtering all the invalid data rows
    refinedJourneysMap
  }
}
