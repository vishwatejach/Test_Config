import java.nio.file.Paths
import java.time.Instant
import csw.config.api.ConfigData
import csw.config.models.{ConfigId, FileType}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.async.Async._
import csw.location.api.scaladsl.LocationService
import csw.location.client.scaladsl.HttpLocationServiceFactory
import csw.config.client.scaladsl.ConfigClientFactory
import org.scalatest.funsuite.AnyFunSuiteLike

class LgripConfigExample(locationService: LocationService) extends AnyFunSuiteLike {

  // Initialize config client API and admin API
  val clientApi = ConfigClientFactory.clientApi(actorSystem, locationService)
  val adminApi = ConfigClientFactory.adminApi(actorSystem, locationService, factory)

  // Sample configuration data
  val defaultConfigData: ConfigData = ConfigData.fromString("foo { bar { baz : 1234 } }")

  // Create a file in the config service
  def createFileExample(): ConfigId = {
    val filePath = Paths.get("/example/path/sample.conf")
    val configId = Await.result(adminApi.create(filePath, defaultConfigData, annex = false, "Initial creation"), 5.seconds)
    configId
  }

  // Check if a file exists
  def checkFileExists(filePath: String): Boolean = {
    Await.result(clientApi.exists(Paths.get(filePath)), 5.seconds)
  }

  // Update a file
  def updateFile(filePath: String, newData: ConfigData): ConfigId = {
    Await.result(adminApi.update(Paths.get(filePath), newData, "Update content"), 5.seconds)
  }

  // Delete a file
  def deleteFile(filePath: String): Unit = {
    Await.result(adminApi.delete(Paths.get(filePath), "Deleting unused file"), 5.seconds)
  }

  // Fetch the latest version of a file
  def getLatestFileVersion(filePath: String): Option[ConfigData] = {
    Await.result(adminApi.getLatest(Paths.get(filePath)), 5.seconds)
  }

  // Retrieve file by a specific ConfigId
  def getFileById(filePath: String, configId: ConfigId): Option[ConfigData] = {
    Await.result(adminApi.getById(Paths.get(filePath), configId), 5.seconds)
  }

  // Fetch file by specific time
  def getFileByTime(filePath: String, time: Instant): Option[ConfigData] = {
    Await.result(adminApi.getByTime(Paths.get(filePath), time), 5.seconds)
  }

  // List all files or by pattern/type
  def listFiles(fileType: Option[FileType] = None, pattern: Option[String] = None): Set[java.nio.file.Path] = {
    Await.result(adminApi.list(fileType, pattern), 5.seconds).map(_.path).toSet
  }

  // Retrieve active version of a file
  def getActiveVersion(filePath: String): Option[ConfigId] = {
    Await.result(adminApi.getActiveVersion(Paths.get(filePath)), 5.seconds)
  }

  // Set active version
  def setActiveVersion(filePath: String, configId: ConfigId): Unit = {
    Await.result(adminApi.setActiveVersion(Paths.get(filePath), configId, "Setting active version"), 5.seconds)
  }
}
