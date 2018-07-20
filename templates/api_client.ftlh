import com.softwaremill.sttp._
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
  }

  <#list resource.resources() as resource2>
    <#list resource2.methods() as method>
    <#if resource2.relativeUri().value() != "">
      <#list resource2.uriParameters() as params>
        <#assign uriParamName = params.name()>
        <#assign uriParam = params.name() + ": " + params.type()?cap_first>
      </#list>
    </#if>
  def ${method.displayName().value()}(<#if uriParam??>${uriParam}</#if>) = {
      <#if method.method() == "get">
    val relativePath = "${resource.relativeUri().value()}" <#if uriParamName??> + ${uriParamName}</#if>
    val response = getRequest(relativePath)
      <#elseif method.method() == "post">
        <#list method.body() as body>
          () = {
            val response = sttp.post(baseUri).body(${body.type()?no_esc}).send();
        </#list>
      </#if>
  }
    </#list>
  </#list>
</#list>

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

}
