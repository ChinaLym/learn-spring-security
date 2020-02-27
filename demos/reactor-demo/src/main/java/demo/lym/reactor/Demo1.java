package demo.lym.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.util.function.BiFunction;

/**
 * @author lym
 * @since 1.0
 */
public class Demo1 {
    public static void main(String[] args) {

        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .subscriberContext(ctx -> ctx.put(key, "World"))
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.getOrDefault(key, "Stranger")));


      /*  Flux.just("tom", "jack", "allen")
                .map(s -> s.concat("@qq.com"))
                .subscribe(System.out::println);
*/

/*
        Flux.just("tom")
                .map(s -> {
                    System.out.println("(concat @qq.com) at [" + Thread.currentThread() + "]");
                    return s.concat("@qq.com");
                })
                .publishOn(Schedulers.newSingle("thread-a"))
                .map(s -> {
                    System.out.println("(concat foo) at [" + Thread.currentThread() + "]");
                    return s.concat("foo");
                })
                .filter(s -> {
                    System.out.println("(startsWith f) at [" + Thread.currentThread() + "]");
                    return s.startsWith("t");
                })
                .publishOn(Schedulers.newSingle("thread-b"))
                .map(s -> {
                    System.out.println("(to length) at [" + Thread.currentThread() + "]");
                    return s.length();
                })
                .subscribeOn(Schedulers.newSingle("source"))
                .subscribe(System.out::println);
    */
    }
}
