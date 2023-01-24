package com.amsavarthan.tally.domain.entity

sealed class TallyKeyPadItem(
    val label: String,
) {
    object Key1 : TallyKeyPadItem("1")
    object Key2 : TallyKeyPadItem("2")
    object Key3 : TallyKeyPadItem("3")
    object Key4 : TallyKeyPadItem("4")
    object Key5 : TallyKeyPadItem("5")
    object Key6 : TallyKeyPadItem("6")
    object Key7 : TallyKeyPadItem("7")
    object Key8 : TallyKeyPadItem("8")
    object Key9 : TallyKeyPadItem("9")
    object KeyDot : TallyKeyPadItem(".")
    object Key0 : TallyKeyPadItem("0")
    object KeyBackspace : TallyKeyPadItem("backspace")

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