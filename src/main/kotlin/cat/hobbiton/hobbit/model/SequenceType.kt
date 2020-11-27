package cat.hobbiton.hobbit.model

enum class SequenceType(val prefix: String) {
    STANDARD_INVOICE("F"),  // Standard invoices with bank direct debit payment type
    SPECIAL_INVOICE("X"),   // Special invoices, with other payments types
    CUSTOMER("C");          // Customers
}
