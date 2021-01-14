package cz.zcu.fav.pia.tictactoe.service.impl;

import cz.zcu.fav.pia.tictactoe.domain.ResultDomain;
import cz.zcu.fav.pia.tictactoe.entity.ResultEntity;
import cz.zcu.fav.pia.tictactoe.repository.ResultEntityRepository;
import cz.zcu.fav.pia.tictactoe.service.StatisticsService;
import cz.zcu.fav.pia.tictactoe.service.LoggedUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service("statisticsService")
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestScope
public class StatisticsServiceImpl implements StatisticsService {

    private final ResultEntityRepository resultEntityRepository;
    private final LoggedUserService loggedUserService;

    private List<ResultDomain> resultDomainList;

    public List<ResultDomain> getResultDomainList() {
        if (this.resultDomainList == null) {
            this.resultDomainList = getResults(loggedUserService.getUser().getUsername());
        }

        return resultDomainList;
    }

    private List<ResultDomain> getResults(String username) {
        List<ResultDomain> resultDomainList = new LinkedList<>();
        String opponentName;
        boolean winner;


        for (ResultEntity resultEntity : resultEntityRepository.findAllByUser1Username(username)) {
            opponentName = resultEntity.getUser2().getUsername();

            winner = resultEntity.getWinner().getUsername().equals(username);

            resultDomainList.add(new ResultDomain(opponentName, winner, resultEntity.getCreateDateTime()));
        }

        for (ResultEntity resultEntity : resultEntityRepository.findAllByUser2Username(username)) {
            opponentName = resultEntity.getUser1().getUsername();

            winner = resultEntity.getWinner().getUsername().equals(username);

            resultDomainList.add(new ResultDomain(opponentName, winner, resultEntity.getCreateDateTime()));
        }

        resultDomainList.sort(Comparator.comparing(ResultDomain::getDateTime));
        Collections.reverse(resultDomainList);

        return Collections.unmodifiableList(resultDomainList);
    }

}
