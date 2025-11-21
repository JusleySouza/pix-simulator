package br.com.pix.simulator.dict.service;

import br.com.pix.simulator.dict.config.LoggerConfig;
import br.com.pix.simulator.dict.dto.KeyCreateRequest;
import br.com.pix.simulator.dict.dto.KeyResponse;
import br.com.pix.simulator.dict.exception.KeyAlreadyExistsException;
import br.com.pix.simulator.dict.exception.KeyNotFoundException;
import br.com.pix.simulator.dict.exception.ValidationException;
import br.com.pix.simulator.dict.model.PixKey;
import br.com.pix.simulator.dict.model.enums.KeyStatus;
import br.com.pix.simulator.dict.repository.PixKeyRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;
    private final PspClientService pspClientService;

    public PixKeyService(PixKeyRepository pixKeyRepository, PspClientService pspClientService) {
        this.pixKeyRepository = pixKeyRepository;
        this.pspClientService = pspClientService;
    }

    @Transactional
    public KeyResponse createKey(KeyCreateRequest request) {
        LoggerConfig.LOGGER_PIX_KEY.info("Attempt to register the key: {}", request.keyValue());

        if (!pspClientService.validateAccount(request)) {
            throw new ValidationException("Invalid or unavailable account, user, or PSP with service provider.");
        }

        PixKey newKey = new PixKey();
        newKey.setKeyValue(request.keyValue());
        newKey.setKeyType(request.keyType());
        newKey.setAccountId(request.accountId());
        newKey.setUserId(request.userId());
        newKey.setPspId(request.pspId());
        newKey.setAccountType(request.accountType());
        newKey.setOwnerType(request.ownerType());
        newKey.setKeyStatus(KeyStatus.ACTIVE);

        try {
            PixKey savedKey = pixKeyRepository.save(newKey);
            LoggerConfig.LOGGER_PIX_KEY.info("Key successfully registered: {}", savedKey.getKeyValue());
            return toResponse(savedKey);

        } catch (DataIntegrityViolationException e) {
            throw new KeyAlreadyExistsException("The Pix key'" + request.keyValue() + "' already registered.");
        }
    }

    private KeyResponse toResponse(PixKey key) {
        return new KeyResponse(
                key.getKeyValue(),
                key.getKeyType(),
                key.getAccountId(),
                key.getPspId(),
                key.getKeyStatus(),
                key.getAccountType(),
                key.getOwnerType(),
                key.getCreatedAt(),
                key.getUpdatedAt()
        );
    }

}
