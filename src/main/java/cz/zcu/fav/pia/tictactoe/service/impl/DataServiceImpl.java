package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("data")
public class DataServiceImpl implements DataService {

	@Override
	public List<String> names() {
		return List.of("Karel", "Václav", "Preis");
	}

}
