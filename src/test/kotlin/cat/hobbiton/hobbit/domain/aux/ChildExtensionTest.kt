package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.testChildren1
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.time.LocalDate


class ChildExtensionTest : DescribeSpec() {

    init {

        val sut = testChildren1().first()

        describe("Child formatting tests") {

            context("completeName") {

                context("with only one surname") {

                    val actual = sut.completeName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }

                context("with two surnames") {

                    val actual = sut.copy(secondSurname = "Valls").completeName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni Valls"
                    }
                }
            }

            context("shortName") {

                context("with only one surname") {

                    val actual = sut.shortName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }

                context("with two surnames") {

                    val actual = sut.copy(secondSurname = "Valls").shortName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }
            }

            context("formattedText") {

                context("active child") {
                    val actual = sut.formattedText()

                    it("returns the correct text") {
                        actual shouldBe "Joana"
                    }
                }

                context("inactive child") {
                    val actual = sut.copy(active = false)
                            .formattedText()

                    it("returns the correct text") {
                        actual shouldBe "(Joana)"
                    }
                }
            }

            context("longText") {

                context("active child") {
                    val actual = sut.longText()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }

                context("inactive child") {
                    val actual = sut.copy(active = false)
                            .longText()

                    it("returns the correct text") {
                        actual shouldBe "(Joana Bibiloni)"
                    }
                }
            }

            context("listText") {

                context("active child") {
                    val actual = sut.listText()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni, 2019-05-25, 1er EI"
                    }
                }

                context("inactive child") {
                    val actual = sut.copy(active = false)
                            .listText()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni, 2019-05-25, 1er EI"
                    }
                }
            }
        }

        describe("Child is under three years old") {
            val now = LocalDate.now()

            context("with a child that is less than three years old") {
                context("case 1") {
                    val actual = sut.copy(birthDate = now.minusYears(2))
                            .wasUnderThreeYearsOldAt(now)

                    it("returns false") {
                        actual shouldBe true
                    }
                }

                context("case 2") {
                    val actual = sut.copy(birthDate = now.minusYears(3).plusDays(1))
                            .wasUnderThreeYearsOldAt(now)

                    it("returns false") {
                        actual shouldBe true
                    }
                }
            }

            context("with a child that is more than three years old") {

                context("case 1") {
                    val actual = sut.copy(birthDate = now.minusYears(4))
                            .wasUnderThreeYearsOldAt(now)

                    it("returns true") {
                        actual shouldBe false
                    }
                }

                context("case 2") {
                    val actual = sut.copy(birthDate = now.minusYears(3).minusDays(1))
                            .wasUnderThreeYearsOldAt(now)

                    it("returns true") {
                        actual shouldBe false
                    }
                }

            }

            context("with a child that is three years old") {
                val actual = sut.copy(birthDate = now.minusYears(3))
                        .wasUnderThreeYearsOldAt(now)

                it("returns true") {
                    actual shouldBe false
                }
            }
        }

    }
}
