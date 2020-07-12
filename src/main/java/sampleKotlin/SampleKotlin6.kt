package sampleKotlin

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.omg.SendingContext.RunTime
import java.lang.ArithmeticException
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class SampleKotlin6 {
    fun run1() {
        var a = 1
        a--
        println("aa")
        Observable.defer{
            Thread.sleep(1000)
            Observable.just(1/a)
        }
            .subscribeOn(Schedulers.newThread())
            .subscribe { println(it) }

        Thread.sleep(100)
        println("aaa")
    }

    fun run2() {
        Observable.fromCallable { 1/0 }
            .subscribe(
                { println(it) },
                { e-> println(e.message) }
            )
    }

    fun run3() {
        // Before
        Observable.just(1, 0)
            .map { x -> 10 / x }
            .subscribe { println(it) }

        // After
        Observable.just(1, 0)
            .flatMap { x ->
                if (x == 0) {
                    Observable.error(ArithmeticException("Divied with Zero!"))
                } else {
                    Observable.just(10 / x)
                }
            }
            .subscribe { println(it) }

    }

    fun run4() {
//        Observable.timer(3, TimeUnit.SECONDS)
//            .timeout(
//                { Observable.timer(1, TimeUnit.SECONDS) },
//                { Observable.timer(2, TimeUnit.SECONDS) },
//            )

        Observable.timer(3, TimeUnit.SECONDS)
            .timeout(1, TimeUnit.SECONDS)
            .subscribe(
                {println(it)},
                {e -> println(e.message)}
            )

        Thread.sleep(3100)
    }


    fun risky() : Observable<String> {
        return Observable.fromCallable {
            if (Math.random() < 0.1) {
                Thread.sleep((Math.random() * 2000).toLong())
                "OK"
            } else {
                throw RuntimeException("Transient")
            }
        }.cache()
    }

    fun run5() {
        risky()
            .timeout(1, TimeUnit.SECONDS)
            .doOnError{
                    th -> System.err.println("will retry : ${th.message}")
            }
            .retry()
            .subscribe { println(it) }

        Thread.sleep(6100)
    }
}
//
//fun risky() : Observable<String> {
//    return Observable.fromCallable {
//        if (Math.random() < 0.1) {
//            Thread.sleep((Math.random() * 2000).toLong())
//            "OK"
//        } else {
//            throw RuntimeException("Transient")
//        }
//    }.cache()
//}
//
//fun run5() {
//    risky()
//        .timeout(1, TimeUnit.SECONDS)
//        .doOnError{
//                th -> System.err.println("will retry : ${th.message}")
//        }
//        .retry()
//        .subscribe { println(it) }
//}
//
fun errorHandleFlatMap() {
    // Before
    Observable.just(1, 0)
        .map { x -> 10 / x }
        .subscribe { println(it) }

    // After
    Observable.just(1, 0)
        .flatMap { x ->
            if (x == 0) {
                Observable.error(
                    ArithmeticException("Divied with Zero!")
                )
            } else {
                Observable.just(10 / x)
            }
        }
        .subscribe { println(it) }
}

fun risky() : Observable<String> {
    return Observable.fromCallable {
        if (Math.random() < 0.1) {
            Thread.sleep((Math.random() * 2000).toLong())
            "OK"
        } else {
            throw RuntimeException("Transient")
        }
    }.cache()
}

fun everytimeFail() {
    risky()
        .timeout(1, TimeUnit.SECONDS)
        .doOnError{
                th -> System.err.println("will retry : ${th.message}")
        }
        .retry()
        .subscribe { println(it) }

    Thread.sleep(6100)
}

fun tryCatchOnCreate() {
    Observable.create<Int> { subscriber ->
        try {
            subscriber.onNext( 1/0)
        } catch (e: Exception) { }
        subscriber.onComplete()
    }
}
