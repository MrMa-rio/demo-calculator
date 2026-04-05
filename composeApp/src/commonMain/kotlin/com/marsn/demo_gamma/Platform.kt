package com.marsn.demo_calculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
