/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views.behaviours

import models.NormalMode
import play.api.data.{Form, FormError}
import play.api.libs.json.{JsObject, Json}
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios

import scala.concurrent.Future

trait QuestionViewBehaviours extends ViewBehaviours {

  def createJson[A](form: Form[A], options: Form[_] => Seq[Radios.Item]): JsObject = {
    Json.obj(
      "form" -> form.data,
      "mode" -> NormalMode,
      "radios" -> options(form)
    )
  }

  val errorKey = "value"
  val errorMessage = "error.number"
  val error = FormError(errorKey, errorMessage)

  def optionsPage[A](
                      createView: (String, JsObject) => Future[HtmlFormat.Appendable],
                      viewName: String,
                      form: Form[A],
                      fieldName: String,
                      options: Form[_] => Seq[Radios.Item]): Unit =
    s"behave like a page with a $fieldName radio options question" - {

      "rendered" - {
        val doc = asDocument(createView(viewName, createJson(form, options)).futureValue)
        for (option <- options(form)) {
          s"contain an input for the value defined for $option" in {
            assertContainsRadioButton(doc, option.id, fieldName, option.value, false)
          }
        }

        "not render an error summary" in {
          val doc = asDocument(createView(viewName, createJson(form, options)).futureValue)
          assertNotRenderedById(doc, "error-summary_header")
        }
      }

      for (option <- options(form)) {
        s"rendered with a $fieldName of '${option.value}'" - {
          s"have the '${option.value}' radio button selected" in {
            val doc = asDocument(createView(viewName, createJson(form.bind(Map(fieldName -> option.value.toString)), options)).futureValue)
            assertContainsRadioButton(doc, option.id, fieldName, option.value, true)

            for (unselectedOption <- options(form).filterNot(o => o == option)) {
              assertContainsRadioButton(doc, unselectedOption.id, fieldName, unselectedOption.value, false)
            }
          }
        }
      }
    }
}