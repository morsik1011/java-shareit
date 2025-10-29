package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank(message = "Название не должно быть null или пустым")
    String name;
    @NotBlank(message = "Описание не должно быть null или пустым")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;
    @NotBlank(message = "Статус доступности не должно быть null или пустым")
    String available;
    ItemRequest request;
}
