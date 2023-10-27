package domain.model

data class SelectedAutocompleteOptionModel(
    val id: Int = 0,
    val asmFilePath: String,
    val optionName: String,
    val jsonPath: String
)
