package cat.hobbiton.hobbit.api.model

/**
 * Entity type
 * Values: Consumption,Customer,Invoice,Product,Sequence
 */
enum class EntityTypeDTO(val value: String) {

    Consumption("Consumption"),

    Customer("Customer"),

    Invoice("Invoice"),

    Product("Product"),

    Sequence("Sequence");

}

