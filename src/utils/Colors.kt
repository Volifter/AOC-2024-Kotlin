package utils

enum class Colors(val code: String) {
    RED("\u001b[31m"),
    GREEN("\u001b[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    GRAY("\u001b[90m"),
    RESET("\u001b[0m")
}
