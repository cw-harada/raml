import com.softwaremill.sttp._

class apiClient(val baseUri: String = "https://api.chatwork.com/", val version: String = "v2") {

  // get the job
  def getJob(job_id: integer = 30) = {}

  // create a new job
  def createJob() = { val response = sttp.post(baseUri).body({ "id" : 5, "name" : "new job name" } ).send(); println(response.body) }

}
