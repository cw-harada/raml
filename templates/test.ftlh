import com.softwaremill.sttp._

class apiClient(val baseUri: String = "${raml.apiV10.baseUri().value()}", val version: String = "${raml.apiV10.version().value()}") {

<#list raml.apiV10.resources() as resource>
  <#list resource.methods() as method>
  // ${method.description().value()}
  def ${method.displayName().value()}<@compress single_line=true>
  <#if method.method() == "get">
    <#list method.queryParameters() as param>
      (${param.name()}: ${param.type()?cap_first}<#if param.defaultValue()??> = ${param.defaultValue()}</#if><#if param_has_next>,</#if></#list>) = {}
  <#elseif method.method() == "post">
    <#list method.body() as body>
      () = {
        val response = sttp.post(baseUri).body(${body.type()?no_esc}).send();
        println(response.body)
      }
    </#list>
  </#if>
  </@compress>


  </#list>
</#list>
}
