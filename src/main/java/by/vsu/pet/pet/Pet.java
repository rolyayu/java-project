package by.vsu.pet.pet;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String breed;

    @CreationTimestamp
    private LocalDateTime creationDate;
}
