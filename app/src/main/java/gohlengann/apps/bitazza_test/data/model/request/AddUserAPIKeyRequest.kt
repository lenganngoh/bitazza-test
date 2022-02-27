package gohlengann.apps.bitazza_test.data.model.request

data class AddUserAPIKeyRequest(
    val UserId: Long,
    val APIKey: String,
    val Permission: String
)