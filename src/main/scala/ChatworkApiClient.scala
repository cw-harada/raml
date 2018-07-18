import com.softwaremill.sttp._
import com.typesafe.config.{Config, ConfigFactory}
import org.json4s._
import org.json4s.native.JsonMethods._

object ChatworkApiClient extends App {
  //val conf: Config = ConfigFactory.load()
  //val apiClient = new ChatworkApiClient("https://api.chatwork.com/", "v2", Map("X-ChatWorkToken" -> conf.getString("chatwork.token")))
  val apiClient = new ChatworkApiClient()
  apiClient.getRooms()
}

class ChatworkApiClient() {

  val baseUri: String = ""

  val version: String = "v2"

  val requestHeaders: Map[String, String] = Map()

  implicit val backend = HttpURLConnectionBackend()
  implicit val formats = DefaultFormats

  def getRooms() = {
    val response = getRequest("/rooms")
    parse(response).extract[Seq[Room]]
  }

  def getRoom(roomId: Int) = {
    val relativePath = "/rooms/" + roomId
    val response = getRequest(relativePath)
    parse(response).extract[Room]
  }

  def updateRoom(roomId: Int, description: Option[String], icon_preset: Option[String], name: Option[String]) = {
    val params = Map("description" -> description, "icon_preset" -> icon_preset, "name" -> name)
    val paramList = params.collect{ case (k, Some(v)) => (k, v)}

    val relativePath = "/rooms/" + roomId
    val response = putRequest(relativePath, paramList)
    parse(response).extract[RoomId]
  }

  def postMessage(roomId: Int, message: String) = {
    val relativePath = "/rooms/" + roomId + "/messages"
    val response = postRequest(relativePath, Map("body" -> message))
    parse(response).extract[Message]
  }

  def getRequest(relativePath: String = "") = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).get(uri"${path}")
    httpRequest(request)
  }

  def postRequest(relativePath: String = "", body: Map[String, String]) = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).body(body).post(uri"${path}")
    httpRequest(request)
  }

  def putRequest(relativePath: String = "", body: Map[String, String]) = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).body(body).put(uri"${path}")
    httpRequest(request)
  }

  def httpRequest(request: Request[String, Nothing]) = {
    try {
      val response = request.send().body match {
        case Right(e) => e
        case Left(e) => throw new Exception
      }
      response
    } catch {
      case e: (Exception) =>
        throw new Exception(e.getMessage)
    }
  }

}
