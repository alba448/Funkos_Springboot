package example.demo.notifications.dto;

public record FunkoNotificationDto(
        Long id,
        String nombre,
        String categoria,
        Double precio,
        String createdAt,
        String updatedAt
) {
}