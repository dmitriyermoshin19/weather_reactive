package ru.job4j.weather.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.job4j.weather.domain.Weather;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {
    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();
    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 35));
        weathers.put(4, new Weather(4, "Smolensk", 45));
        weathers.put(5, new Weather(5, "Kiev", 10));
        weathers.put(6, new Weather(6, "Minsk", 5));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    public Mono<Weather> findTheHottest() {
        final int max = weathers.values().stream().mapToInt(Weather::getTemperature).max().getAsInt();
        return Mono.justOrEmpty(weathers.values().stream().filter(w -> w.getTemperature() == max).findFirst().get());
    }

    public Flux<Weather> findGreaterThan(int val) {
        return Flux.fromStream(weathers.values().stream().filter(w -> w.getTemperature() > val));
    }
}
