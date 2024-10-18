package mock.claimrequest.dto.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@Setter
public class ClaimDetailTestDTO {

    @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;
}
