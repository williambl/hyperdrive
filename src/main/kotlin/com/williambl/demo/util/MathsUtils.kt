package com.williambl.demo.util

fun lerp(first: Double, last: Double, fac: Double): Double {
    return first + (last - first) * fac;
}