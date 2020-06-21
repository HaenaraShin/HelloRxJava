package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import java.util.concurrent.TimeUnit

class SampleKotlin4 {
    fun run1() {
        val offset = System.currentTimeMillis()
        val justSrc = Observable.just(System.currentTimeMillis() - offset)
        val justSrc2 = Observable.just { System.currentTimeMillis() - offset }
        val deferSrc = Observable.defer() { Observable.just(System.currentTimeMillis() - offset) }
        val deferSrc2 = Observable.defer() { justSrc }

        println("#1 now = ${System.currentTimeMillis() - offset}")
        Thread.sleep(5000)
        println("#2 now = ${System.currentTimeMillis() - offset}")

        justSrc.subscribe { println("#1 time = $it") }
        deferSrc.subscribe { println("#2 time = $it") }
        deferSrc2.subscribe { println("#3 time = $it") }
        justSrc2.subscribe { println("#4 time = ${it()}") }
    }

    fun run2() {
//        Observable.just("1", "2", "3").last("default").subscribe{ it -> println(it) }
        Observable.just("1", "2", "3").takeLast(2).subscribe { it -> println(it) }
//        Observable.interval(100, TimeUnit.MILLISECONDS).last(0L ).subscribe{ it -> println(it) }
//        Thread.sleep(5000)
        Single.just("A").subscribe { it -> println(it) }
    }

    fun run3() {
        val o = Observable.range(0, 10)
        o.all { it < 10 }.subscribe { b -> println(b) }
        o.any { it == 0 }.subscribe { b -> println(b) }
        o.contains(0).subscribe { b -> println(b) }
    }

    fun run4() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .contains(1L).subscribe { b -> println(b) }
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .contains(-1L).subscribe { b -> println(b) }
        Thread.sleep(5000)
    }

    fun run5() {
        Observable.combineLatest(
            Observable.just("1", "2", "3"),
            Observable.just("A", "B", "C"),
            BiFunction { s: String, f: String -> "$f:$s" }
        ).subscribe { println(it) }

        Observable.combineLatest(
            Observable.just("1", "2", "3"),
            Observable.just("A", "B", "C"),
            Observable.just("가", "나", "다"),
            Function3 { s: String, f: String, z: String -> "$f:$s:$z" }
        ).subscribe { println(it) }
    }

    fun run6() {
        Observable.combineLatest(
            Observable.interval(100, TimeUnit.MILLISECONDS),
            Observable.interval(500, TimeUnit.MILLISECONDS),
            BiFunction { s: Long, f: Long -> "$f:$s" }
        ).subscribe { println(it) }

        Thread.sleep(5000)

//        Observable.combineLatest(
//            Observable.just("1", "2", "3"),
//            Observable.just("A", "B", "C"),
//            Observable.just("가", "나", "다"),
//            Function3 { s: String, f: String, z: String -> "$f:$s:$z" }
//        ).subscribe { println(it) }
    }

    fun run7() {
        Observable.zip(
            Observable.just(1, 2, 3),
            Observable.just(1, 2, 3),
            Observable.just(1, 2, 3),
            Function3 { s: Int, f: Int, z: Int -> "$f:$s:$z" }
        ).subscribe { println(it) }
    }

    fun run8() {
        Observable.zip(
            Observable.range(0, 1000000),
            Observable.range(0, 1000000),
            Observable.range(0, 1000000),
            Function3 { s: Int, f: Int, z: Int -> "$f:$s:$z" }
        ).subscribe { println(it) }
    }

    fun run9() {
//        Observable.just("1", "2", "a", "3")
//            .map { s -> s.toInt() }
//            .retry(2)
//            .onErrorComplete()
//            .subscribe { println(it) }

        Observable.just("1", "2", "a", "3")
            .map { s -> s.toInt() }
            .onErrorComplete()
            .retry(2)
            .subscribe { println(it) }
    }

}