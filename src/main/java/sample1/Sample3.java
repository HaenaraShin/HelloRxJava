package sample1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Sample3 {
    // cache로 같은 값 반환. 매번 create하지 않는다.
    public void run1() {
        Observable<Object> a = Observable.create(s -> {
                System.out.println("create");
                s.onNext("one");
                s.onNext("two");
                s.onComplete();
        }).cache();

        a.subscribe(s->{
            System.out.println("1 : " + s);
        });
        a.subscribe(s->{
            System.out.println("2 : " + s);
        });
    }

    // 비동기 Cache 일 경우 값이 나올때 까지 기다렸다가 캐시값을 사용한다.
    public void run2() {
        Observable<Object> a = Observable.create(s -> {
            new Thread(() -> {
                System.out.println("create");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                s.onNext("one");
                s.onNext("two");
                s.onComplete();
            }).start();
        }).cache();

        a.subscribe(s->{
            System.out.println("1 : " + s);
        });
        a.subscribe(s->{
            System.out.println("2 : " + s);
        });
    }

    // RxJava 2.x 이후부터는 subscribe가 Subscription 대신 Disposable을 반환한다.
    public void run3() {
        System.out.println("start");
        Disposable d = o().subscribe();
        d.dispose();
    }

    // ObservableEmitter 에 add()도 삭제되었다.
    private static Observable<Integer> o() {
        return Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        sleep(10, SECONDS);
                        subscriber.onNext(10);
                        subscriber.onComplete();
                    };
                    final Thread thread = new Thread(r);
                    thread.start();
                }
        );
    }

    private static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run4() {
        // 아무일도 안일어난다...
        Observable.interval(0, 1, SECONDS).subscribe(System.out::println);
    }


    public void run5() {
        Observable.just(1).subscribe(
                status -> {System.out.println(status);},
                ex -> {System.err.println(ex);},
                () -> {System.out.println("Done");}
        );

        Observable.range(0,10).subscribe(
                status -> {System.out.println(status);},
                ex -> {System.err.println(ex);},
                () -> {System.out.println("Done");}
        );

        Observable.never().subscribe(
                status -> {System.out.println(status);},
                ex -> {System.err.println(ex);},
                () -> {System.out.println("Done");}
        );

        Observable.error(new Exception("에러")).fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("call()");
                return "null";
            }
        }).subscribe(
                status -> {System.out.println(status);},
                ex -> {System.err.println(ex);},
                () -> {System.out.println("Done");}
        );

        Observable.just(1).fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("call()");
                return "null";
            }
        }).subscribe(
                status -> {System.out.println(status);},
                ex -> {System.err.println(ex);},
                () -> {System.out.println("Done");}
        );
    }

}


