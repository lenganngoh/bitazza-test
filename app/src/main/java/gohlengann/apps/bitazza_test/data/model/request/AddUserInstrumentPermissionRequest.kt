package gohlengann.apps.bitazza_test.data.model.request

data class AddUserInstrumentPermissionRequest(
    val OMSId: Long = 0,
    val InstrumentIds: Long,
    val UserId: List<Long>
)