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

package views

import base.SpecBase
import config.FrontendAppConfig
import controllers.actions.{DataRequiredAction, DataRequiredActionImpl, FakeIdentifierAction, IdentifierAction}
import models.UserAnswers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.Assertion
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.twirl.api.Html
import renderer.Renderer

import scala.reflect.ClassTag

trait ViewSpecBase extends SpecBase {

  override lazy val app: Application = commonApplicationBuilder().build()

  val renderer: Renderer = injected[Renderer]
  val config: FrontendAppConfig = injected[FrontendAppConfig]

  def viewFor[A](data: Option[UserAnswers] = None)(implicit tag: ClassTag[A]): A = {
    val application = applicationBuilder(data).build()
    val view = injected[A]
    application.stop()
    view
  }

  def asDocument(html: Html): Document = Jsoup.parse(html.toString())

  def assertEqualsMessage(doc: Document, cssSelector: String, expectedMessageKey: String): Assertion =
    assertEqualsValue(doc, cssSelector, messages(expectedMessageKey))

  def assertEqualsValue(doc : Document, cssSelector : String, expectedValue: String): Assertion = {
    val elements = doc.select(cssSelector)

    if(elements.isEmpty) throw new IllegalArgumentException(s"CSS Selector $cssSelector wasn't rendered.")

    //<p> HTML elements are rendered out with a carriage return on some pages, so discount for comparison
    assert(elements.first().html().replace("\n", "") == expectedValue)
  }

  def assertPageTitleEqualsMessage(doc: Document, expectedMessageKey: String, args: Any*): Assertion = {
    val headers = doc.getElementsByTag("h1")
    headers.size mustBe 1
    headers.first.text.replaceAll("\u00a0", " ") mustBe messages(expectedMessageKey, args:_*).replaceAll("&nbsp;", " ")
  }

  def assertContainsText(doc:Document, text: String): Assertion = assert(doc.toString.contains(text), "\n\ntext " + text + " was not rendered on the page.\n")

  def assertContainsMessages(doc: Document, expectedMessageKeys: String*): Unit = {
    for (key <- expectedMessageKeys) assertContainsText(doc, messages(key))
  }

  def assertRenderedById(doc: Document, id: String): Assertion = {
    assert(doc.getElementById(id) != null, "\n\nElement " + id + " was not rendered on the page.\n")
  }

  def assertNotRenderedById(doc: Document, id: String): Assertion = {
    assert(doc.getElementById(id) == null, "\n\nElement " + id + " was rendered on the page.\n")
  }

  def assertRenderedByCssSelector(doc: Document, cssSelector: String): Assertion = {
    assert(!doc.select(cssSelector).isEmpty, "Element " + cssSelector + " was not rendered on the page.")
  }

  def assertNotRenderedByCssSelector(doc: Document, cssSelector: String): Assertion = {
    assert(doc.select(cssSelector).isEmpty, "\n\nElement " + cssSelector + " was rendered on the page.\n")
  }

  def assertContainsLabel(doc: Document, forElement: String, expectedText: String, expectedHintText: Option[String] = None): Any = {
    val labels = doc.getElementsByAttributeValue("for", forElement)
    assert(labels.size == 1, s"\n\nLabel for $forElement was not rendered on the page.")
    val label = labels.first
    assert(label.text().contains(expectedText), s"\n\nLabel for $forElement was not $expectedText")

    if (expectedHintText.isDefined) {
      assert(label.getElementsByClass("form-hint").first.text == expectedHintText.get,
        s"\n\nLabel for $forElement did not contain hint text $expectedHintText")
    }
  }

  def assertElementHasClass(doc: Document, id: String, expectedClass: String): Assertion = {
    assert(doc.getElementById(id).hasClass(expectedClass), s"\n\nElement $id does not have class $expectedClass")
  }

  def assertContainsRadioButton(doc: Document, id: String, name: String, value: String, isChecked: Boolean): Assertion = {
    assertRenderedById(doc, id)
    val radio = doc.getElementById(id)
    assert(radio.attr("name") == name, s"\n\nElement $id does not have name $name")
    assert(radio.attr("value") == value, s"\n\nElement $id does not have value $value")
    if (isChecked) {
      assert(radio.hasAttr("checked"), s"\n\nElement $id is not checked")
    } else {
      assert(!radio.hasAttr("checked"), s"\n\nElement $id is checked")
    }
  }

  def assertElementTextById(doc: Document, id: String, value: String): Assertion ={

    val forElement = doc.getElementById(id)
    assert(forElement.text==value,
      s"\n\nLabel for $forElement did not contain hint text $value")
  }

  def assertLink(doc: Document, linkId: String, url: String): Assertion = {
    val link = doc.select(s"a[id=$linkId]")
    assert(link.size() == 1, s"\n\nLink $linkId is not displayed")
    val href = link.attr("href")
    assert(href == url, s"\n\nLink $linkId has href $href not $url")
  }

  def assertLinkByUrl(doc: Document, url: String): Assertion = {
    val link = doc.select(s"a[href=$url]")
    val href = link.attr("href")
    assert(href == url, s"\n\nLink has href $href not $url")
  }

  def pageWithSubmitButton(doc: Document,name: String): Unit = {
    "behave like a page with a button" in {
      val button = doc.getElementsByClass("govuk-button")
      val actualName = button.attr("name")
      assert(name == actualName, "\n\nElement " + name + " was not rendered on the page.\n")
    }
  }

  def assertContainsValue(doc: Document, cssSelector: String, expectedValue: String): Assertion = {
    val elements = doc.select(cssSelector)

    if (elements.isEmpty) throw new IllegalArgumentException(s"CSS Selector $cssSelector wasn't rendered.")

    //<p> HTML elements are rendered out with a carriage return on some pages, so discount for comparison
    assert(elements.first().html().replace("\n", "").contains(expectedValue))
  }

}
