package main.refundsapi.component;

import lombok.RequiredArgsConstructor;
import main.refundsapi.entity.UserJoinAccessEntity;
import main.refundsapi.repository.UserJoinAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader {

    private UserJoinAccessRepository userJoinAccessRepository;

    @Autowired
    public DataLoader(UserJoinAccessRepository userJoinAccessRepository){
        this.userJoinAccessRepository = userJoinAccessRepository;
        LoadUsers();
    }

    private void LoadUsers(){
        userJoinAccessRepository.saveAll(
                Arrays.asList(
                        UserJoinAccessEntity.builder().name("홍길동").regNo("860824-1655068").build(),
                        UserJoinAccessEntity.builder().name("김둘리").regNo("921108-1582816").build(),
                        UserJoinAccessEntity.builder().name("마징가").regNo("880601-2455116").build(),
                        UserJoinAccessEntity.builder().name("베지터").regNo("910411-1656116").build(),
                        UserJoinAccessEntity.builder().name("손오공").regNo("820326-2715702").build()
                )
        );

    }
}
