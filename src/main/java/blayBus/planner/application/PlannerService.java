package blayBus.planner.application;

import blayBus.common.exception.PlannerAlreadyExistsException;
import blayBus.common.exception.PlannerNotFoundException;
import blayBus.common.exception.UserNotFoundException;
import blayBus.planner.application.request.PlannerCreateCommand;
import blayBus.planner.domain.Planner;
import blayBus.planner.domain.PlannerRepository;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;

    @Transactional
    public Planner createPlanner(PlannerCreateCommand command) {
        if (plannerRepository.existsByDateAndUserId(command.date(), command.userId())) {
            throw new PlannerAlreadyExistsException();
        }

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        Planner planner = Planner.create(command.date(), user);

        return plannerRepository.save(planner);
    }

    @Transactional
    public Planner setStudyTime(Long plannerId, int minutes) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(PlannerNotFoundException::new);

        planner.setTotalStudyTime(minutes);
        return plannerRepository.save(planner);
    }
}
