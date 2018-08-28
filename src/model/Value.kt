package model

class Value(val values: List<String>) {
    override fun hashCode(): Int {
        return toString().hashCode()
    }
    override fun equals(other: Any?): Boolean {
        if(other is Value) {
            other.values.forEach { if(values.contains(it)) return true }
        }
        return false
    }

    override fun toString(): String {
        return values.joinToString(separator = " or ")
    }
}