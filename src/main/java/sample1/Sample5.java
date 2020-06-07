package sample1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Sample5 {
    public void run1(){
        Observable<BigInteger> factorials = Observable
                .range(2, 100)
                .scan(BigInteger.ONE, (big, cur) ->
                        big.multiply(BigInteger.valueOf(cur)));

        factorials.subscribe(System.out::println);
    }

    public void run2(){
        ArrayList<Integer> list = new ArrayList<>();
        Single<ArrayList<Integer>> all = Observable
                .range(10, 20)
                .collect(()->list, (l, p)-> l.add(p));

        all.subscribe(System.out::println);
    }
}
