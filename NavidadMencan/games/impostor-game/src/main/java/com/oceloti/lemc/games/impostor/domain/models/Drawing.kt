package com.oceloti.lemc.games.impostor.domain.models

/**
 * Represents a drawing made by a player as a clue
 * 
 * @param playerId The player who made this drawing
 * @param paths List of drawing paths that make up the complete drawing
 * @param timestamp When this drawing was created
 */
data class Drawing(
    val playerId: PlayerId,
    val paths: List<DrawingPath>,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Check if this drawing has any content
     */
    fun isEmpty(): Boolean = paths.isEmpty()
    
    /**
     * Get the total number of points in this drawing
     */
    fun getTotalPoints(): Int = paths.sumOf { it.points.size }
    
    /**
     * Get the bounding box of this drawing
     */
    fun getBoundingBox(): DrawingBounds? {
        if (paths.isEmpty()) return null
        
        val allPoints = paths.flatMap { it.points }
        if (allPoints.isEmpty()) return null
        
        val minX = allPoints.minOf { it.x }
        val maxX = allPoints.maxOf { it.x }
        val minY = allPoints.minOf { it.y }
        val maxY = allPoints.maxOf { it.y }
        
        return DrawingBounds(minX, minY, maxX, maxY)
    }
}

/**
 * Represents a single path in a drawing (one continuous stroke)
 * 
 * @param points List of points that make up this path
 * @param color Color of this path (Android Color int)
 * @param strokeWidth Width of the stroke in pixels
 */
data class DrawingPath(
    val points: List<DrawingPoint>,
    val color: Int,
    val strokeWidth: Float
) {
    companion object {
        const val DEFAULT_COLOR = -16777216 // Black color
        const val DEFAULT_STROKE_WIDTH = 5f
    }
    
    /**
     * Check if this path has enough points to be drawable
     */
    fun isValid(): Boolean = points.size >= 2
    
    /**
     * Get the length of this path (approximate)
     */
    fun getLength(): Float {
        if (points.size < 2) return 0f
        
        var length = 0f
        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val dx = curr.x - prev.x
            val dy = curr.y - prev.y
            length += kotlin.math.sqrt(dx * dx + dy * dy)
        }
        return length
    }
}

/**
 * Represents a single point in a drawing path
 * 
 * @param x X coordinate in the drawing canvas
 * @param y Y coordinate in the drawing canvas
 * @param timestamp When this point was recorded
 */
data class DrawingPoint(
    val x: Float,
    val y: Float,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Calculate distance to another point
     */
    fun distanceTo(other: DrawingPoint): Float {
        val dx = x - other.x
        val dy = y - other.y
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
}

/**
 * Represents the bounding box of a drawing
 * 
 * @param left Left boundary
 * @param top Top boundary  
 * @param right Right boundary
 * @param bottom Bottom boundary
 */
data class DrawingBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    /**
     * Get the width of the bounding box
     */
    fun getWidth(): Float = right - left
    
    /**
     * Get the height of the bounding box
     */
    fun getHeight(): Float = bottom - top
    
    /**
     * Get the center point of the bounding box
     */
    fun getCenter(): DrawingPoint = DrawingPoint(
        x = (left + right) / 2f,
        y = (top + bottom) / 2f
    )
}