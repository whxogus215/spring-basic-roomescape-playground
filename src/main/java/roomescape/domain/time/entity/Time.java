package roomescape.domain.time.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Time {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value")
    private String time;

    public Time() {

    }

    public Time(String value) {
        this.time = value;
    }

    public Time(Long id, String value) {
        this.id = id;
        this.time = value;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
