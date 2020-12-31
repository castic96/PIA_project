package cz.zcu.fav.pia.tictactoe.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/*
 * CREATE TABLE SAMPLE (id UUID PRIMARY KEY)
 */
@Entity
@Table(name = "SAMPLE")
@Data
public class SampleEntity {

	@Id
	private UUID id;

}
