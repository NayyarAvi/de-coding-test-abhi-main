package ai.humn.telematics.utils

import com.opencsv.{CSVParserBuilder, CSVReaderBuilder}

import scala.io.Source


object DataLoader {

  /**
   * Reads a CSV file and converts it into a list of maps, where each map represents a journey.
   * Each map contains key-value pairs derived from the CSV headers and corresponding row values.
   *
   * @param csvFilePath The path to the CSV file to be read.
   * @return A list of maps, where each map represents a journey with key-value pairs corresponding to the CSV headers and row values.
   */
  def loadJourneys(csvFilePath: String): List[Map[String, String]] = {

    // Reading the CSV file using OpenCSV
    val reader = Source.fromFile(csvFilePath).bufferedReader()
    val parser = new CSVParserBuilder().withSeparator(',').build()
    val csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()

    // Reading headers from the first row
    val headers = csvReader.readNext().map(_.trim)

    // Initializing a list to store maps
    var journeysList: List[Map[String, String]] = List()

    // Read remaining rows and convert them to maps
    var row: Array[String] = csvReader.readNext()
    while (row != null) {
      val rowData = headers.zip(row).toMap
      journeysList = journeysList :+ rowData
      row = csvReader.readNext()
    }

    // Close resources
    reader.close()
    csvReader.close()

    // returning journeys list
    journeysList
  }
}
