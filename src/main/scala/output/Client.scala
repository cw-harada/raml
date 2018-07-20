import com.softwaremill.sttp._
import org.json4s._
import org.json4s.native.JsonMethods._

class ApiClient() {

  val baseUri: String = "https://api.chatwork.com/"

  val version: String = "v2"

  val requestHeaders: Map[String, String] = Map()

  implicit val backend = HttpURLConnectionBackend()
  implicit val formats = DefaultFormats

  // get rooms
  def getRooms() {
    val response = getRequest("/rooms")
    parse(response).extract[Seq[Room]]
  }

  def getRoom(roomId: Integer) = {
    val relativePath = "/rooms"  + roomId
    val response = getRequest(relativePath)
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
