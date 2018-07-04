import java.io.{File, OutputStreamWriter, Writer}
import scala.collection.JavaConverters._
import freemarker.template.{Configuration, Template, TemplateExceptionHandler}

import scala.collection.immutable.HashMap

object Main extends App {


  /* ------ ------------------------------------------------------------------ */
  /* You should do this ONLY ONCE in the whole application life-cycle:        */

  /* Create and adjust the configuration singleton */
  val cfg = new Configuration(Configuration.VERSION_2_3_27)
  cfg.setDirectoryForTemplateLoading(new File("/Users/haradashingo/raml/templates"))
  cfg.setDefaultEncoding("UTF-8")
  cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
  cfg.setLogTemplateExceptions(false)
  cfg.setWrapUncheckedExceptions(true)

  /* ------------------------------------------------------------------------ */
  /* You usually do these for MULTIPLE TIMES in the application life-cycle:   */

  /* Create a data-model */
  val root: java.util.Map[String, String] = HashMap("user" -> "Big Joe").asJava

  /* Get the template (uses cache internally) */
  val temp: Template = cfg.getTemplate("test.ftlh");

  /* Merge data-model with template */
  val out: Writer = new OutputStreamWriter(System.out);
  temp.process(root, out);
  // Note: Depending on what `out` is, you may need to call `out.close()`.
  // This is usually the case for file output, but not for servlet output.
}
