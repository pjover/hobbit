package cat.hobbiton.hobbit.domain

enum class SequenceType(val prefix: String) {
    STANDARD_INVOICE("I"),  // Standard invoices with bank direct debit payment type
    SPECIAL_INVOICE("S"),   // Special invoices, with other payments types
    CUSTOMER("C");          // Customers
}
