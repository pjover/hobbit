package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.TestsMessages.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.util.*

class I18nTranslatorTest : DescribeSpec() {

    init {

        describe("english") {

            context("with no params") {
                val actual = I18nTranslator.english(ERROR_TEST_ZERO_PARAMS)

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ZERO PARAMS"
                }
            }

            context("with one params") {
                val actual = I18nTranslator.english(ERROR_TEST_ONE_PARAMS, 3)

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ONE PARAMS: 3"
                }
            }

            context("with many params") {
                val actual = I18nTranslator.english(
                        ERROR_TEST_MANY_PARAMS,
                        3, 33, 333
                )

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR MANY PARAMS: 3, 33, 333"
                }
            }
        }

        describe("englishWithParamsArray") {

            context("with no params") {
                val actual = I18nTranslator.englishWithParamsArray(
                        ERROR_TEST_ZERO_PARAMS,
                        arrayOf()
                )

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ZERO PARAMS"
                }
            }

            context("with one params") {
                val actual = I18nTranslator.englishWithParamsArray(
                        ERROR_TEST_ONE_PARAMS,
                        arrayOf(3))

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ONE PARAMS: 3"
                }
            }

            context("with many params") {
                val actual = I18nTranslator.englishWithParamsArray(
                        ERROR_TEST_MANY_PARAMS,
                        arrayOf(3, 33, 333)
                )

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR MANY PARAMS: 3, 33, 333"
                }
            }

            val actual = I18nTranslator.englishWithParamsArray(
                    ERROR_TEST_MANY_PARAMS,
                    arrayOf(3, 33, 333)
            )

            it("translates the message") {
                actual shouldBe "TEST TEXT FOR MANY PARAMS: 3, 33, 333"
            }
        }

        describe("translate") {

            context("other locale with translation") {
                val actual = I18nTranslator.translate(
                        Locale("ca"),
                        ERROR_TEST_ZERO_PARAMS
                )

                it("translates the message") {
                    actual shouldBe "CAT TEXT FOR ZERO PARAMS"
                }
            }

            context("other locale without translation") {
                val actual = I18nTranslator.translate(
                        Locale("ru"),
                        ERROR_TEST_ZERO_PARAMS
                )

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ZERO PARAMS"
                }
            }

        }
    }

}