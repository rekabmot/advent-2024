package utils

data class Vec2(val x: Int, val y: Int) {
    companion object {
        val ORDINAL_DIRECTIONS = listOf(
            Vec2(0, -1), // N
            Vec2(1, -1), // NE
            Vec2(1, 0), // E
            Vec2(1, 1), // SE
            Vec2(0, 1), // S
            Vec2(-1, 1), // SW
            Vec2(-1, 0), // W
            Vec2(-1, -1), // NW
        )

        val CARDINAL_DIRECTIONS = listOf(
            Vec2(0, -1), // N
            Vec2(1, 0), // E
            Vec2(0, 1), // S
            Vec2(-1, 0), // W
        )
    }

    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vec2) = Vec2(this.x - other.x, this.y - other.y)
    operator fun times(factor: Int) = Vec2(this.x * factor, this.y * factor)
}

