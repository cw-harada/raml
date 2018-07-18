import java.io.File

import org.raml.v2.api.model.common.ValidationResult
import org.raml.v2.api.model.v10.api.Api
import org.raml.v2.api.{RamlModelBuilder, RamlModelResult}
import scala.collection.JavaConverters._

object RamlParser {

  def parse(input: File) = {

    val ramlModelResult: RamlModelResult = new RamlModelBuilder().buildApi(input)
    if (ramlModelResult.hasErrors()) {
      val arrayList = ramlModelResult.getValidationResults
      throw new Exception(arrayList.asScala.head.getMessage)
    } else {
      ramlModelResult
    }
  }

}
