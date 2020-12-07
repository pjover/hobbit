package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.service.generate.bdd.string.Bdd

interface BddBuilder {
    fun build(bdd: Bdd): String
}
