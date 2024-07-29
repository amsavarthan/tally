package com.amsavarthan.tally.domain.entity

sealed class TallyKeyPadItem(
    val label: String,
) {
    data object Key1 : TallyKeyPadItem("1")
    data object Key2 : TallyKeyPadItem("2")
    data object Key3 : TallyKeyPadItem("3")
    data object Key4 : TallyKeyPadItem("4")
    data object Key5 : TallyKeyPadItem("5")
    data object Key6 : TallyKeyPadItem("6")
    data object Key7 : TallyKeyPadItem("7")
    data object Key8 : TallyKeyPadItem("8")
    data object Key9 : TallyKeyPadItem("9")
    data object KeyDot : TallyKeyPadItem(".")
    data object Key0 : TallyKeyPadItem("0")
    data object KeyBackspace : TallyKeyPadItem("backspace")

    companion object {
        fun getAllKeys() = listOf(
            Key1,
            Key2,
            Key3,
            Key4,
            Key5,
            Key6,
            Key7,
            Key8,
            Key9,
            KeyDot,
            Key0,
            KeyBackspace,
        )
    }

}