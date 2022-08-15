package zerobase.dividendservice.persist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "DIVIEND")
@Getter
@ToString
@NoArgsConstructor
public class DiviendEntity {

    @Id
    private Long id;

    private 
}
