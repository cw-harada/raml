import java.io.{File, FileOutputStream, OutputStreamWriter, Writer}

import freemarker.template.{Configuration, Template, TemplateExceptionHandler}

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

  val temp: Template = cfg.getTemplate("test.ftlh")

  val root: java.util.HashMap[String, Object] = new java.util.HashMap[String, Object]

  root.put("raml", ramlModelResult)

  val out: Writer = new OutputStreamWriter(System.out)
  temp.process(root, out)

  val outputDirectory = new File(".").getCanonicalPath + "/output"
  val fileOutPutStream = new FileOutputStream(outputDirectory + "/Client.scala")
  val outFile: Writer = new OutputStreamWriter(fileOutPutStream)
  temp.process(root, outFile)

}
