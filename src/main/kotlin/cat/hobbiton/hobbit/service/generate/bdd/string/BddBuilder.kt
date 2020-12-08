package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.service.generate.bdd.string.Bdd

interface BddBuilder {
    fun build(bdd: Bdd): String
}
