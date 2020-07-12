package sampleKotlin

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit


class SampleKotlin7 {

    fun run1() {
        var i = 1
        i--
        Observable.just(1/0)
            .subscribe(
                { println("it") },
                {e -> println("error :$e")}
            )
    }

    fun run2(){
        Observable.create { emitter: ObservableEmitter<String?> ->
            println("subscribing")
            emitter.onError(RuntimeException("always fails"))
        }
            .retryWhen { attempts: Observable<Throwable?> ->
                attempts.zipWith(
                    Observable.range(1, 6),
                    BiFunction { n: Throwable?, i: Int -> i }
                ).flatMap { i: Int ->
                    if (i < 3) {
                        println("delay retry by $i second(s)")
                        Observable.timer(i.toLong(), TimeUnit.SECONDS)
                    } else {
                        Observable.error(java.lang.RuntimeException("실패"))
                    }
                }
            }
//            .blockingForEach { x: String? -> println(x) }
            .subscribe( { println(it) } ,
                {println(it.message)})

        Thread.sleep(6100)
    }

    fun run3() {
        Observable.just("1", "2", "3")
            .map { it.toInt() }
            .retryWhen {it ->
                println("retryWhen")
                it
            }
            .subscribe { println(it) }
    }
}