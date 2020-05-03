import io.reactivex.rxjava3.core.Observable;

public class HelloRx {
    public static void main(String[] args) {
        Observable.just("Hello RxJava!").subscribe(System.out::println);
    }
}
