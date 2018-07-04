import java.io.{File, OutputStreamWriter, Writer}

import freemarker.template.{Configuration, Template, TemplateExceptionHandler}
import model.Product

object Main extends App {

  val cfg = new Configuration(Configuration.VERSION_2_3_27)
  val templateDirectory = new java.io.File(".").getCanonicalPath + "/templates"
  cfg.setDirectoryForTemplateLoading(new File(templateDirectory))
  cfg.setDefaultEncoding("UTF-8")
  cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
  cfg.setLogTemplateExceptions(false)
  cfg.setWrapUncheckedExceptions(true)

  val temp: Template = cfg.getTemplate("test.ftlh")

  val root: java.util.HashMap[String, Object] = new java.util.HashMap[String, Object]

  val latest = new Product("http://example.com", "Click here")

  root.put("product", latest)

  val out: Writer = new OutputStreamWriter(System.out)
  temp.process(root, out)

}
