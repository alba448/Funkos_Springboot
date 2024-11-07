package example.demo.funko.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.demo.categoria.service.CategoriaService;
import example.demo.funko.dto.FunkoDto;
import example.demo.funko.mappers.FunkoMapper;
import example.demo.funko.model.Funko;
import example.demo.funko.repository.FunkoRepository;


import example.demo.funko.validators.FunkoValidator;
import example.demo.notifications.config.WebSocketConfig;
import example.demo.notifications.config.WebSocketHandler;
import example.demo.notifications.dto.FunkoNotificationDto;
import example.demo.notifications.mapper.FunkoNotificationMapper;
import example.demo.notifications.models.Notificacion;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = {"funkos"})
public class FunkoServiceImpl implements FunkoService{
    private final FunkoRepository repository;
    private final FunkoMapper mapper;
    private final CategoriaService categoriaService;
    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketHandler;
    private final FunkoNotificationMapper notificacionMapper;
    private ObjectMapper objectMapper;
    private FunkoValidator validator;

    @Autowired
    public FunkoServiceImpl(FunkoRepository repository, FunkoMapper mapper, CategoriaService categoriaService, WebSocketConfig webSocketConfig, FunkoNotificationMapper notificacionMapper, FunkoValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoriaService = categoriaService;
        this.webSocketConfig = webSocketConfig;
        webSocketHandler = webSocketConfig.webSocketHandler();
        objectMapper = new ObjectMapper();
        this.notificacionMapper = notificacionMapper;
        this.validator = validator;
    }

    @Override
    public List<Funko> getAll() {
        return repository.findAll();
    }

    @Cacheable
    @Override
    public Funko getById(String id) {
        if (!validator.isIdValid(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }
        return repository.findById(Long.valueOf(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El Funko con id " + id + " no se ha encontrado.")
        );
    }

    @Override
    public Funko getByNombre(String nombre) {
        return repository.findByNombre(nombre).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El funko " + nombre + " no existe")
        );
    }

    @CachePut
    @Override
    public Funko save(FunkoDto funkoDto) {
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria().toUpperCase());
        if (!validator.isNameUnique(funkoDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del funko ya existe");
        }
        var funkoSaved = repository.save(mapper.toFunko(funkoDto, categoria));
        onChange(Notificacion.Tipo.CREATE, funkoSaved);
        return funkoSaved;
    }

    @CachePut
    @Override
    public Funko update(String id, FunkoDto funkoDto) {
        if (!validator.isIdValid(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }

        var res = repository.findById(Long.valueOf(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El Funko con id " + id + " no se ha encontrado.")
        );
        if (!validator.isNameUnique(funkoDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del funko ya existe");
        }
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria());
        res.setNombre(funkoDto.getNombre());
        res.setPrecio(funkoDto.getPrecio());
        res.setCategoria(categoria);
        var funkoUpdated = repository.save(res);
        onChange(Notificacion.Tipo.UPDATE, funkoUpdated);
        return funkoUpdated;
    }

    @CacheEvict
    @Override
    public Funko delete(String id) {
        if (!validator.isIdValid(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }

        Funko funko = repository.findById(Long.valueOf(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El Funko con id " + id + " no se ha encontrado.")
        );
        repository.deleteById(Long.valueOf(id));
        onChange(Notificacion.Tipo.DELETE, funko);
        return funko;
    }

    void onChange(Notificacion.Tipo tipo, Funko data) {
        log.debug("Servicio de funkos onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketHandler == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketHandler = this.webSocketConfig.webSocketHandler();
        }

        try {
            Notificacion<FunkoNotificationDto> notificacion = new Notificacion<>(
                    "FUNKOS",
                    tipo,
                    notificacionMapper.toFunkoNotificationDto(data),
                    LocalDateTime.now().toString()
            );

            String json = objectMapper.writeValueAsString((notificacion));

            log.info("Enviando mensaje a los clientes ws");
            Thread senderThread = new Thread(() -> {
                try {
                    webSocketHandler.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }

    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
}