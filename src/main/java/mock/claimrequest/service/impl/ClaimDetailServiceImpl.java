package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimDetailDTO;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.service.ClaimDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClaimDetailServiceImpl implements ClaimDetailService {
    private final ClaimDetailRepository claimDetailRepository;

    public ClaimDetailServiceImpl(ClaimDetailRepository claimDetailRepository, ModelMapper modelMapper) {
        this.claimDetailRepository = claimDetailRepository;
    }

    @Override
    public List<ClaimDetailDTO> findAllByClaimId(UUID id) {
        return claimDetailRepository.findAllByClaimId(id).stream().map(claimDetail -> {
            var claimDetailDTO = new ClaimDetailDTO();
            claimDetailDTO.setId(claimDetail.getId());
            claimDetailDTO.setStartTime(claimDetailDTO.getStartTime());
            claimDetailDTO.setEndTime(claimDetailDTO.getEndTime());
            return claimDetailDTO;
        }).toList();
    }
}
