import java.io.{File, FileOutputStream, OutputStreamWriter, Writer}

import com.sun.org.apache.bcel.internal.generic.ObjectType
import freemarker.template.{Configuration, Template, TemplateExceptionHandler}
import org.raml.v2.api.model.v10.datamodel.{ObjectTypeDeclaration, TypeDeclaration}

import scala.collection.JavaConverters._

object Main extends App {
  val ramlDirectory = new File(".").getCanonicalPath + "/raml"

  val ramlModelResult = RamlParser.parse(new File(ramlDirectory + "/input.raml"))

  val cfg = new Configuration(Configuration.VERSION_2_3_27)
  val templateDirectory = new File(".").getCanonicalPath + "/templates"
  cfg.setDirectoryForTemplateLoading(new File(templateDirectory))
  cfg.setDefaultEncoding("UTF-8")
  cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
  cfg.setLogTemplateExceptions(false)
  cfg.setWrapUncheckedExceptions(true)

  val root: java.util.HashMap[String, Object] = new java.util.HashMap[String, Object]
  createCaseClass()
  createApiClient()

  def createCaseClass() = {

    val types = ramlModelResult.getApiV10.types().asScala
    var dataTypes = List[ObjectTypeDeclaration]()
    for (i <- types) {
      dataTypes :+= i.asInstanceOf[ObjectTypeDeclaration]
    }
    val caseClassTemplate: Template = cfg.getTemplate("case_class.ftlh")
    root.put("dataTypes", dataTypes.asJava)

    outputFile("Model.scala", caseClassTemplate)
  }

  def createApiClient() = {

    val apiClientTemplate: Template = cfg.getTemplate("api_client.ftlh")
    root.put("raml", ramlModelResult)

    outputFile("Client.scala", apiClientTemplate)
  }

  def outputFile(outputPath: String, template: Template) = {
    val outputDirectory = new File(".").getCanonicalPath + "/src/main/scala/output"
    val fileOutPutStream = new FileOutputStream(outputDirectory + "/" + outputPath)
    val outFile: Writer = new OutputStreamWriter(fileOutPutStream)
    template.process(root, outFile)
  }

}
