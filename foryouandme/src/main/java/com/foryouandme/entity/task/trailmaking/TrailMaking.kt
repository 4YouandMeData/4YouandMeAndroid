package com.foryouandme.entity.task.trailmaking

data class Point(val x: Double, val y: Double)
data class TrailMakingPoint(val x: Double, val y: Double, val name: String)
data class TrailMakingTap(val index: Int, val timestamp: Long, val incorrect: Boolean)
