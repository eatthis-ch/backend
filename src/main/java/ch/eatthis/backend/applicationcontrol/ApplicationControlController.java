package ch.eatthis.backend.applicationcontrol;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not found")
})
@RequestMapping("api/v1/applicationcontrol")
public class ApplicationControlController {

    @GetMapping("/getTimeStamp")
    public Timestamp getDelayLine() {
        return new Timestamp(new Date().getTime());
    }


}
