package sample1

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class RxUnitTest {

    @Test
    fun rxTest() {
        val o = Observable.just("hello")
        val toTest = o.test()
        toTest.assertComplete()
        toTest.assertNoErrors()
        toTest.assertValue("hello")
    }

    @Test
    fun rxTestError() {
        val o = Observable.error<Exception>(SampleException("sample"))
        val toTest = o.test()
        // 단순히 클래스로 구분할 경우 부모클래스도 맞는걸로 친다.
        toTest.assertError(Exception::class.java)
        toTest.assertError(SampleException::class.java)
        toTest.assertError{it.message == "sample"}
    }
    
    @Test
    fun rxTestList() {
        val o = Observable.range(10, 10)
        val toTest = o.test()
        // 단순히 반환되는 갯수를 확인하는 방법
        toTest.assertValueCount(10)
        // observable을 all() 로 Boolean 변환 후 test하여 비교하는 방법
        o.all { it in 10..19 }.test().assertValue { true }
        // observer의 값을 values로 뽑아서 실제 리스트와 비교하는 방법
        assertEquals(toTest.values(), listOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19))
        // observable을 toList()로 list로 변환 후 리스트 통째로 비교하는 방법
        o.toList().test().assertValue(listOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19))
        // assertValue 안에 예상조건을 입력하는 것은 동작하지 않는다.. 어째서?
        // toTest.assertValue { it in 10..19 } // no working
    }

    @Test
    fun rxTestSingle() {
        val s = Single.just("hello")
        val toTest = s.test()
        toTest.assertComplete()
        toTest.assertNoErrors()
        toTest.assertValue("hello")
    }

    @Test
    fun rxTestCompletable() {
        val c = Completable.complete()
        val toTest = c.test()
        toTest.assertComplete()
        toTest.assertNoErrors()
    }

    inner class SampleException(message : String) : Exception(message)
}