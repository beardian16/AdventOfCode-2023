package com.devian.adventofcode

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: Int, name: String) = Path("src/test/java/com/devian/adventofcode/$year/input/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Int.toFilename() = "day" + toString().padStart(2, '0')

inline fun <T> Iterable<T>.sumOfIndexed(selector: (index: Int, T) -> Long): Long {
    var sum = 0L
    forEachIndexed { index, item ->
        sum += selector(index, item)
    }
    return sum
}

inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapNotNullTo(
    destination: C, transform: (T) -> Iterable<R>
): C {
    for (element in this) {
        val list = transform(element)
        destination.addAll(list.filterNot { it == null })
    }
    return destination
}

inline fun <T, R> Iterable<T>.flatMapNotNull(transform: (T) -> Iterable<R>): List<R> {
    return flatMapNotNullTo(ArrayList<R>(), transform)
}
