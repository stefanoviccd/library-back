package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.Statistics;
import rs.ac.bg.fon.libraryback.service.StatisticsService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    public StatisticsController(){
    }


    @GetMapping
    @CrossOrigin
    public ResponseEntity<Response> getStatisticalInfo() {

        Response response = new Response();
        try {
            Statistics stat = statisticsService.getStatistics();
            response.setResponseData(stat);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }
}
