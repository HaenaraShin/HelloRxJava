package sample1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.reactivestreams.Subscriber;

public class Sample1 {

    public void run1() {
//                 Observable.just("Hello RxJava!").subscribe(System.out::println);

        Observable.create(s -> {
            s.onNext("Hello RxJava!");
            s.onComplete();
        }).subscribe(hello -> System.out.println(hello));
    }

    public void run2() {
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        });
        o.map(i -> "Number " + i)
                .subscribe(s -> System.out.println(s));
    }

    public void run3() {
        Observer o = new Observer<String>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };


        o.onSubscribe(new Disposable() {
            @Override
            public void dispose() {
                System.out.println("dispose");
            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });

        o.onNext("HelloWorld");
        o.onNext("OnNext manually");
        o.onNext("...");
        o.onComplete();
    }
}
