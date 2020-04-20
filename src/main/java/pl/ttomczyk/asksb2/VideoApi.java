package pl.ttomczyk.asksb2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideoApi {

    private List<Car> videoList;

    @GetMapping
    public ResponseEntity<List<Car>> getVideos() {
        return new ResponseEntity<List<Car>>(videoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getVideoById(@PathVariable long id) {
        Optional<Car> first = videoList.stream().filter(video -> video.getId() == id).findFirst();

        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addVideo(@RequestBody Car video) {
        boolean add = videoList.add(video);

        if (add)
        {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity modVideo(@RequestBody Car newVideo) {
        Optional<Car> first = videoList.stream().filter(video -> video.getId() == newVideo.getId())
                .findFirst();

        if (first.isPresent()) {
            videoList.remove(first.get());
            videoList.add(newVideo);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity removeVideo(@PathVariable long id) {
        Optional<Car> first = videoList.stream().filter(video -> video.getId() == id)
                .findFirst();

        if (first.isPresent()) {
            videoList.remove(first.get());
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
