package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProcessDataFileTest extends FlatSpec with Matchers {

  val testFilePath = this.getClass.getClassLoader.getResource("2021-10-05_journeys.csv")

  // This test is failing but I don't know why :(
  it should "generate results to stdout" in {
    ProcessDataFile.main(Array(testFilePath.getPath))
  }


}
