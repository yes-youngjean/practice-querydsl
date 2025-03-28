package study.querydsl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Hello {

    @Id
    @Getter
    private Long id;
}
