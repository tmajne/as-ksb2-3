package pl.ttomczyk.asksb2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> cars;

    public CarApi()
    {
        this.cars = new ArrayList<>();
        this.cars.add(new Car(1, "Fiat", "Panda", "red"));
        this.cars.add(new Car(2, "Fiat", "Punto", "black"));
        this.cars.add(new Car(3, "Toyota", "Avensis", "red"));
        this.cars.add(new Car(4, "Ford", "Focus", "white"));
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<List<Car>> cars()
    {
        return new ResponseEntity<List<Car>>(cars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> car(@PathVariable long id)
    {
        Optional<Car> element = cars.stream().filter(car -> car.getId() == id).findFirst();
        if (element.isPresent()) {
            return new ResponseEntity<Car>(element.get(), HttpStatus.OK);
        }

        return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/colors")
    public ResponseEntity<List<Car>> carsByColor(@RequestParam String color)
    {
        List<Car> elements = cars.stream()
                .filter(car -> car.getColor().equals(color))
                .collect(Collectors.toList());

        return new ResponseEntity<>(elements, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car)
    {
        boolean result = cars.add(car);

        if (!result)
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateCar(@RequestBody Car newCar)
    {
        Optional<Car> element = cars.stream()
            .filter(car -> car.getId() == newCar.getId())
            .findFirst();

        if (element.isPresent()) {
            cars.remove(element.get());
            cars.add(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modifyCar(@PathVariable long id, @RequestBody Car newCarData)
    {
        Optional<Car> element = cars.stream()
                .filter(car -> car.getId() == id)
                .findFirst();

        if (element.isPresent()) {
            Car currentCar = element.get();
            if (newCarData.getColor() != null) {
                currentCar.setColor(newCarData.getColor());
            }
            if (newCarData.getModel() != null) {
                currentCar.setModel(newCarData.getModel());
            }
            if (newCarData.getMark() != null) {
                currentCar.setMark(newCarData.getMark());
            }
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(@PathVariable long id)
    {
        Optional<Car> element = cars.stream()
                .filter(car -> car.getId() == id)
                .findFirst();

        if (element.isPresent()) {
            cars.remove(element.get());
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
