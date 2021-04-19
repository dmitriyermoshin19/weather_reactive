package ru.job4j.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.domain.Weather;
import ru.job4j.weather.service.WeatherService;
import java.time.Duration;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weathers;

    @Autowired
    public WeatherController(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        final Flux<Weather> data = weathers.all();
        final Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping("/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    @GetMapping("/hottest")
    public Mono<Weather> hottest() {
        return weathers.findTheHottest();
    }

    @GetMapping("/greaterThan/{val}")
    public Flux<Weather> greaterThan(@PathVariable Integer val) {
        return weathers.findGreaterThan(val);
    }
}
