<#list dataTypes as type>
case class ${type.name()}
(
  <#list type.properties() as property>
  `${property.name()}`: ${property.type()?cap_first}<#if property_has_next>,</#if>
  </#list>
)
</#list>