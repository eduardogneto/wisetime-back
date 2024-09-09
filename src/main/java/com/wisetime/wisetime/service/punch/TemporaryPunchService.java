package com.wisetime.wisetime.service.punch;

import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.repository.punch.TemporaryPunchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemporaryPunchService {

    @Autowired
    private TemporaryPunchRepository temporaryPunchRepository;

    public TemporaryPunch save(TemporaryPunch temporaryPunch) {
        return temporaryPunchRepository.save(temporaryPunch);
    }

    public List<TemporaryPunch> saveAll(List<TemporaryPunch> temporaryPunches) {
        return temporaryPunchRepository.saveAll(temporaryPunches);
    }

    public List<TemporaryPunch> findByUserId(Long userId) {
        return temporaryPunchRepository.findByUserId(userId);
    }

    public List<TemporaryPunch> findByRequestId(Long requestId) {
        return temporaryPunchRepository.findByRequestId(requestId);
    }
}

