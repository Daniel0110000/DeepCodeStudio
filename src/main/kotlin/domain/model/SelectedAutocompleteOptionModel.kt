package domain.model

data class SelectedAutocompleteOptionModel(
    val uuid: String = "",
    val asmFilePath: String,
    val optionName: String,
    val jsonPath: String
)
