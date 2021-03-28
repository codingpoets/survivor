
enum class Direction {
    LEFT, RIGHT;
    fun toggle() = if (this == LEFT) RIGHT else LEFT
}
