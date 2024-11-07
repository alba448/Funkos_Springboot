package example.demo.notifications.mapper;


import example.demo.funko.model.Funko;
import example.demo.notifications.dto.FunkoNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class FunkoNotificationMapper {
    public FunkoNotificationDto toFunkoNotificationDto(Funko funko) {
        return new FunkoNotificationDto(
                funko.getId(),
                funko.getNombre(),
                funko.getCategoria().getNombre(),
                funko.getPrecio(),
                funko.getCreatedAt().toString(),
                funko.getUpdatedAt().toString()
        );
    }
}
