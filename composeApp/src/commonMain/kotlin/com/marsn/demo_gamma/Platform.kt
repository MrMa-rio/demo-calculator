package com.marsn.demo_gamma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform