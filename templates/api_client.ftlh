import com.softwaremill.sttp._
import com.softwaremill.sttp._
import com.typesafe.config.{Config, ConfigFactory}
import org.json4s._
import org.json4s.native.JsonMethods._

class ApiClient() {

  val baseUri: String = "${raml.apiV10.baseUri().value()}"

  val version: String = "${raml.apiV10.version().value()}"

  val requestHeaders: Map[String, String] = Map()

  implicit val backend = HttpURLConnectionBackend()
  implicit val formats = DefaultFormats

<#list raml.apiV10.resources() as resource>
  <#list resource.methods() as method>
  // ${method.description().value()}
  def ${method.displayName().value()}() {
    <#if method.method() == "get">
    val response = getRequest("${resource.relativeUri().value()}")
    <#elseif method.method() == "post">
    val response = postRequest("${resource.relativeUri().value()}")
    </#if>
    <#list method.responses() as response>
      <#if response.code().value() == "200">
       <#list response.body() as body>
         <#assign array_object = body.type()?ends_with("[]")>
         <#assign type_class =  array_object?then("Seq[" + body.type()?remove_ending("[]") + "]", body.type())>
    parse(response).extract[${type_class}]
       </#list>
      </#if>
    </#list>
  </#list>
</#list>
  }


  def getRequest(relativePath: String = "") = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).get(uri"${r"${path}"}")
    httpRequest(request)
  }

  def postRequest(relativePath: String = "", body: Map[String, String]) = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).body(body).post(uri"${r"${path}"}")
    httpRequest(request)
  }

  def putRequest(relativePath: String = "", body: Map[String, String]) = {
    val path = baseUri + version + relativePath
    val request = sttp.headers(requestHeaders).body(body).put(uri"${r"${path}"}")
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

  <#--
  <#list resource.resources() as resource2>
    <#list resource2.methods() as method>
      // ${method.description().value()}
      def ${method.displayName().value()}(<@compress single_line=true>
      <#if method.method() == "get">
        <#list method.queryParameters() as param>
          ${param.name()}: ${param.type()?cap_first}<#if param.defaultValue()??> = ${param.defaultValue()}</#if><#if param_has_next>,</#if></#list>) = {}
      <#elseif method.method() == "post">
        <#list method.body() as body>
          () = {
            val response = sttp.post(baseUri).body(${body.type()?no_esc}).send();
          }
        </#list>
      </#if>
      </@compress>
    </#list>
  </#list>
  -->
}
