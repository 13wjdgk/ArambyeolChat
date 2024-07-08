package Arambyeol.chat.domain.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Getter
@Entity
public class Prefixes {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String prefix;
}
