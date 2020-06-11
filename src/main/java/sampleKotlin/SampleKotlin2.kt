package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class SampleKotlin2 {
    // GroupBy 테스트
    fun run1() {
        Observable.just("hello Rx!").subscribe { println(it) }
        Observable.just(
            "AAA",
            "ABC",
            "ABCD",
            "123",
            "112345",
            "111",
            "000",
            "0123",
            "0101010"
        )
            .groupBy { item ->
                when {
                    item.startsWith("A") -> "A"
                    item.startsWith("1") -> "1"
                    item.startsWith("0") -> "0"
                    else -> ""
                }
            }.subscribe { group ->
                println("${group.key} 그룹 발행 시작")
                group.subscribe {
                    println("${group.key} : $it")
                }
            }
    }

    fun run2() {
        // Timer와 머지한다면?
        Observable.merge(
            Observable.timer(1, TimeUnit.SECONDS),
            Observable.range(0, 10)
        ).doOnComplete {
            println("complete")
        }.subscribe {
            println(it)
        }
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) { }
    }

    fun run3() {
        // delay 테스
//        println("start")
//        Observable.range(0, 10).delay(1, TimeUnit.SECONDS).subscribe { println(it) }
//        println("end")
//        try {
//            Thread.sleep(5000)
//        } catch (e: InterruptedException) {
//        }
//        println("finish")

        // concat 테스트 == delay 처럼 동작하기
        println("start")
        Observable.timer(1, TimeUnit.SECONDS).map { "" }.concatWith(Observable.just("hello concat!"))
            .subscribe { println(it) }
        println("end")
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
        }
        println("finish")
    }

    fun run4() {
        // doOnEach 테스트
        Observable.just("1", "2", "hello", "world")
            .doOnEach { noti ->
                when {
                    noti.isOnNext -> {
                        println("onNext :: ${noti.value}")
                    }
                    noti.isOnError -> {
                        println("onError :: ${noti.error.localizedMessage}")
                    }
                    noti.isOnComplete -> {
                        println("onComplete")
                    }
                }
            }.subscribe()
    }

    fun run5() {
        // doOnSubscribe 테스트
        val o = Observable.just("Hello Rx!").doOnSubscribe {
            println("start subscribe")
        }
        o.subscribe()
        o.subscribe()
        o.subscribe()
        o.subscribe()
    }

}