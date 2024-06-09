package Arambyeol.chat.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Arambyeol.chat.domain.chat.entity.Prefixes;

public interface PrefixesRepository extends JpaRepository<Prefixes,Integer> {
	Optional<Prefixes> findPrefixesById(int id);
}
