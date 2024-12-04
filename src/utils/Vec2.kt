package utils

data class Vec2(val x: Int, val y: Int) {
    fun add(other: Vec2): Vec2 = Vec2(this.x + other.x, this.y + other.y)
}