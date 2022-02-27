package gohlengann.apps.bitazza_test.data.remote

enum class Functions(val function: String) {
    AUTHENTICATE_USER("AuthenticateUser"),
    GET_INSTRUMENTS("GetInstruments"),
    SUBSCRIBE("SubscribeLevel1"),
    ADD_USER_INSTRUMENT_PERMISSION("AddUserInstrumentPermissions"),
    ADD_USER_API_KEY_PERMISSION("AddUserAPIKeyPermission"),
    GET_ACCOUNT_POSITIONS("GetAccountPositions"),
    LOGOUT("LogOut")
}